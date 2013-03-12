package cmf.eventing;

import java.util.Map;

import cmf.bus.Envelope;

public interface IEventHandler<TEVENT> {

    Class<TEVENT> getEventType();

    Object handle(TEVENT event, Map<String, String> headers);

    Object handleFailed(Envelope envelope, Exception e);
}
