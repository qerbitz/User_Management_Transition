package com.example.task4itransition.controller;

import com.example.task4itransition.entity.User;
import com.example.task4itransition.exception.EmailExistException;
import com.example.task4itransition.exception.UserNotFoundException;
import com.example.task4itransition.exception.UsernameExistException;
import com.example.task4itransition.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("registration")
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping("/showRegistrationForm")
    public String showRegistrationForm(Model model){

        User user = new User();
        model.addAttribute("user", user);

        return "registration_form";
    }

    @PostMapping("/processRegistration")
    public String processRegistration(@Valid @ModelAttribute("user") User user, Model model, BindingResult bindingResult) {

        if(userService.checkUniquenessUsername(user.getUsername())) {
            bindingResult.addError(new FieldError("username", "username", "Username already exist"));
        }
        if(!(user.getPassword().length()<=1)){
            bindingResult.addError(new FieldError("password", "password", "Password too short"));
        }
        if(!isValidEmail(user.getEmail()) || userService.checkUniquenessEmail(user.getEmail())){
            bindingResult.addError(new FieldError("email", "email", "E-mail is not valid or already exist"));
        }
        else{
            userService.register(user);
            return "goodregistery";
        }


        return "registration_form";
    }

    @GetMapping("/good_registery")
    public String good_registery(Model theModel){
        return "goodregistery";
    }


    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();

        return validator.isValid(email);
    }

}