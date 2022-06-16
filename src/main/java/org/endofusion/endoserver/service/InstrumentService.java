package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface InstrumentService {

    List<InstrumentDto> fetchInstruments();

    List<InstrumentDto> fetchInstrumentsSeriesCodes();

    List<InstrumentDto> fetchInstrumentsByInstrumentSeriesCode(long qrCode);

    Page<InstrumentDto> getInstrumentsList(Pageable pageable, String instrumentName, Date purchaseDateFrom, Date purchaseDateTo, Collection<Long> instrumentSeriesCodesList);

    long createInstrument(InstrumentDto dto);

    boolean updateInstrument(InstrumentDto dto);

    InstrumentDto getInstrumentById(long id);

    boolean deleteInstrument(Long id);
}



