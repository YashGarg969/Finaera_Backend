package com.yash.FinanceTracker.controllers;


import com.yash.FinanceTracker.constants.AmountFilterType;
import com.yash.FinanceTracker.domain.Expense;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.ExpenseDTO;
import com.yash.FinanceTracker.dto.TotalAmountDTO;
import com.yash.FinanceTracker.services.expense.ExpenseService;
import com.yash.FinanceTracker.services.user.UserService;
import com.yash.FinanceTracker.utils.RestResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/expense")
public class ExpenseController {


    @Autowired
    private final ExpenseService expenseService;
    @Autowired
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveExpense(@Valid @RequestBody ExpenseDTO expenseDTO) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<User> user = userService.findUserByUserId(userId);

        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);
        try {
            Expense createdExpense = expenseService.saveExpense(expenseDTO, userId);
            return ResponseEntity.ok(new RestResponse<>(createdExpense, true, null, null, null));
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, e.getMessage(), 409, null), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, e.getMessage(), 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<User> user = userService.findUserByUserId(userId);

        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);

        int rowsAffected = expenseService.deleteExpById(expenseId, userId);
        if (rowsAffected > 0)
            return ResponseEntity.ok(new RestResponse<>("Income with id " + expenseId + " has been deleted successfully", true, null, null, null));
        return new ResponseEntity<>(new RestResponse<>(null, false, "Income with id " + expenseId + " does not exist", 404, null), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-all-expenses")
    public ResponseEntity<?> getExpenses() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<User> user = userService.findUserByUserId(userId);

        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);
        else {
            List<Expense> expenses = expenseService.getAllExpenses(userId);
            return ResponseEntity.ok(new RestResponse<>(expenses, true, null, null, null));
        }
    }

    @GetMapping("/filter-expenses")
    public ResponseEntity<?> getFilteredExpenses(@RequestParam AmountFilterType amountFilterType) {
        try {
            List<?> filteredList = switch (amountFilterType) {
                case DAY_WISE -> expenseService.getDayWiseExpenses("9876544321");
                case WEEK_WISE -> expenseService.getWeeklyExpenses("9876544321");
                case MONTH_WISE -> expenseService.getMonthlyExpenses("9876544321");
                case YEAR_WISE -> expenseService.getYearlyExpenses("9876544321");
            };

            return ResponseEntity.ok(new RestResponse<>(filteredList, true, null, null, null));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, "Invalid filter type: " + amountFilterType, 400, null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, "Some error occurred: " + e.getMessage(), 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-total-expense")
    public ResponseEntity<?> getTotalExpense()
    {
        try
        {
            TotalAmountDTO totalAmounts= expenseService.getTotalExpenses("9876544321");
            return ResponseEntity.ok(new RestResponse<>(totalAmounts,true,null,null,null));
        }
        catch (Exception e){
            return new ResponseEntity<>(new RestResponse<>(null,false,"Some error occurred: "+e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
