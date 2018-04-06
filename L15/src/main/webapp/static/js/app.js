var endpointUrl = "ws://" + window.location.host + "/chat/ws";

// dev
var restart = function() {
    ws = new WebSocket(endpointUrl);
    ws.onopen = function() {
        console.log("opened");
    }
    ws.onclose = function() {
        console.log("closed");
    }
}
var send = function(msg) {
    ws.send(JSON.stringify(msg));
}

var generateUuid = function() {
    return Math.round(Math.random() * 1e6);
}

var message = function (action, obj) {
    var newObj = {"uuid": generateUuid(), "action" : action};
    var keys = Object.keys(obj);
    for (var keyId in keys) {
        newObj[keys[keyId]] = obj[keys[keyId]];
    }
    return newObj;
}
var signup = function(login, password) {
    console.log("signup", login, password);
    send(message("signup", {"login": login, "password": password}));
}
var signin = function(login, password) {
    console.log("signin", login, password);
    send(message("signin", {"login": login, "password": password}));
}
var sendTextMessage = function(text) {
    console.log("text message", text);
    send(message("message", {"text": login}));
}

restart();