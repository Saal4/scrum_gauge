package com.example.demo.Questionnaire;


import com.example.demo.Question.Question;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;
import com.example.demo.user.User;

import javax.persistence.*;
import java.util.*;

@Entity
public class Questionnaire{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;
    @Column(name = "QuestionnaireName")
    private String name;
    @Column(name = "category")
    private String category;
    @Column(name= "Short_description")
    private String description;
    @JoinTable(
            name = "Question_questionnaire",
            joinColumns = @JoinColumn(name = "questionId", nullable = false, updatable = false),
            inverseJoinColumns =@JoinColumn(name = "questionnaireId", nullable = false, updatable = false)
    )
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Set<Question> questions;

    @OneToMany(cascade= CascadeType.ALL ,mappedBy = "questionnaire")
    private List<Questionnaire_submit> questionnaire_submits;

    @JoinTable(
            name = "questionnaire_users",
            joinColumns = @JoinColumn(name = "questionnaireId", nullable = false, updatable = false),
            inverseJoinColumns =@JoinColumn(name = "userId", nullable = false, updatable = false)
    )
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Questionnaire(String name, String category, String description) {
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        questions = new HashSet<>();
        questionnaire_submits = new ArrayList<>();
    }
    public Questionnaire(String name, String category, String description, List<Question> questionsList,List<User>userslist) {
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        questions = new HashSet<>(questionsList) ;
        users = new HashSet<>(userslist);
        questionnaire_submits = new ArrayList<>();
    }
    public void editQuestionnaire(String name, String category, String description, List<Question> questionsList,List<User>userList) {
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        questions = new HashSet<>(questionsList) ;
        users = new HashSet<>(userList);
        questionnaire_submits = new ArrayList<>();
    }
    public void addQuestionnaire_submit(Questionnaire_submit qs){
        if(this.questionnaire_submits == null){
            questionnaire_submits=new ArrayList<>();
        }
        this.questionnaire_submits.add(qs);
    }
    public void removeUser(User u){
        this.users.remove(u);
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Questionnaire() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addQuestion (Question q){
        this.questions.add(q);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
