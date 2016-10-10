package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;


import tm.entity.UserBean;

public class JsonUtil {

    public static String makeReqStr(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        String str = "";
        String toke = "";
        UserBean user = AppCfg._instance.getCurrentUser();
        if(user!=null){
            toke = user.getToken();
        }
        sb.append("{\"extend\":{\"version\":\"1.0\",\"terminal\":\"1\",\"token\":\""
                + toke + "\"},\"main\":{");
        if (params != null && params.size() > 0) {
            Iterator itor = params.keySet().iterator();
            while (itor.hasNext()) {
                String key = itor.next().toString();
                try {
                    String val =URLEncoder.encode(params.get(key).toString(), "utf-8");
                    val = val.replace("%2C", ",");
                    sb.append("\""+ key+ "\":\""+ val + "\",");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            str = sb.toString();
            str = str.substring(0, str.length() - 1);
            str += "}}";
        } else {
            str = sb.toString() + "}}";
        }
        System.out.println(str);
        try {
            System.out.println(URLEncoder.encode(str, "utf-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return str;
        // try {
        // } catch (UnsupportedEncodingException e) {
        // return null;
        // }
    }
    public static String makeReqStr(List<NameValuePair> params) {
        StringBuffer sb = new StringBuffer();
        String str = "";
        String toke = "";
        UserBean user = AppCfg._instance.getCurrentUser();
        if(user!=null){
            toke = user.getToken();
        }
//        sb.append("{\"extend\":{\"version\":\"1.0\",\"terminal\":\"1\",\"token\":\""
//                + toke + "\"},\"main\":{");
        if (params != null && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                NameValuePair nv = params.get(i);
                if(nv.getValue().toString().startsWith("[")){
                    sb.append("\""
                            + nv.getName()
                            + "\":"
                            + nv.getValue().toString() + ",");
                }else{
                    sb.append(
                            nv.getName()
                                    + "="
                                    + (nv.getValue().toString()) + "&");
//                    + (nv.getValue() == null ? "" : nv.getValue().toString()) + "\",");
                }

            }
            str = sb.toString();
            str = str.substring(0, str.length() - 1);
//            str += "}}";
        } else {
            str = sb.toString() + "}}";
        }
        System.out.println(str);
        //return str;
//        try {
//            return URLEncoder.encode(str, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        }
//        http://hsaiqs.xicp.net:20806/CBDParkingMSs/login?userName=123456789&userPassword=123456
//        http://hsaiqs.xicp.net:20806/CBDParkingMSs/login?userName=admin111&userPassword=123123
        return str;
    }

    /***
     * returnJsonForMap 通过map 返回json对象
     *
     * @param actionName
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String returnJsonForRequestList(String actionName,
                                                  List<NameValuePair> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"actionName\":\"" + actionName + "\",");
        try {
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    NameValuePair nv = params.get(i);
                    sb.append("\""
                            + nv.getName()
                            + "\":\""
                            + URLEncoder.encode(nv.getValue() == null ? "" : nv
                            .getValue().toString(), "utf-8") + "\",");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        str += "}";
        System.out.println(str);
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /***
     * returnJsonForMap 通过map 返回json对象
     *
     * @param actionName
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String returnJsonForMap(String actionName, Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"actionName\":\"" + actionName + "\",");

        try {
            if (map != null) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    sb.append("\""
                            + entry.getKey()
                            + "\":\""
                            + URLEncoder.encode(entry.getValue() == null ? ""
                            : entry.getValue().toString(), "utf-8")
                            + "\",");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        str += "}";
        System.out.println(str);
        return str;
    }

    public static String returnJsonForMap(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        try {
            if (map != null) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    sb.append("\""
                            + entry.getKey()
                            + "\":\""
                            + URLEncoder.encode(entry.getValue() == null ? ""
                            : entry.getValue().toString(), "utf-8")
                            + "\",");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e.getMessage();
            return null;
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        str += "}";
        System.out.println(str);
        return str;
    }

    /***
     * getMapByJsonStr 传入JSON 字符串对返回map对象
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static Map getMapByJsonStr(String jsonStr) {
        Map map = new HashMap();
        try {
            // jsonStr = URLDecoder.decode(jsonStr, "utf-8");
            JSONObject obj = new JSONObject(jsonStr);
            Iterator iterator = obj.keys();

            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = obj.get(key).toString();
                map.put(key, value);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        return map;
    }

    /***
     * returnResErrJson 系统运行过程中发生异返回错误信息
     *
     * @param msg
     * @return
     */
    public static String returnResErrJson(String msg) {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("{\"errorCode\":\"1\",\"errorMsg\":\""
                    + URLEncoder.encode(msg, "utf-8") + "\"");
        } catch (UnsupportedEncodingException e) {
            sb.append("{\"errorCode\":\"1\",\"errorMsg\":\"" + msg + "\"}");
            return sb.toString();
        }
        return sb.toString();
    }

    /***
     * 服务完成操作后返回结果处理成json对象的
     *
     * @param map
     * @return
     */
    public static String returnResSuccessJson(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"errorCode\":\"0\",");
        try {
            if (map != null) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    sb.append("\""
                            + entry.getKey()
                            + "\":\""
                            + URLEncoder.encode(entry.getValue() == null ? ""
                            : entry.getValue().toString(), "utf-8")
                            + "\",");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        str += "}";
        return str;
    }

    public static String makeReqStrForMaps(List<Map> params) {
        StringBuffer sb = new StringBuffer();
        String str = "";
        String toke = "";
        UserBean user = AppCfg._instance.getCurrentUser();
        if(user!=null){
            toke = user.getToken();
        }
        sb.append("{\"extend\":{\"version\":\"1.0\",\"terminal\":\"1\",\"token\":\""
                + toke + "\"},\"main\":{\"data\":[");
        if (params != null && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                StringBuffer jsonStr =new StringBuffer();
                jsonStr.append("{");
                Iterator itor = params.get(i).keySet().iterator();
                while (itor.hasNext()) {
                    String key = itor.next().toString();
                    jsonStr.append("\""+ key+ "\":\""+ params.get(i).get(key).toString() + "\",");
                }
                str = jsonStr.toString();
                str = str.substring(0, str.length() - 1)+"}";
                sb.append(str);
                sb.append(",");
            }
            str = sb.toString();
            str = str.substring(0, str.length() - 1);
            str += "]}}";
        } else {
            str = sb.toString() + "]}}";
        }
        System.out.println(str);
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
}
