package edu.carole.thread;

import edu.carole.data.local.UserTokenManager;

public class UserManageThread extends Thread {

    private final UserTokenManager userTokenManager;

    private final long delay;

    public UserManageThread(UserTokenManager manager, long delay) {
        userTokenManager = manager;
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("Thread <" + this.getName() + "> standing by!");
        while (isAlive()) {
            userTokenManager.removeOvertimeUsers(delay);
            try {
                Thread.sleep(delay / 4);
            } catch (InterruptedException ignored) {}
        }
    }
}
