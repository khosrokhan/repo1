import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReaderWebSocketClient extends WebSocketServer {

    public static List<WebSocket> clients = new ArrayList<>();


    public ReaderWebSocketClient(){

    }

    public ReaderWebSocketClient(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        if(conn != null ) {
            clients.add(conn);
            System.out.println("new ReaderWebSocketClient was connected: " + address);
        }
    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        Iterator<WebSocket> iter = clients.iterator();
        while (iter.hasNext()) {
            WebSocket client = iter.next();
            if (conn ==  client) {
                iter.remove();
                System.out.println("ReaderWebSocketClient was dicconnected");
            }
        }
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        System.out.println("ReaderWebSocketClient has new message: " + address + " -> " + message);
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, ByteBuffer message) {
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            System.out.println("ReaderWebSocketClient.onError : " + ex.getMessage());
        }
    }

    @Override
    public void onStart() {
        //setConnectionLostTimeout(100);
        System.out.println("ReaderWebSocketClient is started on port: " + getPort());
    }

    public void sendToAllWebSocketReader(String message){
        //broadcast(message, ReaderWebSocketClient.clients);


        List<WebSocket> clients = new ArrayList<>();

        Iterator<WebSocket> iter =ReaderWebSocketClient.clients.iterator();
        while (iter.hasNext()) {
            WebSocket client = iter.next();
            if (client.isOpen()) {
                clients.add(client);
            }
        }

        broadcast(message, clients);
    }
}
