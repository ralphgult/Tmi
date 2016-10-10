package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;

/***
 * 移动执法系统网络通讯接口
 * @author admin
 *
 */
public interface NetHttpInterface {

    /***
     *  commonHttpCilent 公共的请求方法 </br>
     * @param handler  服务消息处理类</br>
     * @param contextfinal  上下文对象</br>
     * @param actionName  访问的方法标识</br>
     * @param List<NameValuePair> params  参数Map对象</br>
     * @return 如果方法正常则 返回一个 seccess标识并将结果对象以Map结构转储到 用户缓存对象中</br>
     * 如果方法返回异常 则返回不同标识 应用级别错误、json解析错误、应用超时错误等。</br>
     */
    public void commonHttpCilent(final Handler handler, final Context contextfinal,final String actionName,final List<NameValuePair> params);

    public void commonHttpCilentForMap(final Handler handler, final Context contextfinal,final String actionName,final List<Map> params);

    public void checkUpdateHttpCilent(final Handler handler, final Context contextfinal,final String actionName,final List<NameValuePair> params);

}
