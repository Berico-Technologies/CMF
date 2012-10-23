package cmf.eventing.patterns.rpc;

import java.util.Collection;

import org.joda.time.Duration;

public interface IRpcReceiver {

    Object getResponseTo(Object request, Duration timeout, String expectedTopic);

    <TResponse> TResponse getResponseTo(Object request, Duration timeout, Class<TResponse> expectedType);

    @SuppressWarnings("rawtypes")
    Collection gatherResponsesTo(Object request, Duration timeout, String... expectedTopics);

    <TResponse> Collection<TResponse> gatherResponsesTo(Object request, Duration timeout);
}
