
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);

var pythonId;
var javaId;

slog("SERVER INITIALIZED", 0);
io.listen(2242,function(){
    console.log("Server is now running...");
});


// Add a connect listener
io.on('connection', function(socket) {

    slog(socket.id,0);

    socket.on('id',function (id) {
        if( id == 0 ){
            javaId = socket.id;
            socket.broadcast.to(javaId).emit("CONNECTED", true);
            slog("Java Client: " + javaId, 50);
        }else if( id == 1 ){
            pythonId = socket.id;
            socket.broadcast.to(pythonId).emit("CONNECTED", true);
            slog("Python Client: " + pythonId, 50);
        }else{
            slog("Unknown client", -99);
            socket.broadcast.to(pythonId).emit("CONNECTED", false);
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
            socket.broadcast.to(pythonId).emit("SEND-PYTHON", message);
        }else{
		    slog("Python Client not connected", -99)
        }
	});

    socket.on('SEND-CLIENT', function(message){
        slog(message, 100);
        if (javaId != null){
            socket.broadcast.to(javaId).emit("SEND-CLIENT", message);
        }else{
            slog("Java Client not connected", -99)
        }
    });
});

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
            messageType+="CONNECTED:\t";
            break;
        case 1:
            messageType+="MESSAGE:\t";
            break;
        case 50:
            messageType+="ID:\t\t";
            break;
        case 100:
            messageType+="SEND:\t\t";
            break;
        case -99:
            messageType+="ERROR:\t";
            break;
        default:
            messageType+="UNDEF:\t";
    }    console.log(timeStamp + messageType + message);

}
