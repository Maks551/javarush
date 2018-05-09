package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private final static int ORDER_CREATING_INTERVAL = 100;
    private final static LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args){
        Locale.setDefault(Locale.ENGLISH);

        DirectorTablet directorTablet = new DirectorTablet();
        Waiter waiter = new Waiter();

        String[] cooksName = new String[]{"Professor", "Superman", "Strongman"};
        List<Cook> cooks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cook cook = new Cook(cooksName[i]);
            cook.setQueue(orderQueue);
            cook.setWaiter(waiter);
            cooks.add(cook);
        }

        List<Tablet> tablets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.setQueue(orderQueue);
            tablets.add(tablet);
        }
        RandomOrderGeneratorTask task = new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL);
        Thread thread = new Thread(task);
        for (Cook cook : cooks) {
            Thread thread1 = new Thread(cook);
            thread1.setDaemon(true);
            thread1.start();
        }

        Thread threadWaiter = new Thread(waiter);
        thread.start();
        threadWaiter.start();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkLoading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        threadWaiter.interrupt();

//        Tablet tablet_1 = new Tablet(5);
//        Cook amigo = new Cook("Amigo");
//        Waiter waiter = new Waiter();
//        amigo.addObserver(waiter);
//        tablet_1.addObserver(amigo);
//        tablet_1.createOrder();
//        DirectorTablet directorTablet_1 = new DirectorTablet();
//        directorTablet_1.printAdvertisementProfit();
//        directorTablet_1.printCookWorkLoading();
//        directorTablet_1.printActiveVideoSet();
//        directorTablet_1.printArchivedVideoSet();
//        Cook cook2 = new Cook("mAmigo");
//        Tablet tablet2 = new Tablet(5);
//        Waiter waiter2 = new Waiter();
//        cook2.addObserver(waiter2);
//        tablet2.addObserver(cook2);
//        tablet2.createOrder();
//        directorTablet_1 = new DirectorTablet();
//        directorTablet_1.printAdvertisementProfit();
//        directorTablet_1.printCookWorkLoading();
//        directorTablet_1.printActiveVideoSet();
//        directorTablet_1.printArchivedVideoSet();
//        Cook cook3 = new Cook("Am");
//        Tablet tablet3 = new Tablet(5);
//        Waiter waiter3 = new Waiter();
//        cook3.addObserver(waiter3);
//        tablet3.addObserver(cook3);
//        tablet3.createOrder();
//        directorTablet_1 = new DirectorTablet();
//        directorTablet_1.printAdvertisementProfit();
//        directorTablet_1.printCookWorkLoading();
//        directorTablet_1.printActiveVideoSet();
//        directorTablet_1.printArchivedVideoSet();
    }
}
