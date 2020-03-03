package optimized.resolution.algorithm;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.rules.Rules;

import java.util.List;

public class ConflictResolver {

    private List<Rules> rules;
    private List<Anomalies> anomalies;

    public ConflictResolver() {
    }

    public ConflictResolver(List<Rules> rules, List<Anomalies> anomalies) {
        this.anomalies = anomalies;
        this.rules = rules;
    }

    public void resolveAnomalies() {

    }


    public List<Rules> getRules() {
        return rules;
    }

    public void setRules(List<Rules> rules) {
        this.rules = rules;
    }

    public List<Anomalies> getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(List<Anomalies> anomalies) {
        this.anomalies = anomalies;
    }

}
