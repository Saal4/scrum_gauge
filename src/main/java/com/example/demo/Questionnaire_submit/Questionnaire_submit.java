package com.example.demo.Questionnaire_submit;

import com.example.demo.Answer.Answer;
import com.example.demo.Question.Question;
import com.example.demo.user.User;
import com.example.demo.Questionnaire.Questionnaire;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
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
    @Column(name = "result")
    private double result;

    public Questionnaire_submit() {
    }

    public Questionnaire_submit(User u, Questionnaire q) {
        this.user = u;
        this.questionnaire = q;
        respuestas = "";
        preguntas = new ArrayList<>();
    }

    public void addValue(Question q, String s) {
        this.respuestas += "," + q.getId() + "·" + s;
        this.preguntas.add(q);
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire_submit that = (Questionnaire_submit) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void orderQuestionList() {
        String[] answerByQuestion = respuestas.substring(1).split(",");
        List<Question> finalOrder = new ArrayList<>();
        for (String answer : answerByQuestion) {
            String[] allAnswerofAnswer = answer.split("@");
            List<String> answersInaQuestion = Arrays.asList(allAnswerofAnswer);
            for (Question q : preguntas) {
                List<Answer> questionAnswers = q.getAnswers();
                for (Answer a : questionAnswers) {
                    if (answersInaQuestion.contains(String.valueOf(a.getId()))) {
                        finalOrder.add(q);
                        break;
                    }
                }
            }
        }
    }

    public HashMap<Integer, String[]> getAnswerGiven() {
        HashMap<Integer, String[]> resultMap = new HashMap<>();
        String[] respuestasDadas = respuestas.substring(1).split(",");
        for (String answerByQuestion : respuestasDadas) {
            String[] answerByQuestionSplit = answerByQuestion.split("·");
            int questionId = Integer.parseInt(answerByQuestionSplit[0]);
            resultMap.put(questionId, answerByQuestionSplit[1].split("@"));
        }
        return resultMap;
    }

    public double correctQuestionnaire() {
        double totalCorrect = 0;
        double totalRestado = 0;
        HashMap<Integer, String[]> answerMap = this.getAnswerGiven();
        for (Question q : this.preguntas) {
            int idQuestion = q.getId();
            List<Answer> answers = new ArrayList<>(q.getAnswers());
            List<Integer> answersCorrect = new ArrayList<>();
            HashMap<Integer, Integer> subtractMap = new HashMap<>();
            for (Answer a : answers) {
                subtractMap.put(a.getId(), a.getValueAnswer());
                if (a.isCorrect()) {
                    answersCorrect.add(a.getId());
                }
            }
            String[] QuestionAnswer = answerMap.get(idQuestion);
            for (String respuestasDada : QuestionAnswer) {
                if (!respuestasDada.equals("") && !respuestasDada.equals("-") && answersCorrect.contains(Integer.parseInt(respuestasDada))) {
                    totalCorrect += ((double) 1 / answersCorrect.size());
                } else if (!respuestasDada.equals("") && !respuestasDada.equals("-")) {
                    //int rest = 30;
                    int rest = subtractMap.get(Integer.parseInt(respuestasDada));
                    totalRestado += (double) rest / 100;
                }
            }

        }
        double finalResult = questionnaire.isSubtraction() ? totalCorrect / this.preguntas.size() - totalRestado / this.preguntas.size() : totalCorrect / this.preguntas.size();
        return finalResult >= 0 ? finalResult : 0;
    }
}
