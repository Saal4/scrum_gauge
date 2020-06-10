package com.example.demo.controllers;

import com.example.demo.Question.QuestionRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserComponent;
import com.example.demo.user.UserRepository;
import com.example.demo.Question.Question;
import com.example.demo.Questionnaire.Questionnaire;
import com.example.demo.Questionnaire.QuestionnaireRespository;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;
import com.example.demo.Questionnaire_submit.Questionnaire_submit_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class QuestionnaireController {
    @Autowired
    UserComponent userComponent;
    @Autowired
    QuestionnaireRespository questionnaireRespository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Questionnaire_submit_Repository questionnaire_submit_repository;


    @RequestMapping("/questionnaire")
    public String questionnaire(Model model) {
        User u = userComponent.getLoggedUser();
        int idUser = u.getId();
        User user = userRepository.getUserById(idUser);
        String role = user.getRol();
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        model.addAttribute("role",role.equals("ROLE_USER")?"User":"Scrum Master");
        model.addAttribute("name", user.getUsername());
        Collection<Questionnaire> questionnairesUser = user.getQuestionnaires();
        int count = questionnairesUser.size();
        model.addAttribute("count", count);
        List<Questionnaire> questionnaires = this.questionnaireRespository.findAll();
        model.addAttribute("questionnnaires", questionnaires);
        model.addAttribute("logIn", true);
        return "Questionnaire_administration";
    }
    @RequestMapping("/questionnaire/pending")
    public String questionnairePending(Model model){
        User u = userComponent.getLoggedUser();
        int id = u.getId();
        User user = userRepository.getUserById(id);
        String role = user.getRol();
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        model.addAttribute("role",role.equals("ROLE_USER")?"User":"Scrum Master");
        model.addAttribute("name", user.getUsername());
        Collection<Questionnaire> questionnaires = user.getQuestionnaires();
        model.addAttribute("count",questionnaires.size());
        model.addAttribute("questionnnaires",questionnaires);
        model.addAttribute("logIn", true);
        return "formularios_pendientes";
    }


    @GetMapping("/questionnaire/Reply/{id}")
    public String questionnaire_reply(Model model,@PathVariable int id){
        User u = userComponent.getLoggedUser();
        int idUser = u.getId();
        User user = userRepository.getUserById(idUser);
        String role = user.getRol();
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        Questionnaire questionnaire = questionnaireRespository.getQuestionnaireById(id);
        model.addAttribute("number_questions", questionnaire.getQuestions().size());
        model.addAttribute("questionnaire",questionnaire);
        model.addAttribute("logIn",true);
        return "Questionnaire_reply";
    }
    @GetMapping("/questionnaire/edit/{id}")
    public String questionnnaire_edit(Model model, @PathVariable int id) {
        model.addAttribute("logIn", true);
        Questionnaire q = new Questionnaire("","","");
        q.setId(-1);
        List<Questionnaire> questionnairesCategory = questionnaireRespository.findAll();
        Set<String> categories = new HashSet<>();
        for (Questionnaire questionnaire:questionnairesCategory){
            categories.add(questionnaire.getCategory());
        }
        if(id != -1) {
            q = this.questionnaireRespository.getQuestionnaireById(id);
        }
        model.addAttribute("questionnaire", q);
        Collection<Question> questionsList = questionRepository.findAll();
        Collection<User> users = userRepository.findAll();
        if(id!=-1) {
            Collection<User> usersListDiferent = new ArrayList<>(q.getUsers());
            Collection<User> similarUser = new HashSet<>(users);
            Collection<User> differentUSer = new HashSet<>();
            differentUSer.addAll(users);
            differentUSer.addAll(usersListDiferent);
            similarUser.retainAll(usersListDiferent);
            differentUSer.removeAll(similarUser);
            model.addAttribute("userslist", differentUSer);

            Collection<Question> questionListQuestionnnaire = new ArrayList<>(q.getQuestions());
            Collection<Question> similar = new HashSet<Question>(questionsList);
            Collection<Question> different = new HashSet<Question>();
            different.addAll(questionsList);
            different.addAll(questionListQuestionnnaire);
            similar.retainAll(questionListQuestionnnaire);
            different.removeAll(similar);
            model.addAttribute("questionsList", different);
        }else{
            model.addAttribute("questionsList", questionsList);
            model.addAttribute("userslist" , users);
        }
        User u = userComponent.getLoggedUser();
        int idUser = u.getId();
        User user = userRepository.getUserById(idUser);
        String role = user.getRol();
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        model.addAttribute("role",role.equals("ROLE_USER")?"User":"Scrum Master");
        model.addAttribute("name", user.getUsername());
        Collection<Questionnaire> questionnaires = user.getQuestionnaires();
        int count = questionnaires.size();
        model.addAttribute("count", count);
        model.addAttribute("categories",categories);
        return "Questionnaire_edit";
    }

    @RequestMapping("/questionnaire/submit")
    public String questionnaire_save(Model model, @RequestParam String id, @RequestParam String answersQuestion){
        String[] answersStr = answersQuestion.split("/");
        User u = userComponent.getLoggedUser();
        int idu = u.getId();
        User user = userRepository.getUserById(idu);
        int idQuestionnaire = Integer.parseInt(id);
        Questionnaire questionnaire = questionnaireRespository.getQuestionnaireById(idQuestionnaire);
        Questionnaire_submit questionnaire_submit = new Questionnaire_submit(user,questionnaire);
        List<Question> listQuestion =  new ArrayList<>();
        for(String s: answersStr){
            String[] questions = s.split("-");
            Question q = questionRepository.getQuestionById(Integer.parseInt(questions[0]));
            if(questions.length>1 && questions[1]!=null) {
                questionnaire_submit.addValue(q, questions[1]);
            }else{
                questionnaire_submit.addValue(q,"-");
            }
            listQuestion.add(q);
        }
        questionnaire_submit_repository.save(questionnaire_submit);
        user.addQuestionnaire_submit(questionnaire_submit);
        questionnaire.addQuestionnaire_submit(questionnaire_submit);
        for(Question question:listQuestion){
            question.addQuestionnaire_submit(questionnaire_submit);
            questionRepository.save(question);
        }
        user.deleteQuestionnaire(questionnaire);
        questionnaire.removeUser(user);
        questionnaireRespository.save(questionnaire);
        userRepository.save(user);


        return "/questionnaire/pending";
    }


    @RequestMapping("/questionnaire/save")
    public String questionnaire_save(Model model, @RequestParam String id,@RequestParam String name, @RequestParam String category,
                                    @RequestParam String description, @RequestParam String idQuestion, @RequestParam String users) {

        //Cogemos el id de las preguntas asociadas
        String[] listId;
        if(!idQuestion.equals("")) {
            listId = idQuestion.split(",");
        }else{
            listId = new String[0];
        }

        //Creamos y rellenamos un array con las preguntas
        List<Question> l = new ArrayList<>();
        for (String s:listId ) {
            int i = Integer.parseInt(s);
            Question q = questionRepository.getQuestionById(i);
            l.add(q);
        }

        //creamos y rellanamos un array con los usuarios
        List<User> userList = new ArrayList<>();
        if(!users.equals("")) {
            String[] usersId = users.split(",");
            for (String userStr : usersId) {
                int userIdInt = Integer.parseInt(userStr);
                User u = userRepository.getUserById(userIdInt);
                userList.add(u);

            }
        }
        Questionnaire questionnaire;
        if(id.equals("new")){
            questionnaire = new Questionnaire(name,category, description,l,userList);
        }else{
            questionnaire = questionnaireRespository.getQuestionnaireById(Integer.parseInt(id));
            questionnaire.editQuestionnaire(name,category,description,l,userList);
        }
        questionnaireRespository.save(questionnaire);
        for (User u: userList){
            u.addQuestionnaire(questionnaire);
            userRepository.save(u);
        }
        for(Question question: l){
            question.addQuestionnarie(questionnaire);
            questionRepository.save(question);
        }

        return "redirect:/questionnaire";
    }


}
