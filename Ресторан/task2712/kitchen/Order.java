package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;
    private Lock lock;

    public Order(Tablet tablet) throws IOException {
        initDishes();
        this.tablet = tablet;
        lock = new ReentrantLock();
    }
    public int getTotalCookingTime(){
        lock.lock();
        int time = 0;
        for (Dish dish : dishes) {
            time += dish.getDuration();
        }
        lock.unlock();
        return time;
    }

    public Tablet getTablet() {
        return tablet;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public boolean isEmpty(){
        return dishes.size() == 0;
    }

    @Override
    public String toString() {
        if (dishes.size() == 0) return "";
        return "You order: " + dishes.toString() + " of " + tablet.toString();
    }

    protected void initDishes() throws IOException {
        dishes = ConsoleHelper.getAllDishesForOrder();
    }
}
