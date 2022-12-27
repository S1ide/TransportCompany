package org.example;

import org.example.dataset.PieDataSet;
import org.example.models.Carrier;
import org.example.models.Manager;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    public static Integer currentDay = 1;
    public static Integer countOfDays = 365;
    private static int nextOrderDay = 1;

    public static void main(String[] args) {
        runLifeCycle();
    }

    public static void runLifeCycle() {
        System.out.println("Начало жизненного цикла");
        Manager manager = new Manager();
        for (; currentDay < countOfDays; currentDay++) {

            Random random = new Random();
            int K = random.nextInt(100, 500);
            int L = random.nextInt(1, 15);
            manager.next();
            if (currentDay == nextOrderDay) {
                manager.receiveNewOrder(K, L);
                nextOrderDay = currentDay + random.nextInt(2, 8);
            }
        }

        JFrame jFrame = new JFrame();
        jFrame.add(new PieDataSet().createDemoPanel(manager.getAllCarriers().stream().map(Carrier::getCountOfOrders).collect(Collectors.toList())));
        jFrame.setSize(1280, 1024);
        jFrame.setVisible(true);
        manager.getAllCarriers().forEach(carrier -> System.out.printf("Число заказов: %d\t" +
                "Перевозчик: %d\n", carrier.getCountOfOrders(), carrier.getId()));
        manager.getAllCarriers().forEach(carrier -> System.out.printf("Число дней без заказов: %d\t Перевозчик: %d\n",
                carrier.getCountDaysWithoutOrders(), carrier.getId()));
        System.out.println(manager.getDaysWithoutOrders());
    }
}