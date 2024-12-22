package edu.carole.thread;

import edu.carole.data.local.TokenManager;

import java.util.HashSet;
import java.util.List;

public class ResourceThread extends Thread {

    private final HashSet<TokenManager> managers;
    private final long time;

    public ResourceThread(long time) {
        managers = new HashSet<>();
        this.time = time;
    }

    public ResourceThread(long time, TokenManager... managers) {
        this(time);
        this.managers.addAll(List.of(managers));
    }

    public void addManager(final TokenManager manager) {
        this.managers.add(manager);
    }

    @Override
    public void run() {
        System.out.println("Thread <" + this.getName() + "> standing by!");
        while (isAlive()) {
            managers.forEach(TokenManager::saveToDisk);
            try {
                Thread.sleep(time);
            } catch (InterruptedException interrupt) {
                managers.forEach(TokenManager::saveToDisk);
                break;
            }
        }
    }
}
