

package stratego2.model.Player.AI;

import java.util.Map;
import stratego2.model.Rank;

/**
 *
 * @author roussew
 */
class ProbabilityDistribution {
    private double marshalProb, generalProb, colonelProb, majorProb, captainProb, 
            leiutenantProb, sergeantProb, minerProb, scoutProb, spyProb, bombProb,
            flagProb;

    public double getMarshalProb() {
        return marshalProb;
    }

    public void setMarshalProb(double marshalProb) {
        this.marshalProb = marshalProb;
    }

    public double getGeneralProb() {
        return generalProb;
    }

    public void setGeneralProb(double generalProb) {
        this.generalProb = generalProb;
    }

    public double getColonelProb() {
        return colonelProb;
    }

    public void setColonelProb(double colonelProb) {
        this.colonelProb = colonelProb;
    }

    public double getMajorProb() {
        return majorProb;
    }

    public void setMajorProb(double majorProb) {
        this.majorProb = majorProb;
    }

    public double getCaptainProb() {
        return captainProb;
    }

    public void setCaptainProb(double captainProb) {
        this.captainProb = captainProb;
    }

    public double getLeiutenantProb() {
        return leiutenantProb;
    }

    public void setLeiutenantProb(double leiutenantProb) {
        this.leiutenantProb = leiutenantProb;
    }

    public double getSergeantProb() {
        return sergeantProb;
    }

    public void setSergeantProb(double sergeantProb) {
        this.sergeantProb = sergeantProb;
    }

    public double getMinerProb() {
        return minerProb;
    }

    public void setMinerProb(double minerProb) {
        this.minerProb = minerProb;
    }

    public double getScoutProb() {
        return scoutProb;
    }

    public void setScoutProb(double scoutProb) {
        this.scoutProb = scoutProb;
    }

    public double getSpyProb() {
        return spyProb;
    }

    public void setSpyProb(double spyProb) {
        this.spyProb = spyProb;
    }

    public double getBombProb() {
        return bombProb;
    }

    public void setBombProb(double bombProb) {
        this.bombProb = bombProb;
    }

    public double getFlagProb() {
        return flagProb;
    }

    public void setFlagProb(double flagProb) {
        this.flagProb = flagProb;
    }
    
    public void setProb(Rank rank, double prob) {
        switch (rank) {
            case BOMB: setBombProb(prob);
                break;
                
            case FLAG: setFlagProb(prob);
                break;
            
            case SPY: setSpyProb(prob);
                break;
                
            case SCOUT: setScoutProb(prob);
                break;
                
            case MINER: setMinerProb(prob);
                break;
                
            case SERGEANT: setSergeantProb(prob);
                break;
            
            case LIEUTENANT: setLeiutenantProb(prob);
                break;
                
            case CAPTAIN: setCaptainProb(prob);
                break;
                
            case MAJOR: setMajorProb(prob);
                break;
                
            case COLONEL: setColonelProb(prob);
                break;
                
            case GENERAL: setGeneralProb(prob);
                break;
                
            case MARSHAL: setMarshalProb(prob);
                break;
        }
    }
    
    public double getProb(Rank rank) {
        switch (rank) {
            case BOMB: return getBombProb();
                
            case FLAG: return getFlagProb();
            
            case SPY: return getSpyProb();
                
            case SCOUT: return getScoutProb();
                
            case MINER: return getMinerProb();
                
            case SERGEANT: return getSergeantProb();
            
            case LIEUTENANT: return getLeiutenantProb();
                
            case CAPTAIN: return getCaptainProb();
                
            case MAJOR: return getMajorProb();
                
            case COLONEL: return getColonelProb();
                
            case GENERAL: return getGeneralProb();
                
            case MARSHAL: return getMarshalProb();
             
            default: return 0; //because we're all just slaves to the compiler
        }
    }
    
    public ProbabilityDistribution copy() {
        ProbabilityDistribution copy = new ProbabilityDistribution();
        for(Rank r: Rank.values()) {
            copy.setProb(r, this.getProb(r));
        }
        return copy;
    }

    @Override
    public String toString() {
        return "ProbabilityDistribution{" + "marshalProb=" + marshalProb + ", generalProb=" + generalProb + ", colonelProb=" + colonelProb + ", majorProb=" + majorProb + ", captainProb=" + captainProb + ", leiutenantProb=" + leiutenantProb + ", sergeantProb=" + sergeantProb + ", minerProb=" + minerProb + ", scoutProb=" + scoutProb + ", spyProb=" + spyProb + ", bombProb=" + bombProb + ", flagProb=" + flagProb + '}';
    }
    
}
