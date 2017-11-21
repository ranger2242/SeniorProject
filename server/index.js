
var app = require('express')();
var server = require('http').Server();
var io = require('socket.io')(server);
var players = [];
var playerCount=0;
var bets=null;
var deck=null;
var currentTurn=1;
var finished = null;

io.listen(2242,function(){
    console.log("Server is now running...");
});

// Add a connect listener
io.on('connection', function(socket) {

    console.log('Client connected.');

    socket.on('pushDeck',function (d) {
		deck=d;
		socket.broadcast.emit('syncDeck',deck);
    });
    socket.on('getSocketID', function (msg) {
		console.log(msg+" :"+socket.id);
        socket.emit('setOrder',players.length+1);
        socket.emit('socketID',socket.id);
    });
	socket.on('queuePlayer', function (player ,order) {
		    //console.log(player);
            players[order] = player;
           // console.log(players)
            //socket.emit('addPlayerToList',players);
            //socket.broadcast.emit('addPlayerToList',players);
    });
    socket.on('requestTurn',function () {
        socket.emit('recieveCurrTurn',currentTurn);
    })
	socket.on('uploadBet', function (bet,order) {
		bets[order]=true;
		socket.broadcast.emit('syncBet', bet,socket.id);
		if(allBetsIn()){
		    emitAll(socket,'out','all bets are in')
            queueFirstRound(socket);
		    emitAll(socket,'recieveCurrTurn',currentTurn);
        }

    })
    socket.on('sendHit',function (index) {
        socket.broadcast.emit('addToDrawQueue', index);
    })
    socket.on('requestPlayerList', function () {
        slog('Attempting to send to '+socket.id);
        for(var i=0;i<players.length;i++) {
            var p= JSON.parse(players[i]);
            console.log(socket.id+": requests----\n"+ p.name);
            socket.emit('recievePlayer', players[i]);
        }
    })
	socket.on('startAll', function () {
		bets=new Array(players.length);
		finished=new Array(players.length);
        for (var i = 0; i < bets.length; ++i) {
        	bets[i] = false;
        	finished[i]=false;
        }

        socket.emit('start');
        socket.emit('makeDeck');
        socket.broadcast.emit('start');
        emitAll(socket,'recieveCurrTurn',currentTurn);
        slog(currentTurn);
    })
    socket.on('endTurn',function (type) {
        if(currentTurn<players.length){
            currentTurn++;
            slog("Changed turn :"+currentTurn);
            emitAll(socket, 'recieveCurrTurn',currentTurn);
        }else{
            slog("End of round");
            emitAll(socket, 'roundOver',"");
        }

    })
    socket.on('removePlayer', function (pos) {
        players.splice(pos,1);
        playerCount=players.length;
        console.log("Removed "+pos);
    })
	socket.on('log', function (msg) {
        console.log(socket.id+" :"+msg);

    });
    socket.on('disconnect', function() {
        console.log(socket.id+': Client disconnected.');
    });
});

function queueFirstRound(socket) {
	var arr=[];
	for(var i=1;i<((players.length+1)*2)+1;i++){
        arr.push(i%(players.length+1));
    }
    console.log(arr);
    emitAll(socket,"syncFirstRoundDrawQueue",arr)
}
function queueNextRound(socket){
    var arr=null;
    for(var i=0;i<finished.length;i++){
        if(!finished[i])
            arr[i]=i+1;

    }
        if(arr.length!==0){
            emitAll(socket,'recieveRoundQueue',arr);
        }
    }

function emitAll(socket,event,arg){
    socket.emit(event,arg);
    socket.broadcast.emit(event,arg);
}
function allBetsIn() {
	for(var i=0;i<bets.length;i++){
		if(!bets[i])
			return false;
	}
	return true;
}
function slog(msg) {
    console.log("SERVER :"+msg);

}
