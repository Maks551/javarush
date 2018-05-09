package com.javarush.task.task27.task2712.statistic.event;

import java.util.Date;

/**    По нему мы определяем, является ли переданный объект событием или нет.*/
public interface EventDataRow {
    EventType getType();
    Date getDate();     // вернет дату создания записи
    int getTime();      // вернет время - продолжительность
}
