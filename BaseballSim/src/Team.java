import java.util.ArrayList;

// A class to represent a particular team in a game of baseball
public class Team {
    int score;
    int hits;
    ArrayList<Hitter> players;
    int orderPosition;

    public Team(double singleRate, double doubleRate, double tripleRate, double homeRunRate, double walkRate) {
        this.players = new ArrayList<Hitter>();
        for (int i = 0; i < 9; i++) {
             this.players.add(new Hitter(singleRate, doubleRate, tripleRate, homeRunRate, walkRate));
        }
        this.score = 0;
        this.hits = 0;
        this.orderPosition = 1;
    }

    public void uptickOrderPosition() {
        if (orderPosition >= 9) {
            orderPosition = 1;
        } else {
            orderPosition++;
        }
    }

}
