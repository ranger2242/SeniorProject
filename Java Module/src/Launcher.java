import java.io.File;
import java.io.IOException;

/**
 * Created by Chris Cavazos on 2/26/2018.
 */
public class Launcher {
    public static void main(String[] args){
        try {
            String path="C:\\Users\\Chris\\Desktop\\SeniorProject\\";
            Runtime.getRuntime().exec(""+path+"Java Module\\src\\starts.bat ",null,new File(path));
            Runtime.getRuntime().exec(""+path+"Python Module\\server\\startp.bat ",null,new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
