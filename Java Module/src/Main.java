import com.google.gson.Gson;
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

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        String path = "ex\\ex2";
        Parser parser = new Parser(fileHandler.load(path));
        slog("Loaded dir: " + path);
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        slog("Classes Parsed");
        ArrayList<ExtractedClass> cl = new ArrayList<>(parsedClasses);
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
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");

        out(s.format(d) + ":\t" + msg);
    }
      /*  public static JSONObject initPipeline(ArrayList<int[][]> list){
            //Create json file for Python project
            Pipeline pipeline = new Pipeline();
            pipeline.createJSONFile(list);
            pipeline.launchPython();
            JSONObject json = pipeline.readJSONFile("python_output", ".json");
            return json;
        }*/

    public static String getIp() {

        return "localhost";
        //return "127.0.0.1";
        // return "0.0.0.0";
        /*
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (whatismyip != null) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                String ip = in.readLine();
               //return InetAddress.getLocalHost().getHostAddress();
                 //return ip;
                return "localhost";
               //return "127.0.0.1";
               // return "0.0.0.0";
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "null IP";
        */
    }

    public static void connectSocket() throws URISyntaxException {
        socket = IO.socket(dir);

        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {//on connect
            @Override
            public void call(Object... args) {
                String msg = "-Connection Established-";
                socket.emit("msg", "ERGO JAVA CLIENT CONNECTED");
                slog("Connected to server");
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
