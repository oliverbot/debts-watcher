package br.com.debts.util;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebtEntryFilter {

    private Long id;
    private String name;
    private Float price;
    private Boolean isExternal;
    private Long installmentNumber;
    private Long installmentTotal;
    private Boolean isRecurrent;
    private Boolean isSubscription;
    private Boolean isInstallment;
    private Long debtSourceId;
    private Date dateAdded;

}