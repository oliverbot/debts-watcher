package br.com.debts.service;

import java.util.List;
import java.util.stream.Collectors;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.debts.dto.DebtsByDebtSource;
import br.com.debts.dto.TotalDebts;
import br.com.debts.model.DebtEntry;
import br.com.debts.model.DebtSource;
import br.com.debts.repository.DebtEntryRepository;
import br.com.debts.repository.DebtSourceRepository;
import br.com.debts.repository.DebtTagRepository;
import br.com.debts.repository.DebtsRepositoryImpl;
import br.com.debts.util.DebtSourceFilter;
import br.com.debts.util.Filter;

@Component
public class DebtService {

    private final static Logger LOG = LoggerFactory.getLogger(DebtService.class);

    @Autowired
    private DebtEntryRepository debtEntryRepo;

    @Autowired
    private DebtSourceRepository debtSourceRepo;

    @Autowired
    private DebtsRepositoryImpl debtImpl;

    @Autowired
    private DebtTagRepository debtTagRepo;

    /**
     * This method is used to build a single object
     * containing all the user debt sources and their
     * unpaid debts, along with total debt numbers.
     * 
     * @return TotalDebts This returns a TotalDebts object containing debts
     *         information and numbers.
     */
    public TotalDebts getTotalCurrentDebts(Boolean getCurrentDebts) throws Exception {

        List<DebtSource> allDebtSources = debtSourceRepo.findAll();

        if (allDebtSources == null)
            return null;

        List<DebtsByDebtSource> DebtsByDebtSources = allDebtSources.stream()
                .map(DebtSource -> debtsByDebtSourceBuilder(DebtSource, getCurrentDebts))
                .collect(Collectors.toList());

        TotalDebts totalCurrentDebts = totalCurrentDebtsBuilder(DebtsByDebtSources);

        return totalCurrentDebts;
    }

    public TotalDebts totalCurrentDebtsBuilder(List<DebtsByDebtSource> debtSources) {
        Long creditCardLimit = debtSources.stream().mapToLong(DebtsByDebtSource::getCreditCardLimit).sum();
        Float totalDebt = roundTotal((float) debtSources.stream().mapToDouble(DebtsByDebtSource::getTotalDebt).sum());
        Float totalDebtExternal = roundTotal((float) debtSources.stream().mapToDouble(DebtsByDebtSource::getTotalDebtExternal).sum());
        Float totalDebtInternal = roundTotal((float) debtSources.stream().mapToDouble(DebtsByDebtSource::getTotalDebtInternal).sum());
        Float availableLimit = roundTotal((float) debtSources.stream().mapToDouble(DebtsByDebtSource::getAvailableLimit).sum());

        TotalDebts totalDebts = TotalDebts.builder()
                .debtSources(debtSources)
                .creditCardLimit(creditCardLimit)
                .totalDebt(totalDebt)
                .totalDebtExternal(totalDebtExternal)
                .totalDebtInternal(totalDebtInternal)
                .availableLimit(availableLimit)
                .build();

        return totalDebts;

    }

    private Float roundTotal(Float total) {
        return (float) Math.round(total * 100) / 100;
    }

    public DebtsByDebtSource debtsByDebtSourceBuilder(DebtSource debtSource, Boolean getCurrentDebts) {
        Long debtSourceId = debtSource.getId();
        String debtSourceLogoURL = debtSource.getLogoURL();
        String debtSourceName = debtSource.getName();
        Long debtSourceLimit = debtSource.getCreditCardLimit();

        Float debtSourceTotalDebt = roundTotal(this.getDebtSourceTotalDebt(debtSourceId, getCurrentDebts));
        Float debtSourceExternalDebt = roundTotal(this.getExternalDebtsByDebtSourceId(debtSourceId, getCurrentDebts));
        Float debtSourceInternalDebt = roundTotal(this.getInternalDebtsByDebtSourceId(debtSourceId, getCurrentDebts));
        Float debtSourceAvailableLimit = roundTotal(
                this.getAvailableLimitByDebtSourceId(debtSourceId, getCurrentDebts));

        DebtsByDebtSource debtsByDebtSource = DebtsByDebtSource.builder()
                .logoURL(debtSourceLogoURL)
                .name(debtSourceName)
                .creditCardLimit(debtSourceLimit)
                .totalDebt(debtSourceTotalDebt)
                .totalDebtExternal(debtSourceExternalDebt)
                .totalDebtInternal(debtSourceInternalDebt)
                .availableLimit(debtSourceAvailableLimit)
                .build();

        return debtsByDebtSource;
    }

    private Float getDebtSourceTotalDebt(Long id, Boolean getCurrentDebts) {
        DebtSourceFilter filter = DebtSourceFilter.builder()
                .cardLimit(false)
                .currentDebt(getCurrentDebts)
                .scopeFilter(Filter.ALL)
                .build();
        Float result = debtImpl.getDebtsByDebtSourceId(id, filter);
        return result;
    }

    private Float getExternalDebtsByDebtSourceId(Long id, Boolean getCurrentDebts) {
        DebtSourceFilter filter = DebtSourceFilter.builder()
                .cardLimit(false)
                .currentDebt(getCurrentDebts)
                .scopeFilter(Filter.EXTERNAL)
                .build();
        Float result = debtImpl.getDebtsByDebtSourceId(id, filter);
        return result;
    }

    private Float getInternalDebtsByDebtSourceId(Long id, Boolean getCurrentDebts) {
        DebtSourceFilter filter = DebtSourceFilter.builder()
                .cardLimit(false)
                .currentDebt(getCurrentDebts)
                .scopeFilter(Filter.INTERNAL)
                .build();
        Float result = debtImpl.getDebtsByDebtSourceId(id, filter);
        return result;
    }

    private Float getAvailableLimitByDebtSourceId(Long id, Boolean getCurrentDebts) {
        DebtSourceFilter filter = DebtSourceFilter.builder()
                .cardLimit(true)
                .currentDebt(getCurrentDebts)
                .scopeFilter(Filter.ALL)
                .build();
        Float result = debtImpl.getDebtsByDebtSourceId(id, filter);
        return result;
    }

}
