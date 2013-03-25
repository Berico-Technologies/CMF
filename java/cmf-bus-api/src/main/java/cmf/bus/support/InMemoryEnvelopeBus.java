package cmf.bus.support;

import java.util.LinkedList;
import java.util.List;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;

/**
 * An in-memory implementation of an envelope bus, suitable for use in
 * test harnesses and integration testing.
 */
public class InMemoryEnvelopeBus implements IEnvelopeBus {

    protected List<IRegistration> registrationList;

    /**
     * Creates a new InMemoryEnvelopeBus.
     */
    public InMemoryEnvelopeBus() {
        registrationList = new LinkedList<IRegistration>();
    }

    /**
     * Dispatches an envelope to a list of registration using a seprate thread.  
     * This method underlies the Send method and may be overriden to provide custom dispatching.
     * 
     * @param envelope The envelope to dispatch
     * @param registrationList The list of registrations to dispatch the envelope to.
     */
    protected void dispatch(final Envelope envelope, final List<IRegistration> registrationList) {
        new Thread() {

            @Override
            public void run() {
                for (IRegistration registration : registrationList) {
                    try {
                        registration.handle(envelope);
                    } catch (Exception e) {
                        try {
                            registration.handleFailed(envelope, e);
                        } catch (Exception failedToFail) {
                            failedToFail.printStackTrace();
                        }
                    }
                }
            }
        }.run();
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public synchronized void register(IRegistration registration) {
        registrationList.add(registration);
    }

    @Override
    public synchronized void send(Envelope envelope) {
        List<IRegistration> registrations = new LinkedList<IRegistration>();
        for (IRegistration registration : registrationList) {
            if (null != registration.getFilterPredicate() && !registration.getFilterPredicate().filter(envelope)) {

                registrations.add(registration);
            } else {
                registrations.add(registration);
            }
        }
        dispatch(envelope, registrations);
    }

    @Override
    public synchronized void unregister(IRegistration registration) {
        registrationList.remove(registration);
    }
}
