package com.yash.FinanceTracker.controllers;

import com.yash.FinanceTracker.constants.AmountFilterType;
import com.yash.FinanceTracker.domain.Investment;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.InvestmentDTO;
import com.yash.FinanceTracker.dto.TotalAmountDTO;
import com.yash.FinanceTracker.services.investments.InvestmentService;
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
@RequestMapping("/api/investment")
public class InvestmentController {
    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/saveInvestment")
    public ResponseEntity<?> saveInvestment(@RequestBody InvestmentDTO investmentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();
        System.out.println("User Sub is " + userId);

        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);

        try {
            Investment savedInvestment = investmentService.saveInvestment(investmentDTO, userId);
            return new ResponseEntity<>(new RestResponse<>(savedInvestment, true, null, null, null), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, e.getMessage(), 409, null), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, e.getMessage(), 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{investmentId}")
    public ResponseEntity<?> deleteInvestment(@PathVariable Long investmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        Optional<User> user = userService.findUserByUserId(userId);

        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);

        try {
            int rowsAffected = investmentService.deleteInvestmentById(userId, investmentId);
            if (rowsAffected > 0)
                return ResponseEntity.ok(new RestResponse<>("Investment with id " + investmentId + " has been deleted successfully", true, null, null, null));
            return new ResponseEntity<>(new RestResponse<>(null, false, "Investment with id " + investmentId + " does not exist", 404, null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Issue while deletion: " + e.getMessage());
            return new ResponseEntity<>(new RestResponse<>(null, false, "Some issue occurred while deletion", 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-investments")
    public ResponseEntity<?> getInvestments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();
        System.out.println("User Id is " + userId);

        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isEmpty())
            return new ResponseEntity<>(new RestResponse<>(null, false, "User is not authorized to access the resource", 403, null), HttpStatus.FORBIDDEN);
        else {
            List<Investment> investments = investmentService.getAllInvestments(userId);
            return ResponseEntity.ok(new RestResponse<>(investments, true, null, null, null));
        }
    }

    @GetMapping("/filter-investments")
    public ResponseEntity<?> getFilteredInvestments(@RequestParam AmountFilterType amountFilterType) {
        try {
            List<?> filteredResults = switch (amountFilterType) {
                case DAY_WISE -> investmentService.getDayWiseInvestments("9876544321");
                case WEEK_WISE -> investmentService.getWeeklyInvestments("9876544321");
                case MONTH_WISE -> investmentService.getMonthlyInvestments("9876544321");
                case YEAR_WISE -> investmentService.getYearlyInvestments("9876544321");
            };
            return ResponseEntity.ok(new RestResponse<>(filteredResults, true, null, null, null));
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse<>(null, false, "some error occurred: " + e.getMessage(), 500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-total-investment")
    public ResponseEntity<?> getTotalInvestments()
    {
        try
        {
            TotalAmountDTO totalAmounts= investmentService.getTotalInvestments("9876544321");
            return ResponseEntity.ok(new RestResponse<>(totalAmounts,true,null,null,null));
        }
        catch (Exception e){
            return new ResponseEntity<>(new RestResponse<>(null,false,"Some error occurred: "+e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
