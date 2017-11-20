//import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
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
    public static String dir = "http://" + "localhost" + ":" + port;
//    static Gson gson = new Gson();

    static io.socket.client.Socket socket;

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        Parser parser = new Parser(fileHandler.load("ex\\ex2"));
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        ArrayList<ExtractedClass> cl = new ArrayList<>(parsedClasses);
//        printAllClasses();
//        Output o = new Output(cl);
//        o.printAll();

        try {
            connectSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        System.out.println();
    }

    /*
        public static JSONObject initPipeline(ArrayList<int[][]> list){
            //Create json file for Python project
            Pipeline pipeline = new Pipeline();
            pipeline.createJSONFile(list);
            pipeline.launchPython();
            JSONObject json = pipeline.readJSONFile("python_output", ".json");
            return json;
        }
    */

    public static String getIp() {
        return "localhost";
    }

    public static void connectSocket() throws URISyntaxException {

        try {
            Socket s = new Socket("localhost", 2242);
            s.setKeepAlive(true);

            String id = "0";
            sendMessage(s,id);
            sendMessage(s, "Ready");


            DataInputStream din = new DataInputStream(s.getInputStream());

            while(s.isConnected()){
                ArrayList<Byte> messageByte = new ArrayList<>();
                while(din.available() == 1){
                    messageByte.add(new Byte(din.readByte()));
                    messageByte.forEach(x->System.out.println(x));
                }

            }

        } catch (IOException e) {
            System.out.println("Disconnected from server");
            e.printStackTrace();
        }

        socket = IO.socket(dir);
        socket.connect();

        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {//on connect
            @Override
            public void call(Object... args) {
                String msg = "-Connection Established-";

                socket.emit("getSocketID", msg);//ask server for socketID
            }

        });
        socket.on("recieveCurrTurn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }

        });

        socket.on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        });
        socket.connect();
    }

    private static void sendMessage(Socket s, String message) throws IOException{
        OutputStream os = s.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(message);
        bw.flush();
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
