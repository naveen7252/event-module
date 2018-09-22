package io.nuls.event.model;

import java.io.Serializable;

public class SubscribableMessage<T>  implements Serializable {

    private boolean success;
    private T data;

    public SubscribableMessage(boolean success,T data){
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
