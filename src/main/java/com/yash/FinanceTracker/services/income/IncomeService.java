package com.yash.FinanceTracker.services.income;

import com.yash.FinanceTracker.domain.Income;
import com.yash.FinanceTracker.dto.*;

import java.util.List;
import java.util.Optional;

public interface IncomeService {

    Income saveIncome(IncomeDTO incomeDTO, String userId) throws Exception;

    Integer deleteIncomeById(String userId, Long incomeId);

    List<Income> getAllIncomes(String userId);

    Optional<Income> searchIncomeById(Long id);

    public List<DayWiseAmountDTO> getDayWiseIncomes(String userId);

    public List<WeekWiseAmountDTO> getWeeklyIncomes(String userId);

    public List<MonthlyAmountDTO> getMonthlyIncomes(String userId);

    public List<YearlyAmountDTO> getYearlyIncomes(String userId);

    public TotalAmountDTO getTotalIncomes(String userId);
}
