package org.endofusion.endoserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endofusion.endoserver.dto.InstrumentDto;

import java.util.Date;

public class InstrumentResponse {

    private Long instrumentId;
    private String instrumentName;
    private String instrumentDescription;
    private String instrumentRef;
    private String instrumentLot;
    private String instrumentManufacturer;
    private Date instrumentPurchaseDate;
    private String instrumentNotes;
    @JsonProperty("userPhoto")
    private byte[] userPhoto;


    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

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

    public InstrumentResponse(InstrumentDto instrumentDto) {
        this.setInstrumentId(instrumentDto.getInstrumentId());
        this.setInstrumentName(instrumentDto.getInstrumentName());
        this.setInstrumentDescription(instrumentDto.getInstrumentDescription());
        this.setInstrumentRef(instrumentDto.getInstrumentRef());
        this.setInstrumentLot(instrumentDto.getInstrumentLot());
        this.setInstrumentManufacturer(instrumentDto.getInstrumentManufacturer());
        this.setInstrumentPurchaseDate(instrumentDto.getInstrumentPurchaseDate());
        this.setUserPhoto(instrumentDto.getUserPhoto());
        this.setInstrumentNotes(instrumentDto.getInstrumentNotes());
    }
}
