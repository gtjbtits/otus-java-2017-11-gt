/*global io:false */
(function(){
const WS_ENDPOINT_URL = "ws://" + window.location.host + window.location.pathname + "ws";
const SIGNIN = "signin";

var createWebSocket = function(options) {
    const UNKNOWN_UUID = "unknown";
    const BROADCAST_UUID = "broadcast";
    const MESSAGE_HANDLING_TIMEOUT_MS = 5000;
    const MESSAGE_ERROR_ACTION = "error";
    const MESSAGE_SIGNUP_ACTION = "signup";
    const MESSAGE_SIGNIN_ACTION = "signin";

    options = options || {
        url: ""
    };

    var ws = new WebSocket(options.url);
    var processingUUIDs = {};

    var handleAddressedMessage = function(message, stored) {
        clearTimeout(stored.timeout);
        if (message.action === MESSAGE_ERROR_ACTION) {
            stored.error(message);
        } else {
            stored.success(message);
        }
    };

    var handleMessage = function(event) {
        var message = JSON.parse(event.data);
        // TODO: unknown, broadcast
        if (processingUUIDs[message.uuid]) {
            handleAddressedMessage(message, processingUUIDs[message.uuid]);
        } else {
            console.log("TODO: unknown, broadcast");
        }
    };

    var prepareMessage = function(action, json) {
        var generateUuid = function() {
            return Math.round(Math.random() * 1e6);
        };
        var keys = Object.keys(json);
        var message = {"uuid": generateUuid(), "action" : action, "ts": Date.now()};
        for (var keyId in keys) {
            message[keys[keyId]] = json[keys[keyId]];
        }
        return message;
    };

    var sendMessage = function(action, json, onSuccess, onError) {
        var message = prepareMessage(action, json);
        ws.send(JSON.stringify(message));
        processingUUIDs[message.uuid] = {
            timeout: setTimeout(onError, MESSAGE_HANDLING_TIMEOUT_MS),
            success: onSuccess,
            error: onError
        };
    };

    var signup = function(login, password, onSuccess, onError) {
        sendMessage(MESSAGE_SIGNUP_ACTION, {
            login: login,
            password: password
        }, onSuccess, onError);
    };

    var signin = function(login, password, onSuccess, onError) {
        sendMessage(MESSAGE_SIGNIN_ACTION, {
            login: login,
            password: password
        }, onSuccess, onError);
    };

    ws.onmessage = handleMessage;

    return {
        signup: signup,
        signin: signin
    };
}

var Chat = {
    name: '',
    colors: {},
    socket: undefined,
    lastMessage: '',
    randomColor: function (format) {
        var rint = Math.round(0xffffff * Math.random());
        switch (format) {
            case 'hex':
                return ('#0' + rint.toString(16)).replace(/^#0([0-9a-f]{6})$/i, '#$1');
            case 'rgb':
                return 'rgb(' + (rint >> 16) + ',' + (rint >> 8 & 255) + ',' + (rint & 255) + ')';
            default:
                return rint;
        }
    },
    
    fixUserName: function(username){
        return username.replace(/\W/gim, '_');
    },
    
    getUserColor: function(user){
        if (!(user in this.colors)) {
            this.colors[user] = this.randomColor('hex');
        }
        return this.colors[user];
    },
    
    onMessage: function(mes){
        var e = JSON.parse(mes);
        
        if (e.writing === undefined) {
            if($('#output div:last-child').hasClass('user-'+e.user)){
                $('#output div:last-child').append('<pre>' + this.escapeHTML(e.message) + '</pre>');
            }else{
                $('#output').append('<div><div class="color-box" style="background:'+this.getUserColor(e.user)+'"></div><b>' + this.escapeHTML(e.user) + '</b>: <pre>' + this.escapeHTML(e.message) + '</pre></div>');
            }
            $('#output').scrollTop($('#output')[0].scrollHeight);
        } else if (e.writing === true) {
            if(e.user === this.name){ return; /* don't print my status */ }
            $('#ww-' + this.fixUserName(e.user)).remove();
            $('#writing').append('<div id="ww-' + this.fixUserName(e.user) + '">' + this.escapeHTML(e.user) + ' is writing...</div>');
        } else if (e.writing === false) {
            $('#ww-' + this.fixUserName(e.user)).remove();
        } else {
            $('#output').append(mes);
            $('#output').scrollTop($('#output')[0].scrollHeight);
        }
    },
    
    setSocketEvents: function(){
        this.socket.on('message', $.proxy(this.onMessage, this));
    },
    
    enterChat: function(){
        $('#login').val($.cookie('login') || '');
        var $this = this;
        $('#enter-chat').click(function(){
            var login = $('#login').val().trim();
            var password = $('#password').val().trim();
            var authtype = $('input[name=authtype]:checked', '#enter-chat-form').val();
            var onSuccess = function(message) {
                $('#entry').attr('disabled', false);
                $.cookie('login', $this.name);
                $('#name-cont').remove();
            };
            var onError = function (message) {
                alert(message);
            };
//            if(name.length < 2){ return alert('Please enter a valid name'); }
//            if(name.length > 20){ return alert('Name is too long'); }
//            var p = $('#participants li');
//
//            for(var i=0; i < p.length; i++){
//                if(p[i].innerHTML == name){
//                    return alert('This name is already used. Please choose another one.');
//                }
//            }
            
//            $this.name = $this.escapeHTML(name);
            if (authtype === SIGNIN) {
                $this.socket.signin(login, password, onSuccess, onError);
            } else {
                $this.socket.signup(login, password, onSuccess, onError);
            }
        });
    },
    
    send: function(object, str){
        this.socket.send(JSON.stringify(object));
        if(str !== ''){
            $('#output').append(str || object.message);
            $('#output').scrollTop($('#output')[0].scrollHeight);
        } 
    },
    
    escapeHTML: function(html){
        return html.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
    },
    
    joined: function(){
        this.socket.emit('join',{
            name: this.name
        });
    },
    
    setKeyEvent: function(){
        var entry = $('#entry');
        var $this = this;
        var time = 0;
        entry.keyup(function(e) {
            if (e.keyCode == 13) {
                $this.send({
                    user: $this.name,
                    message: entry.val()
                }, '');
                $this.lastMessage = entry.val();
                entry.val('');
            }
            
            clearTimeout(time);
            time = setTimeout(function(){
                $this.send({
                    user: $this.name,
                    writing: (entry.val().trim() !== '')
                }, '');    
            }, 200);
        });
    },
    
    onServerMessage: function(){
        var $this = this;
        this.socket.on('serverMessage', function(data){
            $('#output').append('<div class="info-text" style="color:gold">' + $this.escapeHTML(data.message) + '</div>');
            $('#entry').val($this.lastMessage);
        });
    },
    
    participants: function(){
        var $this = this;
        this.socket.on('join', function(data){
            if(data.name){
                $('#output').append('<div class="info-text">' + $this.escapeHTML(data.name) + ' has joined the chat</div>');
            }
        });
        
        this.socket.on('leave', function(data){
            if(data.name){
                $('#output').append('<div class="info-text">' + $this.escapeHTML(data.name) + ' has left the chat</div>');
            }
        });
        
        this.socket.on('updateParticipantList', function(data){
            $('#participants').html('');
            for(var i in data.list){
                if(data.list[i]){
                    $('#participants').append('<li style="color:'+$this.getUserColor(data.list[i])+'" >'+data.list[i]+'</li>');
                }
            }
        });
        
    },

    setOutputHeight: function(){
        var o = $('#output'), c = $('#o-cont');
        var calc = function(){
            o.css('display', 'none');
            var h = c.height()+parseFloat(c.css('padding-top'))+parseFloat(c.css('padding-bottom'));
            o.css('display', '');
            o.css('height', h);
        };
        
        calc();
        var t;
        $(window).resize(function(){
            calc();
            clearTimeout(t);
            t = setTimeout(function(){ calc(); }, 100); // Resize may happen really fast
        });
    },
    
    init: function(){
//        this.socket = io.connect(location.hostname + ":1337");
        this.socket = createWebSocket({
            url: WS_ENDPOINT_URL
        });
//        this.setSocketEvents();
        this.enterChat();
//        this.participants();
//        this.setKeyEvent();
//        this.setOutputHeight();
//        this.onServerMessage();
    }
};

$(function() {
    Chat.init();
});
})();