// A class that represents a particular hitter on a team
public class Hitter {

    double singleRate;
    double doubleRate;
    double tripleRate;
    double homeRunRate;
    double walkRate;

    public Hitter (double singleRate, double doubleRate, double tripleRate, double homeRunRate, double walkRate){
        if(singleRate + doubleRate  + tripleRate + homeRunRate +  walkRate > 1.0) {
            throw new IllegalArgumentException("A hitter's singleRate + doubleRate + tripleRate + " +
                    "homeRunRate + walkRate should never be greater than 1");
        }
        this.singleRate = singleRate;
        this.doubleRate = doubleRate;
        this.tripleRate = tripleRate;
        this.homeRunRate = homeRunRate;
        this.walkRate = walkRate;
    }
}
