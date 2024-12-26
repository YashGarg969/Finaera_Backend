package com.yash.FinanceTracker.services.expense;


import com.yash.FinanceTracker.domain.Expense;
import com.yash.FinanceTracker.dto.*;
import com.yash.FinanceTracker.repository.ExpenseRepository;
import com.yash.FinanceTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Expense saveExpense(ExpenseDTO expenseDTO, String userId) throws IOException {

        Expense expense= new Expense();
        expense.setUser(userRepository.findUserByUserId(userId));
        return saveExpense(expense, expenseDTO);
    }

    public int deleteExpById(Long id,String userId) {
        return deleteExpenseById(id,userId);
    }

    @Transactional
    private Expense saveExpense(Expense expense, ExpenseDTO expenseDTO) throws IOException {

        expense.setExpType(expenseDTO.getExpType());
        expense.setExpAmount(expenseDTO.getExpAmount());
        expense.setExpDesc(expenseDTO.getDesc());
        expense.setExpDate(expenseDTO.getDate());
        return expenseRepository.save(expense);
    }

    public Optional<Expense> searchExpenseById(Long expId) {
        Optional<Expense> expense= expenseRepository.findById(expId);
        return expense;
    }

    private int deleteExpenseById(Long expenseId, String userId){
        return expenseRepository.deleteExpense(userId, expenseId);
    }

    public List<Expense> getAllExpenses(String userId) {

        return expenseRepository.getExpensesByUserId(userId).stream()
                .sorted(Comparator.comparing(Expense::getExpDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DayWiseAmountDTO> getDayWiseExpenses(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();

        List<Object[]> results= expenseRepository.getDayWiseExpenses(userId,currYear,currMonth);
        List<DayWiseAmountDTO> dailyExpensesList= new ArrayList<>();

        for(Object[] result: results)
        {
            Date date= (Date)result[0];
            String dayName= result[1].toString();
            long dayWiseAmount= ((Number)result[2]).longValue();
            dailyExpensesList.add(new DayWiseAmountDTO(date.toLocalDate(),dayName,dayWiseAmount));
        }

        return dailyExpensesList;
    }

    @Override
    public List<WeekWiseAmountDTO> getWeeklyExpenses(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();

        List<Object[]> results= expenseRepository.getWeeklyExpenses(userId,currYear);
        List<WeekWiseAmountDTO> weeklyExpensesList= new ArrayList<>();
        for(Object[] result: results)
        {
            int weekNumber= ((Number)result[0]).intValue();
            long weeklyAmount= ((Number) result[1]).longValue();
            weeklyExpensesList.add(new WeekWiseAmountDTO(weekNumber,weeklyAmount));
        }
        return weeklyExpensesList;
    }

    @Override
    public List<MonthlyAmountDTO> getMonthlyExpenses(String userId) {
        LocalDate currDate = LocalDate.now();
        int currYear = currDate.getYear();

        List<Object[]> results= expenseRepository.getMonthlyExpenses(userId,currYear-1);
        List<MonthlyAmountDTO> monthlyExpensesList= new ArrayList<>();

        for(Object[] result:results)
        {
            int year= ((Integer)result[0]);
            String monthName= result[1].toString();
            long monthlyAmount= ((Number)result[2]).longValue();
            monthlyExpensesList.add(new MonthlyAmountDTO(year,monthName,monthlyAmount));
        }

        return monthlyExpensesList;
    }

    @Override
    public List<YearlyAmountDTO> getYearlyExpenses(String userId) {
        List<Object[]> results= expenseRepository.getYearlyExpenses(userId);
        List<YearlyAmountDTO> yearlyExpensesList= new ArrayList<>();
        for (Object[] result: results)
        {
            int year = ((Number) result[0]).intValue();
            long amount= ((Number) result[1]).longValue();
            yearlyExpensesList.add(new YearlyAmountDTO(year,amount));
        }
        return yearlyExpensesList;
    }

    @Override
    public TotalAmountDTO getTotalExpenses(String userId) {
        Long totalCurrMonthIncome= getTotalExpensesOfCurrMonth(userId);
        Long totalCurrYearIncome= getTotalExpensesOfCurrYear(userId);
        return new TotalAmountDTO(totalCurrMonthIncome,totalCurrYearIncome);
    }


    private Long getTotalExpensesOfCurrMonth(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();
        Number result=  expenseRepository.getTotalExpenseOfCurrMonth(userId,currYear,currMonth);
        if(result==null)
            return 0L;
        return result.longValue();
    }

    private Long getTotalExpensesOfCurrYear(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        Number result= expenseRepository.getTotalExpenseOfCurrYear(userId,currYear);
        if(result==null)
            return 0L;
        return result.longValue();
    }

}
