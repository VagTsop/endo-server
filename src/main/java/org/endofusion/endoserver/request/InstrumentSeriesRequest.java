package org.endofusion.endoserver.request;

import java.util.Collection;

public class InstrumentSeriesRequest {

    private Long instrumentSeriesCode;

    private Collection<Long> instrumentIdsList;

    private Collection<String> connectedInstrumentsIds;

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public Collection<Long> getInstrumentIdsList() {
        return instrumentIdsList;
    }

    public void setInstrumentIdsList(Collection<Long> instrumentIdsList) {
        this.instrumentIdsList = instrumentIdsList;
    }

    public Collection<String> getConnectedInstrumentsIds() {
        return connectedInstrumentsIds;
    }

    public void setConnectedInstrumentsIds(Collection<String> connectedInstrumentsIds) {
        this.connectedInstrumentsIds = connectedInstrumentsIds;
    }
}
