
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);


slog("SERVER INITIALIZED", 0);
io.listen(2242,function(){
    console.log("Server is now running...");
});

// Add a connect listener
io.on('connection', function(socket) {

    slog(socket.id,0);

    socket.on('msg',function (msg) {
        slog(msg,1);
    });

    socket.on('disconnect', function() {
        slog(socket.id,-1);
    });
});



function emitAll(socket,event,arg){
    socket.emit(event,arg);
    socket.broadcast.emit(event,arg);
}

function slog(msg, type) {
    var s ="";
    var d = new Date();
    var a=d.toLocaleDateString()+"::"+ d.toLocaleTimeString()+": ";
    var r= d.getDate()+" "+d.getTime();
    var datetime =  d.getDate() + "/"
        + (d.getMonth()+1)  + "/"
        + d.getFullYear() + "::"
        + d.getHours() + ":"
        + d.getMinutes() + ":"
        + d.getSeconds()+" ";
    switch(type) {
        case -1:
            s+="DISCONNECTED:\t";

            break;
        case 0:
            s+="CONNECTED:\t";
            break;
        case 1:
            s+="MESSAGE:\t";
            break;
        default:
            s+="UNDEF:\t";
    }    console.log(a+s+msg);

}
