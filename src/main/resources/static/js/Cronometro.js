var tiempo = {
    hora: 0,
    minuto: 0,
    segundo: 0
};

var running_time = null;

function startCrono(time) {
    console.log(time);
    var timeH = time.split(":");
    tiempo.hora = timeH[0].trim() * 1;
    tiempo.minuto = timeH[1].trim() * 1;
    tiempo.segundo = timeH[2].trim() * 1;
    console.log(tiempo);
    running_time = setInterval(function () {
        tiempo.segundo--;
        if (tiempo.segundo < 0) {
            tiempo.segundo = 59;
            tiempo.minuto--;
        }
        if (tiempo.minuto < 0) {
            tiempo.minuto = 59;
            tiempo.hora--;
            if (tiempo.hora <= 0) {
                submitQuestionnaire(true);
                return;
            }
        }
        $("#hour").text(tiempo.hora < 10 ? '0' + tiempo.hora : tiempo.hora);
        $("#minute").text(tiempo.minuto < 10 ? '0' + tiempo.minuto : tiempo.minuto);
        $("#second").text(tiempo.segundo < 10 ? '0' + tiempo.segundo : tiempo.segundo);
    }, 1000);
}

function stopCrono() {
    clearInterval(running_time);
}