package cmf.bus.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cmf.bus.core.Envelope;
import cmf.bus.core.serializer.GsonSerializer;
import cmf.bus.core.serializer.ISerializer;

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
        envelope.setId(id);
        envelope.setPayload(payload);
    }

    @Test
    public void identicalEnvelopesAreEqual() {
        Envelope duplicate = new Envelope();
        duplicate.setId(id);
        duplicate.setPayload(payload);
        assertEquals(envelope, duplicate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingPayloadToNullThrows() {
        envelope.setPayload(null);
    }

}
