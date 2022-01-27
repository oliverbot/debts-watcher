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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.debts.dto.ResponseAllDebts;
import br.com.debts.dto.TotalDebts;
import br.com.debts.model.DebtSource;
import br.com.debts.service.DebtService;
import br.com.debts.util.DebtEntryFilter;

@RestController
@RequestMapping("/api")
public class DebtController {

    private final static Logger LOG = LoggerFactory.getLogger(DebtController.class);

    @Autowired
    private DebtService service;

    @GetMapping(value = "/getTotalDebtsData")
    public ResponseEntity<?> getTotalDebtsData(@RequestParam("getCurrentDebts") Boolean getCurrentDebts)
            throws Exception {
        ResponseAllDebts response = new ResponseAllDebts();

        HttpStatus status = HttpStatus.OK;
        List<String> errors = new ArrayList<String>();
        TotalDebts data = TotalDebts.builder().build();

        try {
            data = service.getTotalDebts(getCurrentDebts);
        } catch (Exception e) {
            status = HttpStatus.valueOf(500);
            errors.add("ERROR FETCHING DATA");

            LOG.info("//----------------------------//");
            LOG.info("\r\n ERROR: " + e.getMessage() + "\r\n CAUSED BY: " + e.getCause());
            LOG.info("//----------------------------//");
        }

        if (errors.isEmpty()) {
            errors.add("NONE");
            response = this.buildResponseAllDebts("OK", errors, data);
        } else if (data == null) {
            errors.add("COULDN'T FIND RESOURCE");

            status = HttpStatus.NOT_FOUND;
            response = this.buildResponseAllDebts("ERROR", errors, data);
        } else {
            response = this.buildResponseAllDebts("ERROR", errors, data);
        }

        return ResponseEntity.status(status).body(response);
    }

    private ResponseAllDebts buildResponseAllDebts(String status, List<?> errors, TotalDebts data) {
        ResponseAllDebts response = new ResponseAllDebts();

        response.setStatus(status);
        response.setErrors(errors);
        response.setAllDebts(data);

        return response;
    }

    @GetMapping(value = "/getDebts")
    public ResponseEntity<?> getDebts(@RequestParam("getCurrentDebts") Boolean getCurrentDebts, @RequestBody DebtEntryFilter filter)
            throws Exception {

                return ResponseEntity.ok().body(service.getDebtEntries(getCurrentDebts, filter));
            }

}
