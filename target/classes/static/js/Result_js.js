var lastValueUSerSelector = 1;
var lastValueUSerSelectorCategory = 1;
$(document).ready(function () {
    $('#selector').change(function () {
        var values = $("#selector").val();
        if (values && values[0] === "all" && lastValueUSerSelector === 1) {
            values.shift();
            console.log("Caso 1 " + values);
            $('#selector').selectpicker("val", values);
            lastValueUSerSelector = 2;
        } else if (values && values[0] === "all" && lastValueUSerSelector === 2) {
            $('#selector').selectpicker("selectAll");
            lastValueUSerSelector = 1;
        } else if (values && values[0] !== "all" && lastValueUSerSelector === 1) {
            $('#selector').selectpicker("deselectAll");
            lastValueUSerSelector = 2;
        }
    });
    $('#selectorCategory').change(function () {
        var values = $("#selectorCategory").val();
        if (values && values[0] === "all" && lastValueUSerSelectorCategory === 1) {
            values.shift();
            console.log("Caso 1 " + values);
            $('#selectorCategory').selectpicker("val", values);
            lastValueUSerSelectorCategory = 2;
        } else if (values && values[0] === "all" && lastValueUSerSelectorCategory === 2) {
            $('#selectorCategory').selectpicker("selectAll");
            lastValueUSerSelectorCategory = 1;
        } else if (values && values[0] !== "all" && lastValueUSerSelectorCategory === 1) {
            $('#selectorCategory').selectpicker("deselectAll");
            lastValueUSerSelectorCategory = 2;
        }
    });
});

window.onload = function () {
    filter();
};

var colorNames = Object.keys(window.chartColors);

function getConfigRadarCustom(arrayLabel, arrayData, titulo) {
    var color = Chart.helpers.color;
    var config = {
        type: 'radar',
        data: {
            labels: arrayLabel,
            datasets: [{
                label: 'Resultados Por categorías',
                backgroundColor: color(window.chartColors.blue).alpha(0.2).rgbString(),
                borderColor: window.chartColors.blue,
                pointBackgroundColor: window.chartColors.blue,
                data: arrayData
            }]
        },
        options: {
            legend: {
                position: 'bottom',
            },
            title: {
                display: true,
                text: titulo
            },
            scale: {
                ticks: {
                    beginAtZero: true,
                    suggestedMin: 0,
                    suggestedMax: 100
                }
            }
        }
    };
    return config;
}

function getConfigBarVCustom(arrayLabel, arrayData, titulo) {
    var color = Chart.helpers.color;
    var config = {
        type: 'bar',
        data: {
            labels: arrayLabel,
            datasets: [{
                label: 'Resultados Por categorías',
                backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
                borderColor: window.chartColors.blue,
                pointBackgroundColor: window.chartColors.blue,
                data: arrayData
            }]
        },
        options: {
            legend: {
                position: 'bottom',
            },
            title: {
                display: true,
                text: titulo,
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                    }
                }],
                yAxes: [{
                    display: true,
                    ticks: {
                        beginAtZero: true,
                        steps: 10,
                        stepValue: 5,
                        max: 100,
                        min: 0
                    }
                }]
            },
        }
    };
    return config;
}

function getConfigBarHCustom(arrayLabel, arrayData, arrayDateLabel, titulo) {
    var color = Chart.helpers.color;
    var dataSet = [];
    for (let i = 0; i < arrayDateLabel.length; i++) {
        console.log(arrayData[i]);
        arrayDateLabel[i] = arrayDateLabel[i].replace("[","");
        arrayDateLabel[i] = arrayDateLabel[i].replace("]","");
        arrayDateLabel[i] = arrayDateLabel[i].replace(" ","");
        var dataSetConfig = {
            label: arrayDateLabel[i],
            backgroundColor: getRandomColor(),
            borderColor: getRandomColor(),
            borderWidth: 1,
            data: arrayData[i].substring(1,arrayData[i].toString().length-1).split(",")
        };
        dataSet.push(dataSetConfig);
    }
    var config = {
        type: 'horizontalBar',
        data: {
            labels: arrayLabel,
            datasets: dataSet,
        },
        options: {
            // Elements options apply to all of the options unless overridden in a dataset
            // In this case, we are setting the border of each horizontal bar to be 2px wide
            elements: {
                rectangle: {
                    borderWidth: 2,
                }
            },
            responsive: true,
            legend: {
                position: 'right',
            },
            title: {
                display: true,
                text: titulo,
            },
            scales: {
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                    }
                }],
                xAxes: [{
                    display: true,
                    ticks: {
                        beginAtZero: true,
                        steps: 10,
                        stepValue: 5,
                        max: 100,
                        min: 0
                    }
                }]
            },
        }
    };
    return config;
}

