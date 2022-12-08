package com.example.mediaplayerjfx;
import javafx.concurrent.Task;

public class Tasker extends Task<Integer> {

    private final int end;
    private int start;
    public Tasker(int start, int end) {
        this.end = end;
        this.start = start;
    }

    @Override
    protected Integer call() throws Exception {
        int start1 = minuteCalc(start); // used for text view on the root
        while (start != end) {
            if (isCancelled()) { break; }
            updateValue(start1);
            updateProgress(start, end);
            Thread.sleep(1000);
            start += 1;
            start1 += 1; // used for calculating time left
        }
        return start;
    }
    public int minuteCalc (int number) {
        if (number == 60) return 100;
        else if (number == 160) return 200;
        else if (number == 260) return 300;
        return number;
    }
    public int[] getData() {
        return new int[] {this.start, this.end};
    }
}
