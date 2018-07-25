package examples.audiotechnik;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static examples.audiotechnik.JSON_Reader.contains;

public class JSON_Writer {

    public static void writeJSON(String filename,int[] resultValues ) throws FileNotFoundException {

        System.out.println(filename);

        String directory = "./resources/"+ filename;

        InputStream fis = new FileInputStream("./resources/kout.json");

        JsonReader reader = Json.createReader(fis);

        JsonObject personObject = reader.readObject();

        reader.close();

        JsonArray elementArray = personObject.getJsonArray("element");

        int size = elementArray.size();

        boolean isFirst = true;

        double[] used_offsets = new double[ size ];

        int [] numElementsAt = new int[size];

        for(int i = 0 ; i <= elementArray.size()-1; i++){
            double x = elementArray.getJsonObject(i).getJsonNumber("offset").doubleValue();

            if(!contains(used_offsets, x, size, isFirst)){
                isFirst = false;
                used_offsets[i] = x;
                numElementsAt[i] = 1;

            }
            else{
                System.out.println("Doppelter Offsetvalue");
                numElementsAt[i] = numElementsAt[i] +1;
                System.out.println("zwei mal die" + x);
            }
        }

    }

}
