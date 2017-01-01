"use strict";

var Echo = Echo || {};

Echo.sendmessage = function () {
    var echo = $('#echo');
    var message = echo.val();
    if (message != '') {
        Echo.socket.send(message);
        echo.val('');
    }
};

Echo.sendBinary = function (message) {
    console.log("binary message: " + message)
    if (Echo.socket) {
        Echo.socket.send(message);
    }
};

Echo.connect = function (host) {
    if ("WebSocket" in window) {
        console.log("host: " + host);
        Echo.socket = new WebSocket(host);
    } else if ("MozWebSocket" in window) {
        Echo.socket = new MozWebSocket(host);
    } else {
        console.log("error: WebSocket is not supported in this browser");
        return;
    }

    Echo.socket.onopen = function () {
        console.log("Info: connection opened");
        $("#echo").keydown(function (evt) {
            if (evt.keyCode == 13) {
                console.log('enter key pressed!')
                Echo.sendmessage();
            }
        });
    }

    Echo.socket.onclose = function () {
        console.log("Info: connection Closed");
    }

    Echo.socket.onmessage = function (message) {
        console.log("message: " + message.data);
        $('#echoBack').append("\n" + message.data);
    }
};

Echo.initialize = function () {
    var ep = "/websocket-chatapp-1.0.0/chat";
    console.log("protocol: " + window.location.protocol);
    if (window.location.protocol == "http:") {
        Echo.connect("ws://" + window.location.host + ep);
    } else {
        Echo.connect("wss://" + window.location.host + ep);
    }
};

Echo.initialize();

