package com.ANZR.Ergo.io;

import com.ANZR.Ergo.Ergo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import static com.ANZR.Ergo.io.Logger.slog;
import java.net.*;
import java.util.*;


public class Pipeline {

    private static Socket socket;
    private static String ip= "localhost";
    private static String port = "2242";
    private static String dir = "http://" + ip + ":" + port;
    private static String clientID;
    private static boolean serverIsReady = false;
    private Ergo ergo;


    enum Codes{
        CONNECTED("CONNECTED"),
        SENDCLIENT("SEND-CLIENT"),
        ID("ID"),
        SENDPYTHON("SEND-PYTHON"),
        MSG("msg");
        private final String text;

        Codes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum RecievedCodes{
        RESULTS("RESULTS"),
        RECEIVED("RECEIVED");
        private final String text;

        RecievedCodes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    public Pipeline(Ergo ergo){
        this.ergo = ergo;

        try {
            slog("Connecting to " + dir);
            connectSocket();
        } catch (URISyntaxException e) {
            slog("Failed to connect");
            e.printStackTrace();
        }
    }

    public static void setShutdownOperations(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                slog("Disconnected from server");
                socket.disconnect();
            }
        });
    }

    public static void sendToServer(String data){
        //Attempt to send message to server, will timeout after 15 seconds
        for(int i = 0; i < 15; i++){
            if(serverIsReady){
                socket.emit("SEND-PYTHON", data);
                slog("Data Sent To Server");
                return;
            } else {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    slog("Error happened while waiting for server to connect...");
                }
            }
        }
        slog("Message to server timed out...");
    }

    public void connectSocket() throws URISyntaxException {
        socket = IO.socket(dir);

        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {//on connect
            @Override
            public void call(Object... args) {
            }

        });

        socket.on("CONNECTED", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                slog("Connected to server");
                clientID = (String) args[0];
                serverIsReady = true;
            }
        });

        socket.on("ID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("ID", 0);
            }
        });



        socket.on(Codes.SENDCLIENT.toString(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {



                slog("Data Recieved: ");

                Gson gson = new Gson();
                JsonElement element = gson.fromJson ((String) args[0], JsonElement.class);
                JsonObject jsonObj = element.getAsJsonObject();


                String type = jsonObj.keySet().iterator().next();
                JsonElement data = jsonObj.get(type);

                if( RecievedCodes.RECEIVED.toString().equals(type)){
                    if(data.getAsString().toLowerCase().equals("true")){
                        slog("Server Received data...");
                    }else{
                        slog("Error: Server failed to received data...");
                    }
                }else if( RecievedCodes.RESULTS.toString().equals(type)){
                    ergo.interpretData(data);
                }else{
                    slog("UNKNOWN RECEIVE CODE FOUND...");
                }
            }
        });

        socket.on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                slog("Disconnected from server");
                serverIsReady = false;
            }
        });

        socket.connect();
    }

    public static void viewIP() {
        try {
            Logger.out("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        for (; n.hasMoreElements(); ) {
            NetworkInterface e = n.nextElement();

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements(); ) {
                InetAddress addr = a.nextElement();
                Logger.out("  " + addr.getHostAddress());
            }
        }
    }

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }







}
