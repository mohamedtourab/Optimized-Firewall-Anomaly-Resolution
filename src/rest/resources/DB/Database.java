package rest.resources.DB;

import ofar.generated.classes.input.ServiceInput;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    public final static Map<Integer, ServiceInput> dbHashMap = new ConcurrentHashMap<>();

    public static synchronized ServiceInput insertEntry(ServiceInput serviceInput) {
        if (serviceInput == null) {
            return null;
        }
        serviceInput.setId(dbHashMap.size());
        dbHashMap.put(dbHashMap.size(), serviceInput);
        return serviceInput;
    }

    public static ServiceInput getEntry(int key) {
        return dbHashMap.get(key);
    }
}
