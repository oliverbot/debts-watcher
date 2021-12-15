package br.com.debts.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.debts.model.DebtTag;

public interface DebtTagRepository extends CrudRepository<DebtTag, Long> {
    
    Optional<DebtTag> findById(Long id);
}