function filter() {
var pickerValue = $("#checkBoxSwitch").prop("checked");
if(pickerValue==="true" || pickerValue === true){
    getNewChart("Category");
}else{
    getNewChart("User");
}
}

var arrayLabels = [];
var arrayPoints = [];
var arrayDataLabels = [];
/*
 * SECCIÓN getNewChart
 */
function getNewChart(typeStr){
    var users = $("#selector").val();
    var categories = $("#selectorCategory").val();
    var type = typeStr;
    var usersAux = "all";
    if(users[0] !== "all"){
        usersAux=[];
        for (let i = 0; i < users.length; i++) {
            usersAux.push(users[i].split("-")[0]);
        }
    }
    if(categories[0] === "all"){
        categories.shift();
    }
    $.ajax({
        url: "/Result/allusersSomeCategories",
        data: {
            categories: categories.toString(),
            users: usersAux.toString(),
            type: type
        }
    }).done(function (data) {
        var dataPart= data.split("-");
        var firstPartData = dataPart[0];
        var dataLabels = dataPart[1];
        console.log(data);
        arrayDataLabels = dataLabels.split(",");
        var labels = [];
        var result = [];
        var dataStr = firstPartData.split("|");
        for (let i = 0; i < dataStr.length; i++) {
            var arrayUserData = dataStr[i].split(":");
            var name = arrayUserData[0];
            var arrayPuntuaciones = arrayUserData[1];
            labels.push(name);
            result.push(arrayPuntuaciones);
        }
        arrayLabels = labels;
        arrayPoints = result;
        console.log(arrayLabels);
        console.log(arrayPoints);
        $("#araña").attr("onclick", "getRadar()");
        $("#barrasVerticales").attr("onclick", "getBarV()");
        $("#barrasHorizontales").attr("onclick", "getBarH()");
        getRadar()
    });
}

/*
 * SECCIÓN CAMBIAR DE CHART
 */
async function getRadar() {
    var pickerValue = $("#checkBoxSwitch").prop("checked");
    var titulo = "Grafico de araña (Media de todas las categorias seleccionadas)";
    if(pickerValue==="true" || pickerValue === true){
       titulo = "Grafico de araña (Media de todos los usuarios seleccionados)";
    }
    var finalArray = getAverage(arrayPoints);
    var canvas = await replaceCavas("radar");
    window.myRadar = new Chart(canvas, getConfigRadarCustom(arrayLabels, finalArray, titulo))
}

async function getBarV() {
    var pickerValue = $("#checkBoxSwitch").prop("checked");
    var titulo = "Grafico de araña (Media de todas las categorias seleccionadas)";
    if(pickerValue==="true" || pickerValue === true){
        titulo = "Grafico de araña (Media de todos los usuarios seleccionados)";
    }
    var finalArray = getAverage(arrayPoints);
    var canvas = await replaceCavas("bar");
    window.myRadar = new Chart(canvas, getConfigBarVCustom(arrayLabels, finalArray, titulo));
}

async function getBarH() {
    var finalArray = createArrayBarH(arrayPoints);
    var canvas = await replaceCavas("bar");
    window.myRadar = new Chart(canvas, getConfigBarHCustom(arrayLabels, finalArray, arrayDataLabels, 'Grafico de barras horizontales'));
}

function createArrayBarH(array) {
    var finalArray = [];
    for (let i = 0; i < arrayDataLabels.length; i++) {
        var arrayAux = [];
        for (let j = 0; j < array.length; j++) {
            //console.log("array j -- " + array[j] );
            var positionj = array[j].toString();
            var arrayJstr = positionj.substring(1,positionj.length-1);
            var arrayJ= arrayJstr.split(",");
            //console.log("arrayJ - " + arrayJ);
            arrayAux.push(parseFloat(arrayJ[i]));
            //console.log(arrayAux)
        }
        var auxStr = "[" + arrayAux.toString() + "]";
        finalArray.push(auxStr);
    }
    console.log("array final - " + finalArray);
    return finalArray;
}

function getAverage(array) {
    var finalArray = [];
    for (let i = 0; i < array.length; i++) {
        var points = array[i];
        points = points.substring(1, points.length);
        //console.log(points);
        var pointsArray = points.split(",");
        var totalNumber = pointsArray.length;
        console.log(totalNumber);
        var total = 0;
        for (let j = 0; j < pointsArray.length; j++) {
            total += parseFloat(pointsArray[j]);
        }
        total = (total) / pointsArray.length;
        finalArray.push(total);
    }
    return finalArray;
}

function replaceCavas(classElement) {
    var elementCanvas = document.getElementById("canvas");
    var parent = elementCanvas.parentNode;
    parent.removeChild(elementCanvas);
    var divCanvas = document.createElement("canvas");
    divCanvas.setAttribute("id","canvas");
    divCanvas.setAttribute("class",classElement);
    parent.appendChild(divCanvas);
    return divCanvas;
}

function getRandomColor(){
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}