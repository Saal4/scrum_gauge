package com.example.demo;

import javax.annotation.PostConstruct;

import com.example.demo.Answer.Answer;
import com.example.demo.Answer.AnswerRepository;
import com.example.demo.Question.Question;
import com.example.demo.Question.QuestionRepository;
import com.example.demo.Questionnaire.Questionnaire;
import com.example.demo.Questionnaire.QuestionnaireRespository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class DatabaseInitializer {

	private static int numTopic =1;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private QuestionnaireRespository questionnaireRespository;
	@Autowired
	private AnswerRepository answerRepository;

	@PostConstruct
	public void init() {

		User u1= new User("Alberto","123","ua","ROLE_USER");
		User u2= new User("Alberto","123","ub","ROLE_SCRUM_GAUGE");
		userRepository.save(u1);
		userRepository.save(u2);

		String question1 = "pregunta 1";
		String question2 = "pregunta 2";
		String question3 = "Cuales son los roles principales dentro del equipo scrum";

		Question q1 = new Question(question1,"RU");
		Question q2 = new Question(question2,"MR");
		Question q3 = new Question(question3,"MR");

		Answer a11 = new Answer("respuesta 1-1",true,q1);
		Answer a12 = new Answer("respuesta 1-2",false,q1);
		Answer a13 = new Answer("respuesta 1-3",false,q1);
		Answer a14 = new Answer("respuesta 1-4",false,q1);
		Set<Answer> answersQ1 = new HashSet<>();
		answersQ1.add(a11);
		answersQ1.add(a12);
		answersQ1.add(a13);
		answersQ1.add(a14);
		q1.setAnswers(answersQ1);

		Answer a21 = new Answer("respuesta 2-1",true,q2);
		Answer a22 = new Answer("respuesta 2-2",false,q2);
		Answer a23 = new Answer("respuesta 2-3",true,q2);
		Answer a24 = new Answer("respuesta 2-4",false,q2);
		Set<Answer> answersQ2 = new HashSet<>();
		answersQ2.add(a21);
		answersQ2.add(a22);
		answersQ2.add(a23);
		answersQ2.add(a24);
		q2.setAnswers(answersQ2);

		Answer a31 = new Answer("respuesta 3-1",true,q3);
		Answer a32 = new Answer("respuesta 3-2",false,q3);
		Answer a33 = new Answer("respuesta 3-3",true,q3);
		Answer a34 = new Answer("respuesta 3-4",true,q3);
		Set<Answer> answersQ3 = new HashSet<>();
		answersQ3.add(a31);
		answersQ3.add(a32);
		answersQ3.add(a33);
		answersQ3.add(a34);
		q3.setAnswers(answersQ3);

		Questionnaire questionnaire1 = new Questionnaire("cuestionario 1","Category 1","Descripcion");
		Questionnaire questionnaire2 = new Questionnaire("cuestionario 2","Category 2","Descripcion del cuestionario numero 2");
		Questionnaire questionnaire3 = new Questionnaire("cuestionario 3","Category 1","Descripcion del cuestionario numero 3");
		Questionnaire questionnaire4 = new Questionnaire("cuestionario 4","Category 2","Descripcion del cuestionario numero 4");
		Questionnaire questionnaire5 = new Questionnaire("cuestionario 5","Category 1","Descripcion del cuestionario numero 5");
		Questionnaire questionnaire6 = new Questionnaire("cuestionario 6","Category 3","Descripcion del cuestionario numero 6");
		Questionnaire questionnaire7 = new Questionnaire("cuestionario 7","Category 4","Descripcion del cuestionario numero 7");





		questionnaire1.addQuestion(q1);
		questionnaire1.addQuestion(q2);
		q1.addQuestionnarie(questionnaire1);
		q2.addQuestionnarie(questionnaire1);

		questionRepository.save(q1);
		questionRepository.save(q2);
		questionRepository.save(q3);
		answerRepository.save(a11);
		answerRepository.save(a12);
		answerRepository.save(a13);
		answerRepository.save(a14);
		answerRepository.save(a21);
		answerRepository.save(a22);
		answerRepository.save(a23);
		answerRepository.save(a24);
		answerRepository.save(a31);
		answerRepository.save(a32);
		answerRepository.save(a33);
		answerRepository.save(a34);
		questionnaireRespository.save(questionnaire1);
		questionnaireRespository.save(questionnaire2);
		questionnaireRespository.save(questionnaire3);
		questionnaireRespository.save(questionnaire4);
		questionnaireRespository.save(questionnaire5);
		questionnaireRespository.save(questionnaire6);
		questionnaireRespository.save(questionnaire7);





	}


}
