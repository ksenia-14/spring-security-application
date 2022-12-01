package com.example.springsecurityapplication.controllers.excCont;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController {

//    @GetMapping("/index")
//    public String index() {
//        /* Получаем объект аутентификации - с помощью Spring SecurityContextHolder
//        * обращаемся к контексту и на нем вызываем метод аутентификации
//        * Из потока для текущего пользователя получаем объект, который был положен
//        * в сессию после аутентификации */
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
////        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
////        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
////        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
//        return "index";
//    }
}
