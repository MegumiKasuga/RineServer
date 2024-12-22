package edu.carole.thread;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zerotier.sockets.ZeroTierServerSocket;
import com.zerotier.sockets.ZeroTierSocket;
import edu.carole.AllAffairs;
import edu.carole.Main;
import edu.carole.Server;
import edu.carole.data.affairs.Affair;
import edu.carole.packet.Packet;
import edu.carole.packet.TestPacket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ListenerThread extends Thread {

    private final Server server;
    private final ZeroTierServerSocket socket;

    public ListenerThread(Server server) throws IOException {
        this.server = server;
        socket = server.getSocket();
    }

    private void replyAndClose(ZeroTierSocket socket, JsonObject json) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(Main.gson.toJson(json).getBytes(StandardCharsets.UTF_8));
        socket.close();
    }

    private Packet state400() {
        TestPacket testPacket = new TestPacket();
        testPacket.setStateCode(400);
        return testPacket;
    }

    @Override
    public void run() {
        System.out.println("Thread <" + this.getName() + "> standing by!");
        while (isAlive()) {
            try {
                ZeroTierSocket client = socket.accept();
                InputStream stream = client.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                StringBuilder builder = new StringBuilder();
                while ((len = stream.read(buffer)) != -1) {
                    builder.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                }
                JsonElement element = JsonParser.parseString(builder.toString());
                if (!element.isJsonObject() || !element.getAsJsonObject().has("service_code")) {
                    replyAndClose(client, state400().getReplyJson(null));
                    continue;
                } else {
                    JsonObject obj = element.getAsJsonObject();
                    Integer serviceCode = obj.get("service_code").getAsInt();
                    System.out.println("Accepted Service: " + serviceCode);
                    Affair affair = AllAffairs.AFFAIRS.getOrDefault(serviceCode, null);
                    if (affair != null) {
                        Packet packet = affair.getPacket();
                        JsonObject replay = packet.getReplyJson(affair.handle(packet.fromJson(obj)));
                        replyAndClose(client, replay);
                        continue;
                    }
                    replyAndClose(client, state400().getReplyJson(null));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
