// A class that represents a particular hitter on a team
public class Hitter {

    double singleRate;
    double doubleRate;
    double homeRunRate;
    double walkRate;

    public Hitter (double singleRate, double doubleRate, double homeRunRate, double walkRate){
        if(singleRate + doubleRate + homeRunRate + walkRate > 1.0) {
            throw new IllegalArgumentException("A hitter's singleRate + doubleRate + homeRunRate + walkRate should never be more than 1."); 
        }
        this.singleRate = singleRate;
        this.doubleRate = doubleRate;
        this.homeRunRate = homeRunRate;
        this.walkRate = walkRate;
    }
}
