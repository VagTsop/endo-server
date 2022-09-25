package org.endofusion.endoserver.dto;

public class InstrumentSeriesDetails {

    String instrumentName;
    String instrumentDescription;
    String instrumentLot;
    Long instrumentsCount;

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
}
