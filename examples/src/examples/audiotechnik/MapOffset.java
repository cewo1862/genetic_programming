package examples.audiotechnik;

import java.util.HashMap;
import java.util.Map;

public class MapOffset {
    public Map getOffset(double[] offset){
        Map<Integer,Double> notemap = new HashMap<>();
        for(int i = 0; i <= offset.length; i++){
            notemap.put(i, offset[i]);
        }
        return notemap;
    }
}
