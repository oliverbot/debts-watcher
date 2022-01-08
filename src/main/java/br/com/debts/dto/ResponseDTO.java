package br.com.debts.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDTO {

    private String status;

    private TotalDebts data;

    private List<?> errors;

    
}
