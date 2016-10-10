package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpClients {



    public static Map doGet(String action, List<NameValuePair> params) {
        String param =  JsonUtil.makeReqStr(params);
        Log.e("info","param==="+param);
//        String urlAndParaStr = action+"?param="+param;
        String urlAndParaStr = action+"?"+param;
        Log.e("info", "urlAndParaStr==" + urlAndParaStr);
        URL url = null;
        HttpPost httpRequest = new HttpPost(urlAndParaStr);

        try {
//            DefaultHttpClient client = new DefaultHttpClient();
//            client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
//            HttpResponse response = client.execute(httpRequest);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            int res = httpResponse.getStatusLine().getStatusCode();
            if (res == HttpStatus.SC_OK) {
                System.out.println(httpResponse.getEntity());
                HttpEntity he = httpResponse.getEntity();
                String result = EntityUtils.toString(httpResponse.getEntity());
                return JsonUtil.getMapByJsonStr(result);
            } else {
                return JsonUtil.getMapByJsonStr("");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (IOException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        }
    }

    public static Map doGetForMap(String action, List<Map> params) {
        String param =  JsonUtil.makeReqStrForMaps(params);
        String urlAndParaStr = action+"?param="+param;
        URL url = null;
        HttpPost httpRequest = new HttpPost(urlAndParaStr);
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(httpResponse.getEntity());
                HttpEntity he = httpResponse.getEntity();
                String result = EntityUtils.toString(httpResponse.getEntity());
                return JsonUtil.getMapByJsonStr(result);
            } else {
                return JsonUtil.getMapByJsonStr("");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (IOException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        }
    }

    public static Map doPost(String webUrl, String actionName,
                             List<NameValuePair> params) {
        if (webUrl.equals("") || actionName.equals("")) {
            return null;
        }
        webUrl = webUrl + actionName;
        HttpPost httpRequest = new HttpPost(webUrl);
        try {
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
            httpRequest.setEntity(httpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                return JsonUtil.getMapByJsonStr(result);
            } else {
                return JsonUtil.getMapByJsonStr("");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        } catch (IOException e) {
            e.printStackTrace();
            String res = JsonUtil.returnResErrJson(e.getMessage());
            System.out.println(res);
            return JsonUtil.getMapByJsonStr(res);
        }
    }
}
