package cmf.eventing.patterns.rpc;

import java.util.Collection;

import org.joda.time.Duration;

public interface IRpcReceiver {

    <TResponse> Collection<TResponse> gatherResponsesTo(Object request, Duration timeout);

    @SuppressWarnings("rawtypes")
    Collection gatherResponsesTo(Object request, Duration timeout, String... expectedTopics);

    <TResponse> TResponse getResponseTo(Object request, Duration timeout, Class<TResponse> expectedType);

    Object getResponseTo(Object request, Duration timeout, String expectedTopic);
}
