var lastValueUSerSelector = 2;
var firstChange = true;
$(document).ready(function () {
    $('#selectorQuestionnaire').change(function () {
        var values = $("#selectorQuestionnaire").val();
        if (values && values[0] === "all" && lastValueUSerSelector === 1) {
            values.shift();
            console.log("Caso 1 " + values);
            $('#selectorQuestionnaire').selectpicker("val", values);
            lastValueUSerSelector = 2;
            firstChange = false;
        } else if (values && values[0] === "all" && lastValueUSerSelector === 2) {
            $('#selectorQuestionnaire').selectpicker("selectAll");
            lastValueUSerSelector = 1;
            firstChange = false;
        } else if (values && values[0] !== "all" && lastValueUSerSelector === 1) {
            $('#selectorQuestionnaire').selectpicker("deselectAll");
            lastValueUSerSelector = 2;
            firstChange = false;
        }else if (values && values[0] !== "all" && firstChange) {//&& lastValueUSerSelector === 1
            $('#selectorQuestionnaire').selectpicker("deselectAll");
            lastValueUSerSelector = 2;
            firstChange = false;
        }
    });
});


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

    var users = $('#selectorQuestionnaire').val();
    if (questionArray.length === 0 && users && users.length > 0) {
        alert("No puede asignar un cuestionario sin preguntas a un usuario");
        return;
    }
    if (!users) {
        users = [];
    } else {
        var usersAux = "all";
        if (users[0] !== "all") {
            usersAux = [];
            for (let i = 0; i < users.length; i++) {
                var str = users[i].split("-")[0];
                str = str.trim();
                usersAux.push(str);
            }
        }
        users = usersAux;
    }
    var name = document.getElementById("name").value;
    var description = document.getElementById("description").value;

    var category =  document.getElementById("category").value;
    var id = document.getElementById("id").value;
    if(id === "-1"){
        id = "new";
    }
    //console.log(id + "    " + category + "   " + description + "    " + name);
    var subtract = $("#checkBoxSwitch").prop("checked");
    var minute = document.getElementById("time").value;
    $.ajax({
        url: "/questionnaire/save",
        data: {
            id: id,
            name: name,
            category: category,
            description:description,
            idQuestion: questionArray.toString(),
            users: users.toString(),
            subtract:subtract.toString(),
            minute:minute,
        }
    }).done(function () {
        window.location.href="/questionnaire";
    });
}

function deleteQuestionaire(id) {
        document.getElementById("deleteButton").setAttribute("action", "/questionnaire/delete/" + id);
}