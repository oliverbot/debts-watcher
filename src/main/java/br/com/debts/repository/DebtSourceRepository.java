package br.com.debts.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.debts.model.DebtSource;
import br.com.debts.model.DebtEntry;

public interface DebtSourceRepository extends CrudRepository<DebtSource, Long> {

    static final Logger LOG = LoggerFactory.getLogger(DebtsRepositoryImpl.class);
    
    Optional<DebtSource> findById(Long id);

    List<DebtSource> findAll();


    // getCurrentMonthDebts

    @Query("SELECT sum(de.price) "
        +  "FROM DebtEntry de "
        +  "INNER JOIN de.debtSource ds "
        +  "WHERE ds.id = :ID "
        +  "AND de.dateAdded "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()),',',year(now())), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ")
    public Optional<Float> getCurrentDebtsByDebtSourceId(@Param("ID") Long id);

    @Query("SELECT sum(de.price) "
        +  "FROM DebtEntry de "
        +  "INNER JOIN de.debtSource ds "
        +  "WHERE de.isExternal IS TRUE AND ds.id = :ID "
        +  "AND de.dateAdded "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()),',',year(now())), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ")
    public Optional<Float> getCurrentExternalDebtsByDebtSourceId(@Param("ID") Long id);

    @Query("SELECT sum(de.price) "
        +  "FROM DebtEntry de "
        +  "INNER JOIN de.debtSource ds "
        +  "WHERE de.isExternal IS FALSE AND ds.id = :ID "
        +  "AND de.dateAdded "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()),',',year(now())), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ")
    public Optional<Float> getCurrentInternalDebtsByDebtSourceId(@Param("ID") Long id);

    @Query("SELECT ds.creditCardLimit - sum(((de.installmentTotal - de.installmentNumber) + 1) * de.price) "
        +  "FROM DebtEntry de "
        +  "INNER JOIN de.debtSource ds "
        +  "WHERE ds.id = :ID "
        +  "AND de.dateAdded "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()),',',year(now())), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.dueDay,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ")
    public Optional<Float> getCurrentAvailableLimitByDebtSourceId(@Param("ID") Long id);


    // getLastMonthDebts

    @Query(value = "SELECT sum(de.price) "
        +  "FROM debt_entries de "
        +  "INNER JOIN debt_source ds ON ds.id = de.debt_source_id "
        +  "WHERE ds.id = :ID "
        +  "AND de.date_added "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.due_day,',',IF(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ", nativeQuery = true)
    public Optional<Float> getLastDebtsByDebtSourceId(@Param("ID") Long id);

    @Query(value = "SELECT sum(de.price) "
        +  "FROM debt_entries de "
        +  "INNER JOIN debt_source ds ON ds.id = de.debt_source_id "
        +  "WHERE de.is_external IS TRUE AND ds.id = :ID "
        +  "AND de.date_added "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.due_day,',',if(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ", nativeQuery = true)
    public Optional<Float> getLastExternalDebtsByDebtSourceId(@Param("ID") Long id);

    @Query(value = "SELECT sum(de.price) "
        +  "FROM debt_entries de "
        +  "INNER JOIN debt_source ds ON ds.id = de.debt_source_id "
        +  "WHERE de.is_external IS FALSE AND ds.id = :ID "
        +  "AND de.date_added "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.due_day,',',if(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ", nativeQuery = true)
    public Optional<Float> getLastInternalDebtsByDebtSourceId(@Param("ID") Long id);

    @Query(value = "SELECT ds.credit_card_limit - sum(((de.installment_total - de.installment_number) + 1) * de.price) "
        +  "FROM debt_entries de "
        +  "INNER JOIN debt_source ds ON ds.id = de.debt_source_id "
        +  "WHERE ds.id = :ID "
        +  "AND de.date_added "
            +  "BETWEEN "
                +  "STR_TO_DATE(concat(ds.due_day,',',if(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') "
            +  "AND "
                +  "STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ", nativeQuery = true)
    public Optional<Float> getLastAvailableLimitByDebtSourceId(@Param("ID") Long id);
    
}
