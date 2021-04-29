import java.util.ArrayList;

public class BaseballSim {
    // These are our teams
    static ArrayList<Hitter> AwayTeam = new ArrayList<Hitter>();
    static ArrayList<Hitter> HomeTeam = new ArrayList<Hitter>();

    // Setting up the two teams; if I ever move on to non-uniform teams this will need to be amended
    static {
        for (int i = 0; i < 9; i++) {
            // Single, Double, and HR-rate are all based on the original question.
            // Walk-rate is based on the MLB league average walk-rate as of 4/29/2021, shortened to 3 decimal places
            AwayTeam.add(i, new Hitter(0.300, 0.0, 0.0, 0.088));
            HomeTeam.add(i, new Hitter(0.0, 0.13, .07, 0.088));
        }
    }

    public static void main(String[] args){
        int AwayTeamWins = 0;
        int HomeTeamWins = 0;

        //for (int j = 0; j < 1000000; j++){
            // These keep track of the batting order for the respective teams
            int AwayTeamOrderPosition = 1;
            int HomeTeamOrderPosition = 1;
            // These keep track of the score for the respective teams
            int AwayTeamScore = 0;
            int HomeTeamScore = 0;
            // These keep track of the number of hits each team well... hits
            int AwayTeamHits = 0;
            int HomeTeamHits = 0;
            // These keep track of the outs in a half-inning and the basepaths
            int outs = 0;
            boolean isGameOver = false;
            int inning = 1;
            ArrayList<Hitter> basepaths = new ArrayList<Hitter>();
            // This loop just sets up the basepaths
            for (int i = 0; i < 3; i++) {
                basepaths.add(null);
            }
            // This variable just keeps track of the outcome of the current plate appearance
            PAOutcome PAResult;
            // This is the main loop
            while (!isGameOver) {
            /* This while loop simulates a half inning with TeamOne hitting. This is probably
             the area of the code I'm least satisfied with, because it repeats itself a fair bit
             in the bottom half of the inning.*/
                while (outs < 3) {
                    // Simulates a plate appearance for the current batter
                    PAResult = simulatePA(AwayTeam.get(AwayTeamOrderPosition - 1));
                    // This if/else if monster handles the various outcomes of a plate appearance
                    if (PAResult == PAOutcome.Out) {
                        outs++;
                    } else if (PAResult == PAOutcome.Single) {
                        if (basepaths.get(2) != null) {
                            basepaths.set(2, null);
                            AwayTeamScore++;
                        }
                        if (basepaths.get(1) != null) {
                            basepaths.set(2, basepaths.get(1));
                            basepaths.set(1, null);
                        }
                        if (basepaths.get(0) != null) {
                            basepaths.set(1, basepaths.get(0));
                        }
                        basepaths.set(0, AwayTeam.get(AwayTeamOrderPosition - 1));
                        AwayTeamHits++;
                    } else if (PAResult == PAOutcome.Double) {
                        if (basepaths.get(2) != null) {
                            basepaths.set(2, null);
                            AwayTeamScore++;
                        }
                        if (basepaths.get(1) != null) {
                            basepaths.set(1, null);
                            AwayTeamScore++;
                        }
                        if (basepaths.get(0) != null) {
                            basepaths.set(2, basepaths.get(0));
                            basepaths.set(0, null);
                        }
                        basepaths.set(1, AwayTeam.get(AwayTeamOrderPosition - 1));
                        AwayTeamHits++;
                    } else if (PAResult == PAOutcome.Triple) {
                        AwayTeamScore = ScoreAllBases(AwayTeamScore, basepaths);
                        basepaths = ClearBases(basepaths);
                        basepaths.set(2, AwayTeam.get(AwayTeamOrderPosition - 1));
                        AwayTeamHits++;
                    } else if (PAResult == PAOutcome.HomeRun) {
                        AwayTeamScore = ScoreAllBases(AwayTeamScore, basepaths) + 1;
                        basepaths = ClearBases(basepaths);
                        AwayTeamHits++;
                    } else {
                        // The only case left to handle should be a walk, so this should handle that
                        if (basepaths.get(0) != null) {
                            if (basepaths.get(1) != null) {
                                if (basepaths.get(2) != null) {
                                    AwayTeamScore++;
                                }
                                basepaths.set(2, basepaths.get(1));
                            }
                            basepaths.set(1, basepaths.get(0));
                        }
                        basepaths.set(0, AwayTeam.get(AwayTeamOrderPosition - 1));
                    }
                    // Uptick the batting order after the plate appearance resolves
                    AwayTeamOrderPosition = UptickOrderPosition(AwayTeamOrderPosition);
                }
                outs = 0;
                basepaths = ClearBases(basepaths);
                // In the current build this is the middle of the inning

                if (inning == 9 && (HomeTeamScore > AwayTeamScore)) {
                    isGameOver = true;
                    System.out.println("The game ended after 8 1/2 innings");
                    break;
                }

                while (outs < 3) {
                    PAResult = simulatePA(HomeTeam.get(HomeTeamOrderPosition - 1));
                    if (PAResult == PAOutcome.Out) {
                        outs++;
                    } else if (PAResult == PAOutcome.Single) {
                        if (basepaths.get(2) != null) {
                            basepaths.set(2, null);
                            HomeTeamScore++;
                        }
                        if (basepaths.get(1) != null) {
                            basepaths.set(2, basepaths.get(1));
                            basepaths.set(1, null);
                        }
                        if (basepaths.get(0) != null) {
                            basepaths.set(1, basepaths.get(0));
                        }
                        basepaths.set(0, HomeTeam.get(HomeTeamOrderPosition - 1));
                        HomeTeamHits++;
                    } else if (PAResult == PAOutcome.Double) {
                        if (basepaths.get(2) != null) {
                            basepaths.set(2, null);
                            HomeTeamScore++;
                        }
                        if (basepaths.get(1) != null) {
                            basepaths.set(1, null);
                            AwayTeamScore++;
                        }
                        if (basepaths.get(0) != null) {
                            basepaths.set(2, basepaths.get(0));
                            basepaths.set(0, null);
                        }
                        basepaths.set(1, HomeTeam.get(HomeTeamOrderPosition - 1));
                        HomeTeamHits++;
                    } else if (PAResult == PAOutcome.Triple) {
                        HomeTeamScore = ScoreAllBases(HomeTeamScore, basepaths);
                        basepaths = ClearBases(basepaths);
                        basepaths.set(2, HomeTeam.get(HomeTeamOrderPosition - 1));
                        HomeTeamHits++;
                    } else if (PAResult == PAOutcome.HomeRun) {
                        HomeTeamScore = ScoreAllBases(HomeTeamScore, basepaths) + 1;
                        basepaths = ClearBases(basepaths);
                        HomeTeamHits++;
                    } else {
                        // The only case left to handle should be a walk, so this should handle that
                        if (basepaths.get(0) != null) {
                            if (basepaths.get(1) != null) {
                                if (basepaths.get(2) != null) {
                                    HomeTeamScore++;
                                }
                                basepaths.set(2, basepaths.get(1));
                            }
                            basepaths.set(1, basepaths.get(0));
                        }
                        basepaths.set(0, HomeTeam.get(HomeTeamOrderPosition - 1));
                    }
                    // Uptick the batting order after the plate appearance resolves
                    HomeTeamOrderPosition = UptickOrderPosition(HomeTeamOrderPosition);
                }
                // Reset the outs & bases
                outs = 0;
                basepaths = ClearBases(basepaths);
                System.out.println("End of Inning " + inning + ": Away: " + AwayTeamScore + " Home: " + HomeTeamScore);
                if (inning >= 9 && (AwayTeamScore != HomeTeamScore)) {
                    isGameOver = true;
                }
                inning++;

            }
            System.out.println("Final: Away: " + AwayTeamScore + ", Home: " + HomeTeamScore);
            System.out.println("Hits: Away: " + AwayTeamHits + ", Home: " + HomeTeamHits);
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
        } else if (rng <= batter.walkRate + batter.singleRate + batter.doubleRate + batter.homeRunRate) {
            return PAOutcome.HomeRun;
        } else {
            return PAOutcome.Out;
        }

    }

    public static int UptickOrderPosition(int OrderPosition){
        if (OrderPosition >= 9) {
            return 1;
        }
        return OrderPosition + 1;
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

    public static ArrayList<Hitter> ClearBases(ArrayList<Hitter> bases) {
        if (bases.size() != 3) {
            throw new IllegalArgumentException("The provided bases don't have the right number of bases");
        }
        for (int i = 0; i < 3; i++) {
            bases.set(i, null);
        }
        return bases;
    }
    //Maybe a method or two to deal with runners on third?
}
