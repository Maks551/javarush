package com.javarush.task.task27.task2712.statistic.event;

public enum EventType {
    COOKED_ORDER,           //повар приготовил заказ
    SELECTED_VIDEOS,        //выбрали набор видео-роликов для заказа
    NO_AVAILABLE_VIDEO      //нет ни одного видео-ролика, который можно показать во время приготовления заказа
}
