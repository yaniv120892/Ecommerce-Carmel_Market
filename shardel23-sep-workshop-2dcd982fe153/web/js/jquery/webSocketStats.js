//var ws = new WebSocket("ws://192.168.1.13:8080/ws");
//var ws = new WebSocket("ws://85.65.49.90:8080/ws");
var ws = new WebSocket("wss://192.168.1.12:8443/wsStats");

ws.onopen = function () {
    function refresh() {
        // make Ajax call here, inside the callback call:
        ws.send("give me more");
        setTimeout(refresh, 5000);
    }
    refresh();
};

ws.onmessage = function (message) {
    //need to get the object and update
    //window.alert(message.data);

};

function closeConnect() {
    ws.close();
}