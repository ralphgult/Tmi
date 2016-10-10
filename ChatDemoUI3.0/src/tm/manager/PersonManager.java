package tm.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.hyphenate.chat.EMClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RalphGult on 8/29/2016.
 */

public class PersonManager {

    public static int TYPE_COMPNAME = 0;
    public static int TYPE_COMPINTOR = 1;
    public static int TYPE_FARMNAME = 2;
    public static int TYPE_FARMINTOR = 3;


    private PersonManager personManager = null;
    private String url = null;


    public void uploadPersonHeanPic(File file) throws IOException {
        String userId = EMClient.getInstance().getCurrentUser();
        String fileString = null;
        url  = "http://hsaiqs.xicp.net:20806/CBDParkingMSs/";
        //文件转换为String
        file = new File("/storage/emulated/0/RM/res/icon/role/1006_head.png");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();
        fileString = Base64.encodeToString(appicon,Base64.DEFAULT);

        if(null != fileString){
            //开始请求
            HttpPost httpPost = new HttpPost(url + "updatePhoto");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("file", fileString));
            params.add(new BasicNameValuePair("userId", userId));

            HttpResponse httpResponse = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                httpResponse = new DefaultHttpClient().execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    if(null != result){
                        Log.e("info","result ======= " + result);
                        JSONObject object = new JSONObject(result);
                        if(object.getInt("authId") == 0){
                            //上传成功
                        }else{

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void updateTexts(String text,int type){
        String userId = EMClient.getInstance().getCurrentUser();
        url  = "http://hsaiqs.xicp.net:20806/CBDParkingMSs/";
        String key = null;
        switch (type){
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
        if(null != text){
            HttpPost httpPost = new HttpPost(url + "updatemse");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(key, text));
            params.add(new BasicNameValuePair("userId", userId));

            HttpResponse httpResponse = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                httpResponse = new DefaultHttpClient().execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    if(null != result){
                        Log.e("info","result ======= " + result);
                        JSONObject object = new JSONObject(result);
                        if(object.getInt("authId") == 0){
                            //上传成功
                        }else{

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
