package edu.carole;

import com.zerotier.sockets.ZeroTierNode;
import com.zerotier.sockets.ZeroTierServerSocket;
import edu.carole.data.local.ChatManager;
import edu.carole.data.UserToken;
import edu.carole.data.local.UserTokenManager;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.Login;
import edu.carole.thread.ListenerThread;
import edu.carole.thread.ResourceThread;
import edu.carole.thread.UserManageThread;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;

public class Server {

    @Getter
    private final Config config;

    @Getter
    private final ZeroTierNode node;

    @Getter
    private final UserTokenManager userManager;

    @Getter
    private final ChatManager chatManager;

    private final ListenerThread entryThread;
    private final ResourceThread resourceThread;
    private final UserManageThread userManageThread;

    public Server(Config config) {
        Main.server = this;
        this.config = config;
        this.userManager = new UserTokenManager("user_token.json", "user_data.json");
        this.chatManager = new ChatManager("chat_data.json");
        node = new ZeroTierNode();
        node.initFromStorage(config.getStoragePath());
        node.initSetPort(config.getPort());
        node.start();
        int counter = 0;
        int times = config.getDelayMs() / 100;
        System.out.println("Server started at port " + config.getPort());
        while (!node.isOnline()) {
            if (counter > times) {
                System.err.println("Waiting for too long!");
                System.exit(1);
                entryThread = null;
                resourceThread = null;
                userManageThread = null;
                return;
            }
            delay(100);
            counter++;
        }
        counter = 0;
        long networkId = config.getNetworkId();
        node.join(networkId);
        while (!node.isNetworkTransportReady(networkId)) {
            if (counter > times) {
                System.err.println("Waiting for too long to be able to transport!");
                System.exit(1);
                entryThread = null;
                resourceThread = null;
                userManageThread = null;
                return;
            }
            delay(100);
            counter++;
        }
        System.out.println("Server started at port " + config.getPort());
        System.out.println("NodeID: "+ Long.toHexString(node.getId()));
        ListenerThread t;
        try {
            t = new ListenerThread(this);
        } catch (IOException e) {
            e.printStackTrace();
            entryThread = null;
            resourceThread = null;
            userManageThread = null;
            System.exit(1);
            return;
        }
        entryThread = t;
        entryThread.setName("entry thread");
        entryThread.start();

        resourceThread = new ResourceThread(60000);
        resourceThread.setName("resource thread");
        resourceThread.addManager(chatManager);
        resourceThread.addManager(userManager);
        resourceThread.start();

        userManageThread = new UserManageThread(userManager, 5 * 60 * 1000);
        userManageThread.setName("user manage thread");
        userManageThread.start();
    }

    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getAddress() {
        return node.getIPv4Address(getConfig().getNetworkId());
    }

    public InetAddress getIPv6Address() {
        return node.getIPv6Address(getConfig().getNetworkId());
    }

    public String getMacAddress() {
        return node.getMACAddress(getConfig().getNetworkId());
    }

    public ZeroTierServerSocket getSocket() throws IOException {
        return new ZeroTierServerSocket(config.getPort());
    }

    public ZeroTierServerSocket getSocket(int port) throws IOException {
        return new ZeroTierServerSocket(port);
    }

    public String toString() {
        return config.toString();
    }

    public void closeSocket() {
        entryThread.interrupt();
        resourceThread.interrupt();
        userManageThread.interrupt();
    }

    public UserToken hasUser(Login login) {
        for (UserToken token : userManager.getValues()) {
            if (token.token() == login.getToken() &&
                token.uid().equals(login.getId())) {
                return token;
            }
        }
        return null;
    }

    public UserToken getOnlineUserByToken(int token) {
        for (UserToken tokens : userManager.getOnlineUsers().keySet()) {
            if (tokens.token() == token) {
                return tokens;
            }
        }
        return null;
    }

    public Chat getChatById(long id) {
        for (Chat chat : chatManager.getValues()) {
            if (chat.getId() == id) {
                return chat;
            }
        }
        return null;
    }
}
