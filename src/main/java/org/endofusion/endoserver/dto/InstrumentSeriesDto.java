package org.endofusion.endoserver.dto;

import org.endofusion.endoserver.request.GenericRequest;

public class InstrumentSeriesDto extends GenericRequest {

    private Long instrumentSeriesCode;

    private Long instrumentsCount;

    public InstrumentSeriesDto() {
    }

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public Long getInstrumentsCount() {
        return instrumentsCount;
    }

    public void setInstrumentsCount(Long instrumentsCount) {
        this.instrumentsCount = instrumentsCount;
    }
}
