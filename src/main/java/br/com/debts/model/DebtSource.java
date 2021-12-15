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
@Table(name = "debt_source")
public class DebtSource {

    @Id
	@Column(name = "id")
    private Long id;

    @Column(name = "logo_url")
    private String logoURL;

    private String name;

    private String description;

    @Column(name = "date_added")
    private Date dateAdded;

    @Column(name = "is_credit_card")
    private Boolean isCreditCard;

    @Column(name = "credit_card_limit")
    private Long creditCardLimit;
    
}
