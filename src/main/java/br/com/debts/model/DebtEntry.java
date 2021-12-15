package br.com.debts.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "debt_entries")
public class DebtEntry {

    @Id
	@Column(name = "id")
    private Long id;

    private String name;

    private Float price;

    @Column(name = "is_father")
    private Boolean isFather;

    @Column(name = "installment_number")
    private Long installmentNumber;

    @Column(name = "installment_total")
    private Long installmentTotal;

    @Column(name = "pay_date")
    private Date payDate;

    @Column(name = "is_recurrent")
    private Boolean isRecurrent;

    @Column(name = "is_subscription")
    private Boolean isSubscription;

    @Column(name = "is_installment")
    private Boolean isInstallment;

    @Column(name = "date_added")
    private Date dateAdded;
    
}
