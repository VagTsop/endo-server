package org.endofusion.endoserver.response;

import org.endofusion.endoserver.dto.InstrumentSeriesDetails;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.endofusion.endoserver.request.GenericRequest;

import java.util.Collection;
import java.util.List;

public class InstrumentSeriesResponse extends GenericRequest {

    private Long instrumentSeriesId;

    private Long instrumentsCount;

    private String instrumentSeriesCode;

    private String instrumentLot;

    private List<InstrumentSeriesDetails> instrumentSeriesDetails;

    private Collection<String> connectedInstrumentsIds;

    public InstrumentSeriesResponse() {
    }

    public InstrumentSeriesResponse(Long id, String instrumentSeriesCode, List<InstrumentSeriesDetails> instrumentSeriesDetails) {
        this.id = id;
        this.instrumentSeriesCode = instrumentSeriesCode;
        this.instrumentSeriesDetails = instrumentSeriesDetails;
    }

    public Long getInstrumentSeriesId() {
        return instrumentSeriesId;
    }

    public void setInstrumentSeriesId(Long instrumentSeriesId) {
        this.instrumentSeriesId = instrumentSeriesId;
    }

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

    public List<InstrumentSeriesDetails> getInstrumentSeriesDetails() {
        return instrumentSeriesDetails;
    }

    public void setInstrumentSeriesDetails(List<InstrumentSeriesDetails> instrumentSeriesDetails) {
        this.instrumentSeriesDetails = instrumentSeriesDetails;
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

    public InstrumentSeriesResponse(InstrumentSeriesDto instrumentSeriesDto) {
        this.setInstrumentSeriesCode(instrumentSeriesDto.getInstrumentSeriesCode());
        this.setConnectedInstrumentsIds(instrumentSeriesDto.getConnectedInstrumentsIds());
        this.setName(instrumentSeriesDto.getName());
        this.setDescription(instrumentSeriesDto.getDescription());
        this.setInstrumentLot(instrumentSeriesDto.getInstrumentLot());
        this.setInstrumentsCount(instrumentSeriesDto.getInstrumentsCount());
    }
}
