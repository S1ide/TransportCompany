package org.example.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Main.currentDay;


public class Manager {


    private final ArrayList<Carrier> allCarriers;
    private boolean isOfferReady = false;
    private int timeManagerProcessesIsReady;
    private int K;
    private int L;

    public Manager() {
        allCarriers = Carrier.generateCarriers(6);
    }

    public void receiveNewOrder(int K, int L) {
        System.out.printf("Получен новый заказ.\tЧисло товаров: %d\tРасстояние: %d\n", K, L);
        sendOrderForCarriers(); //рассылка заказов перевозчикам
        this.K = K;
        this.L = L;
    }

    public void next() {
        System.out.printf("День номер: %d\n", currentDay);
        refresh(); //обновление статусов менеджера и перевозчиков
        List<Carrier> carriersWithOffers = allCarriers.stream().filter(Carrier::isAvailableForOder)
                .filter(Carrier::isOfferReady).collect(Collectors.toList()); //получение перевозчиков с предложениями
        if (carriersWithOffers.size() != 0 && !isOfferReady) timeManagerProcessesIsReady = currentDay + 1; //обработка предложений, если есть таковые
        if (isOfferReady) { //если менеджер обработал предложения
            receiveBestCarrier(carriersWithOffers, K, L).getOrder(K, L); //заказ отправляется лучшему перевозчику
            sendRejectForOthers(); //остальным отказ
            isOfferReady = false; // предложение возращается в изначальный статус, не готово
        }
    }

    private static Carrier receiveBestCarrier(List<Carrier> carriers, int K, int L) {
        return carriers.stream().filter(Carrier::isAvailableForOder)
                .min(Comparator.comparingDouble(o -> o.getPriceForCarriage(K, L))).orElseThrow();
    }

    private void sendOrderForCarriers() {
        allCarriers.stream().filter(Carrier::isAvailableForOder).forEach(Carrier::getOffer);
    }

    private void sendRejectForOthers() {
        allCarriers.stream().filter(Carrier::isAvailableForOder).forEach(Carrier::getReject);
    }

    public void refresh() {
        allCarriers.forEach(Carrier::refreshStatus);
        if (timeManagerProcessesIsReady == currentDay) isOfferReady = true;
    }

    public ArrayList<Carrier> getAllCarriers() {
        return allCarriers;
    }


}
