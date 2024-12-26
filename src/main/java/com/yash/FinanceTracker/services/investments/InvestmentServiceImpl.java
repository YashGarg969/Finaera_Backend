package com.yash.FinanceTracker.services.investments;

import com.yash.FinanceTracker.domain.Investment;
import com.yash.FinanceTracker.dto.*;
import com.yash.FinanceTracker.repository.InvestmentRepository;
import com.yash.FinanceTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService{

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Investment saveInvestment(InvestmentDTO investmentDTO, String userId) {
        Investment investment= new Investment();
        investment.setUser(userRepository.findUserByUserId(userId));
        return saveInvestment(investment, investmentDTO);
    }

    @Transactional
    private Investment saveInvestment(Investment investment, InvestmentDTO investmentDTO)
    {
        investment.setInvestedAmount(investmentDTO.getInvestedAmount());
        investment.setInvestmentType(investmentDTO.getInvestmentType());
        investment.setInvestmentDate(investmentDTO.getInvestmentDate());
        investment.setInvestmentDesc(investmentDTO.getInvestmentDesc());
        return investmentRepository.save(investment);
    }

    @Override
    public List<Investment> getAllInvestments(String userId) {
        return investmentRepository.getAllInvestments(userId).stream()
                .sorted(Comparator.comparing(Investment::getInvestmentDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public int deleteInvestmentById(String userId, Long investmentId) {
        return investmentRepository.deleteInvestmentById_userId(userId,investmentId);
    }

    @Override
    public List<DayWiseAmountDTO> getDayWiseInvestments(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();

        List<Object[]> results= investmentRepository.getDayWiseInvestments(userId,currYear,currMonth);
        List<DayWiseAmountDTO> dailyInvestmentsList= new ArrayList<>();

        for(Object[] result: results)
        {
            Date date= (Date)result[0];
            String dayName= result[1].toString();
            long dayWiseAmount= ((Number)result[2]).longValue();
            dailyInvestmentsList.add(new DayWiseAmountDTO(date.toLocalDate(),dayName,dayWiseAmount));
        }

        return dailyInvestmentsList;
    }

    @Override
    public List<WeekWiseAmountDTO> getWeeklyInvestments(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();

        List<Object[]> results= investmentRepository.getWeeklyInvestments(userId,currYear);
        List<WeekWiseAmountDTO> weeklyInvestmentsList= new ArrayList<>();
        for(Object[] result: results)
        {
            int weekNumber= ((Number)result[0]).intValue();
            long weeklyAmount= ((Number) result[1]).longValue();
            weeklyInvestmentsList.add(new WeekWiseAmountDTO(weekNumber,weeklyAmount));
        }
        return weeklyInvestmentsList;
    }

    @Override
    public List<MonthlyAmountDTO> getMonthlyInvestments(String userId) {
        LocalDate currDate = LocalDate.now();
        int currYear = currDate.getYear();

        List<Object[]> results= investmentRepository.getMonthlyInvestments(userId,currYear-1);
        List<MonthlyAmountDTO> monthlyInvestmentsList= new ArrayList<>();

        for(Object[] result:results)
        {
            int year= ((Integer)result[0]);
            String monthName= result[1].toString();
            long monthlyAmount= ((Number)result[2]).longValue();
            monthlyInvestmentsList.add(new MonthlyAmountDTO(year,monthName,monthlyAmount));
        }

        return monthlyInvestmentsList;
    }

    @Override
    public List<YearlyAmountDTO> getYearlyInvestments(String userId) {
        List<Object[]> results= investmentRepository.getYearlyInvestments(userId);
        List<YearlyAmountDTO> yearlyInvestmentsList= new ArrayList<>();
        for (Object[] result: results)
        {
            int year = ((Number) result[0]).intValue();
            long amount= ((Number) result[1]).longValue();
            yearlyInvestmentsList.add(new YearlyAmountDTO(year,amount));
        }
        return yearlyInvestmentsList;
    }

    @Override
    public TotalAmountDTO getTotalInvestments(String userId) {
        Long totalCurrMonthIncome= getTotalInvestmentsOfCurrMonth(userId);
        Long totalCurrYearIncome= getTotalInvestmentsOfCurrYear(userId);
        return new TotalAmountDTO(totalCurrMonthIncome,totalCurrYearIncome);
    }

    private Long getTotalInvestmentsOfCurrMonth(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();
        Number result=  investmentRepository.getTotalInvestmentsOfCurrMonth(userId,currYear,currMonth);
        if(result==null)
            return 0L;
        return result.longValue();
    }

    private Long getTotalInvestmentsOfCurrYear(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        Number result= investmentRepository.getTotalInvestmentsOfCurrYear(userId,currYear);
        if(result==null)
            return 0L;
        return result.longValue();
    }
}
