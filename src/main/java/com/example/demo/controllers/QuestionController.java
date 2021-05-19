package com.example.demo.controllers;

import com.example.demo.Question.Question;
import com.example.demo.Question.QuestionRepository;
import com.example.demo.Questionnaire.QuestionnaireRespository;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;
import com.example.demo.Questionnaire_submit.Questionnaire_submit_Repository;
import com.example.demo.user.User;
import com.example.demo.user.UserComponent;
import com.example.demo.user.UserRepository;
import com.example.demo.Answer.Answer;
import com.example.demo.Answer.AnswerRepository;
import com.example.demo.Questionnaire.Questionnaire;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    Questionnaire_submit_Repository questionnaire_submit_repository;

    @Autowired
    QuestionnaireRespository questionnaireRespository;
    @Autowired
    AnswerRepository answerRespository;

    @Autowired
    UserComponent userComponent;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/questions")
    public String questionMain(Model model) {
        User u = userComponent.getLoggedUser();
        int id = u.getId();
        User user = userRepository.getUserById(id);
        String role = user.getRol();
        model.addAttribute("scrum_user",!role.equals("ROLE_USER"));
        model.addAttribute("role",role.equals("ROLE_USER")?"User":"Scrum Master");
        model.addAttribute("name", user.getUsername());
        Collection<Questionnaire> questionnaires = user.getQuestionnaires();
        int count = questionnaires.size();
        model.addAttribute("count", count);
        List<Question> questionList = this.questionRepository.findAll();
        model.addAttribute("questions", questionList);
        model.addAttribute("logIn", true);
        return "Questions_administration";
    }

    @RequestMapping("questions/delete/{id}")
    public String deleteQuestion(Model model, @PathVariable String id) {
        questionRepository.deleteById(Integer.parseInt(id));
        return "redirect:/questions";
    }

    @RequestMapping("/question/save")
    public String saveQuestion(Model model, @RequestParam String questionId, @RequestParam String question,
                               @RequestParam String question_type, @RequestParam String answers) {

        Question newQuestion;
        if (questionId.equals("new")) {
            newQuestion = new Question(question, question_type);
        } else {
            newQuestion = questionRepository.getQuestionById(Integer.parseInt(questionId));
            Collection<Answer> answers1 = newQuestion.getAnswers();
            Collection<Questionnaire_submit> questionnaire_submits = newQuestion.getQuestionnaire_submits();
            for(Questionnaire_submit q1: questionnaire_submits){
                User user = q1.getUser();
                user.remove_Questionnaire_submit(q1);
                q1.setUser(null);
                Questionnaire questionnaire = q1.getQuestionnaire();
                questionnaire.deleteQuestionnaire_submit(q1);
                q1.setQuestionnaire(null);
                userRepository.save(user);
                questionnaireRespository.save(questionnaire);
                questionnaire_submit_repository.deleteById(q1.getId());
            }
            for (Answer a1: answers1){
                answerRespository.deleteById(a1.getId());
            }
            List<Answer> testlist = answerRespository.findAllByQuestionId(newQuestion.getId());
            newQuestion.setAnswers(new ArrayList<>());
        }
        //System.out.println(answers);
        String[] answer = answers.split("·");
        ArrayList<Answer> aux = new ArrayList<>();
        for (String s:answer){
            String[] sAux = s.split("#");
            Answer answer1;
            //System.out.println(Arrays.toString(sAux));
            answer1 = new Answer(sAux[0],Boolean.parseBoolean(sAux[1]),newQuestion,Integer.parseInt(sAux[2]));
            aux.add(answer1);
            //System.out.println(aux.size());
            //answerSet.add(answer1);
        }
        newQuestion.editQuestion(question,aux,question_type);
        questionRepository.save(newQuestion);
        for (Answer a:aux){
            //System.out.println("test");
            answerRespository.save(a);
        }
        this.remove_null_questionnaire();
        return "redirect:/questions";

    }
    private void remove_null_questionnaire(){
        List<Questionnaire_submit> questionnaire_submits = questionnaire_submit_repository.findAllByQuestionnaireIsNull();
        for (Questionnaire_submit q:questionnaire_submits ) {
            questionnaire_submit_repository.deleteById(q.getId());
        }
    }
    @RequestMapping("/question/edit/{id}")
    public String editQuestion(Model model, @PathVariable String id) {
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
        if (id.equals("new")) {
            model.addAttribute("questions", true);
            model.addAttribute("question", "");
            model.addAttribute("uniqueResponse", true);
            model.addAttribute("numberAnswer", 0);
            return "modal_edit_question";
        }
        Question question = questionRepository.getQuestionById(Integer.parseInt(id));
        List<Answer> answer = question.getAnswers();
        model.addAttribute("questions", question);
        model.addAttribute("numberAnswer", answer.size());
        model.addAttribute("uniqueResponse", question.getType().equals("RU"));

        return "modal_edit_question";
    }


}
