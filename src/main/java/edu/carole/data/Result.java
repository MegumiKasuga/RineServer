package edu.carole.data;

import edu.carole.data.affairs.Affair;
import edu.carole.data.local.DataPack;
import lombok.Getter;

@Getter
public class Result<T> {

    private final int stateCode;
    private final T data;
    private final Affair input;
    private final boolean success;
    private final DataPack dataPack;
    private final Bundle bundle;

    public Result(boolean success, DataPack dataPack, Bundle bundle, Affair input, T data, int stateCode) {
        this.stateCode = stateCode;
        this.success = success;
        this.data = data;
        this.input = input;
        this.dataPack = dataPack;
        this.bundle = bundle;
    }
}
