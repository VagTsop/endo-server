package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;

import java.util.List;

public interface InstrumentSeriesService {

    List<InstrumentSeriesResponse> getInstrumentSeriesList();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();

    List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(String qrCode);

    List<InstrumentDto> fetchAvailableInstruments();

    long createInstrumentSeries(InstrumentSeriesDto dto);

    boolean updateInstrumentSeries(InstrumentSeriesDto dto);

    List<InstrumentDto> getInstrumentSeriesById(long id);

    boolean deleteInstrumentSeries(Long id);
}
