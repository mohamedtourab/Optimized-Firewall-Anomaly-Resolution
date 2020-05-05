package generated.classes.wrappers;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.ObjectFactory;

public class AnomaliesWrapper {
    ObjectFactory objectFactory = new ObjectFactory();
    Anomalies anomalies = objectFactory.createAnomalies();

    public Anomalies getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(Anomalies anomalies) {
        this.anomalies = anomalies;
    }
}
