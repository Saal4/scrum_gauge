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
    @Column (name = "subtraction")
    private boolean subtraction;
    @Column (name = "time")
    private int time;
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

    public Questionnaire(String name, String category, String description,boolean subtraction,int time) {
        this.setName(name);
        this.setDescription(description);
        this.subtraction = subtraction;
        this.setCategory(category);
        questions = new HashSet<>();
        users = new HashSet<>();
        questionnaire_submits = new ArrayList<>();
        this.time = time;
    }
    public Questionnaire(String name, String category, String description, List<Question> questionsList,List<User>userslist,boolean subtraction,int time) {
        this.setName(name);
        this.subtraction = subtraction;
        this.setDescription(description);
        this.setCategory(category);
        questions = new HashSet<>(questionsList) ;
        users = new HashSet<>(userslist);
        questionnaire_submits = new ArrayList<>();
        this.time = time;
    }
    public void editQuestionnaire(String name, String category, String description, List<Question> questionsList,List<User>userList,boolean subtraction,int time) {
        this.subtraction = subtraction;
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        questions = new HashSet<>(questionsList) ;
        users = new HashSet<>(userList);
        questionnaire_submits = new ArrayList<>();
        this.time = time;
    }
    public void addQuestionnaire_submit(Questionnaire_submit qs){
        if(this.questionnaire_submits == null){
            questionnaire_submits=new ArrayList<>();
        }
        this.questionnaire_submits.add(qs);
    }
    public void deleteQuestionnaire_submit(Questionnaire_submit qs){
        this.questionnaire_submits.remove(qs);
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

    public boolean isSubtraction() {
        return subtraction;
    }

    public void setSubtraction(boolean subtraction) {
        this.subtraction = subtraction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addQuestion (Question q){
        this.questions.add(q);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Questionnaire_submit> getQuestionnaire_submits() {
        return questionnaire_submits;
    }

    public void setQuestionnaire_submits(List<Questionnaire_submit> questionnaire_submits) {
        this.questionnaire_submits = questionnaire_submits;
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
