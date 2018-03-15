package com.javarush.task.task18.task1827;

/* 
Прайсы
CrUD для таблицы внутри файла.
Считать с консоли имя файла для операций CrUD.

Программа запускается со следующим набором параметров:
-c productName price quantity

Значения параметров:
где id - 8 символов.
productName - название товара, 30 chars (60 bytes).
price - цена, 8 символов.
quantity - количество, 4 символа.
-c - добавляет товар с заданными параметрами в конец файла, генерирует id самостоятельно, инкрементируя максимальный id, найденный в файле.

В файле данные хранятся в следующей последовательности (без разделяющих пробелов):
id productName price quantity

Данные дополнены пробелами до их длины.

Пример:
19846   Шорты пляжные синие           159.00  12
198478  Шорты пляжные черные с рисунко173.00  17
19847983Куртка для сноубордистов, разм10173.991234


Требования:
1. Программа должна считать имя файла для операций CrUD с консоли.
2. При запуске программы без параметров список товаров должен остаться неизменным.
3. При запуске программы с параметрами "-c productName price quantity" в конец файла должна добавится новая строка с товаром.
4. Товар должен иметь следующий id, после максимального, найденного в файле.
5. Форматирование новой строки товара должно четко совпадать с указанным в задании.
6. Созданные для файлов потоки должны быть закрыты.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {
    public static void main(String[] args) throws Exception {
        List<Integer> number = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        reader.close();
        BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
        while (fileReader.ready()){
            String [] s = fileReader.readLine().split("");
            StringBuilder sbId = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                if (!s[i].equals(" ")){
                    sbId.append(s[i]);
                }
            }
            number.add(Integer.parseInt(String.valueOf(sbId)));
        }
        fileReader.close();
        Collections.sort(number);
        StringBuffer c = new StringBuffer();
        int id;                 //8
        String productName;     //30
        double price;           //8
        int quantity;           //4
        if (args[0].equals("-c")){
            FileWriter bw = new FileWriter(fileName, true);
            id = number.get(number.size()-1)+1;
            String id_final = getId(id);
            productName = args[1];
            String productName_final = getProductName(productName);
            price = Double.parseDouble(args[2]);
            String price_final = getPrise(price);
            quantity = Integer.parseInt(args[3]);
            String quantity_final = getQuantity(quantity);
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append(id_final).append(productName_final).append(price_final).append(quantity_final);
            bw.write(String.valueOf(sb));
            bw.close();

        }
    }
    private static String getId(int id){
        String s1 = String.valueOf(id);
        if (s1.length()>8) return s1.substring(0,8);

        StringBuilder s = new StringBuilder();
        s.append(id);
        while (true){
            if (s.length()<8){
                s.append(" ");
            } else break;
        }
        return String.valueOf(s);
    }

    private static String getProductName(String productName){
        if (productName.length()>30) return productName.substring(0,30);
        StringBuilder s = new StringBuilder();
        s.append(productName);
        while (true){
            if (s.length()<30) s.append(" ");
            else break;
        }
        return String.valueOf(s);
    }

    private static String getPrise(double prise){
        String result;
        String s1 = String.valueOf(prise);
        if (prise>99999) result = s1.substring(0,8);
        else if (s1.length()>8) result = s1.substring(0,8);
        else {
            StringBuilder s = new StringBuilder();
            s.append(prise);
            while (true) {
                if (s.length() < 8) s.append(" ");
                else break;
            }
            result = String.valueOf(s);
        }
        return result;
    }

    private static String getQuantity(int quantity){
        if (quantity>9999) return String.valueOf(9999);

        StringBuilder s = new StringBuilder();
        s.append(quantity);
        while (true){
            if (s.length()<4){
                s.append(" ");
            } else break;
        }
        return String.valueOf(s);
    }

}
