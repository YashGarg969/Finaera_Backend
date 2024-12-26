package com.yash.FinanceTracker.repository;

import com.yash.FinanceTracker.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface InvestmentRepository extends JpaRepository<Investment,Long> {

    @Query(value = "SELECT * from investments where userId= :userId", nativeQuery = true)
    public List<Investment> getAllInvestments(@Param("userId") String userId);

    @Query(value = "DELETE from investments where userId= :userId and investmentId= :investmentId", nativeQuery = true)
    @Modifying
    @Transactional
    public int deleteInvestmentById_userId(@Param("userId") String userId, @Param("investmentId") Long investmentId);

    @Query(value = "SELECT investmentDate, DATE_FORMAT(investmentDate,'%a') as dayName, SUM(investedAmount) from investments where YEAR(investmentDate)= :currYear and MONTH(investmentDate)= :currMonth and userId= :userId group by investmentDate, DATE_FORMAT(investmentDate,'%a') order by investmentDate", nativeQuery = true)
    @Modifying
    public List<Object[]> getDayWiseInvestments(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT WEEK(investmentDate), SUM(investedAmount) from investments where userId= :userId and YEAR(investmentDate)= :currYear group by WEEK(investmentDate) order by WEEK(investmentDate)", nativeQuery = true)
    @Modifying
    public List<Object[]> getWeeklyInvestments(@Param("userId") String userId, @Param("currYear") Integer currYear);

    @Query(value = "SELECT YEAR(investmentDate),DATE_FORMAT(investmentDate,'%b') as monthName,SUM(investedAmount) from investments where userId= :userId and YEAR(investmentDate)>= :limitingYear group by YEAR(investmentDate),DATE_FORMAT(investmentDate,'%b')", nativeQuery = true)
    @Modifying
    public List<Object[]> getMonthlyInvestments(@Param("userId") String userId, @Param("limitingYear") Integer limitingYear);

    @Query(value = "SELECT YEAR(investmentDate) as year, SUM(investedAmount) from investments where userId= :userId group by YEAR(investmentDate) order by year", nativeQuery = true)
    @Modifying
    public List<Object[]> getYearlyInvestments(@Param("userId") String userId);


    @Query(value ="SELECT SUM(investedAmount) from investments where userId= :userId and YEAR(investmentDate)= :currYear and MONTH(investmentDate)= :currMonth;", nativeQuery = true)
    public Long getTotalInvestmentsOfCurrMonth(@Param("userId") String userId, @Param("currYear") Integer currYear, @Param("currMonth") Integer currMonth);

    @Query(value = "SELECT SUM(investedAmount) from investments where userId= :userId and YEAR(investmentDate)= :currYear", nativeQuery = true)
    public Long getTotalInvestmentsOfCurrYear(@Param("userId") String userId, @Param("currYear") Integer currYear);


}
