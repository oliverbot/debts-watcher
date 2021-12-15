package br.com.debts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.debts.repository.DebtEntryRepository;
import br.com.debts.repository.DebtSourceRepository;
import br.com.debts.repository.DebtTagRepository;

public class DebtService {

    private final static Logger LOG = LoggerFactory.getLogger(DebtService.class);

    @Autowired
	private DebtEntryRepository debtEntryRepo;

    @Autowired
    private DebtSourceRepository debtSourceRepo;

    @Autowired
    private DebtTagRepository debtTagRepo;


    

    
}
