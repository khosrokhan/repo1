import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlatFileSaveLog implements  IQueryProvider {

    String input;
    String path;
    String remoteAddress;
    DataOutputStream  dataOutputStream =null;

    public FlatFileSaveLog(String input,String remoteAddress ,String path) {
        this.input=input;
        this.remoteAddress=remoteAddress;
        this.path = path;
    }

    @Override
    public List<String> getResult() throws  Exception {
        try {

            List<String> result = new ArrayList<>();

            Log log = Log.parse(input, remoteAddress);
            if (log != null) {
                LocalDateTime time = LocalDateTime.now();
                String fileName = "log-" + time.toLocalDate();
                path = path + fileName;
                File file = new File(path);

                boolean fileExists = file.exists();
                if (!fileExists) {
                    file.createNewFile();
                }

                dataOutputStream = new DataOutputStream(new FileOutputStream(path, true));
                dataOutputStream.writeBytes(log.toString().replace("\r\n", "\t") + "\n");
                dataOutputStream.close();

                result.add(log.toString());
            } else {
                System.out.println("cannot parse log");
                result.add("cannot parse log");
            }
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean broadcast() {
        return true;
    }
}
