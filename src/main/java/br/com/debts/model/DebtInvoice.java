package br.com.debts.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "debt_invoices")
public class DebtInvoice {

    @Id
	@Column(name = "id")
    private Long id;

    private int month;

    private int year;

    @OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="debt_source_id")
    private DebtSource debtSource;

    @Column(name = "is_paid")
    private Boolean isPaid;

}
