package com.example.demo.Question;

import com.example.demo.Answer.Answer;
import com.example.demo.Questionnaire.Questionnaire;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;

import javax.persistence.*;
import java.util.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    public int id;
    @Column(name="Question")
    private String question ;
    @Column(name="Type")
    private String type;
    @Column(name = "uniqueResponse")
    private Boolean uniqueResponse;

    @OneToMany(cascade= CascadeType.ALL ,mappedBy = "question")
    private Set<Answer> answers;

    @ManyToMany (cascade = {CascadeType.MERGE,CascadeType.DETACH},fetch = FetchType.LAZY, mappedBy = "preguntas")
    private List<Questionnaire_submit> questionnaire_submits;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH},fetch = FetchType.LAZY, mappedBy = "questions")
    private Set<Questionnaire> questionnaire;


    public Question() {
    }
    public Question(String question, Set<Answer> answers, String type) {
        this.question = question;
        this.setAnswers(answers);
        this.type = type;
        this.setUniqueResponse(type.equals("RU"));
        questionnaire = new HashSet<>();
        questionnaire_submits = new ArrayList<>();
    }
    public Question(String question, String type) {
        this.question = question;
        this.answers = new HashSet<>();
        this.type = type;
        this.setUniqueResponse(type.equals("RU"));
        questionnaire = new HashSet<>();
        questionnaire_submits = new ArrayList<>();
    }

    public Boolean getUniqueResponse() {
        return uniqueResponse;
    }

    public void setUniqueResponse(Boolean uniqueResponse) {
        this.uniqueResponse = uniqueResponse;
    }

    public void addAnswer(Answer answer){
        this.answers.add(answer);
    }
    public void addQuestionnaire (Questionnaire q){
        this.questionnaire.add(q);
    }
    public void addQuestionnaire_submit(Questionnaire_submit qs){this.questionnaire_submits.add(qs);}
    public void editQuestion(String question, Set<Answer> answers, String type){
        this.question = question;
        this.setAnswers(answers);
        this.type = type;
        this.setUniqueResponse(type.equals("RU"));
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }


    public Set<Questionnaire> getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Set<Questionnaire> questionnaire) {
        this.questionnaire = questionnaire;
    }
    public  void addQuestionnarie(Questionnaire q){
        this.questionnaire.add(q);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
