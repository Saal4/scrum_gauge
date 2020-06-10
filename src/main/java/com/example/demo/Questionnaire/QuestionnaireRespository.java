package com.example.demo.Questionnaire;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRespository extends JpaRepository<Questionnaire,Integer> {
    Questionnaire getQuestionnaireById(int id);


}
