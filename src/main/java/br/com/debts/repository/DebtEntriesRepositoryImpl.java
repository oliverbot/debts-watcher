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

import br.com.debts.util.DebtEntryFilter;
import br.com.debts.util.DebtSourceFilter;
import br.com.debts.util.Filter;


@Component
@SuppressWarnings("unchecked")
public class DebtEntriesRepositoryImpl {

    static final Logger LOG = LoggerFactory.getLogger(DebtsRepositoryImpl.class);

	@Autowired
	private EntityManager em;
    

    public Float getDebtsByDebtSourceId(Long id, DebtEntryFilter filter) {
        try {
            
            String nativeQueryString = this.queryBuilder(filter);

            Query query = this.em.createNativeQuery(nativeQueryString);

            query.setParameter("ID", id);

            Double queryResult = (Double)query.getSingleResult();

            if (queryResult == null) return 0f;

            return queryResult.floatValue();

        } catch (Exception e) {
            LOG.error("ERROR: Exception executing QUERY from getDebtsByDebtSourceId.");
            LOG.error("MESSAGE: " + e.getMessage());
            LOG.error("CAUSE: " + e.getCause());
        }
        
        return 0f;
    }


    private String queryBuilder(DebtEntryFilter filter) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT de FROM DebtEntry de");
        sql.append("WHERE 1=1");
        sql.append(this.getQueryScope(filter));

        String nativeQueryString = sql.toString();

        return nativeQueryString;

    }

    private StringBuilder getQueryScope(DebtEntryFilter filter) {

        StringBuilder sql = new StringBuilder();

        if (filter.getId() != null)
            sql.append("AND de.id = :ID ");

        if (filter.getName() != null)
            sql.append("AND de.name rlike :NAME ");

        if (filter.getIsExternal() != null)
            sql.append("AND de.isExternal IS :IS_EXTERNAL ");

        if (filter.getInstallmentNumber() != null)
            sql.append("AND de.installmentNumber = :INSTALLMENT ");

        if (filter.getInstallmentTotal() != null)
            sql.append("AND de.installmentTotal = :INSTALLMENT_TOTAL ");
        
        if (filter.getIsRecurrent() != null) 
            sql.append("AND de.isRecurrent IS :IS_RECURRENT ");

        if (filter.getDateAdded() != null)
            sql.append("AND de.dateAdded = :DATE ");

        if (filter.getIsSubscription() != null)
            sql.append("AND de.isSubscription IS IS_SUBSCRIPTION ");

        if (filter.getIsInstallment() != null)
            sql.append("AND de.isInstallment IS IS_INSTALLMENT ");

        if (filter.getDebtSourceId() != null)
            sql.append("AND de.DebtSource.id = :SOURCE_ID ");
        

    //    sql.append("ORDER BY de.price ASC ");
    //    sql.append("ORDER BY de.price DESC ");
        
        return sql;
    }

    	    
}
