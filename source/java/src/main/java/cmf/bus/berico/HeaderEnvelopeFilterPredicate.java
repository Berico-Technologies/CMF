package cmf.bus.berico;

import java.util.Iterator;
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
        Iterator<Entry<String, String>> filterIterator = filterHeaders.entrySet().iterator();
        boolean filter = envelope.getHeaders().size() == filterHeaders.size();
        while (filter && filterIterator.hasNext()) {
            Entry<String, String> filterEntry = filterIterator.next();
            String filterValue = filterEntry.getValue();
            String envelopeValue = envelope.getHeaders().get(filterEntry.getKey());
            filter = StringUtils.equals(filterValue, envelopeValue);
        }

        return filter;
    }

    public void setFilterHeaders(Map<String, String> filterHeaders) {
        this.filterHeaders = filterHeaders;
    }
}
