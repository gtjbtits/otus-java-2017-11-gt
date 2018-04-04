(function () {

    var stompClient = null, username, channel;

    function setUsername(s) {
        username = s || "Unnamed";
    }

    function connect(cb) {
        var socket = new SockJS('chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({"name": username}, function (frame) {
            console.log('Connected: ' + frame);
            cb();
            stompClient.subscribe('topic/general', function (greeting) {
//                showMessage(JSON.parse(greeting.body).content);
                showMessage(greeting);
            });
        });
    }

    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    function sendMsg() {
        stompClient.send("app/general", {}, JSON.stringify({'name': username, 'msg': $("#msg").val()}));
        $("#msg").val("");
    }

    function showMessage(message) {
        $("#messages").prepend("<tr><td>" + message + "</td></tr>");
    }

    $(function () {
        setUsername("user1");
        connect(function () {
            console.log("Send handler installed");
            $("#send").click(function () {
                sendMsg();
            });
        });

        $("form#send_msg_form").on('submit', function (e) {
            e.preventDefault();
        });
    });

    // TODO: connect, send message, handle incoming msg

    /* connect
    var socket = new SockJS("/spring-websocket-portfolio/portfolio");
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
    }
    */

    /* STOMP workflow: https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/websocket.html

    stomp spec: https://stomp.github.io/stomp-specification-1.2.html

    SUBSCRIBE
    id:sub-1
    destination:/topic/price.stock.*

    ^@

    SEND
    destination:/queue/trade
    content-type:application/json
    content-length:44

    {"action":"BUY","ticker":"MMM","shares",44}^@

    /topic/... - one to many
    /queue/ - one to one

    broadcast:
    MESSAGE
    message-id:nxahklf6-1
    subscription:sub-1
    destination:/topic/price.stock.MMM

    {"ticker":"MMM","price":129.45}^@

    */

})();