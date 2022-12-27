package org.example.models;

import org.example.Main;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

public class Carrier {

    private int id;
    private Status status;
    private int timeWhenOrderIsCompleted;
    private int countOfOrders = 0;
    private boolean isOfferReady = false;
    private int timeWhenOfferIsReady;
    private int countDaysWithoutOrders = 0;

    protected Carrier() {
        status = Status.executor;
    }

    public void getOrder(int k, int l) {
        status = Status.contractor;
        isOfferReady = false;
        countOfOrders++;
        timeWhenOrderIsCompleted = Main.currentDay + l;
        System.out.printf("Заказ получил carrier :%d \n", id);
    }

    public void getReject() {
        isOfferReady = false;
    }

    public void refreshStatus(){
        if (timeWhenOrderIsCompleted == Main.currentDay)
            status = Status.executor;
        if (timeWhenOfferIsReady == Main.currentDay)
            isOfferReady = true;
        if (status == Status.executor) countDaysWithoutOrders++;
    }

    public void getOffer() {
        if (timeWhenOfferIsReady <= Main.currentDay)  timeWhenOfferIsReady = Main.currentDay + 3;
    }

    public int getCountOfOrders() {
        return countOfOrders;
    }

    public int getCountDaysWithoutOrders() {
        return countDaysWithoutOrders;
    }

    enum Status {
        executor,
        contractor
    }

    protected double getPriceForCarriage(int K, int L) {
        if (status != Status.contractor) {
            Random random = new Random();
            return random.nextDouble(K * L - 0.2 * K * L, K * L + 0.2 * K * L);
        }
        else throw new NoSuchElementException();
    }

    protected static ArrayList<Carrier> generateCarriers(int count){
        ArrayList<Carrier> carriers = new ArrayList<>();
        for (int i = 1; i <= count; i++){
            Carrier carrier = new Carrier();
            carrier.id = i;
            carriers.add(carrier);
        }
        return carriers;
    }

    public boolean isAvailableForOder(){
        return status == Status.executor;
    }

    public boolean isOfferReady() {
        return isOfferReady;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrier carrier = (Carrier) o;
        return id == carrier.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
