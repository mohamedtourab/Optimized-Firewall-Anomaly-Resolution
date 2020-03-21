package optimized.resolution.algorithm;

import ofar.generated.classes.AnomalyType;
import ofar.generated.classes.RuleType;

import java.util.Set;

public class RemovedEntries {
    private Set<RuleType> removedRules;
    private Set<AnomalyType> removedAnomalies;

    public RemovedEntries(Set<RuleType> removedRules, Set<AnomalyType> removedAnomalies) {
        this.removedRules = removedRules;
        this.removedAnomalies = removedAnomalies;
    }

    public Set<RuleType> getRemovedRules() {
        return removedRules;
    }

    public Set<AnomalyType> getRemovedAnomalies() {
        return removedAnomalies;
    }

}
