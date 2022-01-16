package br.com.debts.repository;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.debts.model.DebtEntry;
import br.com.debts.util.DebtEntryFilter;
import br.com.debts.util.DebtSourceFilter;
import br.com.debts.util.Filter;


@Component
@SuppressWarnings("unchecked")
public class DebtEntriesRepositoryImpl {

    static final Logger LOG = LoggerFactory.getLogger(DebtsRepositoryImpl.class);

	@Autowired
	private EntityManager em;
    

    public List<DebtEntry> getDebtsByDebtSourceId(Boolean getCurrentDebts, DebtEntryFilter filter) {
        try {
            
            String nativeQueryString = this.queryBuilder(getCurrentDebts, filter);

            Query query = this.em.createNativeQuery(nativeQueryString);

            if (filter.getId() != null)
                query.setParameter("ID", filter.getId());

            if (filter.getName() != null)
                query.setParameter("NAME", "%" + filter.getName() + "%");

            if (filter.getIsExternal() != null)
                query.setParameter("IS_EXTERNAL", filter.getIsExternal());

            if (filter.getInstallmentNumber() != null)
                query.setParameter("INSTALLMENT", filter.getInstallmentNumber());

            if (filter.getInstallmentTotal() != null)
                query.setParameter("INSTALLMENT_TOTAL", filter.getInstallmentTotal());
            
            if (filter.getIsRecurrent() != null) 
                query.setParameter("IS_RECURRENT", filter.getIsRecurrent());

            if (filter.getDateAdded() != null)
                query.setParameter("DATE", filter.getDateAdded());

            if (filter.getIsSubscription() != null)
                query.setParameter("IS_SUBSCRIPTION", filter.getIsSubscription());

            if (filter.getIsInstallment() != null)
                query.setParameter("IS_INSTALLMENT", filter.getIsInstallment());

            if (filter.getDebtSourceId() != null)
                query.setParameter("SOURCE_ID", filter.getDebtSourceId());

            List<Object[]> queryResult = query.getResultList();

            List<DebtEntry> debtsResult = new ArrayList<>();

            for (Object[] obj : queryResult) {
                DebtEntry debt = new DebtEntry();

                BigInteger id = (BigInteger) obj[0];
                String name = (String)obj[1];
                Float price = (Float)obj[2];
                Integer installmentnumber = (Integer) obj[4];
                Integer installmenttotal = (Integer) obj[5];
                Boolean is_external = (Byte) obj[3] == 1 ? true : false;
                Boolean is_recurrent = (Byte) obj[7] == 1 ? true : false;
                Boolean is_subscription = (Byte) obj[8] == 1 ? true : false;
                Boolean is_installment = (Byte) obj[9] == 1 ? true : false;
                Timestamp date_added = (Timestamp)obj[10];
                

                debt.setId(id.longValue());
                debt.setName(name);
                debt.setPrice(price);
                debt.setIsExternal(is_external);
                debt.setInstallmentNumber(installmentnumber.longValue());
                debt.setInstallmentTotal(installmenttotal.longValue());
                debt.setIsRecurrent(is_recurrent);
                debt.setIsSubscription(is_subscription);
                debt.setIsInstallment(is_installment);
                debt.setDateAdded(date_added);

                debtsResult.add(debt);
            }

            return debtsResult;

        } catch (Exception e) {
            LOG.error("ERROR: Exception executing QUERY from getDebtsByDebtSourceId.");
            LOG.error("MESSAGE: " + e.getMessage());
            LOG.error("CAUSE: " + e.getCause());
        }
        
        return null;
    }


    private String queryBuilder(Boolean getCurrentDebts, DebtEntryFilter filter) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT de.* FROM debt_entries de ");
        sql.append("INNER JOIN debt_source ds ON ds.id = de.debt_source_id ");
        sql.append("WHERE 1 = 1 ");
        sql.append(this.getQueryScope(getCurrentDebts, filter));
        sql.append(this.getCurrentEntries(getCurrentDebts));

        String nativeQueryString = sql.toString();

        return nativeQueryString;

    }

    private StringBuilder getQueryScope(Boolean getCurrentDebts, DebtEntryFilter filter) {

        StringBuilder sql = new StringBuilder();

        if (filter.getId() != null)
            sql.append("AND de.id = :ID ");

        if (filter.getName() != null)
            sql.append("AND de.name LIKE :NAME ");

        if (filter.getIsExternal() != null)
            sql.append("AND de.is_external = :IS_EXTERNAL ");

        if (filter.getInstallmentNumber() != null)
            sql.append("AND de.installment_number = :INSTALLMENT ");

        if (filter.getInstallmentTotal() != null)
            sql.append("AND de.installment_total = :INSTALLMENT_TOTAL ");
        
        if (filter.getIsRecurrent() != null) 
            sql.append("AND de.is_recurrent = :IS_RECURRENT ");

        if (filter.getDateAdded() != null)
            sql.append("AND de.date_added = :DATE ");

        if (filter.getIsSubscription() != null)
            sql.append("AND de.is_subscription = IS_SUBSCRIPTION ");

        if (filter.getIsInstallment() != null)
            sql.append("AND de.is_installment = IS_INSTALLMENT ");

        if (filter.getDebtSourceId() != null)
            sql.append("AND de.debt_source_id = :SOURCE_ID ");

    //    sql.append("ORDER BY de.price ASC ");
    //    sql.append("ORDER BY de.price DESC ");
        
        return sql;
    }

    private StringBuilder getCurrentEntries(Boolean filter) {

        StringBuilder sql = new StringBuilder();
        
        sql.append("AND de.date_added ");

            sql.append("BETWEEN ");

                if (filter) {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ");
                } else {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',IF(month(now()) = 1,'12',(month(now()) - 1)),',',if(month(now()) = 1,(year(now()) - 1),year(now()))), '%d,%m,%Y') ");
                }

            sql.append("AND ");
                    
                if (filter) {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()) + 1,',',year(now())), '%d,%m,%Y') ");
                } else {
                    sql.append("STR_TO_DATE(concat(ds.due_day,',',month(now()),',',year(now())), '%d,%m,%Y') ");
                }

        return sql;
    }

    	    
}
