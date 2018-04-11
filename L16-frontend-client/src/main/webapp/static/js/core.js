/*global io:false */
(function(){
const WS_ENDPOINT_URL = "ws://" + window.location.host + window.location.pathname + "ws";
const SIGNIN = "signin";
const ERROR_UNKNOWN = "1";
const ERROR_USER_ALREADY_EXISTS = "101";
const ERROR_USER_NOT_FOUND = "102";
const ERROR_USER_PASSWORD_MISMATCH = "103";

var createWebSocket = function(options) {
    const UUID_UNKNOWN = "unknown";
    const UUID_BROADCAST = "broadcast";
    const MESSAGE_HANDLING_TIMEOUT_MS = 5000;
    const ACTION_ERROR = "error";
    const ACTION_SIGNUP = "signup";
    const ACTION_SIGNIN = "signin";
    const ACTION_CLIENT_MESSAGE = "client_message";
    const ACTION_SERVER_MESSAGE = "server_message";

    options = options || {
        url: "",
        onUserMessage: function () {},
        onError: function () {}
    };

    var ws = new WebSocket(options.url);
    var processingUUIDs = {};

    var handleAddressedMessage = function(message, stored) {
        clearTimeout(stored.timeout);
        if (message.action === ACTION_ERROR) {
            stored.error(message);
        } else {
            stored.success(message);
        }
    };

    function handleBroadcastMessage(message) {
        if (message.action === ACTION_SERVER_MESSAGE) {
            options.onUserMessage(message);
        }
    }

    var handleMessage = function(event) {
        var message = JSON.parse(event.data);
        if (processingUUIDs[message.uuid]) {
            handleAddressedMessage(message, processingUUIDs[message.uuid]);
        } else if (message.uuid === UUID_BROADCAST) {
            handleBroadcastMessage(message);
        } else {
            options.onError(message);
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
        onError = onError || options.onError;
        onSuccess = onSuccess || function () {};
        var message = prepareMessage(action, json);
        if (ws.readyState !== ws.OPEN) {
            return options.onError({message: "Network connection error"});
        }
        ws.send(JSON.stringify(message));
        processingUUIDs[message.uuid] = {
            timeout: setTimeout(onError, MESSAGE_HANDLING_TIMEOUT_MS),
            success: onSuccess,
            error: onError
        };
    };

    var signup = function(login, password, onSuccess, onError) {
        sendMessage(ACTION_SIGNUP, {
            login: login,
            password: password
        }, onSuccess, onError);
    };

    var signin = function(login, password, onSuccess, onError) {
        sendMessage(ACTION_SIGNIN, {
            login: login,
            password: password
        }, onSuccess, onError);
    };

    var text = function(text) {
        sendMessage(ACTION_CLIENT_MESSAGE, {
            message: text
        }, options.onUserMessage);
    };

    ws.onmessage = handleMessage;

    return {
        signup: signup,
        signin: signin,
        message: text
    };
}

var Chat = {
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
    
    onMessage: function(message){
          var isMyMessage = message.name === undefined || message.name === $.cookie('login');
          var userName = !isMyMessage ? message.name : "me";
            if($('#output div:last-child').hasClass('user-'+userName)){
                $('#output div:last-child').append('<pre>' + this.escapeHTML(message.message) + '</pre>');
            }else{
                $('#output').append('<div><div class="color-box" style="background:'+this.getUserColor(userName)+'"></div><span class="' + (isMyMessage ? "my-message" : "") + '">' + this.escapeHTML(userName) + '</span>: <pre>' + this.escapeHTML(message.message) + '</pre></div>');
            }
            $('#output').scrollTop($('#output')[0].scrollHeight);
    },
    
    enterChat: function(){
        $('#login').val($.cookie('login') || '');
        var $this = this;
        $('#enter-chat').click(function(){
            var $formError = $(".form-error");
            var login = $('#login').val().trim();
            var password = $('#password').val().trim();
            var authtype = $('input[name=authtype]:checked', '#enter-chat-form').val();
            var onSuccess = function(message) {
                $('#entry').attr('disabled', false);
                $.cookie('login', login);
                $('#name-cont').remove();
                $formError.empty();
            };
            var onError = function (message) {
                switch (message.message) {
                    case ERROR_USER_NOT_FOUND:
                    case ERROR_USER_PASSWORD_MISMATCH:
                        $formError.text("Wrong login/password");
                        break;
                    case ERROR_USER_ALREADY_EXISTS:
                        $formError.text("This login is already used by some other user. Choose another one, please");
                        break;
                    default:
                        $formError.text("Unknown error");
                        break;
                }
            };
            if (authtype === SIGNIN) {
                $this.socket.signin(login, password, onSuccess, onError);
            } else {
                $this.socket.signup(login, password, onSuccess, onError);
            }
        });
    },
    
    escapeHTML: function(html){
        return html.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
    },
    
    setKeyEvent: function(){
        var entry = $('#entry');
        var $this = this;
        entry.keyup(function(e) {
            if (e.keyCode == 13) {
                var message = entry.val();
                message = message.substring(0, message.length - 1); // last "enter" char after send message key
                $this.socket.message(message);
                $this.lastMessage = message;
                entry.val('');
            }
        });
    },
    
    init: function(){
        $this = this;
        this.socket = createWebSocket({
            url: WS_ENDPOINT_URL,
            onUserMessage: function (message) {
                $this.onMessage(message);
            },
            onError: function (message) {
                console.error(message.message, message.trace);
            }
        });
        this.enterChat();
        this.setKeyEvent();
    }
};

$(function() {
    Chat.init();
});
})();