
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);

var pythonId;
var javaId;

slog("SERVER INITIALIZED", 0);
io.listen(2242,function(){
    console.log("Server is now running...");
});


function askForID(socket) {
    socket.emit('ID', socket.id);
}

// Add a connect listener
io.on('connection', function(socket) {

    askForID(socket);

    socket.on('ID',function (id) {

        if( id === 0 ){
            socket.emit('CONNECTED', socket.id);
            slog("Java Client: " + socket.id, 0);
        }else if( id === 1 ){
            pythonId = socket.id;
            socket.emit("CONNECTED", socket.id);
            slog("Python Client: " + pythonId, 0);
        }else{
            slog("Unknown client", -99);
            socket.emit("CONNECTED", null);
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
        case 100:
            messageType+="SEND:\t\t";
            break;
        case -99:
            messageType+="ERROR:\t\t";
            break;
        default:
            messageType+="UNDEF:\t\t";
    }    console.log(timeStamp + messageType + message);

}
