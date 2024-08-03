package synrgy.team4.backend.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class Response {
    public Map templateEror(Object obj) {
        Map map = new HashMap();
        map.put("status", 404);
        map.put("message", obj);
        return map;
    }
    public Map templateSuksess(Object obj) {
        Map map = new HashMap();
        map.put("status", 200);
        map.put("message", obj);
        return map;
    }
}
