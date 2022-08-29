package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    public List<InstrumentDto> fetchInstruments() {
        return instrumentRepository.fetchInstruments();
    }

    @Override
    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {
        return instrumentRepository.fetchInstrumentsSeriesCodes();
    }

    @Override
    public List<InstrumentDto> fetchInstrumentsByInstrumentSeriesCode(long qrCode) {
        return instrumentRepository.fetchInstrumentsByInstrumentSeriesCode(qrCode);
    }

    @Override
    public Page<InstrumentDto> getInstrumentsList(Pageable pageable, String name,
                                                  Date purchaseDateFrom,
                                                  Date purchaseDateTo, Collection<Long> instrumentSeriesCodesList) {
        return instrumentRepository.getInstrumentsList(pageable, new InstrumentDto(name, purchaseDateFrom, purchaseDateTo, instrumentSeriesCodesList));
    }

    @Override
    public long createInstrument(InstrumentDto dto) {

        return instrumentRepository.createInstrument(dto);
    }

    @Override
    public boolean updateInstrument(InstrumentDto dto) {

        return instrumentRepository.updateInstrument(dto);
    }

    @Override
    public InstrumentDto getInstrumentById(long id) {

        return instrumentRepository.getInstrumentById(id);
    }
    @Override
    public boolean deleteInstrument(Long id) {
        return instrumentRepository.deleteInstrument(id);
    }
}
