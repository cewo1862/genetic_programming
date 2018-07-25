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

        int [] numElementsAtValue = new int[size];

        int [] [] elementAt = new int[size][size];

        for(int i = 0 ; i <= elementArray.size()-1; i++){
            double x = elementArray.getJsonObject(i).getJsonNumber("offset").doubleValue();
            int y = (int) x;
            if(!contains(used_offsets, x, size, isFirst)){
                isFirst = false;
                used_offsets[i] = x;
                numElementsAtValue[y] = 1;
                elementAt[y][0] = i;

            }
            else{
                System.out.println("Doppelter Offsetvalue");
                System.out.println();
                elementAt[y] [numElementsAtValue[y]] = i;
                numElementsAtValue[y] = numElementsAtValue[y] +1;
                System.out.println(numElementsAtValue[y] + " mal die " + x);
                System.out.println("An den Stellen: ");
                for(int s = 0; s<numElementsAtValue[y];s++){
                    System.out.println(elementAt[y][s]);
                }
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

        int [] numElementsAtValue = new int[size];

        int [] [] elementAt = new int[size][size];

        for(int i = 0 ; i <= elementArray.size()-1; i++){
            double x = elementArray.getJsonObject(i).getJsonNumber("offset").doubleValue();
            int y = (int) x;
            if(!contains(used_offsets, x, size, isFirst)){
                isFirst = false;
                used_offsets[i] = x;
                numElementsAtValue[y] = 1;
                elementAt[y][1] = i;

            }
            else{
                System.out.println("Doppelter Offsetvalue");
                System.out.println();
                numElementsAtValue[y] = numElementsAtValue[y] +1;
                elementAt[y] [numElementsAtValue[y]] = i;
                System.out.println(numElementsAtValue[y] + " mal die " + x);
                System.out.println("An den Stellen: ");
                for(int s = 0; s<=numElementsAtValue[y];s++){
                    System.out.println(elementAt[y][s]);
                }
            }
        }
        return used_offsets;

    }
}