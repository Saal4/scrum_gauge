package com.example.demo.Answer;


import com.example.demo.Question.Question;

import javax.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private int id;
    @Column(name="answer")
    private String answer;
    @Column(name = "correct")
    private boolean correct;
    @ManyToOne
    public Question question;

    public Answer() {
    }

    public Answer(String answer, boolean correct, Question question) {
        this.answer = answer;
        this.correct = correct;
        this.question = question;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return id == answer.id;
    }
}
