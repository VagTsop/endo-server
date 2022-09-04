package org.endofusion.endoserver.controller;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;
import org.endofusion.endoserver.service.InstrumentSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/instruments-series")
public class InstrumentSeriesController {

    @Autowired
    private InstrumentSeriesService instrumentSeriesService;

    @RequestMapping("/get-instrument-series-list")
    public ResponseEntity<List<InstrumentSeriesResponse>> getInstrumentList() {

        List<InstrumentSeriesResponse> dtos = instrumentSeriesService.getInstrumentSeriesList();

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/fetch-available-instruments")
    public ResponseEntity<List<InstrumentDto>> fetchAvailableInstruments() {
        List<InstrumentDto> retVal = instrumentSeriesService.fetchAvailableInstruments();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }
}
