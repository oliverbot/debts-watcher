package br.com.debts.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.debts.model.DebtSource;

public interface DebtSourceRepository extends CrudRepository<DebtSource, Long> {

    Optional<DebtSource> findById(Long id);
    
}
