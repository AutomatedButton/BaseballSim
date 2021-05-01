// A class that represents a particular hitter on a team
public class Hitter {

    double singleRate;
    double doubleRate;
    double tripleRate;
    double homeRunRate;
    double walkRate;

    public Hitter (double singleRate, double doubleRate, double tripleRate, double homeRunRate, double walkRate){
        if(singleRate + doubleRate + homeRunRate + walkRate > 1.0) {
            throw new IllegalArgumentException("Home Run Rate + Double Rate should never be greater than " +
                    "a hitter's batting average");
        }
        this.singleRate = singleRate;
        this.doubleRate = doubleRate;
        this.tripleRate = tripleRate;
        this.homeRunRate = homeRunRate;
        this.walkRate = walkRate;
    }
}
