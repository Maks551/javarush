package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cook implements Runnable {
    private LinkedBlockingQueue<Order> queue;
    private LinkedBlockingQueue<Order> queueOfCookedOrders;
    private String name;
    private boolean busy;
    private Waiter waiter;

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public boolean isBusy() {
        return busy;
    }

    public Cook(String name) {
        this.name = name;
        queueOfCookedOrders = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!this.isBusy() && !queue.isEmpty()) this.startCookingOrder(queue.poll());

                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private Lock lock = new ReentrantLock();
    public void startCookingOrder(Order order) throws InterruptedException {
        busy = true;
        lock.lock();
        int time = order.getTotalCookingTime();
        StatisticManager.getInstance().register(
                new CookedOrderEventDataRow(order.getTablet().toString(), name, time, order.getDishes()));
        ConsoleHelper.writeMessage("Start cooking - " + order + ", cooking time " + time + "min");
        Thread.sleep(time * 10);
        queueOfCookedOrders.add(order);
        waiter.setCookCook(this);
        waiter.addQueue(order);
        busy = false;
        lock.unlock();
    }
}
