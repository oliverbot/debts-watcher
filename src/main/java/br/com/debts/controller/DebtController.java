package br.com.debts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.debts.dto.TotalDebts;
import br.com.debts.service.DebtService;
import br.com.debts.util.DebtEntryFilter;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class DebtController {

    private final static Logger LOG = LoggerFactory.getLogger(DebtController.class);

    @Autowired
    private DebtService service;

    @ApiOperation(value = "Encontra a prova de acordo com o seu Id")
    @GetMapping(value = "/getTotalDebtsData")
    public ResponseEntity<?> getTotalDebtsData(@RequestParam("getCurrentDebts") Boolean getCurrentDebts)
            throws Exception {
            
        TotalDebts data;

        try {
            data = service.getTotalDebts(getCurrentDebts);
            return ResponseEntity.ok(data);
        } catch (Exception e) {

            LOG.info("//----------------------------//");
            LOG.info("\r\n ERROR: " + e.getMessage() + "\r\n CAUSED BY: " + e.getCause());
            LOG.info("//----------------------------//");

            return ResponseEntity.status(500).body("Internal Server Error.");
        }

    }

    @ApiOperation(value = "Encontra a prova de acordo com o seu Id")
    @GetMapping(value = "/getDebts")
    public ResponseEntity<?> getDebts(@RequestParam("getCurrentDebts") Boolean getCurrentDebts, @RequestBody DebtEntryFilter filter)
            throws Exception {

                return ResponseEntity.ok().body(service.getDebtEntries(getCurrentDebts, filter));
            }

}
