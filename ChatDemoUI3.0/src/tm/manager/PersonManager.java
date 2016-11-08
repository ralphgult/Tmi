package tm.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.hyphenate.chat.EMClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ContentBody;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;
import tm.http.Config;

/**
 * Created by RalphGult on 8/29/2016.
 */

public class PersonManager {

    public static int TYPE_COMPNAME = 0;
    public static int TYPE_COMPINTOR = 1;
    public static int TYPE_FARMNAME = 2;
    public static int TYPE_FARMINTOR = 3;
    public static int UPLOAD_HEADICON_SUCESS = 1001;
    public static int UPLOAD_HEADICON_ERROR = 1002;



    /**
     * 上传头像
     *
     * @param file    头像文件
     * @param userId  用户Id
     * @param handler handler
     */
    public static void SubmitPost(File file, String userId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_UPDATE_IMAGE);
            FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("file", bin);//file1为请求后台的File upload;属性
            reqEntity.addPart("userId", new StringBody(userId));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据

                if (null != handler) {
                    if (object.getInt("authId") == 1) {
                        handler.sendEmptyMessage(UPLOAD_HEADICON_SUCESS);
                    }else{
                        handler.sendEmptyMessage(UPLOAD_HEADICON_ERROR);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(UPLOAD_HEADICON_ERROR);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
    }

    public static void updateTexts(String text, int type, Handler handler) {
        String userId = EMClient.getInstance().getCurrentUser();
        String key = null;
        switch (type) {
            case 0:
                key = "companyName";
                break;
            case 1:
                key = "companyIntroduction";
                break;
            case 2:
                key = "farmName";
                break;
            case 3:
                key = "farmIntroduction";
                break;
        }
        if (null != text) {
            HttpPost httpPost = new HttpPost(Config.URL_CHANGE_INFO);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(key, text));
            params.add(new BasicNameValuePair("userId", userId));

            HttpResponse httpResponse = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                httpResponse = new DefaultHttpClient().execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    if (null != result) {
                        Log.e("info", "result ======= " + result);
                        JSONObject object = new JSONObject(result);
                        if (object.getInt("authId") == 0) {
                            //上传成功
                            Log.e("info","change Sucess");
                            if(null != handler){
                                Message msg = new Message();
                                msg.what = 1001;
                                msg.arg1 = type;
                                handler.sendMessage(msg);
                            }
                        } else {
                            Log.e("info","change Sucess");
                            if(null != handler){
                                Message msg = new Message();
                                msg.what = 1002;
                                msg.arg1 = type;
                                msg.obj = text;
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(null != handler){
                    Message msg = new Message();
                    msg.what = 1002;
                    msg.arg1 = type;
                    handler.sendMessage(msg);
                }
            }
        }
    }
}
