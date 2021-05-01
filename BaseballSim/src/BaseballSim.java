import java.util.ArrayList;

public class BaseballSim {
    public static void main(String[] args){
        int AwayTeamWins = 0;
        int HomeTeamWins = 0;
        Hitter exampleSmallBaller = new Hitter(.300, 0.0, 0.0, 0.0, 0.088);
        Hitter exampleSlugger = new Hitter(0.0, .13, 0.0, 0.07, 0.088);

        //for (int j = 0; j < 1000000; j++){

        Team awayTeam = new Team(exampleSmallBaller);
        Team homeTeam = new Team(exampleSlugger);
            boolean isGameOver = false;
            int inning = 1;
            // This variable just keeps track of the outcome of the current plate appearance
            PAOutcome PAResult;
            // This is the main loop
            while (!isGameOver) {
            /* This while loop simulates a half inning with TeamOne hitting. This is probably
             the area of the code I'm least satisfied with, because it repeats itself a fair bit
             in the bottom half of the inning.*/
                awayTeam = SimulateHalfInning(awayTeam);

                if (inning == 9 && (homeTeam.score > awayTeam.score)) {
                    isGameOver = true;
                    System.out.println("The game ended after 8 1/2 innings");
                    break;
                }

                homeTeam = SimulateHalfInning(homeTeam);
                System.out.println("End of Inning " + inning + ": Away: " + awayTeam.score + " Home: " + homeTeam.score);
                if (inning >= 9 && (awayTeam.score != homeTeam.score)) {
                    isGameOver = true;
                }
                inning++;

            }
            System.out.println("Final: Away: " + awayTeam.score + ", Home: " + homeTeam.score);
            System.out.println("Hits: Away: " + awayTeam.hits + ", Home: " + homeTeam.hits);
            /*if (AwayTeamScore > HomeTeamScore) {
                AwayTeamWins++;
            } else {
                HomeTeamWins++;
            }
        }
        System.out.println("Team Small Ball Wins: " + AwayTeamWins);
        System.out.println("Team Slugger Wins: " + HomeTeamWins);*/
    }

    public static PAOutcome simulatePA(Hitter batter) {
        double rng = Math.random();
        // An idea to make the code more readable is to make that variable an accumulator if I expand this function
        //double everythingBeforeThis = 0.0;

        if (rng <= batter.walkRate) {
            return PAOutcome.Walk;
        } else if (rng <= batter.walkRate + batter.singleRate) {
            return PAOutcome.Single;
        } else if (rng <= batter.walkRate + batter.singleRate + batter.doubleRate) {
            return PAOutcome.Double;
        } else if (rng <= batter.walkRate + batter.singleRate + batter.doubleRate + batter.tripleRate) {
            return PAOutcome.Triple;
        } else if (rng <= batter.walkRate + batter.singleRate + batter.doubleRate + batter.tripleRate + batter.homeRunRate) {
            return PAOutcome.HomeRun;
        } else {
            return PAOutcome.Out;
        }

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

    public static Team SimulateHalfInning(Team team) {
        int outs = 0;
        ArrayList<Hitter> basepaths = new ArrayList<Hitter>();
        // This loop just sets up the basepaths
        for (int i = 0; i < 3; i++) {
            basepaths.add(null);
        }
        //System.out.println(basepaths);
        PAOutcome PAResult;

        while (outs < 3) {
            PAResult = simulatePA(team.players.get(team.orderPosition - 1));
            if (PAResult == PAOutcome.Out) {
                outs++;
            } else if (PAResult == PAOutcome.Single) {
                if (basepaths.get(2) != null) {
                    basepaths.set(2, null);
                    team.score++;
                }
                if (basepaths.get(1) != null) {
                    basepaths.set(2, basepaths.get(1));
                    basepaths.set(1, null);
                }
                if (basepaths.get(0) != null) {
                    basepaths.set(1, basepaths.get(0));
                }
                basepaths.set(0, team.players.get(team.orderPosition - 1));
                team.hits++;
            } else if (PAResult == PAOutcome.Double) {
                if (basepaths.get(2) != null) {
                    basepaths.set(2, null);
                    team.score++;
                }
                if (basepaths.get(1) != null) {
                    basepaths.set(1, null);
                    team.score++;
                }
                if (basepaths.get(0) != null) {
                    basepaths.set(2, basepaths.get(0));
                    basepaths.set(0, null);
                }
                basepaths.set(1, team.players.get(team.orderPosition - 1));
                team.hits++;
            } else if (PAResult == PAOutcome.Triple) {
                team.score = ScoreAllBases(team.score, basepaths);
                basepaths.set(2, team.players.get(team.orderPosition - 1));
                team.hits++;
            } else if (PAResult == PAOutcome.HomeRun) {
                team.score = ScoreAllBases(team.score, basepaths) + 1;
                team.hits++;
            } else {
                // The only case left to handle should be a walk, so this should handle that
                if (basepaths.get(0) != null) {
                    if (basepaths.get(1) != null) {
                        if (basepaths.get(2) != null) {
                            team.score++;
                        }
                        basepaths.set(2, basepaths.get(1));
                    }
                    basepaths.set(1, basepaths.get(0));
                }
                basepaths.set(0, team.players.get(team.orderPosition - 1));
            }
            // Uptick the batting order after the plate appearance resolves
            team.uptickOrderPosition();
        }
        return team;
    }

}
    //Maybe a method or two to deal with runners on third?

