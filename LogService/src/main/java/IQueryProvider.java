import  java.util.*;

public interface IQueryProvider  {
    List<String> getResult() throws  Exception ;
    boolean broadcast();
}
