package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.concurrent.LinkedBlockingQueue;

public class Waiter implements Runnable {
    private LinkedBlockingQueue<Order> queueOfCookedOrders;
    private boolean busyWaiter;
    private Cook cook;

    public Waiter(){
        queueOfCookedOrders = new LinkedBlockingQueue<>();
    }

    public void addQueue(Order order){
        queueOfCookedOrders.add(order);
    }

    public void setCookCook(Cook cook){
        this.cook = cook;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!busyWaiter && !queueOfCookedOrders.isEmpty()){
                    this.startDeliveryOrder(queueOfCookedOrders.poll());
                }
                Thread.sleep(5);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    public void startDeliveryOrder(Order order) throws InterruptedException {
        busyWaiter = true;
        ConsoleHelper.writeMessage(order + " was cooked by " + cook);
        Thread.sleep(10);
        busyWaiter = false;
    }
}
