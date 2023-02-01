import java.util.*;

public final class CommunityLSim {

  	ArrayList<Player> players;
  	int numPeeps;
  	Random random = new Random();
  	Game game = new Game();
  	ArrayList<Player> activePlayers = new ArrayList<>();
  //you will need to add more instance variables

  	public CommunityLSim(int numP) {
		numPeeps = numP;
		//create the players
  		players = new ArrayList<Player>();

		//generate a community of 30
		for (int i = 0; i < numPeeps; i++) {
			if (i < numPeeps/2.0)
				players.add(new Player(PlayerKind.POORLY_PAID, (float)(99+Math.random())));
			else
				players.add(new Player(PlayerKind.WELL_PAID, (float)(100.1+Math.random())));
		}
	}

	public int getSize() {
		return numPeeps;
	}

	public Player getPlayer(int i) {
		return players.get(i);
	}

	public void addPocketChange() {
	  for(Player player:players){
		  if(player.getKind() == PlayerKind.WELL_PAID){
			  player.addMoney(.1f);
		  }else{
			  player.addMoney(.03f);
		  }
	  }
	}

	private void reDoWhoPlays() {
	  this.activePlayers.clear();
	  randomUniqIndx(60,0,0);

	}

	/* generate some random indices for who might play the lottery 
		in a given range of indices */
 	public void randomUniqIndx(int numI, int startRange, int endRange) {
		 int amountOfActivePlayers = this.players.size()/2;
		 HashSet<Integer> currentPlayers = new HashSet<>(amountOfActivePlayers);

		 while(currentPlayers.size() < amountOfActivePlayers){
			 if(currentPlayers.size() < amountOfActivePlayers*(numI*.01)){
				 currentPlayers.add(this.random.nextInt(amountOfActivePlayers));
			 }else{
				 currentPlayers.add(this.random.nextInt(amountOfActivePlayers) + amountOfActivePlayers);
			 }
		 }

		for(Integer indexOfActivePlayer: currentPlayers){
			this.activePlayers.add(players.get(indexOfActivePlayer));
		}
	}

	public void simulateYears(int numYears) {
  		/*now simulate lottery play for some years */
  		for (int year=0; year < numYears; year++) {
			  this.reDoWhoPlays();
			  this.game.winningLotNumber();
			  this.addPocketChange();

			  float mostMoney=0.0f;
			  float leastMoney=200f;
			  // add code so that each member of the community who plays, plays
			  //right now just everyone updates their list of funds each year
			  for (Player p : this.activePlayers) {
				  p.playRandom();
				  p.addMoney(this.game.amountWon(this.game.matchingNumbers(p.getLotteryNumbers())));

				  int redistributionSection = random.nextInt(10);
				  Player scholarshipWinner;
				  if(redistributionSection < 7){
					  scholarshipWinner = players.get(random.nextInt(players.size()/2) + players.size()/2);
					  scholarshipWinner.addMoney(1);
				  }else {
					  scholarshipWinner = players.get(random.nextInt(players.size()/2));
					  scholarshipWinner.addMoney(1);
				  }

				  if(p.getMoney() < leastMoney){
					  leastMoney = p.getMoney();
				  }else if(p.getMoney() > mostMoney){
					  mostMoney = p.getMoney();
				  }

				  /*
				  if(this.game.amountWon(this.game.matchingNumbers(p.getLotteryNumbers())) == -1){
					  int redistributionSection = random.nextInt(10);
					  Player scholarshipWinner;
					  if(redistributionSection < 7){
 					  scholarshipWinner = players.get(random.nextInt(players.size()/2) + players.size()/2);
						  scholarshipWinner.addMoney(1);
					  }else {
						  scholarshipWinner = players.get(random.nextInt(players.size()/2));
						  scholarshipWinner.addMoney(1);
					  }
				  }
				  */
			  }
			  for(Player p : players){
				  p.updateMoneyEachYear();
			  }
			  System.out.printf("Year: %d, Largest Fund: %.2f, Least Fund: %.2f\n", year, mostMoney, leastMoney);

		} //years
  }

}
