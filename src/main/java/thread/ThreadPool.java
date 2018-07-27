package thread;

import annotation.AnnotationAnalysis;
import annotation.ThreadAnnotation;
import json.JSONObject;
import pict.Picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class ThreadPool {

    @ThreadAnnotation(numberThread = 1)
    private int numberThread;

    private Thread[] masThread;
    private Vector<Picture> masThreadTask;


    public void addTask(Picture picture){
        masThreadTask.add(picture);
    }

    public Thread[] getThread(){
        return masThread;
    }

    public void init(){
        masThread = new Thread[numberThread];
        masThreadTask = new Vector<Picture>();

        //главный поток, который мониторит все потоки задач
        Thread thread = new Thread(() -> {
                while (true) {
                    for (int i = 0; i < numberThread; i++) {
                        //если в массиве есть null и список задач не пустой
                        if (masThread[i] == null && masThreadTask.size() != 0) {
                            //создаем поток и назначаем ему задачу
                            Thread th = new Thread(masThreadTask.get(0).getTask());
                            //добавляем поток в массив
                            masThread[i] = th;
                            //делаем его демонов
                            th.setDaemon(true);
                            //назначаем имя
                            th.setName(masThreadTask.get(0).getText());
                            //запускаем поток
                            th.start();
                            //удаляем задачу из списка
                            masThreadTask.remove(0);
                        } else if (masThread[i] != null && !masThread[i].isAlive()) {
                            //если поток закончил задачу, то он задается как null
                            masThread[i] = null;
                        }
                    }
                }
        });
        thread.setDaemon(true);
        thread.start();
    };


}
