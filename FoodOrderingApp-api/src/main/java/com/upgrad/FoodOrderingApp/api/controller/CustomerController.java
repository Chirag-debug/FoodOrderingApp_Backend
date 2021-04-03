package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.common.Utility;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Base64;

@CrossOrigin
@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    Utility utility;

@CrossOrigin
@RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false)final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException
{
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setFirstName(signupCustomerRequest.getFirstName());
    customerEntity.setLastName(signupCustomerRequest.getFirstName());
    customerEntity.setContactnumber(signupCustomerRequest.getContactNumber());
    customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
    customerEntity.setPassword(signupCustomerRequest.getPassword());
    customerEntity.setUuid(UUID.randomUUID().toString());

    utility.isValidSignupRequest(customerEntity);

    CustomerEntity createdCustomer = customerService.saveCustomer(customerEntity);

    SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(createdCustomer.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");

    return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
}

@CrossOrigin
@RequestMapping(method = RequestMethod.POST, path = "/customer/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<LoginResponse> login(@RequestBody(required = false) @RequestHeader("authorization")final String authorization ) throws AuthenticationFailedException
{

    utility.isValidAuthorizationFormat(authorization);

    byte[] decoded = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
    String decodedAuth = new String(decoded);
    String[] decodedArray = decodedAuth.split(":");


    CustomerAuthEntity customerAuthEntity = customerService.authenticate(decodedArray[0], decodedArray[1]);
    CustomerEntity customer = customerAuthEntity.getCustomer();

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setId(customer.getUuid());
    loginResponse.setFirstName(customer.getFirstName());
    loginResponse.setLastName(customer.getLastName());
    loginResponse.setEmailAddress(customer.getEmail());
    loginResponse.setContactNumber(customer.getContactnumber());
    loginResponse.setMessage("LOGGED IN SUCCESSFULLY");

    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", customerAuthEntity.getAccessToken());
    headers.add("access-control-expose-headers", "access-token");
    return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
}

@CrossOrigin
@RequestMapping(method = RequestMethod.POST, path = "/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<LogoutResponse> logout( @RequestBody(required = false) @RequestHeader("authorization")final String authorization) throws AuthorizationFailedException
{
    String accessToken = authorization.split("Bearer ")[1];

    CustomerAuthEntity customerAuthEntity = customerService.logout(accessToken);

    LogoutResponse logoutResponse = new LogoutResponse().id(customerAuthEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");

    return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
}

@CrossOrigin
@RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization")final String authorization, final UpdateCustomerRequest updateCustomerRequest) throws AuthorizationFailedException, UpdateCustomerException
{
    utility.isValidCustomerRequest(updateCustomerRequest.getFirstName());

    String accessToken = authorization.split("Bearer ")[1];

    CustomerEntity customerToUpdateEntity = customerService.getCustomer(accessToken);

    customerToUpdateEntity.setFirstName(updateCustomerRequest.getFirstName());
    customerToUpdateEntity.setLastName(updateCustomerRequest.getLastName());

    CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerToUpdateEntity);

    UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse()
            .firstName(updatedCustomerEntity.getFirstName())
            .lastName(updatedCustomerEntity.getLastName())
            .id(updatedCustomerEntity.getUuid())
            .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

    return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
}

@CrossOrigin
@RequestMapping(method = RequestMethod.PUT,path = "/customer/password",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader("authorization")final String authorization, final UpdatePasswordRequest updatePasswordRequest) throws AuthorizationFailedException, UpdateCustomerException
{
    utility.isValidUpdatePasswordRequest(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());

    String accessToken = authorization.split("Bearer ")[1];

    String oldPassword = updatePasswordRequest.getOldPassword();
    String newPassword = updatePasswordRequest.getNewPassword();

    CustomerEntity customerToUpdateEntity = customerService.getCustomer(accessToken);

    CustomerEntity updatedCustomerEntity = customerService.updateCustomerPassword(oldPassword, newPassword, customerToUpdateEntity);

    UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse()
            .id(updatedCustomerEntity.getUuid())
            .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");


    return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
}

}
