package me.lawrence.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Random;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    public static final int[] DICE = new int[] {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    private int userScore;
    private int userTurnScore;
    private int compScore;
    private int compTurnScore;
    private boolean turn = true;


    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getUserTurnScore() {
        return userTurnScore;
    }

    public void setUserTurnScore(int userTurnScore) {
        this.userTurnScore = userTurnScore;
    }

    public int getCompScore(){
        return compScore;
    }

    public void setCompScore(int compScore){
        this.compScore = compScore;
    }

    public int getCompTurnScore(){
        return compTurnScore;
    }

    public void setCompTurnScore(int compTurnScore){
        this.compTurnScore = compTurnScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void roll(View view) {
        int points = rollDice();
        if (points == 1){
            setUserTurnScore(0);
            updateText("You rolled a one!");
            buttonsEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 900);
        }else {
            int score = getUserTurnScore() + points;
            setUserTurnScore(score);
            updateText("You rolled a " + points);
        }
    }

    public void reset(View view){
        turn = false;
        setUserScore(0);
        setUserTurnScore(0);
        setCompScore(0);
        setCompTurnScore(0);
        updateText("");
        buttonsEnabled(true);
    }

    public void hold(View view){
        int turnScore = getUserTurnScore();
        setUserTurnScore(0);
        int totalScore = getUserScore() + turnScore;
        setUserScore(totalScore);
        updateText("You hold.");

        if (checkGame()) {
            buttonsEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 900);
        }
    }

    public void computerTurn(){
        buttonsEnabled(false);
        turn = true;
        int difference = getCompScore() - getUserScore();
        int remaining = 100 - getCompScore();
        boolean losing = false;
        if(getCompScore() <= getUserScore()){
            losing = true;
        }
        if((losing && getCompTurnScore() >= 10) || getCompTurnScore() >= 20 || (difference > 30 && getCompTurnScore() >= 10) || getCompTurnScore() >= remaining){
            int turnScore = getCompTurnScore();
            setCompTurnScore(0);
            int totalScore = getCompScore() + turnScore;
            setCompScore(totalScore);
            updateText("Computer holds. Your turn.");
            buttonsEnabled(true);
            turn = false;
        }else {
            int points = rollDice();
            if (points == 1) {
                setCompTurnScore(0);
                updateText("Computer rolled a one! Your turn.");
                buttonsEnabled(true);
                turn = false;
            } else {
                int score = getCompTurnScore() + points;
                setCompTurnScore(score);
                updateText("Computer rolled a " + points);
            }
        }

        if(checkGame() && turn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 900);
        }
    }

    public void updateText(String info){
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText("Your Score: " + userScore + "     Computer Score: " + compScore + "\nPlayer Turn Score: " + userTurnScore + "     Computer Turn Score: " + compTurnScore + "\n" + info);
    }

    public int rollDice(){
        ImageView diceView = (ImageView) findViewById(R.id.diceView);
        Random rand = new Random();

        int  diceNumber = rand.nextInt(6) + 1;

        diceView.setImageResource(DICE[diceNumber -1]);
        return diceNumber;
    }

    public boolean checkGame(){
        if (getCompScore() < 100 && getUserScore() < 100){
            return true;
        }else if(getUserScore()>=100){
            updateText("YOU WIN!");
            buttonsEnabled(false);
            return false;
        }else if(getCompScore()>=100){
            updateText("Computer wins. GAME OVER.");
            buttonsEnabled(false);
            return false;
        }
        return true;
    }
    public void buttonsEnabled(boolean check){
        Button hold_button = (Button) findViewById(R.id.hold_button);
        Button roll_button = (Button) findViewById(R.id.roll_button);
        hold_button.setEnabled(check);
        roll_button.setEnabled(check);
    }

}

