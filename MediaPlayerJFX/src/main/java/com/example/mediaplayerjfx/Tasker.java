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

        while (start != end) {
            if (isCancelled()) { break; }
            start = minuteCalc(start);
            updateValue(start);
            updateProgress(start, end);
            Thread.sleep(1000);
            start += 1;
        }
        return start;
    }
    public int minuteCalc (int number) {
        if (number == 60) return number = 100;
        else if (number == 160) return number = 200;
        else if (number == 260) return number = 300;
        return number;
    }
    public int[] getData() {
        return new int[]{this.start, this.end};
    }
}
