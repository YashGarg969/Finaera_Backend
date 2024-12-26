package com.yash.FinanceTracker.repository;

import com.yash.FinanceTracker.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income,Long>
{
    @Query(value = "SELECT * FROM income i where i.userId= :userId", nativeQuery = true)
    public List<Income> getIncomesByUserId(@Param("userId") String userId);

    @Query(value = "DELETE FROM income i where i.userId= :userId and i.incomeId= :incomeId", nativeQuery = true)
    @Modifying
    @Transactional
    public int deleteIncomeByIncomeId_UserId(@Param("userId") String userId, @Param("incomeId") Long incomeId);

    @Query(value = "SELECT incomeDate, DATE_FORMAT(incomeDate,'%a') as dayName, SUM(incomeAmount) from income where YEAR(incomeDate)= :currYear and MONTH(incomeDate)= :currMonth and userId= :userId group by incomeDate, DATE_FORMAT(incomeDate,'%a') order by incomeDate", nativeQuery = true)
    @Modifying
    public List<Object[]> getDayWiseIncomes(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT WEEK(incomeDate), SUM(incomeAmount) from income where userId= :userId and YEAR(incomeDate)= :currYear group by WEEK(incomeDate) order by WEEK(incomeDate)", nativeQuery = true)
    @Modifying
    public List<Object[]> getWeeklyIncomes(@Param("userId") String userId, @Param("currYear") Integer currYear);

    @Query(value = "SELECT YEAR(incomeDate),DATE_FORMAT(incomeDate,'%b') as monthName,SUM(incomeAmount) from income where userId= :userId and YEAR(incomeDate)>= :limitingYear group by YEAR(incomeDate),DATE_FORMAT(incomeDate,'%b')", nativeQuery = true)
    @Modifying
    public List<Object[]> getMonthlyIncomes(@Param("userId") String userId, @Param("limitingYear") Integer limitingYear);

    @Query(value = "SELECT YEAR(incomeDate) as year, SUM(incomeAmount) from income where userId= :userId group by YEAR(incomeDate) order by year", nativeQuery = true)
    @Modifying
    public List<Object[]> getYearlyIncomes(@Param("userId") String userId);

    @Query(value ="SELECT SUM(incomeAmount) from income where userId= :userId and YEAR(incomeDate)= :currYear and MONTH(incomeDate)= :currMonth;", nativeQuery = true)
    public Long getTotalIncomeOfCurrMonth(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT SUM(incomeAmount) from income where userId= :userId and YEAR(incomeDate)= :currYear", nativeQuery = true)
    public Long getTotalIncomeOfCurrYear(@Param("userId") String userId, @Param("currYear") Integer currYear);
}
