package br.com.debts.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAllDebts extends ResponseCommons {

    public ResponseAllDebts() {
    }

    public ResponseAllDebts(String status, List<?> errors, TotalDebts allDebts) {
        this.setStatus(status);
        this.setErrors(errors);
        this.setAllDebts(allDebts);

    }

    private TotalDebts allDebts;

    
}
