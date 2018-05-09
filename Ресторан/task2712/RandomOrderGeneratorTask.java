package com.javarush.task.task27.task2712;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomOrderGeneratorTask implements Runnable {
    private static List<Tablet> tablets;
    private int interval;

    public RandomOrderGeneratorTask(List<Tablet> tablets, int interval) {
        RandomOrderGeneratorTask.tablets = tablets;
        this.interval = interval;
    }

    @Override
    public void run() {
        Tablet tablet = tablets.get(ThreadLocalRandom.current().nextInt(tablets.size()));
        while (!Thread.currentThread().isInterrupted()) {
            try {
                tablet.createTestOrder();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
