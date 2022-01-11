package br.com.debts.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.debts.dto.ResponseDTO;
import br.com.debts.dto.TotalDebts;
import br.com.debts.model.DebtSource;
import br.com.debts.service.DebtService;

@RestController
@RequestMapping("/api")
public class DebtController {

    private final static Logger LOG = LoggerFactory.getLogger(DebtController.class);

    @Autowired
    private DebtService service;

    @GetMapping(value = "/getTotalDebtsData")
    public ResponseEntity<?> getTotalDebtsData(@RequestParam("getCurrentDebts") Boolean getCurrentDebts)
            throws Exception {
        ResponseDTO response = ResponseDTO.builder().build();
        HttpStatus status = HttpStatus.OK;
        TotalDebts data = TotalDebts.builder().build();
        List<String> errors = new ArrayList<String>();

        try {
            data = service.getTotalDebts(getCurrentDebts);
        } catch (Exception e) {
            status = HttpStatus.valueOf(500);
            errors.add("ERROR FETCHING DATA");
            LOG.info("\r\n ERROR: " + e.getMessage() + "\r\n CAUSED BY: " + e.getCause());
        }

        if (errors.isEmpty()) {
            errors.add("NONE");
            response = ResponseDTO.builder()
                    .status("OK")
                    .data(data)
                    .errors(errors)
                    .build();
        } else if (data == null) {
            errors.add("COULDN'T FIND RESOURCE");

            status = HttpStatus.NOT_FOUND;
            response = ResponseDTO.builder()
                    .status("ERROR")
                    .data(data)
                    .errors(errors)
                    .build();
        } else {
            response = ResponseDTO.builder()
                    .status("ERROR")
                    .data(data)
                    .errors(errors)
                    .build();
        }

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(value = "/getDebts")
    public ResponseEntity<?> getDebts(@RequestParam("getCurrentDebts") Boolean getCurrentDebts)
            throws Exception {

                return ResponseEntity.status(200).build();
            }

}
