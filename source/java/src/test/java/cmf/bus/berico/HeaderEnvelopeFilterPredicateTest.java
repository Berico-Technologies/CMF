package cmf.bus.berico;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.Envelope;

public class HeaderEnvelopeFilterPredicateTest {

    @Mock
    private Envelope envelope;
    private Map<String, String> envelopeHeaders;
    private HeaderEnvelopeFilterPredicate filterPredicate;
    private Map<String, String> headers;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<Map<String, String>>() {

            @Override
            public Map<String, String> answer(InvocationOnMock invocation) throws Throwable {
                return envelopeHeaders;
            }
        }).when(envelope).getHeaders();

        headers = new HashMap<String, String>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        filterPredicate = new HeaderEnvelopeFilterPredicate(headers);
        envelopeHeaders = new HashMap<String, String>();
        envelopeHeaders.put("key1", "value1");
        envelopeHeaders.put("key2", "value2");
    }

    @Test
    public void fewerKeysReturnsFalse() {
        headers.remove("key2");
        assertFalse(filterPredicate.filter(envelope));
    }

    @Test
    public void matchingKeyValuesReturnsTrue() {
        assertTrue(filterPredicate.filter(envelope));
    }

    @Test
    public void missingKeyReturnsFalse() {
        headers.remove("key2");
        headers.put("key2b", "value2b");
        assertFalse(filterPredicate.filter(envelope));
    }

    @Test
    public void moreKeysReturnsFalse() {
        headers.put("key2b", "value2b");
        assertFalse(filterPredicate.filter(envelope));
    }

    @Test
    public void wrongValueReturnsFalse() {
        headers.put("key2", "value2b");
        assertFalse(filterPredicate.filter(envelope));
    }
}
