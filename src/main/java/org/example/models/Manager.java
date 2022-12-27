package org.example.models;


import org.example.Main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.example.Main.currentDay;


public class Manager {


    private ArrayList<Carrier> allCarriers;
    private int daysWithoutOrders = 0;
    private List<Carrier> carriersWithOffers;
    private boolean isProcessesOffers = false;
    private boolean isOfferReady = false;
    private int timeManagerProcessesIsReady;
    private int K;
    private int L;

    public Manager() {
        allCarriers = Carrier.generateCarriers(6);
    }

    public void receiveNewOrder(int K, int L) {
        System.out.printf("Получен новый заказ.\tЧисло товаров: %d\tРасстояние: %d\n", K, L);
        sendOrderForCarriers();
        this.K = K;
        this.L = L;
//        catch (NoSuchElementException exception){
//            System.out.println("Нет свободных перевозчиков, в заказе отказано");
//            daysWithoutOrders++;
//        }
    }

    public void next() {
        System.out.printf("День номер: %d\n", currentDay);
        refresh();
        carriersWithOffers = allCarriers.stream().filter(Carrier::isAvailableForOder).filter(Carrier::isOfferReady).collect(Collectors.toList());
        if (carriersWithOffers.size() != 0 && !isOfferReady) timeManagerProcessesIsReady = currentDay + 1;
        if (isOfferReady) {
            receiveBestCarrier(carriersWithOffers, K, L).getOrder(K, L);
            sendRejectForOthers();
            isOfferReady = false;
        }
    }

    private static Carrier receiveBestCarrier(List<Carrier> carriers, int K, int L) {
        return carriers.stream().filter(Carrier::isAvailableForOder)
                .min(Comparator.comparingDouble(o -> o.getPriceForCarriage(K, L))).orElseThrow();
    }

    public int getDaysWithoutOrders() {
        return daysWithoutOrders;
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
