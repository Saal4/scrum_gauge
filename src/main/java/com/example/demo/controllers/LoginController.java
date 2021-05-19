package com.example.demo.controllers;

import com.example.demo.user.User;
import com.example.demo.user.UserComponent;
import com.example.demo.user.UserRepository;
import com.example.demo.Questionnaire.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserComponent userComponent;

    @RequestMapping("/")
    public String emptyPage(Model model){
        return "redirect:/logIn";
    }
    @RequestMapping("/logIn")
    public String logIn(Model model) {
        model.addAttribute("inOut", "out");
        model.addAttribute("logIn", false);
        model.addAttribute("newAccount_successful",false);
        model.addAttribute("errorPassword", false);
        return "index";
    }
    @RequestMapping("/loginError")
    public String logError(Model model) {
        model.addAttribute("inOut", "out");
        model.addAttribute("logIn", false);
        model.addAttribute("newAccount_successful",false);
        model.addAttribute("errorPassword", true);
        return "index";
    }
    @GetMapping("/MainPage/logOut")
    public String logout(Model model, HttpSession session) {
        model.addAttribute("inOut", "out");
        model.addAttribute("logIn", false);
        model.addAttribute("newAccount_successful",false);
        session.invalidate();
        return "redirect:/logIn";
    }
    @RequestMapping("logIn/newAccount")
    public String newAccount(Model model){
        model.addAttribute("logIn", false);
        return "NewAccount";
    }
    @RequestMapping("/logIn/newAccount/try")
    public String newAccountTry(Model model, @RequestParam String username, @RequestParam String rol,
                                @RequestParam String name, @RequestParam String password) {

            User user = userRepository.findByUsername(username);
            if (user != null) {
                model.addAttribute("show", true);
                model.addAttribute("errorUserName", true);
                model.addAttribute("success", false);
                model.addAttribute("showSubmit", true);
                model.addAttribute("logIn", false);
                model.addAttribute("rolError", false);

                return "NewAccount";
            } else {
                User newUser = new User(name, password, username, rol);
                userRepository.save(newUser);

                model.addAttribute("logIn", false);
                model.addAttribute("newAccount_successful",true);
                return "index";
            }
        }
    @RequestMapping("/config/edit/try")
    public String saveAccountTry(Model model, @RequestParam String username, @RequestParam String rol,
                                @RequestParam String name, @RequestParam String password) {

        User actualUser = userComponent.getLoggedUser();
        int id = actualUser.getId();
        User u = userRepository.getUserById(id);
        User user = userRepository.findByUsername(username);
        if (user != null && id!=user.getId()) {
            model.addAttribute("show", true);
            model.addAttribute("errorUserName", true);
            model.addAttribute("success", false);
            model.addAttribute("showSubmit", true);
            model.addAttribute("logIn", false);
            model.addAttribute("rolError", false);
            return "Edit_Acount";
        } else {
            assert user != null;
            u.editUser(name, password, username, rol);
            userRepository.save(u);
            return "redirect:/MainPage";
        }
    }
    @RequestMapping("config/edit")
    public String saveAccount(Model model){
        User u = userComponent.getLoggedUser();
        int id = u.getId();
        User user = userRepository.getUserById(id);
        String role = user.getRol();
        List<Questionnaire> questionnairesUser  = new ArrayList<>(user.getQuestionnaires());
        int count = questionnairesUser.size();
        model.addAttribute("count",count);
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        model.addAttribute("role",role.equals("ROLE_USER")?"User":"Scrum Master");
        model.addAttribute("name", user.getUsername());
        model.addAttribute("user",user);
        model.addAttribute("logIn", true);
        return "Edit_Acount";
    }
    @GetMapping("/logIn/try")
    public String logInTry(Model model) {
        User user = userComponent.getLoggedUser();
        if(user == null){
            model.addAttribute("errorPassword", true);
            return "index";
        }else{
            model.addAttribute("logIn", true);
            return "NewAccount";
        }

    }

}
