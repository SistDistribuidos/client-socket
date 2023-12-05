package socketclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;

/**
 *
 * @author daniel
 */
public class ProcessFile extends Thread {
    private Message message;
    private DataOutputStream out;
    private DataInputStream in;
    private SocketClient socketC;
    public ProcessFile(Message message, DataOutputStream out, DataInputStream in ,SocketClient socketC) {
        super();
        this.message = message;
        this.out = out;
        this.socketC = socketC;
        this.in = in;
    }
    public void run() {
                try {
            FileInputStream fis = new FileInputStream(message.location);
            out.writeUTF("OPEN");
            out.writeLong(message.location.length()); //enviar tamaño de del fichero
            out.writeUTF(message.location.getName()); //enviar el nombre del fichero
            long numeroDePaquetes = (int)(message.location.length() / 4096) + hasOutOfPhase(message.location.length(),4096);
            out.writeLong(numeroDePaquetes); //numero de paquetes que recepcionara
            byte[] buffer = new byte[4096]; //enviar el fichero
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                //message.message = in.readUTF();
                //notifyAllListeners(message);
            }
            out.writeUTF("CLOSE");
            message.message = in.readUTF();
            socketC.notifyAllListeners(message);
        } catch (Exception e) {
            System.out.println("Conexion perdida");
        }
    }
    
        
    private long hasOutOfPhase(long value1, long value2) {
        return (value1 % value2 == 0)?0:1;
    }
}
