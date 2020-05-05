package generated.classes.wrappers;

import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.conflicts.ObjectFactory;

public class AnomalyTypeWrapper {
    ObjectFactory objectFactory = new ObjectFactory();
    AnomalyType anomalyType = objectFactory.createAnomalyType();

    public AnomalyType getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(AnomalyType anomalyType) {
        this.anomalyType = anomalyType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof AnomalyTypeWrapper)) return false;
        AnomalyTypeWrapper other = (AnomalyTypeWrapper) obj;
        if (other.getAnomalyType().getAnomalyName() == null || other.getAnomalyType().getRule() == null || other.getAnomalyType().getAnomalyID() == null)
            return false;
        return other.getAnomalyType().getAnomalyName().equals(this.getAnomalyType().getAnomalyName()) &&
                other.getAnomalyType().getRule().equals(this.getAnomalyType().getRule());
    }

    @Override
    public String toString() {
        return String.format("Anomaly %s\nType %s\nRules Included %s",
                getAnomalyType().getAnomalyID(),
                getAnomalyType().getAnomalyName(),
                getAnomalyType().getRule());
//        return "AnomalyType{" +
//                "ruleID=" + getAnomalyType().getRule() +
//                ", anomalyName=" + getAnomalyType().getAnomalyName() +
//                ", anomalyID=" + getAnomalyType().getAnomalyID() +
//                '}';
    }

}
