package com.example.demo.Questionnaire_submit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Questionnaire_submit_Repository extends JpaRepository<Questionnaire_submit,Integer> {

    List<Questionnaire_submit> findAllByQuestionnaireCategoryAndUserId(String category, int id);
    List<Questionnaire_submit> findAllByUserId(int id);
}
