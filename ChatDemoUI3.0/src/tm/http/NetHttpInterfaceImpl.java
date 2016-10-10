package tm.http;

/**
 * Created by Administrator on 2016/9/7.
 */
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import tm.utils.ConstantsHandler;

public class NetHttpInterfaceImpl implements NetHttpInterface {

    private static final int TIME_LIMIT = 10000; // 十秒钟超时设置

    @Override
    public void commonHttpCilentForMap(final Handler handler, Context contextfinal,
                                       final String actionName, final List<Map> params) {
        try {
            final Thread thread = new Thread() {
                public void run() {
                    Map map = HttpClients.doGetForMap(actionName, params);
                    System.out.println(map != null ? map.toString() : "map为空");
                    if (map != null) {
                        if ("100".equals(map.get("status").toString())) {
                            handlerSendMsg(handler, ConstantsHandler.EXECUTE_SUCCESS, map);
                        } else {
                            handlerSendMsg(handler,ConstantsHandler.EXECUTE_FAIL,map);
                        }
                    } else {
                        handlerSendMsg(handler, ConstantsHandler.JSONPARSE,map);// 如果为NUll
                        // 则表示json解析错误
                    }
                    Thread.currentThread().interrupt();// 打断当前线程
                }
            };
            thread.start();
            connTimeout(thread, handler);// 超时处理
        } catch (Exception e) {
        }
    }
    /***
     * getArchivActivityByUserId 通过人员ID获取记录
     *
     * @param handler
     * @param contextfinal
     * @param actionName
     * @param params
     */
    public void commonHttpCilent(final Handler handler,
                                 final Context contextfinal, final String actionName,
                                 final List<NameValuePair> params) {
        try {
            final Thread thread = new Thread() {
                public void run() {
                    Map map = HttpClients.doGet(actionName, params);
                    System.out.println(map != null ? map.toString() : "map为空");
                    if (map != null) {
                        handlerSendMsg(handler,ConstantsHandler.EXECUTE_SUCCESS, map);
//                        if ("1".equals(map.get("authId").toString())) {
//                            handlerSendMsg(handler,ConstantsHandler.EXECUTE_SUCCESS, map);
//                        } else {
//                            handlerSendMsg(handler,ConstantsHandler.EXECUTE_FAIL,map);
//                        }
                    } else {
                        handlerSendMsg(handler, ConstantsHandler.JSONPARSE,map);// 如果为NUll
                        // 则表示json解析错误
                    }
                    Thread.currentThread().interrupt();// 打断当前线程
                }
            };
            thread.start();
            connTimeout(thread, handler);// 超时处理
        } catch (Exception e) {
        }
    }

    public void checkUpdateHttpCilent(final Handler handler,
                                      final Context contextfinal, final String actionName,
                                      final List<NameValuePair> params) {
        try {
            final Thread thread = new Thread() {
                public void run() {
                    Map map = HttpClients.doGet(actionName, params);
                    System.out.println(map != null ? map.toString() : "map为空");
                    if (map != null) {
                        if ("0".equals(map.get("errorCode").toString())) {
                            handlerSendMsg(handler,ConstantsHandler.EXECUTE_SUCCESS,map);
                        } else {
                            String msg = map.get("errorMsg").toString();
                            handlerSendMsg(handler,ConstantsHandler.EXECUTE_FAIL,map);
                        }
                    } else {
                        handlerSendMsg(handler, ConstantsHandler.JSONPARSE,map);// 如果为NUll
                        // 则表示json解析错误
                    }
                    Thread.currentThread().interrupt();// 打断当前线程
                }
            };
            thread.start();
            connTimeout(thread, handler);// 超时处理
        } catch (Exception e) {
        }
    }

    /***
     * connTimeout 超时处理
     *
     * @param thread
     * @param handler
     */
    public void connTimeout(final Thread thread, final Handler handler) {
        // 设定定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (thread.isAlive()) {
                    handlerSendMsg(handler, ConstantsHandler.ConnectTimeout);
                    thread.interrupt();
                }
            }
        }, TIME_LIMIT);
    }

    // handle消息的传递
    private static void handlerSendMsg(Handler handler, int code) {
        handler.sendEmptyMessage(code);
    }

    private static void handlerSendMsg(Handler handler, int code, Map map) {
        Message msg = handler.obtainMessage();
        msg.what = code;
        msg.obj = map;
        handler.sendMessage(msg);
    }



}
