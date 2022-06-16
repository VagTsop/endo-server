package org.endofusion.endoserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endofusion.endoserver.request.InstrumentRequest;

import java.util.Collection;
import java.util.Date;

public class InstrumentDto {

    private Long instrumentId;

    private Long instrumentSeriesId;

    private String instrumentName;

    @JsonProperty("userPhoto")
    private byte[] userPhoto;

    private String instrumentDescription;

    private String instrumentRef;

    private String instrumentLot;

    private String instrumentManufacturer;

    private Date instrumentPurchaseDate;

    private String instrumentNotes;

    private Long instrumentSeriesCode;

    private Collection<Long> instrumentSeriesCodesList;

    private Date purchaseDateFrom;

    private Date purchaseDateTo;

    private Long instrumentSeriesQrCode;

    public InstrumentDto() {
    }

    public InstrumentDto(String instrumentName, Date purchaseDateFrom, Date purchaseDateTo, Collection<Long> instrumentSeriesCodesList) {
        this.instrumentName = instrumentName;
        this.purchaseDateFrom = purchaseDateFrom;
        this.purchaseDateTo = purchaseDateTo;
        this.instrumentSeriesCodesList = instrumentSeriesCodesList;
    }

    public InstrumentDto(InstrumentRequest request, Long id, boolean isUpdate) {
        this.setInstrumentName(request.getInstrumentName());
        this.setInstrumentDescription(request.getInstrumentDescription());
        this.setInstrumentRef(request.getInstrumentRef());
        this.setInstrumentLot(request.getInstrumentLot());
        this.setInstrumentManufacturer(request.getInstrumentManufacturer());
        this.setInstrumentPurchaseDate(request.getInstrumentPurchaseDate());
        this.setUserPhoto(request.getUserPhoto());
        this.setInstrumentNotes(request.getInstrumentNotes());
        if (isUpdate) {
            this.setInstrumentId(id);
        }
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Long getInstrumentSeriesId() {
        return instrumentSeriesId;
    }

    public void setInstrumentSeriesId(Long instrumentSeriesId) {
        this.instrumentSeriesId = instrumentSeriesId;
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

    public Long getInstrumentSeriesCode() {
        return instrumentSeriesCode;
    }

    public void setInstrumentSeriesCode(Long instrumentSeriesCode) {
        this.instrumentSeriesCode = instrumentSeriesCode;
    }

    public Collection<Long> getInstrumentSeriesCodesList() {
        return instrumentSeriesCodesList;
    }

    public void setInstrumentSeriesCodesList(Collection<Long> instrumentSeriesCodesList) {
        this.instrumentSeriesCodesList = instrumentSeriesCodesList;
    }

    public Date getPurchaseDateFrom() {
        return purchaseDateFrom;
    }

    public void setPurchaseDateFrom(Date purchaseDateFrom) {
        this.purchaseDateFrom = purchaseDateFrom;
    }

    public Date getPurchaseDateTo() {
        return purchaseDateTo;
    }

    public void setPurchaseDateTo(Date purchaseDateTo) {
        this.purchaseDateTo = purchaseDateTo;
    }

    public Long getInstrumentSeriesQrCode() {
        return instrumentSeriesQrCode;
    }

    public void setInstrumentSeriesQrCode(Long instrumentSeriesQrCode) {
        this.instrumentSeriesQrCode = instrumentSeriesQrCode;
    }
}
