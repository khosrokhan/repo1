public class FlatFileQueryProviderFactory extends  QueryProviderFactory {

    private String path;
    public  FlatFileQueryProviderFactory(String path){
        this.path = path;
    }

    @Override
    public IQueryProvider createQueryListProvider(String input) {
        return  new FlatFileQueryList(input,path);
    }

    @Override
    public IQueryProvider createQuerySaveLogProvider(String input,String remoteAddress ) {
        return new FlatFileSaveLog(input,remoteAddress,path);
    }
}
