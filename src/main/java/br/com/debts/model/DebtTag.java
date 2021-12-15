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
@Table(name = "debt_tags")
public class DebtTag {

    @Id
	@Column(name = "id")
    private Long id;

    private String name;

    @Column(name = "date_added")
    private Date dateAdded;

}
