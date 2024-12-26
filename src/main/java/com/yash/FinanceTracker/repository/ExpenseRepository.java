package com.yash.FinanceTracker.repository;

import com.yash.FinanceTracker.domain.Expense;
import com.yash.FinanceTracker.dto.DayWiseAmountDTO;
import com.yash.FinanceTracker.dto.MonthlyAmountDTO;
import com.yash.FinanceTracker.dto.WeekWiseAmountDTO;
import com.yash.FinanceTracker.dto.YearlyAmountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "SELECT * from expense e where e.userId= :userId", nativeQuery = true)
    public List<Expense> getExpensesByUserId(@Param("userId") String userId);

    @Query(value = "DELETE from expense e where e.userId= :userId and e.expenseId= :expenseId", nativeQuery = true)
    @Transactional
    @Modifying
    public int deleteExpense(@Param("userId") String userId, @Param("expenseId") Long expenseId);

    @Query(value = "SELECT expDate, DATE_FORMAT(expDate,'%a') as dayName, SUM(expAmount) from expense where YEAR(expDate)= :currYear and MONTH(expDate)= :currMonth and userId= :userId group by expDate, DATE_FORMAT(expDate,'%a') order by expDate", nativeQuery = true)
    @Modifying
    public List<Object[]> getDayWiseExpenses(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT WEEK(expDate), SUM(expAmount) from expense where userId= :userId and YEAR(expDate)= :currYear group by WEEK(expDate)  order by WEEK(expDate)", nativeQuery = true)
    @Modifying
    public List<Object[]> getWeeklyExpenses(@Param("userId") String userId, @Param("currYear") Integer currYear);

    @Query(value = "SELECT YEAR(expDate),DATE_FORMAT(expDate,'%b') as monthName,SUM(expAmount) from expense where userId= :userId and YEAR(expDate)>= :limitingYear group by YEAR(expDate),DATE_FORMAT(expDate,'%b')", nativeQuery = true)
    @Modifying
    public List<Object[]> getMonthlyExpenses(@Param("userId") String userId, @Param("limitingYear") Integer limitingYear);

    @Query(value = "SELECT YEAR(expDate) as year, SUM(expAmount) from expense where userId= :userId group by YEAR(expDate) order by year", nativeQuery = true)
    @Modifying
    public List<Object[]> getYearlyExpenses(@Param("userId") String userId);

    @Query(value ="SELECT SUM(expAmount) from expense where userId= :userId and YEAR(expDate)= :currYear and MONTH(expDate)= :currMonth;", nativeQuery = true)
    public Long getTotalExpenseOfCurrMonth(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT SUM(expAmount) from expense where userId= :userId and YEAR(expDate)= :currYear", nativeQuery = true)
    public Long getTotalExpenseOfCurrYear(@Param("userId") String userId, @Param("currYear") Integer currYear);
}
