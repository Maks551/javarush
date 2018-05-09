package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }
    public static String readString() throws IOException {
        return reader.readLine();
    }
    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> dishes = new ArrayList<>();
        writeMessage(String.format("Виберіть страву з %s", Dish.allDishesToString()));
        String read = readString();
        while (!read.equalsIgnoreCase("exit")){
            try {
                dishes.add(Dish.valueOf(read));
                writeMessage("Страву додано. Щось ще?");
            } catch (IllegalArgumentException e) {
                writeMessage("Такої страви нема. Спробуєте знову");
            }
            read = readString();
        }
        return dishes;
    }
}
