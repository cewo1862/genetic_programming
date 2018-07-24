package examples.audiotechnik;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JSON_Reader {

    public static void main(String[] args) throws FileNotFoundException {

        String filename = args[0];

        System.out.println(filename);

        String directory = "./resources/"+ filename;

        System.out.println(directory);

        if(directory.equals("./resources/kout.json")){
            System.out.println("correct filename");
        }

        InputStream fis = new FileInputStream("./resources/kout.json");

        JsonReader reader = Json.createReader(fis);

        JsonObject personObject = reader.readObject();

        reader.close();

        JsonArray elementArray = personObject.getJsonArray("element");

        int size = elementArray.size();

        boolean isFirst = true;

        double[] used_offsets = new double[ size ];

        for(int i = 0 ; i <= elementArray.size()-1; i++){
            double x = elementArray.getJsonObject(i).getJsonNumber("offset").doubleValue();

            if(!contains(used_offsets, x, size, isFirst)){
                isFirst = false;
                used_offsets[i] = x;

            }
            else{
                System.out.println("Doppelter Offsetvalue");
            }
        }

    }

    public static boolean contains(double[] used_offsets, double value,int size, boolean isFirst){
        if (isFirst){
            return false;
        }
        else {
            for (int i = 0; i <= size-1; i++) {
                double y = used_offsets[i];
                if (y == value) {
                    System.out.println("y =" + y);
                    System.out.println("value =" + value);
                    return true;
                }
            }
        }

        return false;
    }

    public static double[] readJSON(String filepath) throws FileNotFoundException{
        InputStream fis = new FileInputStream(filepath);

        JsonReader reader = Json.createReader(fis);

        JsonObject personObject = reader.readObject();

        reader.close();

        JsonArray elementArray = personObject.getJsonArray("element");

        int size = elementArray.size();

        boolean isFirst = true;

        double[] used_offsets = new double[ size ];

        for(int i = 0 ; i <= elementArray.size()-1; i++){
            double x = elementArray.getJsonObject(i).getJsonNumber("offset").doubleValue();

            if(!contains(used_offsets, x, size, isFirst)){
                isFirst = false;
                used_offsets[i] = x;

            }
            else{
                System.out.println("Doppelter Offsetvalue");
            }
        }

        return used_offsets;

    }
}