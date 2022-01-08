package br.com.debts.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebtsByDebtSource {

    private String logoURL;
    private String name;
    private Long creditCardLimit;
    private Float totalDebt;
    private Float totalDebtExternal;
    private Float totalDebtInternal;
    private Float availableLimit;

}
