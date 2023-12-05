package socketclient;

import java.net.Socket;

/**
 *
 * @author daniel
 */
public interface EventListenerSocket{
    void onMessageEstablished(Message message);
}
