package br.com.debts.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class ResponseCommons implements Serializable {

    private String status;

    private List<?> errors;

    
}
