import java.util.List;

public class SolrDbSaveLog implements IQueryProvider {
    @Override
    public List<String> getResult() throws Exception {
        return null;
    }

    @Override
    public boolean broadcast() {
        return false;
    }
}
