package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.errors.CustomFieldError;
import com.example.springsecurityapplication.errors.FieldErrorResponse;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.responses.Response;
import com.example.springsecurityapplication.responses.UserInfo;
import com.example.springsecurityapplication.services.PersonDetailsService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService, PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

//    http://localhost:8081/api/registration
    @PostMapping(value = "/registration")
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

    @PostMapping(value = "/login")
    public Authentication resultAuthorization(@Valid @RequestBody Person person) throws BadCredentialsException, UsernameNotFoundException {

        String login = person.getLogin(); // логин
        String password = person.getPassword(); // пароль
        // получаем запись найденного пользователя по логину, если он есть
        UserDetails personFind = personDetailsService.loadUserByUsername(login);
        String password_encode = personFind.getPassword();

        // проверка пароля
        if (!passwordEncoder.matches(password, password_encode)) {
            System.out.println("Некорректный пароль");
            throw new BadCredentialsException("Некорректный пароль");
        }

        System.out.println("Ok");
        return new UsernamePasswordAuthenticationToken(personFind, password_encode, Collections.emptyList());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> handleException(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("msg: ",exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleException(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("msg: ",exception.getMessage()));
    }

//    @GetMapping("/auth/userinfo")
//    public ResponseEntity<?> getUserInfo(Principal user){
//        Person userObj=(Person) personDetailsService.loadUserByUsername(user.getName());
//// TODO отредактировать UserInfo!
//        UserInfo userInfo=new UserInfo();
//        userInfo.setFirstName(userObj.getLogin());
//        userInfo.setLastName(userObj.getPassword());
//        userInfo.setRoles(userObj.getRole());
//
//        return ResponseEntity.ok(userInfo);
//    }

}
