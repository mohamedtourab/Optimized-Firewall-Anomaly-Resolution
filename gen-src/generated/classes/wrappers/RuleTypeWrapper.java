package generated.classes.wrappers;

import ofar.generated.classes.rules.ObjectFactory;
import ofar.generated.classes.rules.RuleType;

public class RuleTypeWrapper {
    ObjectFactory objectFactory = new ObjectFactory();
    RuleType ruleType = objectFactory.createRuleType();

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof RuleTypeWrapper)) return false;
        RuleTypeWrapper other = (RuleTypeWrapper) obj;

        return other.getRuleType().getAction().equals(this.getRuleType().getAction()) &&
                other.getRuleType().getPriority().equals(this.getRuleType().getPriority()) &&
                other.getRuleType().getIPdst().equals(this.getRuleType().getIPdst()) &&
                other.getRuleType().getIPsrc().equals(this.getRuleType().getIPsrc()) &&
                other.getRuleType().getPsrc().equals(this.getRuleType().getPsrc()) &&
                other.getRuleType().getPdst().equals(this.getRuleType().getPdst()) &&
                other.getRuleType().getProtocol().equals(this.getRuleType().getProtocol());
    }

    @Override
    public String toString() {
        return "RuleType{" +
                "priority=" + getRuleType().getPriority() +
                ", iPsrc='" + getRuleType().getIPsrc() + '\'' +
                ", psrc='" + getRuleType().getPsrc() + '\'' +
                ", iPdst='" + getRuleType().getIPdst() + '\'' +
                ", pdst='" + getRuleType().getPdst() + '\'' +
                ", protocol='" + getRuleType().getProtocol() + '\'' +
                ", action='" + getRuleType().getAction() + '\'' +
                ", ruleID=" + getRuleType().getRuleID() +
                '}';
    }
}
