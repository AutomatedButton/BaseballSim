import java.util.ArrayList;

public class BaseballSim {
    // These are our teams
    static ArrayList<Hitter> TeamOne = new ArrayList<Hitter>();
    static ArrayList<Hitter> TeamTwo = new ArrayList<Hitter>();

    // Setting up the two teams; if I ever move on to non-uniform teams this will need to be amended
    static {
        for (int i = 0; i < 8; i++) {
            // Single, Double, and HR-rate are all based on the original question,
            // Walk-rate is based on the MLB league average walk-rate as of 4/29/2021, shortened to 3 decimal places
            TeamOne.add(i, new Hitter(0.300, 0.0, 0.0, 0.088));
            TeamTwo.add(i, new Hitter(0.0, 0.105, .095, 0.088));
        }
    }

    public static void main(String[] args){
        // These keep track of the batting order for the respective teams
        int teamOneOrderPosition = 1;
        int teamTwoOrderPosition = 1;
        // These keep track of the score for the respective teams
        int teamOneScore = 0;
        int teamTwoScore = 0;
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
                PAResult = simulatePA(TeamOne.get(teamOneOrderPosition - 1));
                // This if/else if monster handles the various outcomes of a plate appearance
                if (PAResult == PAOutcome.Out) {
                    outs++;
                } else if (PAResult == PAOutcome.Single) {
                    if (basepaths.get(2) != null) {
                        basepaths.set(2, null);
                        teamOneScore++;
                    }
                    if (basepaths.get(1) != null) {
                        basepaths.set(2, basepaths.get(1));
                        basepaths.set(1, null);
                    }
                    if (basepaths.get(0) != null) {
                        basepaths.set(1, basepaths.get(0));
                    }
                    basepaths.set(0, TeamOne.get(teamOneOrderPosition - 1));
                } else if (PAResult == PAOutcome.Double) {
                    if (basepaths.get(2) != null) {
                        basepaths.set(2, null);
                        teamOneScore++;
                    }
                    if (basepaths.get(1) != null) {
                        basepaths.set(1, null);
                        teamOneScore++;
                    }
                    if (basepaths.get(0) != null) {
                        basepaths.set(2, basepaths.get(0));
                        basepaths.set(0, null);
                    }
                    basepaths.set(1, TeamOne.get(teamOneOrderPosition - 1));
                } else if (PAResult == PAOutcome.Triple) {
                    teamOneScore = ScoreAllBases(teamOneScore, basepaths);
                    basepaths = ClearBases(basepaths);
                    basepaths.set(2, TeamOne.get(teamOneOrderPosition - 1));
                } else if (PAResult == PAOutcome.HomeRun) {
                    teamOneScore = ScoreAllBases(teamOneScore, basepaths) + 1;
                    basepaths = ClearBases(basepaths);
                } else {
                    // The only case left to handle should be a walk, so this should handle that
                    if (basepaths.get(0) != null) {
                        if (basepaths.get(1) != null) {
                            if (basepaths.get(2) != null) {
                                teamOneScore++;
                            }
                            basepaths.set(2, basepaths.get(1));
                        }
                        basepaths.set(1, basepaths.get(0));
                    }
                    basepaths.set(0, TeamOne.get(teamOneOrderPosition - 1));
                }
                // Uptick the batting order after the plate appearance resolves
                teamOneOrderPosition = UptickOrderPosition(teamOneOrderPosition);
            }
            outs = 0;
            basepaths = ClearBases(basepaths);
            // In the current build this is the middle of the inning

            if (inning == 9 && (teamTwoScore > teamOneScore)) {
                isGameOver = true;
                System.out.println("The game ended after 8 1/2 innings");
                break;
            }

            while (outs < 3) {
                PAResult = simulatePA(TeamTwo.get(teamTwoOrderPosition - 1));
                if (PAResult == PAOutcome.Out) {
                    outs++;
                } else if (PAResult == PAOutcome.Single) {
                    if (basepaths.get(2) != null) {
                        basepaths.set(2, null);
                        teamTwoScore++;
                    }
                    if (basepaths.get(1) != null) {
                        basepaths.set(2, basepaths.get(1));
                        basepaths.set(1, null);
                    }
                    if (basepaths.get(0) != null) {
                        basepaths.set(1, basepaths.get(0));
                    }
                    basepaths.set(0, TeamTwo.get(teamTwoOrderPosition - 1));
                } else if (PAResult == PAOutcome.Double) {
                    if (basepaths.get(2) != null) {
                        basepaths.set(2, null);
                        teamTwoScore++;
                    }
                    if (basepaths.get(1) != null) {
                        basepaths.set(1, null);
                        teamOneScore++;
                    }
                    if (basepaths.get(0) != null) {
                        basepaths.set(2, basepaths.get(0));
                        basepaths.set(0, null);
                    }
                    basepaths.set(1, TeamTwo.get(teamTwoOrderPosition - 1));
                } else if (PAResult == PAOutcome.Triple) {
                    teamTwoScore = ScoreAllBases(teamTwoScore, basepaths);
                    basepaths = ClearBases(basepaths);
                    basepaths.set(2, TeamTwo.get(teamTwoOrderPosition - 1));
                } else if (PAResult == PAOutcome.HomeRun) {
                    teamTwoScore = ScoreAllBases(teamTwoScore, basepaths) + 1;
                    basepaths = ClearBases(basepaths);
                } else {
                    // The only case left to handle should be a walk, so this should handle that
                    if (basepaths.get(0) != null) {
                        if (basepaths.get(1) != null) {
                            if (basepaths.get(2) != null) {
                                teamTwoScore++;
                            }
                            basepaths.set(2, basepaths.get(1));
                        }
                        basepaths.set(1, basepaths.get(0));
                    }
                    basepaths.set(0, TeamTwo.get(teamTwoOrderPosition - 1));
                }
                // Uptick the batting order after the plate appearance resolves
                teamTwoOrderPosition = UptickOrderPosition(teamTwoOrderPosition);
            }
            // Reset the outs & bases
            outs = 0;
            basepaths = ClearBases(basepaths);
            System.out.println("End of Inning " + inning + ": TeamOne: " + teamOneScore + " TeamTwo: " + teamTwoScore);
            if (inning >= 9 && teamOneScore != teamTwoScore) {
                isGameOver = false;
            }
            inning++;

        }
        System.out.println("Final: TeamOne: " + teamOneScore + ", TeamTwo: " + teamTwoScore);
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
