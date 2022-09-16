package org.endofusion.endoserver.response;

import org.endofusion.endoserver.dto.InstrumentSeriesDetails;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.endofusion.endoserver.request.GenericRequest;

import java.util.Collection;
import java.util.List;

public class InstrumentSeriesResponse extends GenericRequest {

    private Long instrumentSeriesId;

    private Long instrumentsCount;

    private Long instrumentSeriesCode;

    private List<InstrumentSeriesDetails> instrumentSeriesDetails;

    private Collection<Long> connectedInstrumentsIds;

    public InstrumentSeriesResponse() {
    }

    public InstrumentSeriesResponse(Long id, Long instrumentSeriesCode, List<InstrumentSeriesDetails> instrumentSeriesDetails) {
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

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public Collection<Long> getConnectedInstrumentsIds() {
        return connectedInstrumentsIds;
    }

    public void setConnectedInstrumentsIds(Collection<Long> connectedInstrumentsIds) {
        this.connectedInstrumentsIds = connectedInstrumentsIds;
    }

    public List<InstrumentSeriesDetails> getInstrumentSeriesDetails() {
        return instrumentSeriesDetails;
    }

    public void setInstrumentSeriesDetails(List<InstrumentSeriesDetails> instrumentSeriesDetails) {
        this.instrumentSeriesDetails = instrumentSeriesDetails;
    }

    public Long getInstrumentsCount() {
        return instrumentsCount;
    }

    public void setInstrumentsCount(Long instrumentsCount) {
        this.instrumentsCount = instrumentsCount;
    }

    public InstrumentSeriesResponse(InstrumentSeriesDto instrumentSeriesDto) {
        this.setInstrumentSeriesCode(instrumentSeriesDto.getInstrumentSeriesCode());
        this.setConnectedInstrumentsIds(instrumentSeriesDto.getInstrumentIdsList());
        this.setName(instrumentSeriesDto.getName());
        this.setDescription(instrumentSeriesDto.getDescription());
        this.setInstrumentsCount(instrumentSeriesDto.getInstrumentsCount());
    }
}
