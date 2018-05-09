package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementManager {
    private final AdvertisementStorage storage;
    private int timeSeconds;
    private List<Advertisement> result;

    public AdvertisementManager(int timeSeconds) {
        storage = AdvertisementStorage.getInstance();
        result = new ArrayList<>();
        this.timeSeconds = timeSeconds;
    }
    public void processVideos(){
        List<Advertisement> list = storage.list();
        if (list.size()==0) throw new NoVideoAvailableException();
        if (list.size() > 1) {
            list.sort(((o1, o2) -> (int) (o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying())));
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i-1).getAmountPerOneDisplaying() == list.get(i).getAmountPerOneDisplaying()){
                    if (list.get(i-1).getDuration() < list.get(i).getDuration()){
                        Advertisement ad = list.get(i);
                        list.remove(i);
                        list.add(i-1, ad);
                    }
                }
            }
        }
        selectVideo(0, list, timeSeconds);
        if (result.size()==0) throw new NoVideoAvailableException();

        //регистрирация события "видео выбрано"
        registerVideos();
        // выводим на экран рекламу
        printVideos();
    }

    private void registerVideos() {
        long amount = 0;
        int totalDuration = 0;
        for (Advertisement ad : result) {
            amount += ad.getAmountPerOneDisplaying();
            totalDuration += ad.getDuration();
        }
        StatisticManager.getInstance().register(new VideoSelectedEventDataRow(result, amount, totalDuration));
    }

    private void printVideos() {
        for (Advertisement adv : result) {
            ConsoleHelper.writeMessage(String.format("%s is displaying... %d, %d",
                    adv.getName(), adv.getAmountPerOneDisplaying(),
                    adv.getAmountPerOneDisplaying() * 1000 / adv.getDuration()));
            adv.revalidate();
        }
    }

    private void selectVideo(int index, List<Advertisement> selected, int timeSeconds){
        int a = 0;
        for (int i = index; i < selected.size(); i++) {
            a = i + 1;
            if (selected.get(i).getHits() > 0 && selected.get(i).getDuration() <= timeSeconds){
                result.add(selected.get(i));
                break;
            }
        }
        if (a < selected.size()){
            selectVideo(a, selected, timeSeconds - result.get(result.size()-1).getDuration());
        }
    }
//
//    public void processVideos(){
//        if (storage.list().size() == 0) throw new NoVideoAvailableException();
//        // Отбираем все комбинации
//        getAllCombinations(resultList);
//
//        if (resultList.size() == 0) throw new NoVideoAvailableException();
//        if (resultList.size() > 1) {
//            // Отбираем комбинации, c max price
//            getListWithMaxPrice(resultList);
//            if (resultList.size() > 1) {
//                // Отбираем вариант, у которого суммарное время максимальное
//                getListWithMaxTime(resultList);
//                if (resultList.size() > 1) {
//                    // Отбираем вариант с минимальным количеством роликов
//                    getListWithMinCount(resultList);
//                }
//            }
//        }
//        // выводим на экран рекламу
//        for (Advertisement adv : resultList.get(0)) {
//            ConsoleHelper.writeMessage(String.format("%s is displaying... %d, %d",
//                    adv.getName(), adv.getAmountPerOneDisplaying(),
//                    adv.getAmountPerOneDisplaying() * 1000 / adv.getDuration()));
//            adv.revalidate();
//        }
//    }
//
//    // Перебор списка и возврат всех комбинаций - 1 шаг
//    private void getAllCombinations(List<List<Advertisement>> resultList) {
//        int N = storage.list().size();
//        for (int mask = 0; mask < (1 << N); mask++) {//перебор масок
//            List<Advertisement> list = new ArrayList<>();
//            for (int j = 0; j < N; j++) {//перебор индексов массива
//                if((mask & (1 << j)) != 0){//поиск индекса в маске
//                    if (storage.list().get(j).getHits() > 0 && storage.list().get(j).getAmountPerOneDisplaying() > 0)
//                        list.add(storage.list().get(j));
//                }
//            }
//            if (list.size()>1) {
//                getList(list);
//            }
//            if (getTime(list) > 0 && getTime(list) < timeSeconds) resultList.add(list);
//        }
//    }
//
//    private void getList(List<Advertisement> list){
//        for (int j = 0; j < list.size(); j++) {
//            for (int i = 1; i < list.size(); i++) {
//                if (list.get(i - 1).getAmountPerOneDisplaying() < list.get(i).getAmountPerOneDisplaying()) {
//                    Advertisement ad = list.get(i);
//                    list.remove(i);
//                    list.add(i - 1, ad);
//                }
//            }
//        }
//    }
//
//    // Вибір тих підбірок, які можливо показати за час приготування
//    private int getTime(List<Advertisement> list) {
//        int time = 0;
//        for (Advertisement aList : list) {
//            time += aList.getDuration();
//        }
//        return time;
//    }
//
//    // Вибір підбірок з max price
//    private void getListWithMaxPrice(List<List<Advertisement>> lists){
//        long maxPrice = getPriceWithOneList(lists.get(0));
//        for (int i = 1; i < lists.size(); i++) {
//            long price = getPriceWithOneList(lists.get(i));
//            maxPrice = maxPrice > price ? maxPrice : price;
//        }
//        for (int i = 0; i < lists.size(); ) {
//            if (getPriceWithOneList(lists.get(i)) < maxPrice) lists.remove(i);
//            else i++;
//        }
//    }
//
//    // Max price with one list
//    private long getPriceWithOneList(List<Advertisement> list){
//        long result = 0;
//        for (Advertisement ad : list) {
//            result += ad.getAmountPerOneDisplaying();
//        }
//        return result;
//    }
//
//    // Отбираем вариант, у которого суммарное время максимальное
//    private void getListWithMaxTime(List<List<Advertisement>> lists){
//        long maxDuration = getDurationWithOneList(lists.get(0));
//        for (int i = 1; i < lists.size(); i++) {
//            long duration = getDurationWithOneList(lists.get(i));
//            maxDuration = maxDuration > duration ? maxDuration : duration;
//        }
//        for (int i = 0; i < lists.size(); ) {
//            if (getDurationWithOneList(lists.get(i)) < maxDuration) lists.remove(i);
//            else i++;
//        }
//    }
//    // Max duration with one list
//    private long getDurationWithOneList(List<Advertisement> list){
//        long result = 0;
//        for (Advertisement ad : list) {
//            result += ad.getDuration();
//        }
//        return result;
//    }
//
//    // Отбираем вариант с минимальным количеством роликов
//    private void getListWithMinCount(List<List<Advertisement>> lists){
//        int minSize = lists.get(0).size();
//        for (int i = 1; i < lists.size(); i++) {
//            int size = lists.get(i).size();
//            minSize = minSize < size ? minSize : size;
//        }
//        for (int i = 0; i < lists.size(); ) {
//            if (lists.get(i).size() > minSize) lists.remove(i);
//            else i++;
//        }
//    }
}
