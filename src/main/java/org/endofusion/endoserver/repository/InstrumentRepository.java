package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InstrumentRepository {

    List<InstrumentDto> fetchInstruments();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();

    Page<InstrumentDto> getInstrumentsList(Pageable pageable, InstrumentDto dto);

    long createInstrument(InstrumentDto instrumentDto);

    boolean updateInstrument(InstrumentDto instrumentDto);

    InstrumentDto getInstrumentById(long id);

    boolean deleteInstrument(Long id);
}
