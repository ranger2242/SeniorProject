import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
//import slog;






public class Pipeline {


    private static Socket socket;
    private static String ip= "localhost";
    private static String port = "2242";
    private static String dir = "http://" + ip + ":" + port;
    private static String clientID;
    private static boolean serverIsReady = false;


    public Pipeline(){

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

    public static void slog(String msg) {
        Main.slog(msg);
    }

    public static void sendToServer(String data){
        //Attempt to send message to server, will timeout after 15 seconds
        for(int i = 0; i < 15; i++){
            if(serverIsReady){
                socket.emit("SEND-PYTHON", data);
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

    public static void connectSocket() throws URISyntaxException {
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

        socket.on("SEND-CLIENT", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                System.out.println("Data Recieved: ");
                for( Object object : args ) {
                    String data = (String) object;
                    System.out.println(data);
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
            Main.out("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
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
                Main.out("  " + addr.getHostAddress());
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
