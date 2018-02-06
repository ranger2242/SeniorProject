import com.google.gson.Gson;
import io.socket.client.Socket;

/**
 * Created by Chris Cavazos on 2/5/2018.
 */
public class ConnectionHandler {
    String port = "2242";
    String ip = "10.133.228.186";
    String ip3 = "139.94.249.166";
    String ip2 = "localhost";
    String dir = "http://" + ip2 + ":" + port;
    Gson gson = new Gson();
    Socket socket;
    String clientID;

}
