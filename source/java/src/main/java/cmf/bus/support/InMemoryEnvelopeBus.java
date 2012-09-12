package cmf.bus.support;

import java.util.LinkedList;
import java.util.List;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;

public class InMemoryEnvelopeBus implements IEnvelopeBus {

    protected List<IRegistration> registrationList;
    private Object registrationListLock = new Object();

    public InMemoryEnvelopeBus() {
        registrationList = new LinkedList<IRegistration>();
    }

    protected void dispatch(final Envelope envelope, final List<IRegistration> registrationList) {
        new Thread() {

            @Override
            public void run() {
                for (IRegistration registration : registrationList) {
                    try {
                        registration.getEnvelopeHandler().handle(envelope);
                    } catch (Exception e) {
                        registration.getEnvelopeHandler().handleFailed(envelope, e);
                    }
                }
            }
        }.run();
    }

    @Override
    public void register(IRegistration registration) {
        synchronized (registrationListLock) {
            registrationList.add(registration);
        }
    }

    @Override
    public void send(Envelope envelope) {
        List<IRegistration> registrations = new LinkedList<IRegistration>();
        synchronized (registrationListLock) {
            for (IRegistration registration : registrationList) {
                if (registration.getFilterPredicate().filter(envelope)) {
                    registrations.add(registration);
                }
            }
            dispatch(envelope, registrations);
        }
    }
}
