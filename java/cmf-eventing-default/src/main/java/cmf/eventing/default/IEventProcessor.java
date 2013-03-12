package cmf.eventing.default;

public interface IEventProcessor {

	void processEvent(EventContext context, IContinuationCallback continuation) throws Exception;
	
}
