// Enable navigation prompt
window.onbeforeunload = function() {
    return true;
};

function submitQuestionnaire(){
    window.onbeforeunload = false;
    if (!confirm('¿Estas seguro que desea enviar?')) {
        window.onbeforeunload = true;
        return;
    }
    var id = document.getElementsByClassName("card-body-custom")[0].getAttribute("id").split("-")[1];
    var container = document.getElementsByClassName("card-body-custom")[0];
    var questionnaireId = container.getAttribute("id");
    var questions = container.getElementsByClassName("container");
    var finalTest = "";
    var answersQuestion="";
    for(var i=0;i<questions.length;i++){
        var questionId = questions[i].getAttribute("id");
        var formquestionId = "form-"+questionId;
        var idQuestion = questionId.split("-")[1];
        var nameQuestion = "input[name=q-"+idQuestion+"]:checked";
        var response = $(nameQuestion);
        finalTest+=idQuestion +  "-" ;
        answersQuestion = "";
        for(var j=0;j<response.length;j++){
            answersQuestion += response[j].getAttribute("value") + "@";
        }
        finalTest +=answersQuestion + "/";
    }

    $.ajax({
        url: "/questionnaire/submit",
        data: {
            id: id,
            answersQuestion:finalTest,
        }
    }).done(function (data) {
        console.log(data);
        window.location.href="/questionnaire/pending";
    });

}