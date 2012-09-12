package cmf.bus.berico;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;

public class HeaderEnvelopeFilterPredicate implements IEnvelopeFilterPredicate {

    private Map<String, String> filterHeaders;

    public HeaderEnvelopeFilterPredicate() {

    }

    public HeaderEnvelopeFilterPredicate(Map<String, String> filterHeaders) {
        this.filterHeaders = filterHeaders;
    }

    @Override
    public boolean filter(Envelope envelope) {
        boolean filter = true;
        Map<String, String> envelopeHeaders = envelope.getHeaders();
        for (Entry<String, String> entry : filterHeaders.entrySet()) {
            String key = entry.getKey();
            if (!envelopeHeaders.containsKey(key)) {
                filter = false;
                break;
            }
            String envelopeValue = entry.getValue();
            String filterValue = filterHeaders.get(key);
            if (!StringUtils.equals(filterValue, envelopeValue)) {
                filter = false;
                break;
            }
        }

        return filter;
    }

    public void setFilterHeaders(Map<String, String> filterHeaders) {
        this.filterHeaders = filterHeaders;
    }
}
