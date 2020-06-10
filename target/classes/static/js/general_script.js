function deleteQuestion(id) {
    document.getElementById("deleteButton").setAttribute("action", "/questions/delete/" + id);
}
function editQuestion(id) {
    $.ajax({
        url: "/question/edit/" + id
    }).done(function (data) {
        $("#modal_body_id").replaceWith(data);
    })
}
function addAnswerToModal() {
    var i = 0;
    while(true){
        var strRes = "respuesta-"+i;
        if(document.getElementById(strRes)){
            i++;
            continue;
        }
        break;
    }
    var respuestas = document.getElementById("respuestas");
    var parentDiv = document.createElement("div");
    var res = "respuesta-"+i;
    /*var iddiv = document.createAttribute("id");
    iddiv.value=res;*/
    parentDiv.setAttribute("id",res);
    parentDiv.setAttribute("class","input-custom-group input-group");
    var input = document.createElement("input");
    input.setAttribute("type","text");
    input.setAttribute("class", "form-control");
    input.setAttribute("arial-label","answer");
    input.setAttribute("aria-describedby","basic-addon2");
    var div1 = document.createElement("div");
    var select = document.createElement("select");
    select.setAttribute("class","form-control-selector form-control");
    select.setAttribute("id","correct-respuesta-selected");
    var option1 = document.createElement("option");
    option1.setAttribute("value","true");
    option1.innerText = "Correcta";
    var option2 = document.createElement("option");
    option2.setAttribute("value","false");
    option2.setAttribute("Selected",true);
    option2.innerText = "Incorrecta";
    var button = document.createElement("button");
    button.setAttribute("class","btn btn-outline-secondary");
    var onclick = "deleteAnswerFromModal('"+i+"')";
    button.setAttribute("onclick",onclick);
    button.setAttribute("type","button");
    button.innerText="Eliminar";
    select.appendChild(option1);
    select.appendChild(option2);
    div1.appendChild(select);
    parentDiv.appendChild(input);
    parentDiv.appendChild(div1);
    parentDiv.appendChild(button);
    respuestas.appendChild(parentDiv);
}

function deleteAnswerFromModal(number) {
    document.getElementById("respuesta-" + number).remove();
}

function saveQuestion() {

    //cogemos el id de la pregunta
    var questionId = document.getElementById("questionFormId").getAttribute("value");

    //cogemos el valor de la pregunta
    var question = document.getElementById("Pregunta").value;
    console.log(question);
    if(!question || question === "Pregunta"){
        var errorQuestion = document.getElementById("errorQuestion");
        errorQuestion.removeAttribute("hidden");
        return;
    }
    var errquestion = document.getElementById("errorQuestion");
    var attrerrquestion = document.createAttribute("hidden");
    errquestion.setAttributeNode(attrerrquestion);

    //cogemos el tipo de pregunta
    var questionType = document.getElementById("type").value;

    //cogemos las respuestas
    var respuestas = document.getElementById("respuestas");
    var respuestasDiv = respuestas.getElementsByClassName("input-custom-group");

    if(respuestasDiv.length < 2){
        var errorNumberAnswer = document.getElementById("errorNumberAnswer");
        errorNumberAnswer.removeAttribute("hidden");
        return;
    }
    var errNumberAnswer = document.getElementById("errorNumberAnswer");
    var errNumberAnswerAtrr = document.createAttribute("hidden");
    errNumberAnswer.setAttributeNode(errNumberAnswerAtrr);

    var correctAnswer = 0;
    var answers = "";

    for(var i = 0; i<respuestasDiv.length;i++){
        var div = respuestasDiv[i];
        var input = div.getElementsByTagName("input")[0].value;
        var select = div.getElementsByTagName("select")[0].value;
        answers = answers + input + "#" + select + ",";
        if(select===true||select === "true"){
            correctAnswer++;
        }
    }
    console.log(answers);
    if(correctAnswer === 0){
        var errorNumberCorrectAnswer = document.getElementById("errorNumberCorrectAnswer");
        errorNumberCorrectAnswer.removeAttribute("hidden");
        return;
    }else if((correctAnswer===1 && questionType==="MR")||(correctAnswer>1 && questionType==="RU")){
        var errorTypeQuestion = document.getElementById("errorTypeQuestion");
        errorTypeQuestion.removeAttribute("hidden");
        return;
    }

    var errNumberCorrectAnswer = document.getElementById("errorNumberCorrectAnswer");
    var attrerrNumberCorrectAnswer = document.createAttribute("hidden");
    errNumberCorrectAnswer.setAttributeNode(attrerrNumberCorrectAnswer);

    var errTypeQuestion = document.getElementById("errorTypeQuestion");
    var attrerrTypeQuestion = document.createAttribute("hidden");
    errTypeQuestion.setAttributeNode(attrerrTypeQuestion);


    $.ajax({
        url: "/question/save",
        data: {
            questionId: questionId,
            question: question,
            question_type: questionType,
            answers: answers,
        }
    }).done(function () {
        console.log("ajax finalizado");
        location.reload();
        return false;
    });
}

