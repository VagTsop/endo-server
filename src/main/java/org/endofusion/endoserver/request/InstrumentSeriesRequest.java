package org.endofusion.endoserver.request;

import java.util.Collection;

public class InstrumentSeriesRequest {

    private String instrumentSeriesCode;

    private Collection<String> connectedInstrumentsIds;

    private Collection<String> unconnectedInstrumentsIds;

    public String getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(String instrumentSeriesCode) {
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
