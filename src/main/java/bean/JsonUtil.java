package bean;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static String toJson(List<Integer> integerList) {
        return new JSONArray(integerList).toString();
    }

    public static List<Integer> parseIntegerList(String json) {
        List<Integer> integerList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                integerList.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return integerList;
    }
    public static List<Integer> parseIntegerList(JSONArray json) {
        List<Integer> integerList = new ArrayList<>();
        try {
            for (int i = 0; i < json.length(); i++) {
                integerList.add(json.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return integerList;
    }
}