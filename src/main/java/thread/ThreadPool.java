package thread;

import annotation.AnnotationAnalysis;
import annotation.ThreadAnnotation;
import json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class ThreadPool {

    @ThreadAnnotation(numberThread = 2)
    private int numberThread;

    private Thread[] masThread;
    private Vector<Runnable> masThreadTask;
    private Vector<String> nameThreadTask;

    public void addTask(Runnable task, String name){
        nameThreadTask.add(name);
        masThreadTask.add(task);
    }

    public Thread[] getThreadTask(){
        return masThread;
    }

    public void init(){
        masThread = new Thread[numberThread];
        masThreadTask = new Vector<Runnable>();
        nameThreadTask = new Vector<String>();

        //главный поток, который мониторит все потоки задач
        Thread thread = new Thread(() -> {
                while (true) {
                    for (int i = 0; i < numberThread; i++) {
                        //если в массиве есть null и список задач не пустой
                        if (masThread[i] == null && masThreadTask.size() != 0) {
                            //создаем поток и назначаем ему задачу
                            Thread th = new Thread(masThreadTask.get(0));
                            //добавляем поток в массив
                            masThread[i] = th;
                            //делаем его демонов
                            th.setDaemon(true);
                            th.setName(nameThreadTask.get(0));
                            //запускаем поток
                            th.start();
                            nameThreadTask.remove(0);
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
