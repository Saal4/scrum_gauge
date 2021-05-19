var change_modal = false;
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
function setDisableInput(thisParameter){
    console.log(thisParameter.value);
    if(thisParameter.value === "true"){
        var parent = thisParameter.parentNode;
        console.log(parent);
        var input = parent.parentNode.getElementsByTagName("input")[1];
        var a = parent.parentNode.getElementsByTagName("a")[0];
        var onClick = a.getAttribute("onclick");
        var i = onClick.toString().split("'")[1];
        console.log(i);
        //input.removeAttribute("value");
        parent.parentNode.removeChild(input);
        parent.parentNode.removeChild(a);
        //parent.parentNode.getElementsByTagName("input")[1].setAttribute("disabled","");
        //parent.parentNode.getElementsByTagName("input")[1].setAttribute("value","0");
        //console.log(parent.parentNode.getElementsByTagName("input")[1].value);

        var aElement = document.createElement("a");
        aElement.setAttribute("href","#");
        var onclick = "deleteAnswerFromModal('"+i+"')";
        aElement.setAttribute("onclick",onclick);
        aElement.setAttribute("class","a-custom");
        var iElement = document.createElement("i");
        iElement.setAttribute("class","material-icons");
        iElement.innerText = "delete";
        aElement.appendChild(iElement);
        var numberValue = document.createElement("input");
        numberValue.setAttribute("type","number");
        numberValue.setAttribute("class", "numberInput form-control");
        numberValue.setAttribute("arial-label","answer");
        numberValue.setAttribute("aria-describedby","basic-addon2");
        numberValue.setAttribute("max","100");
        numberValue.setAttribute("min", "0");
        numberValue.setAttribute("value","0");
        numberValue.setAttribute("onkeypress","return event.charCode >= 48 && event.charCode <= 57");
        numberValue.setAttribute("disabled", "");
        parent.parentNode.appendChild(numberValue);
        parent.parentNode.appendChild(aElement);

    }else{
        var parent  = thisParameter.parentNode;
        parent.parentNode.getElementsByTagName("input")[1].removeAttribute("disabled");
    }
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
    select.setAttribute("onchange","setDisableInput(this);element_change()");
    var option1 = document.createElement("option");
    option1.setAttribute("value","true");
    option1.innerText = "Correcta";
    var option2 = document.createElement("option");
    option2.setAttribute("value","false");
    option2.setAttribute("Selected",true);
    option2.innerText = "Incorrecta";
    var aElement = document.createElement("a");
    aElement.setAttribute("href","#");
    var onclick = "deleteAnswerFromModal('"+i+"')";
    aElement.setAttribute("onclick",onclick);
    aElement.setAttribute("class","a-custom");
    var iElement = document.createElement("i");
    iElement.setAttribute("class","material-icons");
    iElement.innerText = "delete";
    aElement.appendChild(iElement);
    var numberValue = document.createElement("input");
    numberValue.setAttribute("type","number");
    numberValue.setAttribute("onchange","element_change()");
    numberValue.setAttribute("class", "numberInput form-control");
    numberValue.setAttribute("arial-label","answer");
    numberValue.setAttribute("aria-describedby","basic-addon2");
    numberValue.setAttribute("max","100");
    numberValue.setAttribute("min", "0");
    numberValue.setAttribute("value","0");
    numberValue.setAttribute("onkeypress","return event.charCode >= 48 && event.charCode <= 57");
    select.appendChild(option1);
    select.appendChild(option2);
    div1.appendChild(select);
    parentDiv.appendChild(input);
    parentDiv.appendChild(div1);
    parentDiv.appendChild(numberValue);
    parentDiv.appendChild(aElement);
    respuestas.appendChild(parentDiv);
    element_change();
}

function deleteAnswerFromModal(number) {
    document.getElementById("respuesta-" + number).remove();
    element_change();
}
function element_change(){
    console.log("Dentro");
    if(!change_modal){
        change_modal = true;
    }
}
function saveQuestion() {

    //cogemos el id de la pregunta

    var questionId = document.getElementById("questionFormId").getAttribute("value");
    if(questionId !== "new" && change_modal){
        var confirm_change = confirm("Los cambios realizados sobre esta pregunta pueden afectar a cuestionarios resueltos por los miembros del equipo que la contengan. ¿Estás seguro que desea continuar?")
        if(!confirm_change) return;
    }

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
    //alert(respuestasDiv.length)
    for(var i = 0; i<respuestasDiv.length;i++){
        var div = respuestasDiv[i];
        var input = div.getElementsByTagName("input")[0].value;
        var inputnumber = div.getElementsByTagName("input")[1].value;
        var select = div.getElementsByTagName("select")[0].value;
        answers = answers + input + "#" + select + "#" + inputnumber + "·";
        if(select===true||select === "true"){
            correctAnswer++;
        }
    }
    //alert(answers);
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

