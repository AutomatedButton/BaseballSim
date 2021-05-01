import java.util.ArrayList;

// A class to represent a particular team in a game of baseball
public class Team {
    int score;
    int hits;
    ArrayList<Hitter> players;
    int orderPosition;

    public Team(Hitter player) {
        this(player, player, player, player, player, player, player, player, player);
    }

    public Team(Hitter player1, Hitter player2, Hitter player3, Hitter player4, Hitter player5,
                Hitter player6, Hitter player7, Hitter player8, Hitter player9) {
        this.players = new ArrayList<Hitter>();
        this.players.add(player1);
        this.players.add(player2);
        this.players.add(player3);
        this.players.add(player4);
        this.players.add(player5);
        this.players.add(player6);
        this.players.add(player7);
        this.players.add(player8);
        this.players.add(player9);
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
