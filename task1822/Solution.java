package com.javarush.task.task18.task1822;

/* 
Поиск данных внутри файла
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader(reader.readLine()));

        while (br.ready()){
            String [] num = br.readLine().split(" ");
            for (int i = 0; i<num.length; i++) {
                if (num [i].equals(args[0])){
                    System.out.print(num[i]+" ");
                    for (int j = i+1; j < num.length; j++) {
                        try{
                            double d = Double.parseDouble(num[j]);
                            System.out.print(num[j] + " " + num[j+1]);
                            break;
                        } catch (Exception e){
                            System.out.print(num[j]+" ");
                        }
                    }
                }
            }
        }
        reader.close();
        br.close();
    }
}
