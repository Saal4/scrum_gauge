package com.example.demo.user;

import com.example.demo.Questionnaire.Questionnaire;
import com.example.demo.Questionnaire_submit.Questionnaire_submit;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private int id;


    @Column(name="UserName")
    private String username;
    @Column(name="Name")
    private String name;

    @Column(name="Password")
    private String password;
    @Column(name="Rol")
    private String rol;
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH},fetch = FetchType.EAGER , mappedBy = "users")
    private Set<Questionnaire> questionnaires;

    @OneToMany(cascade={CascadeType.REMOVE},mappedBy = "user")
    private Set<Questionnaire_submit> questionnaire_submits;

    public User() {
    }
    public User(String name, String password, String userName, String userType) {
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = userName;
        this.rol= userType;
        this.questionnaires = new HashSet<>();
        this.questionnaire_submits = new HashSet<>();
    }
    public void editUser(String name, String password, String userName, String userType) {
        this.name = name;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = userName;
        this.rol= userType;
    }
    public void addQuestionnaire (Questionnaire q){
        this.questionnaires.add(q);
    }
    public void deleteQuestionnaire(Questionnaire q){
        this.questionnaires.remove(q);
    }
    public void addQuestionnaire_submit (Questionnaire_submit q){
        this.questionnaire_submits.add(q);
    }

    public Set<Questionnaire_submit> getQuestionnaire_submits() {
        return questionnaire_submits;
    }

    public void setQuestionnaire_submits(Set<Questionnaire_submit> questionnaire_submits) {
        this.questionnaire_submits = questionnaire_submits;
    }

    public Set<Questionnaire_submit> remove_Questionnaire_submit(Questionnaire_submit q){
        Set<Questionnaire_submit> questionnaire_submit= new HashSet<>();
        for (Questionnaire_submit q1: this.questionnaire_submits) {
            if(q1.getId() != q.getId()){
                questionnaire_submit.add(q1);
            }
        }
        this.questionnaire_submits = questionnaire_submit;
        return questionnaire_submit;
    }

    public Set<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
