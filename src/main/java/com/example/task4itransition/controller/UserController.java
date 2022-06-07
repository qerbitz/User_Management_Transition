package com.example.task4itransition.controller;

import com.example.task4itransition.entity.User;
import com.example.task4itransition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/")
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping(value="/block", method=RequestMethod.POST, params="lock")
    public String blockUsers(@RequestParam("idChecked")List<Long> users) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        userService.blockUser(users);
        if(user.isStatus()==false){
            return "redirect:/login?logout";
        }
        else{
            return "redirect:/allUsers";
        }
    }

    @RequestMapping(value="/block", method=RequestMethod.POST, params="activate")
    public String activateUsers(@RequestParam("idChecked")List<Long> users) {
        userService.activateUser(users);
        return "redirect:/allUsers";
    }

    @RequestMapping(value="/block", method=RequestMethod.POST, params="delete")
    public String deleteUsers(@RequestParam("idChecked")List<Long> users) {
        userService.deleteUser(users);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        if(user==null){
            return "redirect:/login?logout";
        }
        else{
            return "redirect:/allUsers";
        }
    }

    @GetMapping("allUsers")
    public String usersList(Model model) {
        model.addAttribute("userObj",new ArrayList<Long>());
        model.addAttribute("usersList", userService.getAllUsers());
        return "panel";
    }
}

