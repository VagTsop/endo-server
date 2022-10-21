package org.endofusion.endoserver.request;

import java.util.Date;

public class InstrumentRequest extends GenericRequest {

    private String instrumentRef;

    private String instrumentLot;

    private String instrumentManufacturer;

    private Date instrumentPurchaseDate;

    private String instrumentNotes;

    public String getInstrumentRef() {
        return instrumentRef;
    }

    public void setInstrumentRef(String instrumentRef) {
        this.instrumentRef = instrumentRef;
    }

    public String getInstrumentLot() {
        return instrumentLot;
    }

    public void setInstrumentLot(String instrumentLot) {
        this.instrumentLot = instrumentLot;
    }

    public String getInstrumentManufacturer() {
        return instrumentManufacturer;
    }

    public void setInstrumentManufacturer(String instrumentManufacturer) {
        this.instrumentManufacturer = instrumentManufacturer;
    }

    public Date getInstrumentPurchaseDate() {
        return instrumentPurchaseDate;
    }

    public void setInstrumentPurchaseDate(Date instrumentPurchaseDate) {
        this.instrumentPurchaseDate = instrumentPurchaseDate;
    }

    public String getInstrumentNotes() {
        return instrumentNotes;
    }

    public void setInstrumentNotes(String instrumentNotes) {
        this.instrumentNotes = instrumentNotes;
    }
}