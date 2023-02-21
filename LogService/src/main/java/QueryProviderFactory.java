public abstract class QueryProviderFactory {

    public abstract IQueryProvider createQuerySaveLogProvider(String input, String remoteAddress);

    public abstract IQueryProvider createQueryListProvider(String input);

    public IQueryProvider getQueryProvider(String input, String remoteAddress) {
        if (input.startsWith("list")) {
            return createQueryListProvider(input);
        } else if (input.isEmpty()) {
            return null;
        } else {
            return createQuerySaveLogProvider(input, remoteAddress);
        }
    }
}
