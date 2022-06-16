package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;

import java.util.List;

public interface InstrumentSeriesRepository {

    List<InstrumentSeriesDto> getInstrumentSeriesList();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();
}
