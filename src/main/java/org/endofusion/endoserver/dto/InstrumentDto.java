package org.endofusion.endoserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endofusion.endoserver.request.GenericRequest;
import org.endofusion.endoserver.request.InstrumentRequest;

import java.util.Collection;
import java.util.Date;

public class InstrumentDto extends GenericRequest {

    private Long instrumentSeriesId;

    @JsonProperty("userPhoto")
    private byte[] userPhoto;

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

    private Long InstrumentsCount;

    public InstrumentDto() {
    }

    public InstrumentDto(String name, Date purchaseDateFrom, Date purchaseDateTo, Collection<Long> instrumentSeriesCodesList) {
        this.name = name;
        this.purchaseDateFrom = purchaseDateFrom;
        this.purchaseDateTo = purchaseDateTo;
        this.instrumentSeriesCodesList = instrumentSeriesCodesList;
    }

    public InstrumentDto(InstrumentRequest request, Long id, boolean isUpdate) {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setInstrumentRef(request.getInstrumentRef());
        this.setInstrumentLot(request.getInstrumentLot());
        this.setInstrumentManufacturer(request.getInstrumentManufacturer());
        this.setInstrumentPurchaseDate(request.getInstrumentPurchaseDate());
        this.setUserPhoto(request.getUserPhoto());
        this.setInstrumentNotes(request.getInstrumentNotes());
        if (isUpdate) {
            this.setId(id);
        }
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Long getInstrumentSeriesId() {
        return instrumentSeriesId;
    }

    public void setInstrumentSeriesId(Long instrumentSeriesId) {
        this.instrumentSeriesId = instrumentSeriesId;
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

    public Long getInstrumentsCount() {
        return InstrumentsCount;
    }

    public void setInstrumentsCount(Long instrumentsCount) {
        InstrumentsCount = instrumentsCount;
    }
}
