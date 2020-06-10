$( function() {
    $( "#sortable1, #sortable2" ).sortable({
        connectWith: ".connectedSortable"
    }).disableSelection();
} );


$(document).ready(function(){
    $("#list1search").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#sortable1 li").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});
$(document).ready(function(){
    $("#list2search").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#sortable2 li").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});
$(document).ready(function() {
    $(".connectedSortable li").dblclick(function(){
        var parentID = $(this).parent().attr('id'); //sortable1 or sortable2
        if(parentID.match(/^(sortable1)$/g))
            $("#sortable2").append(this);
        else if(parentID.match(/^(sortable2)$/g))
            $("#sortable1").append(this);
    });
});

function saveQuestionnaire(){
    var questionArray = [];
    var ulArray = $("#sortable2 li");
    for(var i = 0; i<ulArray.length;i++){
        questionArray.push(ulArray[i].id.split("-")[1]);
    }

    var values = $('#selector').val();
    if(questionArray.length === 0 && values && values.length>0){
        alert("No puede asignar un cuestionario sin preguntas a un usuario");
        return;
    }
    if(!values) {
        values = [];
    }else{
        var aux = [];
        for(var valu= 0; valu<values.length;valu++){
            var str = values[valu].split("-")[0];
            str = str.trim();
            aux.push(str);
        }
        values = aux;
    }
    var name = document.getElementById("name").value;
    var description = document.getElementById("description").value;

    var category =  document.getElementById("category").value;
    var id = document.getElementById("id").value;
    if(id === "-1"){
        id = "new";
    }
    console.log(id + "    " + category + "   " + description + "    " + name);
    $.ajax({
        url: "/questionnaire/save",
        data: {
            id: id,
            name: name,
            category: category,
            description:description,
            idQuestion: questionArray.toString(),
            users: values.toString(),
        }
    }).done(function () {
        window.location.href="/questionnaire";
    });
}