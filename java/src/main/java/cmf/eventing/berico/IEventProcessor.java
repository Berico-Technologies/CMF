package cmf.eventing.berico;

public interface IEventProcessor {

	void processEvent(EventContext context, IContinuationCallback continuation) throws Exception;
	
}
