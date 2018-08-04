//var ws = new WebSocket("ws://192.168.1.13:8080/ws");
//var ws = new WebSocket("ws://85.65.49.90:8080/ws");
var ws = new WebSocket("wss://192.168.1.13:8443/ws");

ws.onopen = function () {
};

ws.onmessage = function (message) {
    window.alert(message.data);
    return false;
};

function closeConnect() {
    ws.close();
}