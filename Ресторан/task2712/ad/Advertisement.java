package com.javarush.task.task27.task2712.ad;

/**
 * Рекламна об'ява
 */

public class Advertisement {
    private Object content; //video
    private String name;
    private long initialAmount;     //начальная сумма, стоимость рекламы в копейках
    private int hits;               //количество оплаченных показов
    private int duration;           //продолжительность в секундах
    private long amountPerOneDisplaying;    //стоимость одного показа рекламного объявления в копейках

    public Advertisement(Object content, String name, long initialAmount, int hits, int duration) {
        this.content = content;
        this.name = name;
        this.initialAmount = initialAmount;
        this.hits = hits;
        this.duration = duration;
        if (hits > 0) {
            amountPerOneDisplaying = initialAmount / hits;
        } else amountPerOneDisplaying = 0;
    }

    public String getName() {
        return name;
    }
    public int getDuration() {
        return duration;
    }
    public long getAmountPerOneDisplaying() {
        return amountPerOneDisplaying;
    }
    public void revalidate(){
        if (hits <= 0) throw new UnsupportedOperationException();
        hits--;
    }
    public int getHits(){
        return hits;
    }
}
