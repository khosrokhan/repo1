import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

public class WriterTcpClient extends Thread {

    ReaderWebSocketClient readerWebSocketLogServer = new ReaderWebSocketClient();

    int port;
    QueryProviderFactory queryProviderFactory = null;
    ByteBuffer buffer = ByteBuffer.allocate(5 * 1024);

    public WriterTcpClient(QueryProviderFactory queryProviderFactory, int port) throws IOException {
        this.port = port;
        this.queryProviderFactory = queryProviderFactory;
    }

    @Override
    public void run() {

        int readerTcpPort=4001;

        Selector selector = null;
        try {
            selector = Selector.open();

            int[] ports = {port,readerTcpPort};
            for (int port : ports) {
                ServerSocketChannel server = ServerSocketChannel.open();
                server.configureBlocking(false);
                server.socket().bind(new InetSocketAddress(port));
                server.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("WriterTcpClient is started on port: " + port);
            }

            SelectionKey key;

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector);
                    }

                    if (key.isReadable()) {
                        handleRead(key, selector);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key, Selector s) throws IOException {
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            String address = (new StringBuilder(socketChannel.socket().getInetAddress().toString())).append(":").append(socketChannel.socket().getPort()).toString();
            socketChannel.configureBlocking(false);
            socketChannel.register(s, SelectionKey.OP_READ, address);

            //ByteBuffer welcomeBuf = ByteBuffer.wrap("Welcome to NioChat!\n".getBytes());
            //sc.write(welcomeBuf);
            //welcomeBuf.rewind();

            System.out.println("new WriterTcpClient was connected: " +address);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void handleRead(SelectionKey key, Selector selector) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if(socketChannel.isConnected()) {
            try {

                StringBuilder sb = new StringBuilder();

                String address = (new StringBuilder(socketChannel.socket().getInetAddress().toString())).append(":").append(socketChannel.socket().getPort()).toString();
                String pureAddress = new StringBuilder(socketChannel.socket().getInetAddress().toString()).toString();

                buffer.clear();
                int read = 0;
                while ((read = socketChannel.read(buffer)) > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    sb.append(new String(bytes));
                    buffer.clear();
                }

                String message = "";
                if (read < 0) {
                    message = "";
                    socketChannel.close();
                    System.out.println("WriterTcpClient was disconnected: " + address);
                } else {
                    message = sb.toString();
                }

                System.out.println("WriterTcpClient has new message: " + address + " -> " + message);

                IQueryProvider queryProvider = queryProviderFactory.getQueryProvider(message, pureAddress);
                if (queryProvider != null) {
                    List<String> result = queryProvider.getResult();
                    for (String s : result) {
                        if (queryProvider.broadcast()) {
                            broadcast(s, selector);

                            readerWebSocketLogServer.sendToAllWebSocketReader(s);
                        } else {
                            sendMessage(s, selector, socketChannel.getRemoteAddress());
                            Thread.sleep(100);
                        }
                    }
                }
            } catch (Exception ex) {
                if (ex.getMessage().contains("reset by peer")) {
                    return;
                } else {
                    throw ex;
                }
            }
        }
    }

    private void broadcast(String message, Selector selector) throws Exception {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            for (SelectionKey key : selector.keys()) {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    if (socketChannel.socket().getLocalPort() == 4001 /* reader port */) {
                        if(socketChannel.isConnected()) {
                            socketChannel.write(buffer);
                            buffer.rewind();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void sendMessage(String message, Selector selector, SocketAddress dest) throws Exception {
        try {
            ByteBuffer msgBuf = ByteBuffer.wrap(message.getBytes());
            for (SelectionKey key : selector.keys()) {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    if (socketChannel.getRemoteAddress() == dest) {
                        socketChannel.write(msgBuf);
                        msgBuf.rewind();
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}
