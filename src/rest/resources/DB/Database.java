package rest.resources.DB;

import ofar.generated.classes.input.ServiceInput;

import javax.ws.rs.NotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    public final static Map<Integer, ServiceInput> dbHashMap = new ConcurrentHashMap<>();

    public static synchronized ServiceInput insertEntry(ServiceInput serviceInput) {
        if (serviceInput == null) {
            return null;
        }
        serviceInput.setId(BigInteger.valueOf(dbHashMap.size()));
        dbHashMap.put(dbHashMap.size(), serviceInput);
        return serviceInput;
    }

    public static ServiceInput getEntry(int key) {
        return dbHashMap.get(key);
    }

    public static List<ServiceInput> getPage(int page, int itemsPerPage) {
        int totalAvailableNumberOfPages = (dbHashMap.size() / itemsPerPage) + 1;
        if (page > totalAvailableNumberOfPages) {
            throw new NotFoundException("Page Doesn't Exist");
        }
        List<ServiceInput> onePage = new ArrayList<>();
        List<List<ServiceInput>> pages = new ArrayList<>();
        int itemIndex = 0;
        for (int i = 0; i <= page; i++) {
            for (int j = 0; j < itemsPerPage && j < dbHashMap.size(); j++) {
                onePage.add(dbHashMap.get(itemIndex++));
            }
            pages.add(onePage);
            onePage = new ArrayList<>();
        }
        itemIndex = 0;
        return pages.get(page-1);
    }
}
