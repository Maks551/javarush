package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.*;

public class DirectorTablet {
    private StatisticManager statisticManager = StatisticManager.getInstance();
    private StatisticAdvertisementManager advertisementManager = StatisticAdvertisementManager.getInstance();

    public void printAdvertisementProfit(){     //какую сумму заработали на рекламе, сгруппировать по дням;
        Map<String, Long> map = statisticManager.getAllVideosWithStorage();
        double totalPrice = 0;
        for (Map.Entry<String, Long> pair : map.entrySet()) {
            String date = pair.getKey();
            double price = ((double) pair.getValue())/100;
            totalPrice += price;
            ConsoleHelper.writeMessage(date + " - " + String.format("%.2f", price));
        }
        if (map.size()>0) {
            ConsoleHelper.writeMessage("Total - " + String.format("%.2f", totalPrice));
            ConsoleHelper.writeMessage("");
        }
    }
    public void printCookWorkLoading(){         //загрузка (рабочее время) повара, сгруппировать по дням;
        Map<String, TreeMap<String, Integer>> map = statisticManager.getAllCooksWithStorage();
        for (Map.Entry<String, TreeMap<String, Integer>> pair : map.entrySet()) {
            ConsoleHelper.writeMessage(pair.getKey());
            for (Map.Entry<String, Integer> pair_2 : pair.getValue().entrySet()) {
                if (pair_2.getValue()>0) {
                    ConsoleHelper.writeMessage(String.format("%s - %d min", pair_2.getKey(), pair_2.getValue()));
                }
            }
            ConsoleHelper.writeMessage("");
        }
    }
    public void printActiveVideoSet(){          //список активных роликов и оставшееся количество показов по каждому;
        List<Advertisement> allVideo = new ArrayList<>(advertisementManager.getAllActiveVideo());
        comparator(allVideo);
        if (allVideo.size() > 0) {
            for (Advertisement ad : allVideo) {
                ConsoleHelper.writeMessage(ad.getName() + " - " + ad.getHits());
            }
            ConsoleHelper.writeMessage("");
        }
    }

    private void comparator(List<Advertisement> allVideo) {
        Comparator<Advertisement> comparator = new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        allVideo.sort(comparator);
    }

    public void printArchivedVideoSet(){        //список неактивных роликов (с оставшемся количеством показов равным нулю).
        ArrayList<Advertisement> allVideo = new ArrayList<>(advertisementManager.getAllArchivedVideo());
        comparator(allVideo);
        if (allVideo.size() > 0) {
            for (Advertisement ad : allVideo) {
                ConsoleHelper.writeMessage(ad.getName());
            }
            ConsoleHelper.writeMessage("");
        }
    }
}
