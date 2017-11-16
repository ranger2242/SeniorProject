
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);


io.listen(2242,function(){
    console.log("Server is now running...");
});

// Add a connect listener
io.on('connection', function(socket) {

    console.log('Client connected.');

    socket.on('msg',function (d) {
        console.log(d);
    });

    socket.on('disconnect', function() {
        console.log(socket.id+': Client disconnected.');
    });
});



function emitAll(socket,event,arg){
    socket.emit(event,arg);
    socket.broadcast.emit(event,arg);
}

function slog(msg) {
    console.log("SERVER :"+msg);

}
