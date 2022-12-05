package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.errors.CustomFieldError;
import com.example.springsecurityapplication.errors.FieldErrorResponse;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.services.PersonDetailsService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.PersonValidator;
import com.example.springsecurityapplication.util.ProductValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdministratorController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final ProductValidator productValidator;
    private final ProductService productService;

    public AdministratorController(PersonValidator personValidator, PersonService personService, PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder, PersonRepository personRepository, ProductValidator productValidator, ProductService productService) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.productValidator = productValidator;
        this.productService = productService;
    }

    /* Получение всех пользователей */
    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers() {
        List<Person> personList = personRepository.findAll();
        return ResponseEntity.ok(personList);
    }

    /* Удаление пользователя */
    @GetMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        personService.deleteById(id);
        return ResponseEntity.ok("ok");
    }

    /* Редактирование пользователя по id */
    @PostMapping("/user/edit/{id}")
    public FieldErrorResponse editUser(@PathVariable("id") int id,
                                      @RequestBody Map<String, Object> person, BindingResult bindingResult) throws ConstraintViolationException {
        // парсинг json
        Person personEdit = personService.findById(id);
        String newLogin = (String) person.get("login");
        String newRole = (String) person.get("role");

        // валидация логина
        if (!(personEdit.getLogin().equals(newLogin))) {
            personEdit.setLogin(newLogin);
            personValidator.validate(personEdit, bindingResult);
        }
        // валидация роли
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();
        if (!(personEdit.getRole().equals(newRole))) {
            List<CustomFieldError> fieldErrors = new ArrayList<>();
            String role = (String) person.get("role");
            if ((!Objects.equals(role, "ROLE_ADMIN")) && (!Objects.equals(role, "ROLE_USER")) && (!Objects.equals(role, "ROLE_SELLER"))) {
                CustomFieldError fieldError = new CustomFieldError();
                fieldError.setField("roleError");
                fieldError.setMessage("Такой роли не существует");
                fieldErrors.add(fieldError);
                fieldErrorResponse.setFieldErrors(fieldErrors);
                return fieldErrorResponse;
            }
        }
        // обновление пользователя
        personEdit.setLogin(newLogin);
        personEdit.setRole(newRole);
//        personEdit.setPassword(passwordEncoder.encode(person.getPassword()));
        personService.updatePerson(id, personEdit);
        return fieldErrorResponse;
    }

    /* Логин не прошел валидацию */
    @ExceptionHandler(ConstraintViolationException.class)
    public  FieldErrorResponse handleException(ConstraintViolationException exception) {
        List<CustomFieldError> fieldErrors = new ArrayList<>();
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();
        CustomFieldError fieldError = new CustomFieldError();
        fieldError.setField("loginError");

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            fieldError.setMessage(violation.getMessage());
            fieldErrors.add(fieldError);
        }
        fieldErrorResponse.setFieldErrors(fieldErrors);
        return fieldErrorResponse;
    }

    /* ********************************************************** */
    /* ПРОДУКТЫ */
    /* ********************************************************** */

    @PostMapping(value = "/product/add")
    public FieldErrorResponse productAdd(@Valid @RequestBody Product product, BindingResult bindingResult) {
        // валидация полей
        productValidator.validate(product, bindingResult);
        List<CustomFieldError> fieldErrors = new ArrayList<>();
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();

        System.out.println(product.getPrice());
        // если есть ошибки - вывод сообщений
        // TODO поправить вывод ошибок по категориям
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
        productService.saveProduct(product);
        return fieldErrorResponse;
    }

    /* Удаление продукта */
    @GetMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("ok");
    }

    /* Редактирование продукта по id */
    @PostMapping("/product/edit/{id}")
    public FieldErrorResponse editProduct(@PathVariable("id") int id, @RequestBody Product product, BindingResult bindingResult) {

        Product productEdit = productService.getProductId(id);

        String newTitle = product.getTitle();
        String newSeller = product.getSeller();
        Double newPrice = product.getPrice();
        String newCategory = product.getCategory();
        System.out.println("newTitle: " + newTitle);
        System.out.println("newSeller: " + newSeller);
        System.out.println("newPrice: " + newPrice);
        System.out.println("newCategory: " + newCategory);

        productEdit.setTitle(newTitle);
        productEdit.setSeller(newSeller);
        productEdit.setPrice(newPrice);
        productEdit.setCategory(newCategory);

        productValidator.validate(productEdit, bindingResult);
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

        productService.updateProduct(id, productEdit);
        return fieldErrorResponse;
    }
}

