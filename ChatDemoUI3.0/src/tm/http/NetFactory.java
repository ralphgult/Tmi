package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
public class NetFactory {

    private static NetHttpInterface httpInterface;

    public static NetHttpInterface instance(){
        if(httpInterface==null){
            httpInterface = new NetHttpInterfaceImpl();
        }
        return httpInterface;
    }
}

