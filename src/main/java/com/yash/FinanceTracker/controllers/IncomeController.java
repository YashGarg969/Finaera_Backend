package com.yash.FinanceTracker.controllers;


import com.yash.FinanceTracker.constants.AmountFilterType;
import com.yash.FinanceTracker.domain.Income;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.IncomeDTO;
import com.yash.FinanceTracker.dto.TotalAmountDTO;
import com.yash.FinanceTracker.services.income.IncomeService;
import com.yash.FinanceTracker.services.user.UserService;
import com.yash.FinanceTracker.utils.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    private final UserService userService;

    @PostMapping("/saveIncome")
    public ResponseEntity<?> saveIncome(@RequestBody IncomeDTO incomeDTO)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userId= authentication.getName();

        Optional<User> user= userService.findUserByUserId(userId);
        if(user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null,false,"User is not authorized to access the resource",403,null),HttpStatus.FORBIDDEN);

        try
        {
            Income createdIncome= incomeService.saveIncome(incomeDTO, userId);
            return ResponseEntity.ok(new RestResponse<>(createdIncome,true,null,null,null));
        }
        catch (DataIntegrityViolationException e)
        {
            return new ResponseEntity<>(new RestResponse<>(null,false,e.getMessage(),409,null),HttpStatus.CONFLICT);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new RestResponse<>(null,false,e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{incomeId}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long incomeId)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userId= authentication.getName();

        Optional<User> user= userService.findUserByUserId(userId);

        if(user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null,false,"User is not authorized to access the resource",403,null),HttpStatus.FORBIDDEN);

        int rowsAffected= incomeService.deleteIncomeById(userId,incomeId);
        if(rowsAffected>0)
            return ResponseEntity.ok(new RestResponse<>("Income with id "+ incomeId+ " has been deleted successfully",true,null,null,null));
        return new ResponseEntity<>(new RestResponse<>(null,false,"Income with id "+incomeId+" does not exist",404,null),HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-all-income")
    public ResponseEntity<?> getIncomes()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userId= authentication.getName();

        Optional<User> user= userService.findUserByUserId(userId);
        if(user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null,false,"User is not authorized to access the resource",403,null),HttpStatus.FORBIDDEN);
        else
        {
            List<Income> incomes= incomeService.getAllIncomes(userId);
            return ResponseEntity.ok(new RestResponse<>(incomes,true,null,null,null));
        }
    }

    @GetMapping("/filter-incomes")
    public ResponseEntity<?> getFilteredIncomes(@RequestParam AmountFilterType amountFilterType)
    {
        try
        {
            List<?> filteredList = switch (amountFilterType)
            {
                case DAY_WISE -> incomeService.getDayWiseIncomes("9876544321");
                case WEEK_WISE -> incomeService.getWeeklyIncomes("9876544321");
                case MONTH_WISE -> incomeService.getMonthlyIncomes("9876544321");
                case YEAR_WISE -> incomeService.getYearlyIncomes("9876544321");
            };
            return ResponseEntity.ok(new RestResponse<>(filteredList,true,null,null,null));
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new RestResponse<>(null, false, "Some error occurred: " + e.getMessage(), 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-total-income")
    public ResponseEntity<?> getTotalIncomes()
    {
        try
        {
            TotalAmountDTO totalAmounts= incomeService.getTotalIncomes("9876544321");
            return ResponseEntity.ok(new RestResponse<>(totalAmounts,true,null,null,null));
        }
        catch (Exception e){
            return new ResponseEntity<>(new RestResponse<>(null,false,"Some error occurred: "+e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
