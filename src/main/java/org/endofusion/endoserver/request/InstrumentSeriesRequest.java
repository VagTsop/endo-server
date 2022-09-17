package org.endofusion.endoserver.request;

import java.util.Collection;

public class InstrumentSeriesRequest {

    private Long instrumentSeriesCode;

    private Collection<String> connectedInstrumentsIds;

    private Collection<String> unconnectedInstrumentsIds;

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public Collection<String> getConnectedInstrumentsIds() {
        return connectedInstrumentsIds;
    }

    public void setConnectedInstrumentsIds(Collection<String> connectedInstrumentsIds) {
        this.connectedInstrumentsIds = connectedInstrumentsIds;
    }

    public Collection<String> getUnconnectedInstrumentsIds() {
        return unconnectedInstrumentsIds;
    }

    public void setUnconnectedInstrumentsIds(Collection<String> unconnectedInstrumentsIds) {
        this.unconnectedInstrumentsIds = unconnectedInstrumentsIds;
    }
}
