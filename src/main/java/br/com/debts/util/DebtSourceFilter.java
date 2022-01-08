package br.com.debts.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebtSourceFilter {

    private Boolean currentDebt;
    private Boolean cardLimit;
    private Filter scopeFilter;
    
    
}