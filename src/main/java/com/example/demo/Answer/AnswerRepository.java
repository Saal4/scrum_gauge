package com.example.demo.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
public interface AnswerRepository extends JpaRepository<Answer,Integer> {
    Answer getAnswerById(int id);

    @Modifying
    @Transactional
    @Query(value="delete from Answer a where a.id = ?1")
    void deleteById(int id);

    List<Answer> findAllByQuestionId(int id);

    void deleteAnswerById(int id);

    void deleteAllByQuestionId(int id);

    List<Answer> findAllByQuestionIdAndCorrectTrue(int id);
}
