package tm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xbh.tmi.DemoApplication;
import com.xbh.tmi.R;

public class ImageLoaders {
	// 调试
	private final static String TAG = "Rock";

	// 上下文保存
	private static Context mContext = null;

	private ImageLoaderListener mListener = null;

	// 线程池配置
	private ThreadPoolExecutor mThreadPool = null;
	// 线程最小维护队列个数
	private final static int CORE_POOL_SIZE = 3;
	// 线程最大维护队列个数
	private final static int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
	// 线程池维护线程所允许的空闲时间(s)
	private final static int KEEP_ALIVE_TIME = 60;
	// 无界限边界队列
	private LinkedBlockingQueue<Runnable> mThreadPoolQueue = null;

	// 设置网络超时
	private final static int NET_CONN_TIMEOUT = 10 * 1000;
	private final static int NET_READ_TIMEOUT = 12 * 1000;

	// 设置显示图片大小
	public final static int IMAGE_VIEW_WIDTH = 200;
	public final static int IMAGE_VIEW_HEIGHT = 200;

	// 最大SD缓存图片数量
	public final static int MAX_SD_SAVE_IMAGE_NUM = 50;

	// 图片内存缓存
	private LruCache<String, Bitmap> mImageCache = null;

	// 图片最长保存时间
	private final static long IMAGE_MAX_SAVE_TIME = 1000 * 60 * 60 * 24;

	// 图片sd存储路径
	private final static String mSavePath = "/mnt/sdcard/ImageLoader/cache/images";

