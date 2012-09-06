package cmf.bus.core.support;

import java.util.LinkedList;
import java.util.List;

import cmf.bus.core.Envelope;
import cmf.bus.core.IEnvelopeBus;
import cmf.bus.core.IRegistration;

public class InMemoryEnvelopeBus implements IEnvelopeBus {

    protected List<IRegistration> registrationList;

    private Object registrationListLock = new Object();

    public InMemoryEnvelopeBus() {
        registrationList = new LinkedList<IRegistration>();
    }

    public void send(Envelope envelope) {
        List<IRegistration> handlerList = new LinkedList<IRegistration>();
        synchronized (registrationListLock) {
            for (IRegistration registration : registrationList) {
                if (registration.filter(envelope)) {
                    handlerList.add(registration);
                }
                dispatch(envelope, handlerList);
            }
        }
    }

    public void register(IRegistration registration) {
        synchronized (registrationListLock) {
            registrationList.add(registration);
        }
    }

    protected void dispatch(final Envelope envelope, final List<IRegistration> handlerList) {
        new Thread() {

            public void run() {
                for (IRegistration handler : handlerList) {
                    try {
                        handler.handle(envelope);
                    } catch (Exception e) {
                        handler.handleFailed(envelope, e);
                    }
                }
            }
        }.run();
    }
}
