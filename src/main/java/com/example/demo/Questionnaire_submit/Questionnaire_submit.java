package com.example.demo.Questionnaire_submit;

import com.example.demo.Answer.Answer;
import com.example.demo.Question.Question;
import com.example.demo.user.User;
import com.example.demo.Questionnaire.Questionnaire;

import javax.persistence.*;
import java.util.*;

@Entity
public class Questionnaire_submit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Questionnaire questionnaire;

    @JoinTable(
            name = "Questions_sumbited",
            joinColumns = @JoinColumn(name = "submit", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "question", nullable = false, updatable = false)
    )
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private List<Question> preguntas;

    @Column(name = "respuestas")
    private String respuestas;

    public Questionnaire_submit() {
    }

    public Questionnaire_submit(User u, Questionnaire q) {
        this.user = u;
        this.questionnaire = q;
        respuestas = "";
        preguntas = new ArrayList<>();
    }

    public void addValue(Question q, String s) {
        this.respuestas += "," + s;
        this.preguntas.add(q);
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public List<Question> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Question> preguntas) {
        this.preguntas = preguntas;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double correctQuestionnaire() {
        double totalCorrect = 0;
        String[] respuestasDadas = respuestas.substring(1).split(",");
        for (int i = 0; i < this.preguntas.size(); i++) {
            Question q = preguntas.get(i);
            List<Answer> answers = new ArrayList<>(q.getAnswers());
            List<Integer> answersCorrect = new ArrayList<>();
            for (Answer a : answers) {
                if (a.isCorrect()) {
                    answersCorrect.add(a.getId());
                }
            }
            String s = respuestasDadas[i];
            String[] respuestasPregunta = s.split("@");
            if (q.getUniqueResponse()) {
                if (!respuestasPregunta[0].equals("") && !respuestasPregunta[0].equals("-") && answersCorrect.contains(Integer.parseInt(respuestasPregunta[0]))) {
                    totalCorrect++;
                }
            } else {
                for (String respuestasDada : respuestasPregunta) {
                    if (!respuestasDada.equals("") && !respuestasDada.equals("-") && answersCorrect.contains(Integer.parseInt(respuestasDada))) {
                        totalCorrect += ((double) 1 / answersCorrect.size());
                    }
                }
            }
        }
        return (double) totalCorrect/this.preguntas.size();
    }

}
