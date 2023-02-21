import java.util.List;

public class MongoDbQueryList extends LogCollector implements IQueryProvider {

    String input;
    String connectionString;

    public MongoDbQueryList(String input, String connectionString) {
        this.input = input;
        this.connectionString = connectionString;
    }

    @Override
    public List<String> getResult() throws Exception {
        clear();
        parse(input);

        try {

        }
        catch (Exception ex){
            throw ex;
        }

        return null;
    }

    @Override
    public boolean broadcast() {
        return false;
    }
}
