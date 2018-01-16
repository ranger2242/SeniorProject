import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
        String path = "ex\\ex4";
        Parser parser = new Parser(fileHandler.load(path));
        slog("Loaded dir: " + path);
        parsedClasses = new LinkedHashSet<>(parser.getExtractedClasses());
        globalEnums = new LinkedHashSet<>(parser.getGlobalEnums());
        slog("Classes Parsed");

        Transformer transformer = new Transformer(parsedClasses, globalEnums);
        double[][][] matrices = transformer.transform();
        slog("Data Transformed");

        Gson gson = new Gson();
        String json = gson.toJson(matrices);

        Pipeline pipeline = new Pipeline();
        pipeline.sendToServer(json);

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

    public static void slog(String msg) {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");
        System.out.println(s.format(d) + ":\t" + msg);
    }

}
