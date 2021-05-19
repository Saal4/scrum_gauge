function SearchQuestionnaire() {
    var input, filter, i, txtValue, element,card_body,name;
    input = document.getElementById("questionnaire_search_input");
    filter = input.value.toUpperCase();
    element = document.getElementById("questionnaire_list");
    card_body = element.getElementsByClassName("col-sm-4");
    for (i = 0; i < card_body.length; i++) {
        name = card_body[i].getElementsByClassName("card-title")[0];
        if (name) {
            txtValue = name.textContent || name.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                card_body[i].style.display = "";
            } else {
                card_body[i].style.display = "none";
            }
        }
    }
}