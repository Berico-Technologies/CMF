package cmf.bus.core.event;

import cmf.bus.core.DeliveryOutcome;

public interface IEventHandler<EVENT> {

    DeliveryOutcome receive(EVENT event);

}
