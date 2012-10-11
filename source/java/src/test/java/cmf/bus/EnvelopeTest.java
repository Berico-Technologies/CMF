package cmf.bus;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cmf.bus.eventing.ISerializer;
import cmf.bus.eventing.berico.GsonSerializer;

public class EnvelopeTest {

    private Envelope envelope;
    private String id = "id";
    private Object message = new Object();
    private byte[] payload;
    private ISerializer serializer = new GsonSerializer();

    @Before
    public void before() {
        payload = serializer.byteSerialize(message);
        envelope = new Envelope();
        envelope.setHeader(id, id);
        envelope.setPayload(payload);
    }

    @Test
    public void identicalEnvelopesAreEqual() {
        Envelope duplicate = new Envelope();
        duplicate.setHeader(id, id);
        duplicate.setPayload(payload);
        assertEquals(envelope, duplicate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingPayloadToNullThrows() {
        envelope.setPayload(null);
    }

}
