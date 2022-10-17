package org.endofusion.endoserver.service;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.dto.InstrumentSeriesDetails;
import org.endofusion.endoserver.dto.InstrumentSeriesDto;
import org.endofusion.endoserver.repository.InstrumentSeriesRepository;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstrumentSeriesServiceImpl implements InstrumentSeriesService {


    @Autowired
    private InstrumentSeriesRepository instrumentSeriesRepository;

    @Override
    public List<InstrumentDto> fetchInstrumentsSeriesCodes() {

        return instrumentSeriesRepository.fetchInstrumentsSeriesCodes();
    }

    @Override
    public List<InstrumentDto> fetchAvailableInstruments() {
        return instrumentSeriesRepository.fetchAvailableInstruments();
    }

    @Override
    public long createInstrumentSeries(InstrumentSeriesDto dto) {
        return instrumentSeriesRepository.createInstrumentSeries(dto);
    }

    @Override
    public boolean updateInstrumentSeries(InstrumentSeriesDto dto) {
        return instrumentSeriesRepository.updateInstrumentSeries(dto);
    }

    @Override
    public List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(String qrCode) {
        return instrumentSeriesRepository.fetchInstrumentsByInstrumentSeriesCode(qrCode);
    }

    @Override
    public List<InstrumentSeriesResponse> getInstrumentSeriesList() {
        List<InstrumentSeriesResponse> instrumentSeriesResponseList = new ArrayList<>();
        List<InstrumentDto> instrumentDtos = this.fetchInstrumentsSeriesCodes();
        List<InstrumentSeriesDto> instrumentSeriesDtos = instrumentSeriesRepository.getInstrumentSeriesList();

        // loop list with instrumentCodes
        for (InstrumentDto tempInstrumentDto : instrumentDtos) {
             Long id = tempInstrumentDto.getId();
             String instrumentSeriesCode = tempInstrumentDto.getInstrumentSeriesCode(); // get instrument code
             List<InstrumentSeriesDetails> instrumentSeriesDetailsList = new ArrayList<>(); // create instrument details list
            for (InstrumentSeriesDto tempInstrumentSeriesDto : instrumentSeriesDtos) {
                if (!tempInstrumentSeriesDto.getInstrumentSeriesCode().equals(instrumentSeriesCode)) {
                    continue;
                } else {
                    InstrumentSeriesDetails instrumentSeriesDetails = new InstrumentSeriesDetails();
                    instrumentSeriesDetails.setInstrumentName(tempInstrumentSeriesDto.getName());
                    instrumentSeriesDetails.setInstrumentDescription(tempInstrumentSeriesDto.getDescription());
                    instrumentSeriesDetails.setInstrumentLot(tempInstrumentSeriesDto.getInstrumentLot());
                    instrumentSeriesDetails.setInstrumentsCount(tempInstrumentSeriesDto.getInstrumentsCount());
                    instrumentSeriesDetailsList.add(instrumentSeriesDetails);
                }
            }
            InstrumentSeriesResponse instrumentSeriesResponse = new InstrumentSeriesResponse(id,instrumentSeriesCode, instrumentSeriesDetailsList);
            instrumentSeriesResponseList.add(instrumentSeriesResponse);
        }
        return instrumentSeriesResponseList;
    }

    @Override
    public List<InstrumentDto> getInstrumentSeriesById(long id) {
        return instrumentSeriesRepository.getInstrumentSeriesById(id);
    }

    @Override
    public boolean deleteInstrumentSeries(Long id) {
        return instrumentSeriesRepository.deleteInstrumentSeries(id);
    }
}
