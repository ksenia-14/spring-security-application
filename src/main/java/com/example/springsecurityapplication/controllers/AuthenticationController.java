package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.errors.CustomFieldError;
import com.example.springsecurityapplication.errors.FieldErrorResponse;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.services.PersonDetailsService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

//    @PostMapping(value = "/registration", consumes = "application/json", produces = "application/json")
//    @ExceptionHandler(MethodArgumentNotValidException.class)
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
    public FieldErrorResponse resultAuthorization(@Valid @RequestBody Person person) {

        String login = person.getLogin();
        String password = passwordEncoder.encode(person.getPassword());
        Person loginPerson = new Person(login, password);

        Optional<Person> personFind = personRepository.findByLogin(loginPerson.getLogin());
        List<CustomFieldError> fieldErrors = new ArrayList<>();
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();

        if (personFind.isEmpty()) {
            CustomFieldError fieldError = new CustomFieldError();
            fieldError.setField("user");
            fieldError.setMessage("Пользователь не найден");
            System.out.println("field: " + fieldError.getField()
                    + "; message: " + fieldError.getMessage());
            fieldErrors.add(fieldError);
            fieldErrorResponse.setFieldErrors(fieldErrors);
            return fieldErrorResponse;
        }

        System.out.println("Ok");
        return fieldErrorResponse;
    }

//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateuser
//            (@RequestBody Person loginRequest) {
//
//        Authentication authentication = authenticationManager
//                .authenticate
//                        (new UsernamePasswordAuthenticationToken
//                                (loginRequest.getUsername(),
//                                        loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext()
//                .setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl)
//                authentication.getPrincipal();
//
//        return ResponseEntity
//                .ok(new JwtResponse(jwt, userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail()));
//    }
}
