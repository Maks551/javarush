package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

/**
 * хранилище рекламных роликов
 */

public class AdvertisementStorage {
    private static AdvertisementStorage instance;
    private final List<Advertisement> videos;

    private AdvertisementStorage(){
        videos = new ArrayList<>();
        Object someContent = new Object();
//        add(new Advertisement(someContent, "1", 152, 3, 3 * 60));
//        add(new Advertisement(someContent, "2", 5, 2, 5 * 60));
//        add(new Advertisement(someContent, "3", 3, 2, 3 * 60));
//        add(new Advertisement(someContent, "4", 99, 10, 2 * 60));
//        add(new Advertisement(someContent, "А", 2000, 3, 6 * 60));
//        add(new Advertisement(someContent, "5", 150, 3, 3 * 60));
//        add(new Advertisement(someContent, "1First Video", 5000, 1, 3 * 60));
//        add(new Advertisement(someContent, "2Second Video", 100, 1, 15 * 60));
        add(new Advertisement(someContent, "четвертое видео", 400, 100, 10 * 60));
        add(new Advertisement(someContent, "First Video", 5000, 15, 3 * 60)); // 3 min
        add(new Advertisement(someContent, "second Video", 100, 6, 15 * 60)); //15 min
        add(new Advertisement(someContent, "Third Video", 400, 2, 10 * 60));   //10 min
    }
    public List<Advertisement> list(){
        return videos;
    }
    public void add(Advertisement advertisement){
        videos.add(advertisement);
    }

    public static AdvertisementStorage getInstance() {
        if (instance == null) instance = new AdvertisementStorage();
        return instance;
    }
}
