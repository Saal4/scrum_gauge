package com.example.demo.Answer;


import com.example.demo.Question.Question;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private int id;
    @Column(name="answer")
    public String answer;
    @Column(name = "correct")
    private boolean correct;
    @Column(name = "valueAnswer")
    private int valueAnswer;
    @ManyToOne
    public Question question;

    public Answer() {
    }

    public Answer(String answer, boolean correct, Question question, int value) {
        this.answer = answer;
        this.correct = correct;
        this.question = question;
        this.valueAnswer = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getValueAnswer() {
        return valueAnswer;
    }

    public void setValueAnswer(int valueAnswer) {
        this.valueAnswer = valueAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return id == answer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
