package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

/**     будет предоставлять информацию из AdvertisementStorage в нужном нам виде */
public class StatisticAdvertisementManager {
    private AdvertisementStorage storage;
    private static StatisticAdvertisementManager instance;

    private StatisticAdvertisementManager(){
        storage = AdvertisementStorage.getInstance();
    }

    public static StatisticAdvertisementManager getInstance(){
        if (instance == null) instance = new StatisticAdvertisementManager();
        return instance;
    }

    public List<Advertisement> getAllActiveVideo(){
        List<Advertisement> result = new ArrayList<>();
        for (Advertisement ad : storage.list()) {
            if (ad.getHits() > 0) result.add(ad);
        }
        return result;
    }
    public List<Advertisement> getAllArchivedVideo(){
        List<Advertisement> result = new ArrayList<>();
        for (Advertisement ad : storage.list()) {
            if (ad.getHits() == 0) result.add(ad);
        }
        return result;
    }
}
