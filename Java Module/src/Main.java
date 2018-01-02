import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
//import Sock

/**
 * Created by Chris Cavazos on 10/1/2017.
 */
@SuppressWarnings("ALL")
class Main {

    public final static String div = "-----------------------------------------------";
    public static Set<ExtractedClass> parsedClasses = new LinkedHashSet<>();
    public static Set<Enum> globalEnums = new LinkedHashSet<>();

    static String port = "2242";
    static String ip="10.133.228.186";
    static String ip3="139.94.249.166";

    static String ip2= "localhost";
    public static String dir = "http://" +ip2+ ":" + port;
    static Gson gson = new Gson();

    static Socket socket;
    private static String clientID;

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        String path = "ex\\ex2";
        Parser parser = new Parser(fileHandler.load(path));
        slog("Loaded dir: " + path);
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        slog("Classes Parsed");



        try {
            slog("Connecting to " + dir);
            connectSocket();

            int[][] matrices = transformData(parsedClasses);
            Gson gson = new Gson();
            String json = gson.toJson(matrices);

            sendToServer(json);

        } catch (URISyntaxException e) {
            slog("Failed to connect");
            e.printStackTrace();
        }

    }

    //Method not implemented yet
    public static int[][] transformData(Set<ExtractedClass> classes){
        int[][] dummyData = {{4, 5, 7,}, {2, 8, 9}};
        return dummyData;
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
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");
        out(s.format(d) + ":\t" + msg);
    }

    public static void sendToServer(String data){
        socket.emit("SEND-PYTHON", data);
    }

    public static String getIp() {
        return "localhost";
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







    private static void printAllClasses() {
        for (ExtractedClass extractedClass : parsedClasses) {
            if (extractedClass.getParent().equals("")) {
                extractedClass.printClass(0);
            }
        }
    }

    public static void out(String s) {
        System.out.println(s);
    }

    public static void outa(String s) {
        System.out.print(s);
    }
}
