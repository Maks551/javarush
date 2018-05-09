package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TestOrder extends Order {

    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() {
        dishes = new ArrayList<>();
        int valueDishes = ThreadLocalRandom.current().nextInt(10);
        for(int i = 0; i< valueDishes; i++){
            int num = ThreadLocalRandom.current().nextInt(Dish.values().length);
            dishes.add(Dish.values()[num]);
        }
    }
}
