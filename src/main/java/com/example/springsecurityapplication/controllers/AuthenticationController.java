package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.errors.CustomFieldError;
import com.example.springsecurityapplication.errors.FieldErrorResponse;
import com.example.springsecurityapplication.errors.LoginResponseDTO;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {
    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @PostMapping(value = "/registration", consumes = "application/json", produces = "application/json")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FieldErrorResponse resultRegistration(@Valid @RequestBody Person person, BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        List<CustomFieldError> fieldErrors = new ArrayList<>();
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();

        if (bindingResult.hasErrors()) {
            System.out.println("Error");
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                CustomFieldError fieldError = new CustomFieldError();
                fieldError.setField(error.getField());
                fieldError.setMessage(error.getDefaultMessage());
                System.out.println("field: " + error.getField()
                        + "; message: " + error.getDefaultMessage());
                fieldErrors.add(fieldError);
            }
            fieldErrorResponse.setFieldErrors(fieldErrors);
            return fieldErrorResponse;
        }
        System.out.println("Ok");
        personService.register(person);
        return fieldErrorResponse;
    }
    

/* *********************************************************************************** */

//    @PostMapping("/add")
//    public

//    @GetMapping("/login")
//    public String login() {
//        return "authentication/login";
//    }

//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("person", new Person());
//    }

//    @GetMapping("/registration")
//    public String registration(@ModelAttribute("person") Person person) {
//        return "authentication/registration";
//    }



}
