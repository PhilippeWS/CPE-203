import java.util.*;

class Player {
	private PlayerKind kind;
	private float money;
	private ArrayList<Float> moneyOverTime;
    Random random = new Random();
	private int red, green, blue;
	private HashSet<Integer> lotteryNumbers = new HashSet<>(5);

	//constructor
	public Player(PlayerKind pK, float startFunds) {
		kind = pK;
		money = startFunds;
		moneyOverTime = new ArrayList<Float>();
		moneyOverTime.add(startFunds);
		red = random.nextInt(100);
		green = random.nextInt(100);
		blue = random.nextInt(100);

		//overall blue tint to POORLY_PAID	
		if (kind == PlayerKind.WELL_PAID) {
			red += 100;
		} else {
			blue += 100;
		}
	}

	public int getR() { return red; }
	public int getG() { return green; }
	public int getB() { return blue; }

	public float getMoney() { return money; }
	public PlayerKind getKind() { return kind; }
	public ArrayList<Float> getFunds() { return moneyOverTime; }

	public HashSet<Integer> getLotteryNumbers() {return lotteryNumbers;}

	public void addMoney(float addedAmount){ this.money += addedAmount; }
	public void updateMoneyEachYear() {
		moneyOverTime.add(money);
	}

	public void playRandom(){
		this.lotteryNumbers.clear();
		while(this.lotteryNumbers.size()<5){
			this.lotteryNumbers.add(this.random.nextInt(42)+1);
		}
		//System.out.println(this.lotteryNumbers);

	}

}
