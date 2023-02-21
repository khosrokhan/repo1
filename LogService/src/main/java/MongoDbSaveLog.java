
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MongoDbSaveLog implements  IQueryProvider {

    String input;
    String connectionString;
    String remoteAddress;
    DataOutputStream dataOutputStream =null;

    public MongoDbSaveLog(String input,String remoteAddress,String connectionString ) {
        this.input=input;
        this.remoteAddress=remoteAddress;
        this.connectionString = connectionString;
    }
    @Override
    public List<String> getResult() throws Exception {
        try {
            List<String> result = new ArrayList<>();

            Log log = Log.parse(input, remoteAddress);
            if (log != null) {


            }else{
                System.out.println("cannot parse log");
                result.add("cannot parse log");
            }
            return result;
        }
        catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public boolean broadcast() {
        return true;
    }
}
