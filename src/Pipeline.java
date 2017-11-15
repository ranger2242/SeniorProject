import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Pipeline {

    public Pipeline(){

    }

    public void createJSONFile(ArrayList<int[][]> matrices){
        JSONObject json = new JSONObject();

        for ( int i = 0; i < matrices.size(); i++ ){
            json.put("mat" + i , matrices.get(i));
        }

        String fileName = "matrices.json";
        String directory = System.getProperty("user.dir") + "\\json";

        try(FileWriter file  = new FileWriter(new File(directory, fileName))){
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
