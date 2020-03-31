package rest.resources.DB;

import ofar.generated.classes.input.ServiceInput;

import java.util.HashMap;

public class Database {
    public static HashMap<Integer, ServiceInput> dbHashMap = new HashMap<>();

    public static ServiceInput insertEntry(ServiceInput serviceInput) {
        if (serviceInput == null) {
            return null;
        }
        serviceInput.setId(dbHashMap.size());
        dbHashMap.put(dbHashMap.size(),serviceInput);
        return serviceInput;
    }
    public static ServiceInput getEntry(int key){
        return dbHashMap.get(key);
    }
}
