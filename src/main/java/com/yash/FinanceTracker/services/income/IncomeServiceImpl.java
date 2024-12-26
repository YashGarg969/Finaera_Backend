package com.yash.FinanceTracker.services.income;

import com.yash.FinanceTracker.domain.Income;
import com.yash.FinanceTracker.dto.*;
import com.yash.FinanceTracker.repository.IncomeRepository;
import com.yash.FinanceTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService{

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    public Income saveIncome(IncomeDTO incomeDTO, String userId) throws Exception {
        Income income= new Income();
        income.setUser(userRepository.findUserByUserId(userId));
        return saveIncome(income, incomeDTO);
    }


    private Income saveIncome(Income income, IncomeDTO incomeDTO) throws Exception
    {
            income.setIncomeSrc(incomeDTO.getIncomeSrc());
            income.setIncomeDesc(incomeDTO.getDesc());
            income.setIncomeAmount(incomeDTO.getIncomeAmount());
            income.setIncomeDate(incomeDTO.getDate());
            return incomeRepository.save(income);
    }

    public Optional<Income> searchIncomeById(Long id)
    {
        return incomeRepository.findById(id);
    }

    @Override
    public List<DayWiseAmountDTO> getDayWiseIncomes(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();

        List<Object[]> results= incomeRepository.getDayWiseIncomes(userId,currYear,currMonth);
        List<DayWiseAmountDTO> dailyIncomeList= new ArrayList<>();

        for(Object[] result: results)
        {
            Date date= (Date)result[0];
            String dayName= result[1].toString();
            long dayWiseAmount= ((Number)result[2]).longValue();
            dailyIncomeList.add(new DayWiseAmountDTO(date.toLocalDate(),dayName,dayWiseAmount));
        }

        return dailyIncomeList;
    }

    @Override
    public List<WeekWiseAmountDTO> getWeeklyIncomes(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();

        List<Object[]> results= incomeRepository.getWeeklyIncomes(userId,currYear);
        List<WeekWiseAmountDTO> weeklyIncomeList= new ArrayList<>();
        for(Object[] result: results)
        {
            int weekNumber= ((Number)result[0]).intValue();
            long weeklyAmount= ((Number) result[1]).longValue();
            weeklyIncomeList.add(new WeekWiseAmountDTO(weekNumber,weeklyAmount));
        }
        return weeklyIncomeList;
    }

    @Override
    public List<MonthlyAmountDTO> getMonthlyIncomes(String userId) {
        LocalDate currDate = LocalDate.now();
        int currYear = currDate.getYear();

        List<Object[]> results= incomeRepository.getMonthlyIncomes(userId,currYear-1);
        List<MonthlyAmountDTO> monthlyIncomeList= new ArrayList<>();

        for(Object[] result:results)
        {
            int year= ((Integer)result[0]);
            String monthName= result[1].toString();
            long monthlyAmount= ((Number)result[2]).longValue();
            monthlyIncomeList.add(new MonthlyAmountDTO(year,monthName,monthlyAmount));
        }

        return monthlyIncomeList;
    }

    @Override
    public List<YearlyAmountDTO> getYearlyIncomes(String userId) {
        List<Object[]> results= incomeRepository.getYearlyIncomes(userId);
        List<YearlyAmountDTO> yearlyIncomeList= new ArrayList<>();
        for (Object[] result: results)
        {
            int year = ((Number) result[0]).intValue();
            long amount= ((Number) result[1]).longValue();
            yearlyIncomeList.add(new YearlyAmountDTO(year,amount));
        }
        return yearlyIncomeList;
    }

    public TotalAmountDTO getTotalIncomes(String userId)
    {
        Long totalCurrMonthIncome= getTotalIncomeOfCurrMonth(userId);
        Long totalCurrYearIncome= getTotalIncomeOfCurrYear(userId);
        return new TotalAmountDTO(totalCurrMonthIncome,totalCurrYearIncome);
    }

    private Long getTotalIncomeOfCurrMonth(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        int currMonth= currDate.getMonthValue();
        Object result=  incomeRepository.getTotalIncomeOfCurrMonth(userId,currYear,currMonth);
        if(result==null)
            return 0L;
        return ((Number)result).longValue();
    }

    private Long getTotalIncomeOfCurrYear(String userId) {
        LocalDate currDate= LocalDate.now();
        int currYear= currDate.getYear();
        Object result= incomeRepository.getTotalIncomeOfCurrYear(userId,currYear);
        if(result==null)
            return 0L;
        return ((Number)result).longValue();
    }

    public Integer deleteIncomeById(String userId, Long incomeId)
    {
        return incomeRepository.deleteIncomeByIncomeId_UserId(userId,incomeId);
    }

    public List<Income> getAllIncomes(String userId)
    {
        return incomeRepository.getIncomesByUserId(userId).stream()
                .sorted(Comparator.comparing(Income::getIncomeDate).reversed())
                .collect(Collectors.toList());
    }

}
