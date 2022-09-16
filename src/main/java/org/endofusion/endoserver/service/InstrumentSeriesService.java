package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;

import java.util.List;

public interface InstrumentSeriesService {

    List<InstrumentSeriesResponse> getInstrumentSeriesList();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();

    List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(long qrCode);

    List<InstrumentDto> fetchAvailableInstruments();

    long createInstrumentSeries(InstrumentSeriesDto dto);

    boolean updateInstrumentSeries(InstrumentSeriesDto dto);

    InstrumentSeriesDto getInstrumentSeriesById(long id);
}
