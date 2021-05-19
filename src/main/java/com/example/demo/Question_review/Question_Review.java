package com.example.demo.Question_review;

import antlr.collections.List;
import com.example.demo.Answer.Answer;
import com.example.demo.Question.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Question_Review {
    public Question question;
    //String 3 opcions: RC (response correct), NR(response not correct) , RI (Response incorrect)
    public ArrayList<Answer_correction> answerList;
    public ArrayList<String> correctAnswer;
    public double point;
    public boolean correctQuestion;

    public Question_Review(Question question) {
        this.question = question;
        answerList = new ArrayList<>();
        correctAnswer = new ArrayList<>();
        point = 0;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Answer_correction> getAnswerList() {
        return answerList;
    }

    public boolean isCorrectQuestion() {
        return correctQuestion;
    }

    public void setCorrectQuestion(boolean correctQuestion) {
        this.correctQuestion = correctQuestion;
    }

    public void setAnswerList(ArrayList<Answer_correction> answerList) {
        this.answerList = answerList;
    }

    public ArrayList<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(ArrayList<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public void addAnswer (Answer a, String s){
        this.answerList.add(new Answer_correction(a,s));
    }
    public void assignCorrectList (){
        ArrayList<Answer> answerList = new ArrayList<>(question.getAnswers());
        ArrayList<String> arrayAux = new ArrayList<>();
        for (Answer a:answerList){
            if (a.isCorrect() && !this.answerList.get(this.answerList.indexOf(new Answer_correction(a,""))).getCorrection().equals("RC")){
                arrayAux.add(a.getAnswer());
            }
        }
        this.correctQuestion = arrayAux.size() > 0;
        this.correctAnswer = arrayAux;
    }
}
