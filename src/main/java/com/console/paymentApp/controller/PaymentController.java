package com.console.paymentApp.controller;

import com.console.paymentApp.entity.Account;
import com.console.paymentApp.exceptions.CustomException;
import com.console.paymentApp.model.PaymentAppResponse;
import com.console.paymentApp.model.Transaction;
import com.console.paymentApp.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    PaymentService paymentService;
    @Autowired
    public PaymentController(@Qualifier("PaymentServiceImpl") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ApiOperation(value ="Fetch the list accounts", response= PaymentAppResponse.class)
    @ApiResponses(value={@ApiResponse(code = 200, message="Successful Operation", response = PaymentAppResponse.class),
    @ApiResponse(code = 401, message="Unauthorized"), @ApiResponse(code=400, message="Bad request. Please check the request parameter." ),
    @ApiResponse(code=500, message="Exception occurred while serving the request")})
    @RequestMapping(method = RequestMethod.GET, value ="/accounts")
    public ResponseEntity<PaymentAppResponse> accounts() {
        PaymentAppResponse paymentAppResp = new PaymentAppResponse();
        HttpStatus httpStatus =null;
        try {
            List<Account> accountList= paymentService.getAllAccount();
            if(accountList.isEmpty()){
                httpStatus =HttpStatus.NO_CONTENT;
            }else{
                paymentAppResp.setAccounts(accountList);
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception ex){
            logger.error("Error while processing this request: {}", ex.getMessage());
            paymentAppResp.setMessage(ex.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
         return new ResponseEntity<>(paymentAppResp, httpStatus);
    }
    @ApiOperation(value ="Fetch the list of Transactions", response= PaymentAppResponse.class)
    @ApiResponses(value={@ApiResponse(code = 200, message="Successful Operation", response = PaymentAppResponse.class),
            @ApiResponse(code = 401, message="Unauthorized"), @ApiResponse(code=400, message="Bad request. Please check the request parameter." ),
            @ApiResponse(code=500, message="Exception occurred while serving the request")})
    @RequestMapping(method = RequestMethod.GET, value ="/transactions/{account}")
    public ResponseEntity<PaymentAppResponse> transactions(@PathVariable Long account) {
        PaymentAppResponse paymentAppResp = new PaymentAppResponse();
        HttpStatus httpStatus =null;
        try {
            if (account == null) {
                return  new ResponseEntity<>( paymentAppResp, HttpStatus.BAD_REQUEST);
            }
            List<Transaction> transactionList = paymentService.getAllTransaction(account);
            if (transactionList.isEmpty()) {
                httpStatus = HttpStatus.NO_CONTENT;
            } else {
                paymentAppResp.setTransactions(transactionList);
                httpStatus = HttpStatus.OK;
            }
        }catch ( Exception ex){
             logger.error("Error while processing this request: {}", ex.getMessage());
             paymentAppResp.setMessage(ex.getMessage());
             httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
         return  new ResponseEntity<>( paymentAppResp, httpStatus);
        }

    @ApiOperation(value ="Update new transactions", response= PaymentAppResponse.class)
    @ApiResponses(value={@ApiResponse(code = 200, message="Successful Operation", response = PaymentAppResponse.class),
            @ApiResponse(code = 401, message="Unauthorized"), @ApiResponse(code=400, message="Bad request. Please check the request parameter." ),
            @ApiResponse(code=500, message="Exception occurred while serving the request")})
    @RequestMapping(method = RequestMethod.POST, value ="/transaction")
    public ResponseEntity<PaymentAppResponse> transaction(@RequestBody Transaction transactionReq) {
        PaymentAppResponse paymentAppResp = new PaymentAppResponse();
        HttpStatus httpStatus =null;
        try {
            if (transactionReq.getFromAccount() ==null  || transactionReq.getToAccount() == null|| transactionReq.getAmount() == null) {
                return  new ResponseEntity<>( paymentAppResp, HttpStatus.BAD_REQUEST);
            }else {
                Long transactionNumber = paymentService.saveTransaction(transactionReq);
                paymentAppResp.setMessage("Transaction successful with transaction number: "+transactionNumber );
                httpStatus = HttpStatus.OK;
            }
        }
        catch ( CustomException ex){
            logger.error("Error while processing this request: {}", ex.getMessage());
            paymentAppResp.setMessage(ex.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        catch ( Exception ex){
            logger.error("Error while processing this request: {}", ex.getMessage());
            paymentAppResp.setMessage(ex.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return  new ResponseEntity<>( paymentAppResp, httpStatus);
    }
}
