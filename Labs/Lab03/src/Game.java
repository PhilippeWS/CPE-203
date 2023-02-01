import java.util.HashSet;
import java.util.Random;

public class Game {
    Random random = new Random();
    private HashSet<Integer> winningLotteryNumbers = new HashSet<>(5);

    public HashSet<Integer> getWinningLotteryNumbers() {return winningLotteryNumbers;}

    public void winningLotNumber(){
        this.winningLotteryNumbers.clear();
        while(this.winningLotteryNumbers.size()<5){
            this.winningLotteryNumbers.add(this.random.nextInt(42)+1);
        }
        //System.out.println("Winning lotto Numbers: " + this.winningLotteryNumbers);
    }

    public int matchingNumbers(HashSet<Integer> playerNumbers){
        int numberOfMatchingNumbers = 0;
        for(Integer lottoNumber : winningLotteryNumbers){
            if (playerNumbers.contains(lottoNumber)){
                numberOfMatchingNumbers++;

            }
        }
        return numberOfMatchingNumbers;
    }

    public float amountWon(int numberOfMatchingNumbers){
        float moneyWon = -1.0f;

        if(numberOfMatchingNumbers == 2){
            moneyWon = 1.0f;
        }else if(numberOfMatchingNumbers == 3){
            moneyWon = 10.86f;
        }else if (numberOfMatchingNumbers == 4){
            moneyWon = 197.53f;
        }else if(numberOfMatchingNumbers == 5){
            moneyWon = 212534.83f;
        }
        return moneyWon;
    }

}

