package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * С его помощью будем регистрировать события в хранилище.
 */
public class StatisticManager {
    private StatisticStorage statisticStorage;
    private static StatisticManager instance;
//    private Set<Cook> cooks;
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

//    public Set<Cook> getCooks(){
//        return cooks;
//    }
    private StatisticManager() {
        statisticStorage = new StatisticStorage();
//        cooks = new HashSet<>();
    }

    public static StatisticManager getInstance() {
        if (instance == null) instance = new StatisticManager();
        return instance;
    }

//    public void register(Cook cook) {
//        cooks.add(cook);
//    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    //База
    private class StatisticStorage {
        Map<EventType, List<EventDataRow>> storage;

        public StatisticStorage() {
            storage = new HashMap<>();
            for (EventType type : EventType.values()) {
                storage.put(type, new ArrayList<>());
            }
        }

        public Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
        private void put(EventDataRow data) {
            storage.get(data.getType()).add(data);
        }
    }

    public Map<String, Long> getAllVideosWithStorage(){
        Map<String, Long> resultMap = new TreeMap<>(Collections.reverseOrder());

        long resultPrice = 0;
        for (EventDataRow event : statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS)) {
            VideoSelectedEventDataRow videoRaw = (VideoSelectedEventDataRow) event;
            List<Advertisement> list = videoRaw.getOptimalVideoSet();

            long allPriceWithDay = 0;
            for (Advertisement ad : list) {
                allPriceWithDay += ad.getAmountPerOneDisplaying();
            }
            String date = DATE_FORMAT.format(videoRaw.getDate());

            if (resultMap.containsKey(date)){
                resultPrice += allPriceWithDay;
            } else {
                resultPrice = allPriceWithDay;
            }
            resultMap.put(date, resultPrice);
        }
        return resultMap;
    }

    public Map<String, TreeMap<String, Integer>> getAllCooksWithStorage(){
        TreeMap<String, TreeMap<String, Integer>> resultMap = new TreeMap<>(Collections.reverseOrder());

        for (EventDataRow event : statisticStorage.getStorage().get(EventType.COOKED_ORDER)) {
            CookedOrderEventDataRow order = (CookedOrderEventDataRow) event;
            String date = DATE_FORMAT.format(order.getDate());

            if (!resultMap.containsKey(date)) {
                TreeMap<String, Integer> workPerCookByDay = new TreeMap<>();
                workPerCookByDay.put(order.getCookName(), order.getTime());
                resultMap.put(date, workPerCookByDay);
            } else {
                TreeMap<String, Integer> workPerCookByDay = resultMap.get(date);
                if (!workPerCookByDay.containsKey(order.getCookName())) {
                    workPerCookByDay.put(order.getCookName(), order.getTime());
                } else {
                    workPerCookByDay.put(order.getCookName(), workPerCookByDay.get(order.getCookName()) + order.getTime());
                }
            }
        }
        return resultMap;
    }
}
