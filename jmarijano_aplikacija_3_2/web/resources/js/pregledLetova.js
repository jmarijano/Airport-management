/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ws;
connect();
function connect() {
    var aplikacija = "/" + document.location.pathname.split("/")[1];
    var wsUri = "ws://" + document.location.host + aplikacija +
            "/infoPoruka";
    console.log(wsUri);
    ws = new WebSocket(wsUri);
    ws.onmessage = function (event) {
        var poruka = event.data;
        console.log(poruka);
        var tablica = document.getElementById("form:osvjezi");
        tablica.click();
    };
}

