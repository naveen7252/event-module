package io.nuls.event.publish;

/**
 * Base interface for Nulls Block Chain Events. Every event class implements this interface to publish the event
 * @author Naveen(naveen.balamuri@gmail.com)
 */
public interface NulsEvent {

    void publish();
}
