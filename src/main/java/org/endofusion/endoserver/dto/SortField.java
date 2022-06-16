package org.endofusion.endoserver.dto;

public class SortField {
    public enum Field {
        INSTRUMENT_NAME("i.name"),
        INSTRUMENT_REF("i.ref"),
        INSTRUMENT_LOT("i.lot"),
        INSTRUMENT_MANUFACTURER("i.manufacturer"),
        INSTRUMENT_PURCHASE_DATE("i.purchase_date"),
        INSTRUMENT_SERIES_CODE("os.instrument_series_qr_code");

        private String value;

        Field(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
