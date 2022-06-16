package org.endofusion.endoserver.dto;

public class InstrumentSeriesDetails {

    String instrumentName;
    String instrumentDescription;
    Long InstrumentsCount;

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentDescription() {
        return instrumentDescription;
    }

    public void setInstrumentDescription(String instrumentDescription) {
        this.instrumentDescription = instrumentDescription;
    }

    public Long getInstrumentsCount() {
        return InstrumentsCount;
    }

    public void setInstrumentsCount(Long instrumentsCount) {
        InstrumentsCount = instrumentsCount;
    }
}
