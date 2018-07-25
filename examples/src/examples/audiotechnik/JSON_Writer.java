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

    public static void main(String[] args) throws FileNotFoundException {

        String filename = args[0];

        System.out.println(filename);

        String directory = "./resources/"+ filename;

        System.out.println(directory);

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

        String JSON_Out = "{\"element\": [{";
        for(int currentPosInSequence = 0; currentPosInSequence < elementArray.size(); currentPosInSequence++){

            for(int i = 1; i <= 5; i++){
                switch (i) {
                    case 1 :
                        JSON_Out = JSON_Out + elementArray.getJsonObject(1).getString("type");
                }
            }
        }

    }

    public static void writeJSON(String filepath,int[] resultValues ) throws FileNotFoundException {

        InputStream fis = new FileInputStream(filepath);

        JsonReader reader = Json.createReader(fis);

        JsonObject personObject = reader.readObject();

        JsonObject clone = personObject;

        reader.close();

        JsonArray elementArray = personObject.getJsonArray("element");

        int size = elementArray.size();

        boolean isFirst = true;

        double[] used_offsets = new double[ size ];

        int [] numElementsAtValue = new int[size];

        int [] [] elementAt = new int[size][size];

        JsonArray cloneArray = clone.getJsonArray("element");

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
                numElementsAtValue[y] = numElementsAtValue[y] +1;
                elementAt[y] [numElementsAtValue[y]] = i;
                for(int s = 0; s<=numElementsAtValue[y];s++){
                    System.out.println(elementAt[y][s]);
                }
            }
        }

        double total_duration = 0.0d;

        String JSON_Out = "{\"element\": [";

        for(int currentPosInSequence = 0; currentPosInSequence < resultValues.length-1; currentPosInSequence++){
            int currentVal = resultValues[currentPosInSequence];
            int numElements = numElementsAtValue[currentVal];
            int currentIndexOfVal = 0;
            double sub_duration = 0.0d;
            String type = "";
            switch (numElements){
                case 1:
                    currentIndexOfVal = elementAt[currentVal][0];

                    type =  elementArray.getJsonObject(currentIndexOfVal).getString("type");

                    if(type.equals("note")){
                        String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                        int octave_value = elementArray.getJsonObject(currentIndexOfVal).getInt("octave");
                        String octave = String.valueOf(octave_value);
                        String pitch = elementArray.getJsonObject(currentIndexOfVal).getString("pitch");
                        String offset = String.valueOf(total_duration);
                        double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                        String duration = String.valueOf(duration_value);

                        total_duration = total_duration + duration_value;
                        JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"octave\": " + octave + ", \"pitch\": \"" + pitch +"\", \"offset\": " + offset + ", \"duration\": " + duration + "},\n";
                    }
                    else if(type.equals("chord")){
                        String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                        String offset = String.valueOf(total_duration);
                        double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                        String duration = String.valueOf(duration_value);
                        String pitch = "";

                        JsonArray pitchArray = elementArray.getJsonObject(currentIndexOfVal).getJsonArray("pitch");
                        int pitchArraySize = pitchArray.size();
                        for(int i = 0; i < pitchArraySize; i++){
                            pitch = pitch + pitchArray.getString(i);
                            if(i != pitchArraySize-1){
                                pitch = pitch + "\", \"";
                            }
                        }

                        total_duration = total_duration + duration_value;
                        JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"offset\": " +  offset + ", \"duration\": " + duration + ", \"pitch\": [\"" + pitch + "\"]},\n";
                    }
                break;

                case 2:
                    sub_duration = 0.0d;
                    for(int i = 0; i < numElements; i++) {
                        currentIndexOfVal = elementAt[currentVal][i];

                        type = elementArray.getJsonObject(currentIndexOfVal).getString("type");

                        if (type.equals("note")) {
                            String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                            int octave_value = elementArray.getJsonObject(currentIndexOfVal).getInt("octave");
                            String octave = String.valueOf(octave_value);
                            String pitch = elementArray.getJsonObject(currentIndexOfVal).getString("pitch");
                            String offset = String.valueOf(total_duration);
                            double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                            String duration = String.valueOf(duration_value);

                            total_duration = total_duration + duration_value;
                            JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"octave\": " + octave + ", \"pitch\": \"" + pitch + "\", \"offset\": " + offset + ", \"duration\": " + duration + "},\n";
                        } else if (type.equals("chord")) {
                            String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                            String offset = String.valueOf(total_duration);
                            double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                            String duration = String.valueOf(duration_value);
                            String pitch = "";

                            JsonArray pitchArray = elementArray.getJsonObject(currentIndexOfVal).getJsonArray("pitch");
                            int pitchArraySize = pitchArray.size();
                            for (int x = 0; x < pitchArraySize; x++) {
                                pitch = pitch + pitchArray.getString(x);
                                if (x != pitchArraySize - 1) {
                                    pitch = pitch + "\", \"";
                                }
                            }
                            sub_duration = sub_duration + duration_value;
                            JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"offset\": " + offset + ", \"duration\": " + duration + ", \"pitch\": [\"" + pitch + "\"]},\n";
                        }
                    }

                    total_duration = total_duration + sub_duration;
                    break;

                case 3:
                    sub_duration = 0.0d;
                    for(int i = 0; i < numElements; i++) {
                        currentIndexOfVal = elementAt[currentVal][i];

                        type = elementArray.getJsonObject(currentIndexOfVal).getString("type");

                        if (type.equals("note")) {
                            String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                            int octave_value = elementArray.getJsonObject(currentIndexOfVal).getInt("octave");
                            String octave = String.valueOf(octave_value);
                            String pitch = elementArray.getJsonObject(currentIndexOfVal).getString("pitch");
                            String offset = String.valueOf(total_duration);
                            double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                            String duration = String.valueOf(duration_value);

                            total_duration = total_duration + duration_value;
                            JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"octave\": " + octave + ", \"pitch\": \"" + pitch + "\", \"offset\": " + offset + ", \"duration\": " + duration + "},\n";
                        } else if (type.equals("chord")) {
                            String name = elementArray.getJsonObject(currentIndexOfVal).getString("name");
                            String offset = String.valueOf(total_duration);
                            double duration_value = elementArray.getJsonObject(currentIndexOfVal).getJsonNumber("duration").doubleValue();
                            String duration = String.valueOf(duration_value);
                            String pitch = "";

                            JsonArray pitchArray = elementArray.getJsonObject(currentIndexOfVal).getJsonArray("pitch");
                            int pitchArraySize = pitchArray.size();
                            for (int x = 0; x < pitchArraySize; x++) {
                                pitch = pitch + pitchArray.getString(x);
                                if (x != pitchArraySize - 1) {
                                    pitch = pitch + "\", \"";
                                }
                            }

                            sub_duration = sub_duration + duration_value;
                            JSON_Out = JSON_Out + "{\"type\": \"" + type + "\", \"name\": \"" + name + "\", \"offset\": " + offset + ", \"duration\": " + duration + ", \"pitch\": [\"" + pitch + "\"]},\n";
                        }
                    }

                    total_duration = total_duration + sub_duration;
                    break;

            }

    }
    System.out.println(JSON_Out);
    }

}
