public class MongoDbQueryProviderFactory extends QueryProviderFactory {

    private String connectionString;
    public   MongoDbQueryProviderFactory(String connectionString){
        this.connectionString = connectionString;
    }

    @Override
    public IQueryProvider createQueryListProvider(String input) {
        return new MongoDbQueryList(input,connectionString);
    }


    @Override
    public IQueryProvider createQuerySaveLogProvider(String input, String remoteAddress) {
        return new MongoDbSaveLog(input,remoteAddress,connectionString);
    }
}
