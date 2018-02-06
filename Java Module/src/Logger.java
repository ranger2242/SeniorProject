import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by Chris Cavazos on 2/5/2018.
 */
public class Logger {
    static boolean enabled=true;
    private static void printAllClasses() {
        for (Set<ExtractedClass> set : Main.parsedClasses) {
            for (ExtractedClass extractedClass : set) {
                if (extractedClass.getParent().equals("")) {
                    extractedClass.printClass(0);
                }
            }
        }
    }

    public static void out(String s) {
        if(enabled)
        System.out.println(s);
    }

    public static void outa(String s) {
        if(enabled)
            System.out.print(s);
    }

    public static void slog(String msg) {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy::HH:mm:ss a");
        if(enabled)
            System.out.println(s.format(d) + ":\t" + msg);
    }
}
