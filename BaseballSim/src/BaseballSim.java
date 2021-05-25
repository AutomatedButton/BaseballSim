import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BaseballSim {


    public static void main(String[] args){

        int AwayTeamWins = 0;
        int HomeTeamWins = 0;
        Hitter exampleSmallBaller = new Hitter(.300, 0.0, 0.0, 0.0, 0.088,
                .250, .100, .062, .200);
        Hitter exampleSlugger = new Hitter(0.0, .130, 0.0, 0.07, 0.088,
                .250, .150, .200, .112);

        //for (int j = 0; j < 1000000; j++){
        GridLayout example = new GridLayout(3,3);
        Team awayTeam = new Team(exampleSmallBaller);
        Team homeTeam = new Team(exampleSlugger);
        JFrame frame = new JFrame("test");
        JPanel panel = new JPanel(example);
        frame.setSize(1600,900);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel scoreLabel = new JLabel("Once a game is simulated the score will go here.");
        JLabel hitLabel = new JLabel("Once a game is simulated the hits will go here.");
        Button simulateButton = new Button("Simulate Game");

        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulateGame(HomeTeamWins, AwayTeamWins, homeTeam, awayTeam, scoreLabel, hitLabel);
                homeTeam.score = 0;
                awayTeam.score = 0;
                homeTeam.hits = 0;
                awayTeam.hits = 0;
            }
        });
        frame.setContentPane(panel);
        frame.getContentPane().add(scoreLabel, BorderLayout.NORTH);
        frame.getContentPane().add(hitLabel, BorderLayout.CENTER);
        frame.getContentPane().add(simulateButton);
        frame.setVisible(true);
    }

    public static void SimulateGame(int homeTeamWins, int awayTeamWins, Team homeTeam, Team awayTeam,
                                    JLabel scoreLabel, JLabel hitLabel) {
        boolean isGameOver = false;
        int inning = 1;
        // This variable just keeps track of the outcome of the current plate appearance
        PAOutcome PAResult;
        // This is the main loop
        while (!isGameOver) {
            /* This while loop simulates a half inning with TeamOne hitting. This is probably
             the area of the code I'm least satisfied with, because it repeats itself a fair bit
             in the bottom half of the inning.*/
            awayTeam = SimulateHalfInning(awayTeam, homeTeam, inning);

            if (inning == 9 && (homeTeam.score > awayTeam.score)) {
                isGameOver = true;
                System.out.println("The game ended after 8 1/2 innings");
                break;
            }

            homeTeam = SimulateHalfInning(homeTeam, awayTeam, inning);
            System.out.println("End of Inning " + inning + ": Away: " + awayTeam.score + " Home: " + homeTeam.score);
            if (inning >= 9 && (awayTeam.score != homeTeam.score)) {
                isGameOver = true;
            }
            inning++;

        }
        System.out.println("Final: Away: " + awayTeam.score + ", Home: " + homeTeam.score);
        scoreLabel.setText(convertToMultiline("Home Team Score: " + homeTeam.score +
                "\nAway Team Score: " + awayTeam.score));
        hitLabel.setText(convertToMultiline("Home Team Hits: " + homeTeam.hits +
                "\nAway Team Hits: " + awayTeam.hits));
        System.out.println("Hits: Away: " + awayTeam.hits + ", Home: " + homeTeam.hits);
    }

    public static int ScoreAllBases(int score, ArrayList<Hitter> bases) {
        if (bases.size() != 3) {
            throw new IllegalArgumentException("The provided bases don't have the right number of bases");
        }
        for (int i = 0; i < 3; i++) {
            if (bases.get(i) != null) {
                score++;
            }
        }
        return score;
    }

    // This method returns true it is late in a close game and by that I mean if there is only
    // one run or less separating the teams and it is the 7th inning or later.
    public static boolean isLateCloseGame(Team teamOne, Team teamTwo, int inning) {
        return Math.abs(teamOne.score - teamTwo.score) <= 1 && inning > 7;
    }

    public static Team SimulateHalfInning(Team battingTeam, Team fieldingTeam, int inning) {
        int outs = 0;
        ArrayList<Hitter> basepaths = new ArrayList<Hitter>();
        // This loop just sets up the basepaths
        for (int i = 0; i < 3; i++) {
            basepaths.add(null);
        }
        //System.out.println(basepaths);
        PAOutcome PAResult;

        while (outs < 3) {
            PAResult = battingTeam.players.get(battingTeam.orderPosition - 1).SimulatePA();
            if (PAResult == PAOutcome.StrikeOut) {
                outs++;
            } else if (PAResult == PAOutcome.FlyOut) {
                outs++;
                // Tagging up logic. Things get kind of complicated here because you obviously
                // can't tag up on an infield fly or shallow outfield fly, but you can tag up
                // on most other fly balls
                if (outs < 3) {
                    // This is an absolutely wild stab at what percent of flyouts can be tagged
                    // up on from 3rd base
                    if (Math.random() < .500) {
                        if (basepaths.get(2) != null) {
                            battingTeam.score++;
                        }
                        // This is a guess that a 1/3 of fly balls that can be tagged up on from
                        // 3rd base can also be tagged up on from 2nd
                        if (basepaths.get(1) != null && Math.random() < .333) {
                            basepaths.set(2, basepaths.get(1));
                            basepaths.set(1, null);
                        }
                    }
                }
            } else if (PAResult == PAOutcome.LineOut){
                outs++;
            } else if (PAResult == PAOutcome.GroundOut){
                outs++;
                // GIDP logic, just if someone is on first, a ground ball will convert into a
                // double play. This gets kind of weird because there are situations where a
                // double play happens but it isn't the hitter and man on first who get out. But
                // those situations are dependent on where the ball is hit, and I don't have a way
                // of tracking that yet.
                if (basepaths.get(0) != null) {

                    if (basepaths.get(1) != null) {
                        if (basepaths.get(2) != null) {
                            // Bases loaded

                        } else {
                            // Runners on first & second, but not 3rd
                        }

                    } else if (basepaths.get(2) != null) {
                        // Runners on the corners
                        if (outs == 2) {
                            // Play is at first, so we don't want to do anything
                        } else if (outs == 1) {
                            // Should be a double play to end the inning & stop the runner at
                            // third from scoring
                        } else {
                            if (isLateCloseGame(battingTeam, fieldingTeam, inning) && outs == 0) {
                                // This bit here might require some fine-tuning
                                
                            }
                        }
                    }
                    // Only runner is on first
                    basepaths.set(0, null);
                    outs++;
                }
            } else if (PAResult == PAOutcome.Single) {
                if (basepaths.get(2) != null) {
                    basepaths.set(2, null);
                    battingTeam.score++;
                }
                if (basepaths.get(1) != null) {
                    basepaths.set(2, basepaths.get(1));
                    basepaths.set(1, null);
                }
                if (basepaths.get(0) != null) {
                    basepaths.set(1, basepaths.get(0));
                }
                basepaths.set(0, battingTeam.players.get(battingTeam.orderPosition - 1));
                battingTeam.hits++;
            } else if (PAResult == PAOutcome.Double) {
                if (basepaths.get(2) != null) {
                    basepaths.set(2, null);
                    battingTeam.score++;
                }
                if (basepaths.get(1) != null) {
                    basepaths.set(1, null);
                    battingTeam.score++;
                }
                if (basepaths.get(0) != null) {
                    basepaths.set(2, basepaths.get(0));
                    basepaths.set(0, null);
                }
                basepaths.set(1, battingTeam.players.get(battingTeam.orderPosition - 1));
                battingTeam.hits++;
            } else if (PAResult == PAOutcome.Triple) {
                battingTeam.score = ScoreAllBases(battingTeam.score, basepaths);
                basepaths.set(2, battingTeam.players.get(battingTeam.orderPosition - 1));
                battingTeam.hits++;
            } else if (PAResult == PAOutcome.HomeRun) {
                battingTeam.score = ScoreAllBases(battingTeam.score, basepaths) + 1;
                battingTeam.hits++;
            } else {
                // The only case left to handle should be a walk, so this should handle that
                if (basepaths.get(0) != null) {
                    if (basepaths.get(1) != null) {
                        if (basepaths.get(2) != null) {
                            battingTeam.score++;
                        }
                        basepaths.set(2, basepaths.get(1));
                    }
                    basepaths.set(1, basepaths.get(0));
                }
                basepaths.set(0, battingTeam.players.get(battingTeam.orderPosition - 1));
            }
            // Uptick the batting order after the plate appearance resolves
             battingTeam.uptickOrderPosition();
        }
        return battingTeam;
    }

    // Stole this from StackOverflow
    public static String convertToMultiline(String orig)
    {
        return "<html>" + orig.replaceAll("\n", "<br/>") + "</html>";
    }

}

