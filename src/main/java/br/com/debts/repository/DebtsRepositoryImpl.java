package br.com.debts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.debts.util.DebtSourceFilter;
import br.com.debts.util.Filter;


@Component
@SuppressWarnings("unchecked")
public class DebtsRepositoryImpl {

    static final Logger LOG = LoggerFactory.getLogger(DebtsRepositoryImpl.class);

	@Autowired
	private EntityManager em;
    

    public Float getDebtsByDebtSourceId(Long id, DebtSourceFilter filter) {
        try {
            
            String nativeQueryString = this.queryBuilder(filter);

            Query query = this.em.createNativeQuery(nativeQueryString);

            query.setParameter("ID", id);

            Double queryResult = (Double)query.getSingleResult();

            if (queryResult == null) return 0f;

            return queryResult.floatValue();

        } catch (Exception e) {
            LOG.error("ERROR: Exception executing QUERY from getDebtsByDebtSourceId.");
            LOG.error("PARAMETERS: ID: " + id + ", getCurrent: " + filter.getCurrentDebt() + ", getLimit: " + filter.getCardLimit() + ", filter: " + filter.getScopeFilter());
            LOG.error("MESSAGE: " + e.getMessage());
            LOG.error("CAUSE: " + e.getCause());
        }
        
        return 0f;
    }


    private String queryBuilder(DebtSourceFilter filter) {
        StringBuilder sql = new StringBuilder();

        sql.append(this.getQueryLimit(filter));
        sql.append(this.getQueryScope(filter));
        sql.append(this.getCurrentEntries(filter));

        String nativeQueryString = sql.toString();

        return nativeQueryString;

    }

    private StringBuilder getQueryLimit(DebtSourceFilter filter) {

        StringBuilder sql = new StringBuilder();

        if (filter.getCardLimit()) {
            sql.append("SELECT IF(ds.is_credit_card is true, ds.credit_card_limit - sum(((de.installment_total - de.installment_number) + 1) * de.price), 0) ");
        } else {
            sql.append("SELECT sum(de.price) ");
        }

            sql.append("FROM debt_entries de ");

            sql.append("INNER JOIN debt_source ds ON ds.id = de.debt_source_id ");

        return sql;
    }

    private StringBuilder getQueryScope(DebtSourceFilter filter) {

        StringBuilder sql = new StringBuilder();
        
        switch(filter.getScopeFilter()) {
            case EXTERNAL:
                sql.append("WHERE de.is_external IS TRUE AND ds.id = :ID ");
                break;
            case INTERNAL:
                sql.append("WHERE de.is_external IS FALSE AND ds.id = :ID ");
                break;
            case ALL:
                sql.append("WHERE ds.id = :ID ");
                break;
        }

        return sql;
    }

    private StringBuilder getCurrentEntries(DebtSourceFilter filter) {

        StringBuilder sql = new StringBuilder();
        
        sql.append("AND de.date_added ");

            sql.append("BETWEEN ");

                if (filter.getCurrentDebt()) {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ");
                } else {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',IF(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') ");
                }

            sql.append("AND ");
                    
                if (filter.getCurrentDebt()) {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ");
                } else {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ");
                }

        return sql;
    }

	    
}
