package edu.carole.data.affairs;

import edu.carole.data.Result;
import edu.carole.packet.Packet;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public abstract class Affair<T> {

    private final int serviceCode;
    private final Supplier<Packet<T>> packetSupplier;

    public Affair(int serviceCode, Supplier<Packet<T>> packetSupplier) {
        this.serviceCode = serviceCode;
        this.packetSupplier = packetSupplier;
    }

    public Packet<T> getPacket() {
        return packetSupplier.get();
    }

    public abstract Result<T> handle(T data);
}
