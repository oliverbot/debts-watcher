package br.com.debts.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "debt_entries")
public class DebtEntry implements Serializable {

    @Id
	@Column(name = "id")
    private Long id;

    private String name;

    private Float price;

    @Column(name = "is_external")
    private Boolean isExternal;

    @Column(name = "installment_number")
    private Long installmentNumber;

    @Column(name = "installment_total")
    private Long installmentTotal;

    @JsonIgnore
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="debt_source_id")
    private DebtSource debtSource;

    private String description;
    
}