	// 图片加载完成
	private final static int MSG_LOAD_IMAGE_COMPLETE = 101;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_IMAGE_COMPLETE: {
				if (mListener != null && msg.obj != null) {
					ImageInfo info = (ImageInfo) msg.obj;
					mListener.onImageLoad(info.view, info.bmp, info.url);
				}
			}
				break;
			default:
				break;
			}
		}

	};

	public static class ImageInfo {
		public View view;
		public String url;
		public Bitmap bmp;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param l
	 */
	public ImageLoaders(Context context, ImageLoaderListener l) {
		mContext = context;
		mListener = l;

		// 线程池初始化
		mThreadPoolQueue = new LinkedBlockingQueue<Runnable>();
		mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS, mThreadPoolQueue);

		// 控制缓存大小为内存的1/8
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mImageCache = new LruCache<String, Bitmap>(cacheSize) {

			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}

		};
	}

	/**
	 * 检测当前网络是否可用
	 * 
	 * @return
	 */
	private boolean isNetworkAvailable() {
		// ConnectivityManager connManager = (ConnectivityManager) mContext
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// if (connManager.getActiveNetworkInfo() != null) {
		// return connManager.getActiveNetworkInfo().isAvailable();
		// }
		return true;
	}

	/**
	 * 加载图片
	 * 
	 * @param v
	 * @param url
	 */
 	public void loadImage(final View v, final String url) {
		if (null == mThreadPool) {
			Log.d(TAG, "Thread pool err, please check");
			return;
		}
		v.refreshDrawableState();
		// 将任务加入队列
		mThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// 取图片顺序为内存-sd卡-网络下载
				Bitmap bmp = null;
				if (null != (bmp = getBitmapFromCache(url))) {
					Log.d(TAG, "find image in cache url = " + url);
					sendBitmapMessage(v, url, bmp);
				} else if (null != (bmp = getBitmapFromSD(url))) {
					Log.d(TAG, "find image in sd url = " + url);
					sendBitmapMessage(v, url, bmp);
				} else  if (null != (bmp = getBitmapFromNet(url))){
					Log.d(TAG, "find image in net url = " + url);
					sendBitmapMessage(v, url, bmp);
				}else {
					sendBitmapMessage(v,url,BitmapFactory.decodeResource(DemoApplication.applicationContext.getResources(), R.drawable.default_pic));
				}
			}

		});
	} 
	/**
	 * 加载图片
	 * 
	 * @param v
	 * @param url
	 */
	public void loadImage_default(final View v, final String url,final BitmapDrawable bmap) {
		if (null == mThreadPool) {
			Log.d(TAG, "Thread pool err, please check");
			return;
		}
		
		// 将任务加入队列
		mThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// 取图片顺序为内存-sd卡-网络下载
				Bitmap bmp = null;
				if (null != (bmp = getBitmapFromCache(url))) {
					Log.d(TAG, "find image in cache url = " + url);
					sendBitmapMessage(v, url, bmp);
				} else if (null != (bmp = getBitmapFromSD(url))) {
					Log.d(TAG, "find image in sd url = " + url);
					sendBitmapMessage(v, url, bmp);
				} else {
					if (null != (bmp = getBitmapFromNet(url))){
						Log.d(TAG, "find image in net url = " + url);
						sendBitmapMessage(v, url, bmp);
					}else{
						sendBitmapMessage(v, url, bmap.getBitmap());
					}
				}
			}
			
		});
	}

	/**
	 * 发送图片加载完成消息
	 * 
	 * @param v
	 * @param url
	 * @param bmp
	 */
	private void sendBitmapMessage(View v, String url, Bitmap bmp) {
		ImageInfo info = new ImageInfo();
		info.bmp = bmp;
		info.url = url;
		info.view = v;

		Message msg = mHandler.obtainMessage(MSG_LOAD_IMAGE_COMPLETE, info);
		mHandler.sendMessage(msg);
	}

	/**
	 * 将图片存入缓存
	 * 
	 * @param url
	 * @param bmp
	 */
	private void addBitmapToCache(String url, Bitmap bmp) {
		mImageCache.put(url, bmp);
	}

	/**
	 * 从缓存中取出图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromCache(String url) {
		return mImageCache.get(url);
	}

	/**
	 * 检测当前sd卡是否可以使用
	 * 
	 * @return
	 */
	private boolean checkSD() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * 检测sd图片缓存是否达到上限
	 */
	private void checkMaxSaveImages() {
		File dir = new File(mSavePath);
		if (dir.exists()) {
			File[] fp = dir.listFiles();
			if (null != fp) {
				if (fp.length > MAX_SD_SAVE_IMAGE_NUM) {
					Log.d(TAG, "ready to del sd cache");
					ArrayList<File> files = new ArrayList<File>();
					for (File f : fp) {
						files.add(f);
					}
					// 当达到上限时，我们将删除sd中较旧的图片删除，数量为上限的一半
					Collections.sort(files, new FileComparator());
					final int len = files.size() / 2;
					for (int i = 0; i < len; i++) {
						File f = files.get(i);
						if (f.exists()) {
							Log.d(TAG, "del file = " + f.getName());
							f.delete();
						}
					}
				}
			}
		}
	}

	/**
	 * 添加图片到sd卡中
	 * 
	 * @param url
	 * @param bmp
	 */
	private void addBitmapToSD(String url, Bitmap bmp) {
		if (!checkSD()) {
			Log.d(TAG, "sd not ready");
			return;
		}

		// 检测是否有空间
		if (!checkSpace(bmp)) {
			Log.d(TAG, "no enough space");
			return;
		}

		initFile();

		// 检测图片数量是否已经达到上限
		checkMaxSaveImages();
		String fileName = getImageName(url);

		FileOutputStream m_fileOutPutStream = null;
		try {
			m_fileOutPutStream = new FileOutputStream(mSavePath + "/"
					+ fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(CompressFormat.PNG, 100, m_fileOutPutStream);
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void initFile() {
		//为头像目录添加.nomedia文件，以便在图库不显示该目录下的文件
		File nomedia = new File(mSavePath + File.separator + ".nomedia");
		if (!nomedia.getParentFile().exists()) {
			nomedia.getParentFile().mkdirs();
		}
		if (!nomedia.exists()) {
			try {
				nomedia.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据url返回文件名
	 * 
	 * @param url
	 * @return
	 */
	private String getImageName(String url) {
		int index = url.lastIndexOf('/');
		return url.substring(index + 1);
	}

	/**
	 * 检测是否有空间存储图片
	 * 
	 * @param bmp
	 * @return
	 */
	@SuppressLint("NewApi")
	private boolean checkSpace(Bitmap bmp) {
		long size = getBitmapsize(bmp);
		long sdSize = getAvailableStore(Environment
				.getExternalStorageDirectory().getPath());

		Log.d(TAG, "size = " + size + " sd size = " + sdSize);
		if (size < sdSize) {
			return true;
		}

		return false;
	}

	/**
	 * 返回图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("NewApi")
	public long getBitmapsize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 获取本机剩余空间
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getAvailableStore(String filePath) {
		StatFs statFs = new StatFs(filePath);
		long blocSize = statFs.getBlockSize();
		long availaBlock = statFs.getAvailableBlocks();
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
	}

	/**
	 * 检测图片是否过期
	 * 
	 * @param lastmodifytime
	 * @return
	 */
	private boolean checkImageDirty(long lastmodifytime) {
		long curtime = System.currentTimeMillis();
		return (curtime - lastmodifytime) > IMAGE_MAX_SAVE_TIME ? true : false;
	}

	/**
	 * 从sd卡中获取图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromSD(String url) {
		if (!checkSD()) {
			Log.d(TAG, "sd card not ready.");
			return null;
		}

		String fileName = getImageName(url);
		File fp = new File(mSavePath + "/" + fileName);
		if (fp.exists()) {
			// 检查图片是否过期，如果过期则删除它
			if (checkImageDirty(fp.lastModified())) {
				Log.d(TAG, "image dirty, ready to del...... file = " + fileName);
				fp.delete();
				return null;
			}
			Bitmap bmp = BitmapFactory.decodeFile(mSavePath + "/" + fileName);
			if (null != bmp && !bmp.isRecycled()) {
				addBitmapToCache(url, bmp);
			}
			return bmp;
		}

		return null;
	}

	public byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 从网络获取图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromNet(String url) {
		if (!isNetworkAvailable()) {
			Log.d(TAG, "net work can not use");
			return null;
		}

		HttpURLConnection conn = null;
		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(NET_CONN_TIMEOUT);
			conn.setReadTimeout(NET_READ_TIMEOUT);
			conn.connect();
			final int responseCode = conn.getResponseCode();
			Log.d(TAG, "network conn response code = " + responseCode);
			if (200 == responseCode) {
				InputStream is = conn.getInputStream();
				byte[] bytes = readStream(is);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inSampleSize = 1;
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				final int realH = options.outHeight;
				final int realW = options.outWidth;
				Log.d(TAG, "image real w = " + realW + " h = " + realH);
				if (realH > IMAGE_VIEW_HEIGHT || realW > IMAGE_VIEW_WIDTH) {
					if (realW > realH) {
						options.inSampleSize = Math.round((float) realH
								/ (float) IMAGE_VIEW_WIDTH);
					} else {
						options.inSampleSize = Math.round((float) realW
								/ (float) IMAGE_VIEW_HEIGHT);
					}
				}
				Log.d(TAG, "new sample size = " + options.inSampleSize);
				options.inJustDecodeBounds = false;
				Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length, options);
				is.close();
				conn.disconnect();
		    	if (null != bmp && !bmp.isRecycled()) {
					addBitmapToCache(url, bmp);
					addBitmapToSD(url, bmp);
				}
                return bmp;
			}
			conn.disconnect();
		} catch (MalformedURLException e) { 
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			
		}
		return null;
	}

	public void clearCache() {
		mThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				deleteFolderFile(mSavePath, true);
			}

		});
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePath
	 * @param deleteThisPath
	 * @throws IOException
	 */
	public void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {
					file.delete();
				} else {
					if (file.listFiles().length == 0) {
						file.delete();
					}
				}
			}
		}
	}

	/**
	 * 文件排序，排序后用于删除较旧的缓存
	 */
	public class FileComparator implements Comparator<File> {
		public int compare(File file1, File file2) {
			if (file1.lastModified() < file2.lastModified()) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	// 图片载入回调
	public interface ImageLoaderListener {
		// 图片加载完成后回调
		public void onImageLoad(View v, Bitmap bmp, String url);
	}
}
