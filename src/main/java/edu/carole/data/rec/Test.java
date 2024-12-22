package edu.carole.data.rec;

import java.net.InetAddress;

public record Test(String msg, InetAddress addr, String mac, String id, Long timeStamp) {

    @Override
    public String toString() {
        return "{" +
                "    msg='" + msg + '\'' +
                "    addr=" + addr +
                "    mac='" + mac + '\'' +
                "    id=" + id +
                "    timeStamp=" + timeStamp +
                "}";
    }
}
