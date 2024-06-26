package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;

import java.util.List;

public interface InstrumentSeriesService {

    List<InstrumentSeriesResponse> getInstrumentSeriesList();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();

}
