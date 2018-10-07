package io.nuls.event.model;

import java.io.Serializable;

/**
 * Message class to hold the data to be sent over web socket to subscribed clients
 * @param <T> data of type T
 * @author Naveen(naveen.balamuri@gmail.com)
 */
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
