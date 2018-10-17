package cc.purplepopo.expression;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

public class AnsweringQuestions {

    private long hour,minute,second;
    private long delay;
    private Label timerText;
    private Timer showtimer;
    private VBox textVBox;

    class MyTask extends TimerTask{
        @Override
        public void run() {
            if (delay>0){
                delay--;
                hour= delay / 60 / 60 % 60;
                minute = delay / 60 % 60;
                second = delay % 60;
                setTime(hour,minute,second);
            }else{
                setTime(hour,minute,second);
                textVBox.setDisable(true);
                AnsweringQuestions.this.showtimer.cancel();
                AnsweringQuestions.this.showtimer.purge();
            }
        }
    };
    public AnsweringQuestions() {
        hour=1;minute=0;second=0;delay = 3600;
    }

    public void setTimer(int hour, int minute, int second) {
        this.hour=hour;this.minute=minute;this.second=second;
        delay=(hour*3600+minute*60+second);
    }
    public void setTime(long hour,long minute,long second){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timerText.setText(hour+":"+minute+":"+second);
            }
        });
    }
    public void startTimer(Label timerText) {
        this.timerText= timerText;
        this.showtimer = new Timer();
        showtimer.schedule(new MyTask(),0,1000);
    }

    public void stopTimer() {
        showtimer.cancel();
        showtimer.purge();
    }
    public void setVBox(VBox textVBox){
        this.textVBox = textVBox;
    }
}
