package org.endofusion.endoserver.controller;

import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.response.InstrumentSeriesResponse;
import org.endofusion.endoserver.service.InstrumentSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/fetch-instruments-by-instrument-series-code")
    public ResponseEntity<List<InstrumentDto>> fetchInstrumentsByInstrumentSeriesCode(
            @RequestParam long qrCode) {
        List<InstrumentDto> retVal = instrumentSeriesService.fetchInstrumentsByInstrumentSeriesCode(qrCode);
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

}
