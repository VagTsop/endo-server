package org.endofusion.endoserver.controller;


import org.endofusion.endoserver.dto.InstrumentDto;
import org.endofusion.endoserver.request.InstrumentRequest;
import org.endofusion.endoserver.response.InstrumentResponse;
import org.endofusion.endoserver.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    @RequestMapping("/get-instruments-list")
    public ResponseEntity<Page<InstrumentDto>> getInstrumentList(
            Pageable pageable,
            @RequestParam Optional<String> name,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> purchaseDateFrom,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> purchaseDateTo,
            @RequestParam Optional<Collection<Long>> instrumentSeriesCodesList

    ) {
        Page<InstrumentDto> retVal = instrumentService.getInstrumentsList(pageable, name.orElse(null), purchaseDateFrom.orElse(null), purchaseDateTo.orElse(null), instrumentSeriesCodesList.orElse(null));
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-instruments")
    public ResponseEntity<List<InstrumentDto>> fetchInstruments() {
        List<InstrumentDto> retVal = instrumentService.fetchInstruments();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-instruments-series-codes")
    public ResponseEntity<List<InstrumentDto>> fetchInstrumentsSeriesCodes() {
        List<InstrumentDto> retVal = instrumentService.fetchInstrumentsSeriesCodes();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/get-instrument-by-id")
    public ResponseEntity<InstrumentResponse> getById(@RequestParam Long id) {
        InstrumentDto instrumentDto = instrumentService.getInstrumentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new InstrumentResponse(instrumentDto));
    }

    @RequestMapping(value = "/create-instrument", method = RequestMethod.POST)
    public ResponseEntity<Long> createInstrument(@RequestBody InstrumentRequest request) {
        InstrumentDto instrumentDto = new InstrumentDto(request, null, false);
        return ResponseEntity.status(HttpStatus.OK).body(instrumentService.createInstrument(instrumentDto));
    }

    @PutMapping("/update-instrument")
    public ResponseEntity<Boolean> update(@RequestParam long id, @RequestBody InstrumentRequest request) {
        InstrumentDto instrumentDto = new InstrumentDto(request, id, true);
        return ResponseEntity.status(HttpStatus.OK).body(instrumentService.updateInstrument(instrumentDto));
    }

    @RequestMapping("/delete-instrument")
    public ResponseEntity<Boolean> deleteInstrument(@RequestBody Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(instrumentService.deleteInstrument(id));
    }

}
