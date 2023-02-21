import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Program {

    private static void createDirectory(String path) {
        File file = new File(path);
        file.mkdir();
    }

    public static void main(String args[]) throws Exception {



        String provider = "";
        String pathOrConnectionString = "";

        try (InputStream input = Program.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (input == null) {
                System.out.println("unable to find application.properties");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            provider = prop.getProperty("provider");
            pathOrConnectionString = prop.getProperty("path");


            QueryProviderFactory queryProviderFactory = null;

            if (provider.contains("mongodb") ) {
                queryProviderFactory = new MongoDbQueryProviderFactory(pathOrConnectionString);
            }
            else{
                queryProviderFactory = new FlatFileQueryProviderFactory(pathOrConnectionString);
            }

            int writerTcpPort = 8585;
            WriterTcpClient writerTcpClient = new WriterTcpClient(queryProviderFactory, writerTcpPort);
            new Thread(writerTcpClient).start();

            int writerWebSocketPort = 8586;
            WriterWebSocketClient writerWebSocketClient = new WriterWebSocketClient(queryProviderFactory, writerWebSocketPort);
            writerWebSocketClient.start();

            int readerWebSocketPort = 8587;
            ReaderWebSocketClient readerWebSocketClient = new ReaderWebSocketClient(readerWebSocketPort);
            readerWebSocketClient.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
