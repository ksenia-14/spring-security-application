package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {
    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("person", new Person());
//    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) {
        return "authentication/registration";
    }

    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person,
                                     BindingResult bindingResult) {
        /* Если валидатор возвращает ошибку - помещаем данную ошибку в bindingResult */
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "authentication/registration";
        }
        personService.register(person);
        return "redirect:/index";
    }
}
