package com.yash.FinanceTracker.services.investments;

import com.yash.FinanceTracker.domain.Investment;
import com.yash.FinanceTracker.dto.*;

import java.util.List;

public interface InvestmentService {

    Investment saveInvestment(InvestmentDTO investmentDTO, String userId);
    List<Investment> getAllInvestments(String userId);

    int deleteInvestmentById(String userId, Long investmentId);

    public List<DayWiseAmountDTO> getDayWiseInvestments(String userId);

    public List<WeekWiseAmountDTO> getWeeklyInvestments(String userId);

    public List<MonthlyAmountDTO> getMonthlyInvestments(String userId);

    public List<YearlyAmountDTO> getYearlyInvestments(String userId);

    public TotalAmountDTO getTotalInvestments(String userId);
}
