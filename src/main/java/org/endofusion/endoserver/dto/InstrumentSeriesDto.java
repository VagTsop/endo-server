package org.endofusion.endoserver.dto;

import org.endofusion.endoserver.request.GenericRequest;
import org.endofusion.endoserver.request.InstrumentSeriesRequest;

import java.util.Collection;

public class InstrumentSeriesDto extends GenericRequest {

    private Long instrumentSeriesCode;

    private String instrumentLot;

    private Long instrumentsCount;

    private Collection<String> connectedInstrumentsIds;

    private Collection<Long> instrumentIdsList;


    public InstrumentSeriesDto() {
    }

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public String getInstrumentLot() {
        return instrumentLot;
    }

    public void setInstrumentLot(String instrumentLot) {
        this.instrumentLot = instrumentLot;
    }

    public Long getInstrumentsCount() {
        return instrumentsCount;
    }

    public void setInstrumentsCount(Long instrumentsCount) {
        this.instrumentsCount = instrumentsCount;
    }

    public Collection<String> getConnectedInstrumentsIds() {
        return connectedInstrumentsIds;
    }

    public void setConnectedInstrumentsIds(Collection<String> connectedInstrumentsIds) {
        this.connectedInstrumentsIds = connectedInstrumentsIds;
    }

    public Collection<Long> getInstrumentIdsList() {
        return instrumentIdsList;
    }

    public void setInstrumentIdsList(Collection<Long> instrumentIdsList) {
        this.instrumentIdsList = instrumentIdsList;
    }

    public InstrumentSeriesDto(InstrumentSeriesRequest request, Long id, boolean isUpdate) {
        this.instrumentSeriesCode = (request.getInstrumentSeriesCode());
        this.connectedInstrumentsIds = (request.getConnectedInstrumentsIds());
    }
}
