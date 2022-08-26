package org.endofusion.endoserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.request.GenericRequest;

import java.util.Date;

public class InstrumentResponse extends GenericRequest {

    private String instrumentRef;
    private String instrumentLot;
    private String instrumentManufacturer;
    private Date instrumentPurchaseDate;
    private String instrumentNotes;
    @JsonProperty("userPhoto")
    private byte[] userPhoto;

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
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
        this.setId(instrumentDto.getId());
        this.setName(instrumentDto.getName());
        this.setDescription(instrumentDto.getDescription());
        this.setInstrumentRef(instrumentDto.getInstrumentRef());
        this.setInstrumentLot(instrumentDto.getInstrumentLot());
        this.setInstrumentManufacturer(instrumentDto.getInstrumentManufacturer());
        this.setInstrumentPurchaseDate(instrumentDto.getInstrumentPurchaseDate());
        this.setUserPhoto(instrumentDto.getUserPhoto());
        this.setInstrumentNotes(instrumentDto.getInstrumentNotes());
    }
}
