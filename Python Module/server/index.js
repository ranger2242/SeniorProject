
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);

var pythonId;
var javaId;

const PYTHON_PASSWORD =	"C8K0AS64KL00Z8455";

slog("SERVER INITIALIZED", 35);
io.listen(2242,function(){
    console.log("Server is now running...");
});


function askForID(socket) {
    socket.emit('ID', socket.id);
}

var timer;

function startTimeoutTimerFor(socket) {
	var timeToTimeOut = 10;
	var targetDate = new Date().getTime() + timeToTimeOut;

	timer = setInterval(function(){
		var nextTime = new Date().getTime();
		var distance = nextTime - targetDate;
		
		if (distance > 10000){
			clearInterval(timer);
			slog("Client -> " + socket.id + " did not send ID...", -99);
			disconnect(socket);
		}
	
	}, 500)

}

// Add a connect listener
io.on('connection', function(socket) {

	slog(socket.id + " attempting to connect", 20)
    askForID(socket);
	startTimeoutTimerFor(socket);
	
	
    socket.on('ID',function (message) {
		
		clearInterval(timer);
		
        if( message === 0){
            socket.emit('CONNECTED', socket.id);
            slog("Java Client: " + socket.id, 0);
        }else if( message[0] === 1){			
			if(message[1] == PYTHON_PASSWORD){
				pythonId = socket.id;
				socket.emit("CONNECTED", socket.id);
				slog("Python Client: " + pythonId, 0);
			}else{
				slog("Unknown client -> " + socket.id, -99);
				disconnect(socket);
			}
        }else{
            slog("Unknown client -> " + socket.id, -99);
			disconnect(socket);
        }
    });

    socket.on('msg',function (msg) {
        slog(msg,1);
    });

    socket.on('disconnect', function() {
        slog(socket.id,-1);
    });
	
	socket.on('SEND-PYTHON', function(message){
		slog(message, 100);
		if (pythonId != null){
            var data = {id: socket.id, data: message}
            socket.broadcast.to(pythonId).emit("SEND-PYTHON", data);
        }else{
		    slog("Python Client not connected", -99)
        }
	});

    socket.on('SEND-CLIENT', function(message){

        var id = message[0];
        var data = message[1];
        slog("To: " + id + "\tData: " + data, 100);

        if (id != null){
            socket.broadcast.to(id).emit("SEND-CLIENT", data);
        }else{
            slog("Java Client not connected", -99)
        }
    });

});

function disconnect(socket){
	socket.broadcast.to(socket.id).disconnect();
}

function emitAll(socket,event,arg){
    socket.emit(event,arg);
    socket.broadcast.emit(event,arg);
}

function slog(message, type) {
    var messageType = "";
    var date = new Date();
    var timeStamp = date.toLocaleDateString()+"::"+ date.toLocaleTimeString()+": ";
    switch(type) {
        case -1:
            messageType+="DISCONNECTED:\t";
            break;
        case 0:
            messageType+="CONNECTED:\t\t";
            break;
        case 1:
            messageType+="MESSAGE:\t";
            break;
        case 50:
            messageType+="ID:\t\t";
            break;
		case 35:
            messageType+="SERVER:\t\t";
            break;
        case 100:
            messageType+="SEND:\t\t";
            break;
        case -99:
            messageType+="ERROR:\t\t";
            break;
		case 20:
            messageType+="CONNECTING:\t\t";
            break;
        default:
            messageType+="UNDEF:\t\t";
    }    console.log(timeStamp + messageType + message);

}




