// A class that represents a particular hitter on a team
public class Hitter {

    double singleRate;
    double doubleRate;
    double tripleRate;
    double homeRunRate;
    double walkRate;
    double strikeOutRate;
    double lineOutRate;
    double flyOutRate;
    double groundOutRate;

    public Hitter (double singleRate, double doubleRate, double tripleRate, double homeRunRate, double walkRate,
                   double strikeOutRate, double lineOutRate, double flyOutRate, double groundOutRate){
        if(singleRate + doubleRate  + tripleRate + homeRunRate +  walkRate > 1.0) {
            throw new IllegalArgumentException("A hitter's singleRate + doubleRate + tripleRate + " +
                    "homeRunRate + walkRate should never be greater than 1");
        }
        this.singleRate = singleRate;
        this.doubleRate = doubleRate;
        this.tripleRate = tripleRate;
        this.homeRunRate = homeRunRate;
        this.walkRate = walkRate;
        this.strikeOutRate = strikeOutRate;
        this.lineOutRate = lineOutRate;
        this.flyOutRate = flyOutRate;
        this.groundOutRate = groundOutRate;
    }

    public PAOutcome SimulatePA() {
        double rng = Math.random();
        double accumulator = 0.0;

        if (rng <= this.walkRate) {
            return PAOutcome.Walk;
        }
        accumulator += this.walkRate;
        if (rng <= accumulator + this.singleRate) {
            return PAOutcome.Single;
        }
        accumulator += this.singleRate;
        if (rng <= accumulator + this.doubleRate) {
            return PAOutcome.Double;
        }
        accumulator += this.doubleRate;
        if (rng <= accumulator+ this.tripleRate) {
            return PAOutcome.Triple;
        }
        accumulator += this.tripleRate;
        if (rng <= accumulator + this.homeRunRate) {
            return PAOutcome.HomeRun;
        }
        accumulator += this.homeRunRate;
        if (rng <= accumulator + this.flyOutRate) {
            return PAOutcome.FlyOut;
        }
        accumulator += this.flyOutRate;
        if (rng <= accumulator + this.lineOutRate) {
            return PAOutcome.LineOut;
        }
        accumulator += this.lineOutRate;
        if (rng <= accumulator + this.groundOutRate) {
            return PAOutcome.GroundOut;
        }
        accumulator += this.groundOutRate;
        if (rng <= accumulator + this.strikeOutRate) {
            return PAOutcome.StrikeOut;
        }
        System.out.println(rng);
        throw new IllegalArgumentException("Somehow we generated a number that didn't line up with any outcome");
    }
}
