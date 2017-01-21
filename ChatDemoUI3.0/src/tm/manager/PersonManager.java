package tm.manager;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xbh.tmi.DemoApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import internal.org.apache.http.entity.mime.HttpMultipartMode;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;
import tm.http.Config;
import tm.utils.ImageUtil;

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
    private static int MOMENTLIKE_SECUSS = 2001;
    private static int MOMENTLIKE_FAIL = 2002;
    private static int MOMENTCOMMENT_SECUSS = 3001;
    private static int MOMENTCOMMENT_FAIL = 3002;


    /**
     * 上传头像
     *
     * @param file    头像文件
     * @param userId  用户Id
     * @param type    上传类型（1：头像；2：企业Logo；3：三农Logo）
     * @param handler handler
     */
    public static void SubmitPost(File file, String userId, int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        String url = null;
        switch (type) {
            case 1:
                url = Config.URL_UPDATE_IMAGE;
                break;
            case 2:
                url = Config.URL_CHANGE_COMP_LOGO;
                break;
            case 3:
                url = Config.URL_CHANGE_FARM_LOGO;
                break;
        }
        try {
            HttpPost httppost = new HttpPost(url);
            FileBody bin = new FileBody(ImageUtil.saveUploadImage("/mnt/sdcard/ImageLoader/cache/images" + File.separator + file.getName(), file.getPath()));
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            reqEntity.addPart("file", bin);//file1为请求后台的File upload;属性
            reqEntity.addPart("userId", new StringBody(userId, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据

                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        handler.sendEmptyMessage(UPLOAD_HEADICON_SUCESS);
                    } else {
                        handler.sendEmptyMessage(UPLOAD_HEADICON_ERROR);
                    }
                }
            } else {
                handler.sendEmptyMessage(UPLOAD_HEADICON_ERROR);
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

    public static void momentLike(String momentId, Handler handler, int position) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_MOMENT_LIKE);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("moodId", new StringBody(momentId, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        Message msg = new Message();
                        msg.what = MOMENTLIKE_SECUSS;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(MOMENTLIKE_FAIL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(MOMENTLIKE_FAIL);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
    }

    public static void momentComment(String momentId, String content, Handler handler, int position) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_MOMENT_COMMENT);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("moodId", new StringBody(momentId, Charset.forName("UTF-8")));
            reqEntity.addPart("comment", new StringBody(content, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        Message msg = new Message();
                        msg.what = MOMENTCOMMENT_SECUSS;
                        msg.arg1 = position;
                        msg.obj = content;
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(MOMENTCOMMENT_FAIL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(MOMENTCOMMENT_FAIL);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
    }

    public static void publishMoment(String content, List<String> filePaths, int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        String url = null;
        int ty = -1;
        switch (type) {
            case 4:
                url = Config.URL_ADD_MOMENT;
                ty = 1;
                break;
            default:
                url = Config.URL_ADD_INFOMATION;
                ty = type;
                break;
        }
        try {
            HttpPost httppost = new HttpPost(url);
            FileBody bin = null;
            File file = null;
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            for (String path : filePaths) {
                file = new File(path);
                bin = new FileBody(ImageUtil.saveUploadImage("/mnt/sdcard/ImageLoader/cache/images" + File.separator + file.getName(), path));
                reqEntity.addPart("file", bin);//file1为请求后台的File upload;属性
            }
            SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("moodContent", new StringBody(content, Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(ty), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        handler.sendEmptyMessage(UPLOAD_HEADICON_SUCESS);
                    } else {
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

    public static void addGoods(String name, String price, String count, String oldPri, String intro, List<String> imgs, int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_ADD_GOODS);
            FileBody bin = null;
            File file = null;
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            for (String path : imgs) {
                file = new File(path);
                bin = new FileBody(ImageUtil.saveUploadImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImageLoader/cache/images" + File.separator + file.getName(), path));
                reqEntity.addPart("file", bin);//file1为请求后台的File upload;属性
            }
            file = new File(imgs.get(0));
            reqEntity.addPart("f", new FileBody(ImageUtil.saveUploadImage("/mnt/sdcard/ImageLoader/cache/images" + File.separator + file.getName(), imgs.get(0))));
            SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("originalPrice", new StringBody(oldPri, Charset.forName("UTF-8")));
            reqEntity.addPart("currentPrice", new StringBody(price, Charset.forName("UTF-8")));
            reqEntity.addPart("count", new StringBody(count, Charset.forName("UTF-8")));
            reqEntity.addPart("goodName", new StringBody(name, Charset.forName("UTF-8")));
            reqEntity.addPart("goodProfiles", new StringBody(intro, Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        handler.sendEmptyMessage(1001);
                    } else {
                        handler.sendEmptyMessage(1002);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateGoods(String id, String name, String price, String count, String intro, String oldPri, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_UPDATE_GOODS);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            reqEntity.addPart("goodId", new StringBody(id, Charset.forName("UTF-8")));
            reqEntity.addPart("originalPrice", new StringBody(oldPri, Charset.forName("UTF-8")));
            reqEntity.addPart("currentPrice", new StringBody(price, Charset.forName("UTF-8")));
            reqEntity.addPart("count", new StringBody(count, Charset.forName("UTF-8")));
            reqEntity.addPart("goodName", new StringBody(name, Charset.forName("UTF-8")));
            reqEntity.addPart("goodProfiles", new StringBody(intro, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") == 1) {
                        handler.sendEmptyMessage(1001);
                    } else {
                        handler.sendEmptyMessage(1002);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteGoodsImg(String imgId, final int position, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_DEL_GOODS_IMG);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        try {
            reqEntity.addPart("giId", new StringBody(String.valueOf(imgId), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2001;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(2002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(4002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void addGoodsImage(String path, String goodsId, Handler handler) {
        File file = new File(path);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_ADD_GOODS_IMG);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(2), Charset.forName("UTF-8")));
            reqEntity.addPart("goodId", new StringBody(goodsId, Charset.forName("UTF-8")));
            reqEntity.addPart("file", new FileBody(ImageUtil.saveUploadImage("/mnt/sdcard/ImageLoader/cache/images" + File.separator + file.getName(), file.getPath())));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") == 1) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 3001;
                            msg.arg1 = object.getInt("giId");
                            msg.obj = object.getString("imgUrl");
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Fail");
                        if (null != handler) {
                            handler.sendEmptyMessage(3002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(3002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void deleteGoods(String goodsId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_DELETE_GOODS);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        try {
            reqEntity.addPart("ids", new StringBody("159,160", Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") > 0) {
                        if (null != handler) {
                            handler.sendEmptyMessage(4001);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(4002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(4002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void updateTexts(String text, int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(Config.URL_CHANGE_SIGN);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
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
            SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart(key, new StringBody(text, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2001;
                            msg.arg1 = type;
                            msg.obj = text;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2002;
                            msg.arg1 = type;
                            msg.obj = text;
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
    }

    public static void uploadImgwall(String filePath, int type, Handler handler) {
        File file = new File(filePath);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_CHANGE_FACEWALL);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            reqEntity.addPart("file", new FileBody(ImageUtil.saveUploadImage("/mnt/sdcard/ImageLoader/cache/images" + File.separator + file.getName(), file.getPath())));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") == 1) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 3001;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Fail");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 3002;
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(3002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void delFaceWallPic(String imgPath, int id, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_DEL_FACEWALL);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        try {
            reqEntity.addPart("fsId", new StringBody(String.valueOf(id), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("result") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 4001;
                            msg.arg1 = id;
                            msg.obj = imgPath;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(4002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(4002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }


    public static void sendFosterInfo(int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_FOSTER_AGRICULTURAL);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                String jsonStr = EntityUtils.toString(response.getEntity());
//                JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (null != handler) {
                        Message msg = new Message();
                        msg.what = 3001;
                        msg.obj = jsonStr;
                        handler.sendMessage(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(3002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /**
     * 获取视频
     * @param type 类型（1：企业；2：三农）
     * @param handler
     */
    public static void getVedio(int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.RUL_GET_VEDIO);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            Log.e("LKing--->","获取视频type = "+new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (null != handler) {
                        if (object.getInt("state") == 1) {
                            Message msg = new Message();
                            msg.what = 5001;
                            msg.obj = object.getString("video");
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(5002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(5002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }
    /**
     * 获取收货地址
     *
     * @param raId    收货地址的ID（若获取地址列表，则传空值）
     * @param handler
     */
    public static void getAddresses(String raId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_GET_ADDRESS);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            if (TextUtils.isEmpty(raId)) {
                reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            } else {
                reqEntity.addPart("raId", new StringBody(raId, Charset.forName("UTF-8")));
            }
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("state") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 1001;
                            msg.obj = object.getJSONArray("address");
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /**
     * 添加/修改收货地址
     *
     * @param addrMap 收货地址的数据集合
     * @param handler handler
     */
    public static void addEditAddress(Map<String, String> addrMap, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_ADD_ADDRESS);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            if (!TextUtils.isEmpty(addrMap.get("id"))) {
                reqEntity.addPart("raId", new StringBody(addrMap.get("id"), Charset.forName("UTF-8")));
                reqEntity.addPart("isDefault", new StringBody(addrMap.get("default"), Charset.forName("UTF-8")));
            } else {
                reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            }
            reqEntity.addPart("address", new StringBody(addrMap.get("content"), Charset.forName("UTF-8")));
            reqEntity.addPart("postcode", new StringBody("", Charset.forName("UTF-8")));
            reqEntity.addPart("name", new StringBody(addrMap.get("name"), Charset.forName("UTF-8")));
            reqEntity.addPart("phone", new StringBody(addrMap.get("phone"), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 1001;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /**
     * 获取商品详情
     *
     * @param goodsId
     * @param handler
     */
    public static void getGoods(String goodsId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_GET_GOODS);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        try {
            reqEntity.addPart("goodId", new StringBody(goodsId, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("state") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 1001;
                            msg.obj = object;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void addShopCar(String goodsId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.RUL_ADD_SHOPPINGCAR);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("goodId", new StringBody(goodsId, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") > 0) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2001;
                            msg.obj = object;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /**
     * 设置默认地址
     *
     * @param raId    收货地址的ID
     * @param handler
     */
    public static void setAddressDefault(String raId, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.URL_SET_ADD_DEFAULT);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("raid", new StringBody(raId, Charset.forName("UTF-8")));
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                Log.e("info","result ============= " + object.toString());
                if (null != handler) {
                    if (object.getInt("authId") == 1) {
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2001;
                            msg.obj = raId;
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    public static void addVedio(File file, int type, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.RUL_ADD_VEDIO);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("type", new StringBody(String.valueOf(type), Charset.forName("UTF-8")));
            reqEntity.addPart("file", new FileBody(file));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                if (null != handler) {
                    if (object.getInt("authId") == 1) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 6001;
                            msg.obj = object.getString("videoUrl");
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Fail");
                        if (null != handler) {
                            handler.sendEmptyMessage(6002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(6003);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }
    public static void getShoppingList(Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.RUL_GET_SHOPPINGCAR_LIST);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("page", new StringBody("0", Charset.forName("UTF-8")));
            reqEntity.addPart("num", new StringBody("50", Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                Log.e("info", "result ================= " + object.toString());
                if (null != handler) {
                    if (object.getInt("state") == 1) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 1001;
                            msg.obj = object.getJSONArray("goods");
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Fail");
                        if (null != handler) {
                            handler.sendEmptyMessage(1002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(1002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /**
     * 删除购物车
     * @param map 购物车商品对象
     * @param handler handler
     */
    public static void delShoppingList(Map<String, String> map, Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.RUL_DEL_SHOPPINGCAR);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        try {
            reqEntity.addPart("scId", new StringBody(map.get("scId"), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                HttpEntity resEntity = response.getEntity();
                JSONObject object = new JSONObject(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                Log.e("info", "result ================= " + object.toString());
                if (null != handler) {
                    if (object.getInt("authId") == 1) {
                        //上传成功
                        Log.e("info", "change Sucess");
                        if (null != handler) {
                            Message msg = new Message();
                            msg.what = 2001;
                            msg.obj = map;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.e("info", "change Fail");
                        if (null != handler) {
                            handler.sendEmptyMessage(2002);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != handler) {
                handler.sendEmptyMessage(2002);
            }
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
    }

    /** 获取竞拍商品列表*/
    public static void getAuctionList(final Handler handler) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.AUCTION_SHOPPING_LIST);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                String jsonStr = EntityUtils.toString(response.getEntity());
                    if (null != handler) {
                        Message msg = new Message();
                        msg.what = 3001;
                        msg.obj = jsonStr;
                        handler.sendMessage(msg);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /** 获取竞拍商品详情*/
    public static void getAuctionDetail(final Handler handler,final String id) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.AUCTION_SHOPPING_DETAIL);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        SharedPreferences sharedPre = DemoApplication.applicationContext.getSharedPreferences("config", DemoApplication.applicationContext.MODE_PRIVATE);
        try {
            reqEntity.addPart("userId", new StringBody(sharedPre.getString("username", ""), Charset.forName("UTF-8")));
            reqEntity.addPart("id",new StringBody(id, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                System.out.println("服务器正常响应.....");
                String jsonStr = EntityUtils.toString(response.getEntity());
                if (null != handler) {
                    Message msg = new Message();
                    msg.what = 3001;
                    msg.obj = jsonStr;
                    handler.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
