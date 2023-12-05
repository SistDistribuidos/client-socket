package socketclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author daniel
 */
public class SocketClient extends Thread implements EventListenerSocket {

    private String host;
    private int port;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket sc;
    private SocketClientFrame frame;
    private List<EventListenerSocket> listOfListeners = new ArrayList<>();

    public SocketClient(String host, int port, SocketClientFrame frame) {
        this.port = port;
        this.host = host;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            sc = new Socket(host, port);
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            String message = in.readUTF();
            System.out.println("Server: " + message);
            if (frame == null) {
                System.out.println("El puntero es nulo");
            }
            this.frame.addNewListener(this);
        } catch (Exception e) {
            System.out.println("Conexion perdida " +e.getMessage().toString());
        }
    }

    public void disconnect() {
        try {
            sc.close();
        } catch (Exception e) {
            System.out.println("Error durante la conexion");
        }
    }

    @Override
    public void onMessageEstablished(Message message) {
        if (message == null || out == null) {
            return;
        }
        
        ProcessFile process = new ProcessFile(message, out, in, this);
        process.start();
    }

    void addNewListener(EventListenerSocket listener) {
        listOfListeners.add(listener);
    }


    void notifyAllListeners(Message message) {
        for (EventListenerSocket listener : listOfListeners) {
            listener.onMessageEstablished(message);
        }
    }
}
