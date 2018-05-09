package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.NoAvailableVideoEventDataRow;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet{
    final int number;
    private static Logger logger = Logger.getLogger(Tablet.class.getName());
    private LinkedBlockingQueue<Order> queue;

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Tablet(int number) {
        this.number = number;
    }

    public Order createOrder(){
        Order order = null;
        try {
            order = new Order(this);

            orderIsEmpty(order);

        } catch (NoVideoAvailableException e){
            logger.log(Level.INFO, "No video is available for the order " + order);
            if (order != null)
                StatisticManager.getInstance().register(new NoAvailableVideoEventDataRow(order.getTotalCookingTime()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        }
        return order;
    }

    private void orderIsEmpty(Order order) {
        if (!order.isEmpty()) {
            ConsoleHelper.writeMessage(order.toString());
            queue.add(order);
            new AdvertisementManager(order.getTotalCookingTime() * 60).processVideos();
        }
    }

    public void createTestOrder(){
        TestOrder testOrder = null;
        try {
            testOrder = new TestOrder(this);

            orderIsEmpty(testOrder);

        } catch (NoVideoAvailableException e){
            logger.log(Level.INFO, "No video is available for the order " + testOrder);
            if (testOrder != null)
                StatisticManager.getInstance().register(new NoAvailableVideoEventDataRow(testOrder.getTotalCookingTime()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        }
    }
    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
