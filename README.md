# BaseballSim

This project started as a thought experiment: 
Which team would be better; A team of .300 hitters that only hit singles, or a team of .200 hitters that exclusively hit for extra bases?

I ended up making this simple baseball simulation to test it out, and with a small sample size (9 games) the .200 hitters won 5 of those games.

## Restrictions/Simplifications

Baseball is a complicated game, and while I will do my best to refine this game to capture some more of its complexities, I made a lot of simplifications to the game to get this simulation up and running as soon as possible.

### Outs

Right now the only type of out in the game is a strikeout, which has a knock-on effect that reduces a lot of the baserunning complexity in the game. This means that among other things, players will never ground into a double/triple play, hit a sacrifice fly, or get picked off on the basepaths.

### Baserunning

Along with the things mentioned in the previous section, players will never attempt to steal a base or tag up on a deep fly ball. However the most important things to mention with regard to baserunning is that players will never get thrown out on the basepaths, but they will never try to get more bases than the batter. So if a player hits a single, all of the baserunners will advance only one base, even though there are situations in baseball which allow for runners to advance multiple bases on a single.

### Pitching

Every pitcher on both teams is assumed to be completely average and unremarkable so that every player can hit their average over time. That said these pitchers will never throw a wild pitch or hit a batter.

### Hitting

In this simulation's current form, I am only keeping track of the outcome of the plate appearance, so I did not simulate where a ball was hit if a batter hit a single. Also, a batter will never try to bunt a ball.

### Fielding

The fielding team is assumed to never make errors.

### Extra Innings

The current iteration of this simulation does not account for extra innings, though this is something that I hope to fix soon, as it should be a simple fix.

### The Count

Because this simulation was created to test the abilities of hitters, I only really cared about the outcome of the plate appearance, and not how it progressed. So there is no mechanism in this project to keep track of the count.

### Team Construction

Right now the teams are entirely uniform, so every player on the same team is essentially the same player. This shouldn't be too difficult to fix, but that is how the simulation currently works.
