package br.com.debts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.debts.model.DebtEntry;

public interface DebtEntryRepository extends CrudRepository<DebtEntry, Long> {
    
    Optional<DebtEntry> findById(Long id);

    

}
