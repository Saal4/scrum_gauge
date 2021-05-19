package com.example.demo.Question_review;

import com.example.demo.Answer.Answer;

import java.util.Objects;

public class Answer_correction {
    public Answer answer;
    public String correction;

    public Answer_correction(Answer answer, String correction) {
        this.answer = answer;
        this.correction = correction;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer_correction that = (Answer_correction) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }
}
