import jdk.nashorn.internal.parser.JSONParser;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Pipeline {

    public Pipeline(){

    }

    public JSONObject readJSONFile(String fileName, String extension){
        String directory = getJSONDirectory();
        File folder = new File(directory);
        File jsonFile = new File(folder + "\\" + fileName + extension);
        File tmpFile = new File(folder + "\\" + fileName + ".tmp");


        if ( !jsonFile.exists()){
            System.out.println("Can not find: " + jsonFile.getAbsolutePath());
            return null;
        }

        if (tmpFile.exists()){
            System.out.println("FILE STILL BEING WRITTEN TO");
            return null;
        }

        FileHandler fileHandler = new FileHandler();
        String fileText = fileHandler.load(jsonFile);
        JSONObject json= new JSONObject(fileText);
        return json;
    }

    public void launchPython(){
        String directory = getRootDirectory();
        directory += "Batch Files\\run_main_python.bat";

        try{
            Process p = Runtime.getRuntime().exec(directory);
        }catch (IOException e){
            System.out.println("ERROR: Can't run run_main_python.bat file");
            e.printStackTrace();
        }

    }

    public void createJSONFile(ArrayList<int[][]> matrices){
        JSONObject json = new JSONObject();

        for ( int i = 0; i < matrices.size(); i++ ){
            json.put("mat" + i , matrices.get(i));
        }

        String fileName = "matrices.json";
        String directory = getJSONDirectory();

        try(FileWriter file  = new FileWriter(new File(directory, fileName))){
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Pipeline ERROR");
            e.printStackTrace();
        }
    }

    private String getRootDirectory(){
        String directory = System.getProperty("user.dir");

        while( !directory.endsWith("\\") ){
            directory = directory.substring(0,directory.length() - 1 );
        }
        return directory;
    }

    private String getJSONDirectory(){
        String directory = getRootDirectory();
        directory += "json";
        return directory;
    }

}
