package com.admin.file.manager;

import java.util.Observable;
import java.util.Observer;


public class TaskObservable extends Observable {

    @Override
    public void addObserver(Observer observer) {
        // TODO Auto-generated method stub
        super.addObserver(observer);
    }

    @Override
    protected void clearChanged() {
        // TODO Auto-generated method stub
        super.clearChanged();
    }

    @Override
    public int countObservers() {
        // TODO Auto-generated method stub
        return super.countObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        // TODO Auto-generated method stub
        super.deleteObserver(observer);
    }

    @Override
    public synchronized void deleteObservers() {
        // TODO Auto-generated method stub
        super.deleteObservers();
    }

    @Override
    public boolean hasChanged() {
        // TODO Auto-generated method stub
        return super.hasChanged();
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object data) {
        // TODO Auto-generated method stub
        super.notifyObservers(data);
    }

    @Override
    public void setChanged() {
        // TODO Auto-generated method stub
        super.setChanged();
    }

    public void notifyChangedDelALL() {
        this.setChanged();
        this.notifyObservers();
    }


}
