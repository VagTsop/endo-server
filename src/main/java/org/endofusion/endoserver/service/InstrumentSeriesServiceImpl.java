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
    public List<InstrumentSeriesDto> fetchInstrumentsByInstrumentSeriesCode(long qrCode) {
        return instrumentSeriesRepository.fetchInstrumentsByInstrumentSeriesCode(qrCode);
    }

    @Override
    public List<InstrumentSeriesResponse> getInstrumentSeriesList() {
        List<InstrumentSeriesResponse> instrumentSeriesResponseList = new ArrayList<>();
        List<InstrumentDto> instrumentDtos;
        List<InstrumentSeriesDetails> instrumentSeriesDetailsList = new ArrayList<>();
        List<InstrumentSeriesDto> instrumentSeriesDtos;
        Long instrumentSeriesCode;

        instrumentDtos = this.fetchInstrumentsSeriesCodes();

        instrumentSeriesDtos = instrumentSeriesRepository.getInstrumentSeriesList();
        InstrumentSeriesResponse instrumentSeriesResponse;

        for (InstrumentDto tempInstrumentDto : instrumentDtos) {
             instrumentSeriesCode = tempInstrumentDto.getInstrumentSeriesCode();


            instrumentSeriesDetailsList = new ArrayList<>();
            for (InstrumentSeriesDto tempInstrumentSeriesDto : instrumentSeriesDtos) {
                if (tempInstrumentSeriesDto.getInstrumentSeriesCode() != tempInstrumentDto.getInstrumentSeriesCode()) {
                    continue;
                } else {
                    InstrumentSeriesDetails instrumentSeriesDetails = new InstrumentSeriesDetails();
                    instrumentSeriesDetails.setInstrumentName(tempInstrumentSeriesDto.getName());
                    instrumentSeriesDetails.setInstrumentDescription(tempInstrumentSeriesDto.getDescription());
                    instrumentSeriesDetails.setInstrumentsCount(tempInstrumentSeriesDto.getInstrumentsCount());
                    instrumentSeriesDetailsList.add(instrumentSeriesDetails);
                }
            }
            instrumentSeriesResponse = new InstrumentSeriesResponse(instrumentSeriesCode, instrumentSeriesDetailsList);
            instrumentSeriesResponseList.add(instrumentSeriesResponse);
        }
        return instrumentSeriesResponseList;
    }
}
