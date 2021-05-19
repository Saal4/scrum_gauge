package com.example.demo.controllers;


import com.example.demo.user.User;
import com.example.demo.user.UserComponent;
import com.example.demo.user.UserRepository;
import com.example.demo.Questionnaire.Questionnaire;
import com.example.demo.Questionnaire.QuestionnaireRespository;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;
import com.example.demo.Questionnaire_submit.Questionnaire_submit_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
public class MainController {
    @Autowired
    private UserComponent userComponent;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Questionnaire_submit_Repository questionnaire_submit_repository;
    @Autowired
    private QuestionnaireRespository questionnaireRespository;

    @GetMapping(path = "/MainPage")
    public String mainP(Model model) {
        User u = userComponent.getLoggedUser();
        int id = u.getId();
        User user = userRepository.getUserById(id);
        String role = user.getRol();
        model.addAttribute("scrum_user", !role.equals("ROLE_USER"));
        model.addAttribute("role", role.equals("ROLE_USER") ? "User" : "Scrum Master");
        model.addAttribute("name", user.getUsername());
        HashMap<String, Integer> categoryTotal = new HashMap<>();
        List<Questionnaire> questionnairesUser = new ArrayList<>(user.getQuestionnaires());
        int count = questionnairesUser.size();
        List<Questionnaire> questionnaires = questionnaireRespository.findAll();
        List<String> categories = new ArrayList<>();
        ArrayList<Double> percentage = new ArrayList<>();
        ArrayList<Integer> numberTest = new ArrayList<>();
        ArrayList<Double> percentageAux = new ArrayList<>();
        if (role.equals("ROLE_USER")) {
            for (Questionnaire questionnaire : questionnaires) {
                if (!categoryTotal.containsKey(questionnaire.getCategory())) {
                    categoryTotal.put(questionnaire.getCategory(), categories.size());
                    categories.add(questionnaire.getCategory());
                    percentage.add(0.0);
                    numberTest.add(0);
                    percentageAux.add(0.0);
                }

            }
            List<Questionnaire_submit> questionnaire_submits_User = questionnaire_submit_repository.findAllByUserId(id);
            for (Questionnaire_submit q : questionnaire_submits_User) {
                String category = q.getQuestionnaire().getCategory();
                double totalQuestionnaire = q.correctQuestionnaire();
                if (categoryTotal.containsKey(category)) {
                    int position = categoryTotal.get(category);
                    double totalNow = percentage.get(position);
                    totalNow += totalQuestionnaire;
                    percentage.remove(position);
                    percentage.add(position, totalNow);
                    int index = numberTest.get(position);
                    index++;
                    numberTest.remove(position);
                    numberTest.add(position, index);
                }
            }
        } else {
            //Dividir entre el numero de con el array de arriba
            //crear grafico de barras
            //hacer bien las formatos
            for (Questionnaire questionnaire : questionnaires) {
                if (!categoryTotal.containsKey(questionnaire.getCategory())) {
                    categoryTotal.put(questionnaire.getCategory(), categories.size());
                    categories.add(questionnaire.getCategory());
                    percentage.add(0.0);
                    numberTest.add(0);
                    percentageAux.add(0.0);
                }
            }
            List<Questionnaire_submit> listaSubmited = this.questionnaire_submit_repository.findAll();
            for (Questionnaire_submit q : listaSubmited) {
                String category = q.getQuestionnaire().getCategory();
                double totalQuestionnaire = q.correctQuestionnaire();
                int position = categories.size();
                if (categoryTotal.containsKey(category)) {
                    position = categoryTotal.get(category);
                    double totalNow = percentage.get(position);
                    totalNow += totalQuestionnaire;
                    percentage.remove(position);
                    percentage.add(position, totalNow);
                    int index = numberTest.get(position);
                    index++;
                    numberTest.remove(position);
                    numberTest.add(position, index);
                }
            }
        }
        for (int i = 0; i < percentage.size(); i++) {
            double percent = percentage.get(i);
            int number = numberTest.get(i);
            if (percent == 0 || number == 0) {
                continue;
            }
            double total = (percent * 100) / number;
            if (total > 99.5) {
                total = 100;
            }
            percentageAux.remove(i);
            percentageAux.add(i, total);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("percent", percentageAux);
        model.addAttribute("count", count);
        model.addAttribute("logIn", true);
        return "Main";
    }

    @GetMapping(path = "/Result")
    public String result(Model model) {
        User u = userComponent.getLoggedUser();
        int id = u.getId();
        User user = userRepository.getUserById(id);
        String role = user.getRol();
        List<Questionnaire> questionnairesUser = new ArrayList<>(user.getQuestionnaires());
        int count = questionnairesUser.size();
        List<User> userList = userRepository.findAll();
        HashMap<String, Integer> categoryTotal = new HashMap<>();
        List<Questionnaire> questionnaires = questionnaireRespository.findAll();
        List<String> categories = new ArrayList<>();
        ArrayList<Double> percentage = new ArrayList<>();
        ArrayList<Integer> numberTest = new ArrayList<>();
        ArrayList<Double> percentageAux = new ArrayList<>();
        if (role.equals("ROLE_USER")) {
            for (Questionnaire questionnaire : questionnaires) {
                if (!categoryTotal.containsKey(questionnaire.getCategory())) {
                    categoryTotal.put(questionnaire.getCategory(), categories.size());
                    categories.add(questionnaire.getCategory());
                    percentage.add(0.0);
                    numberTest.add(0);
                    percentageAux.add(0.0);
                }

            }
            List<Questionnaire_submit> questionnaire_submits_User = questionnaire_submit_repository.findAllByUserId(id);
            for (Questionnaire_submit q : questionnaire_submits_User) {
                String category = q.getQuestionnaire().getCategory();
                double totalQuestionnaire = q.correctQuestionnaire();
                if (categoryTotal.containsKey(category)) {
                    int position = categoryTotal.get(category);
                    double totalNow = percentage.get(position);
                    totalNow += totalQuestionnaire;
                    percentage.remove(position);
                    percentage.add(position, totalNow);
                    int index = numberTest.get(position);
                    index++;
                    numberTest.remove(position);
                    numberTest.add(position, index);
                }
            }
        } else {
            for (Questionnaire questionnaire : questionnaires) {
                if (!categoryTotal.containsKey(questionnaire.getCategory())) {
                    categoryTotal.put(questionnaire.getCategory(), categories.size());
                    categories.add(questionnaire.getCategory());
                    percentage.add(0.0);
                    numberTest.add(0);
                    percentageAux.add(0.0);
                }
            }
            List<Questionnaire_submit> listaSubmited = this.questionnaire_submit_repository.findAll();
            for (Questionnaire_submit q : listaSubmited) {
                String category = q.getQuestionnaire().getCategory();
                double totalQuestionnaire = q.correctQuestionnaire();
                int position;
                if (categoryTotal.containsKey(category)) {
                    position = categoryTotal.get(category);
                    double totalNow = percentage.get(position);
                    totalNow += totalQuestionnaire;
                    percentage.remove(position);
                    percentage.add(position, totalNow);
                    int index = numberTest.get(position);
                    index++;
                    numberTest.remove(position);
                    numberTest.add(position, index);
                }
            }
        }
        for (int i = 0; i < percentage.size(); i++) {
            double percent = percentage.get(i);
            int number = numberTest.get(i);
            if (percent == 0 || number == 0) {
                continue;
            }
            double total = (percent * 100) / number;
            if (total > 99.5) {
                total = 100;
            }
            percentageAux.remove(i);
            percentageAux.add(i, total);
        }
        model.addAttribute("userslist", role.equals("ROLE_USER")?new ArrayList<>(Collections.singletonList(user)):userList);
        model.addAttribute("scrum_user", !role.equals("ROLE_USER"));
        model.addAttribute("role", role.equals("ROLE_USER") ? "User" : "Scrum Master");
        model.addAttribute("count", count);
        model.addAttribute("categories", categories);
        model.addAttribute("percent", percentageAux);
        model.addAttribute("name", user.getUsername());
        model.addAttribute("logIn", true);
        return "Result";
    }

    @RequestMapping("/Result/allusersSomeCategories")
    public String allUsersSomeCategories(Model model, @RequestParam String categories, @RequestParam String users, @RequestParam String type){
        String[] groupBy;
        String[] dataLabel;
        List<User> userList = new ArrayList<>();
        User actual_user = userComponent.getLoggedUser();
        int id_actual_user = actual_user.getId();
        User user = userRepository.getUserById(id_actual_user);
        String role = user.getRol();
        if(role.equals("ROLE_USER")){
            userList = Collections.singletonList(user);
        }else if (users.equals("all")){
            userList = userRepository.findAll();
        }else{
            String[] usersId = users.split(",");
            for (String s:usersId) {
                s = s.replace(" ","");
                int id = Integer.parseInt(s);
                User u = userRepository.getUserById(id);
                userList.add(u);
            }
        }
        String[] categoriesString = categories.split(",");
        String str;
        if(type.equals("User")){
            str = getUserJson(userList,categoriesString);
        }else{
            str = getCategoryJson(categoriesString,userList);
        }

        //String str = this.createJson(finalJsonArray);
        //String str = "ua:[15.0,100.0,50.0,80.0]|ub:[88.00,77.9,25.00,25]";
        model.addAttribute("information", str);
        return "ChartInformation";
    }
    private String getUserJson(List<User> userList, String[] categoriesList){
        ArrayList<Double> totalArray = new ArrayList<>();
        HashMap<String, ArrayList<Double>> finalJsonArray = new HashMap<>();
        double totalCero = 0.0;
        for (User user : userList) {
            totalArray = new ArrayList<>();
            String nombre = user.getUsername();
            int id = user.getId();
            for (String s : categoriesList) {
                double totalCategory = 0.0;
                List<Questionnaire_submit> q = questionnaire_submit_repository.findAllByQuestionnaireCategoryAndUserId(s, id);
                if (q != null && !q.isEmpty()) {
                    for (Questionnaire_submit questionnaire_submit : q) {
                        totalCategory += questionnaire_submit.correctQuestionnaire();
                    }
                    totalCategory = (totalCategory*100)/q.size();
                    totalArray.add(totalCategory);
                } else {
                    totalArray.add(totalCero);
                }
            }
            finalJsonArray.put(nombre, totalArray);
        }
        String JsonStr = this.createJson(finalJsonArray);
        String finalJsonStr = JsonStr + "-" + Arrays.toString(categoriesList);
        return finalJsonStr;
    }
    private String getCategoryJson(String[] categoriesList, List<User> userList){
        ArrayList<Double> totalArray;
        HashMap<String, ArrayList<Double>> finalJsonArray = new HashMap<>();
        double totalCero = 0.0;
        boolean first = true;
        String usersStr = "";
        for (String category : categoriesList) {
            totalArray = new ArrayList<>();
            for (User user : userList) {
                if(first){
                    usersStr += user.getUsername() + ",";
                }
                int id = user.getId();
                double totalCategory = 0.0;
                List<Questionnaire_submit> q = questionnaire_submit_repository.findAllByQuestionnaireCategoryAndUserId(category, id);
                if (q != null && !q.isEmpty()) {
                    for (Questionnaire_submit questionnaire_submit : q) {
                        totalCategory += questionnaire_submit.correctQuestionnaire();
                    }
                    totalCategory = (totalCategory*100)/q.size();
                    totalArray.add(totalCategory);
                } else {
                    totalArray.add(totalCero);
                }
            }
            first = false;
            finalJsonArray.put(category, totalArray);
        }
        String[] userArray = usersStr.split(",");
        String JsonStr = this.createJson(finalJsonArray);
        String finalJsonStr = JsonStr + "-" + Arrays.toString(userArray);
        return finalJsonStr;
    }
    private String createJson(HashMap<String, ArrayList<Double>> hashMap) {
        String json = "";
        for (Map.Entry<String, ArrayList<Double>> entry : hashMap.entrySet()) {
            json += entry.getKey() + ":" + entry.getValue().toString() + "|";
        }
        json = json.substring(0, json.length() - 1);
        return json;
    }


}