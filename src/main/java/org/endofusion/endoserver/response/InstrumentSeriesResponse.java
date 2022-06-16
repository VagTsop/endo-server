package org.endofusion.endoserver.response;

import org.endofusion.endoserver.dto.InstrumentSeriesDetails;

import java.util.List;

public class InstrumentSeriesResponse {

    private Long instrumentSeriesId;

    private Long instrumentSeriesCode;

    private List<InstrumentSeriesDetails> instrumentSeriesDetails;

    public InstrumentSeriesResponse() {
    }

    public InstrumentSeriesResponse(Long instrumentSeriesCode, List<InstrumentSeriesDetails> instrumentSeriesDetails) {
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

    public List<InstrumentSeriesDetails> getInstrumentSeriesDetails() {
        return instrumentSeriesDetails;
    }

    public void setInstrumentSeriesDetails(List<InstrumentSeriesDetails> instrumentSeriesDetails) {
        this.instrumentSeriesDetails = instrumentSeriesDetails;
    }

}
