import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;


public class WriterWebSocketClient extends WebSocketServer {

    QueryProviderFactory queryProviderFactory = null;
    ReaderWebSocketClient readerWebSocketLogServer = new ReaderWebSocketClient();

    public WriterWebSocketClient(){

    }

    public WriterWebSocketClient(QueryProviderFactory queryProviderFactory, int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.queryProviderFactory = queryProviderFactory;
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        String address= conn.getRemoteSocketAddress().getAddress().getHostAddress();
        System.out.println("WriterWebSocketClient new connect: " +address);
    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WriterWebSocketClient dicconnect");
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        System.out.println("WriterWebSocketClient received message: " + address + " -> " + message);

        try {
            IQueryProvider queryProvider = queryProviderFactory.getQueryProvider(message, address);
            if (queryProvider != null) {
                List<String> result = queryProvider.getResult();
                for (String s : result) {
                    if (queryProvider.broadcast()) {
                        readerWebSocketLogServer.sendToAllWebSocketReader(s);
                    } else {
                        //broadcast(message.getBytes(),clients);
                        Thread.sleep(100);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, ByteBuffer message) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            System.out.println("WriterWebSocketClient.onError : " + ex.getMessage());
        }
    }

    @Override
    public void onStart() {
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

        System.out.println("WriterWebSocketClient is started on port: " + getPort());
    }
}

