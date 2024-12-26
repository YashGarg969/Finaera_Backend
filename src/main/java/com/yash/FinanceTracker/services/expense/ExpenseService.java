package com.yash.FinanceTracker.services.expense;

import com.yash.FinanceTracker.domain.Expense;
import com.yash.FinanceTracker.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    public Expense saveExpense(ExpenseDTO expenseDTO,String userId) throws IOException;

    public int deleteExpById(Long id,String userId);

    public Optional<Expense> searchExpenseById(Long expId);

    public List<Expense> getAllExpenses(String userId);

    public List<DayWiseAmountDTO> getDayWiseExpenses(String userId);

    public List<WeekWiseAmountDTO> getWeeklyExpenses(String userId);

    public List<MonthlyAmountDTO> getMonthlyExpenses(String userId);

    public List<YearlyAmountDTO> getYearlyExpenses(String userId);

    public TotalAmountDTO getTotalExpenses(String userId);
}
