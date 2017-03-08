package tm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

/**
 * 图片工具类
 */
public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();

    public static final int RESIZE_MODE_WIDTH_LIMIT = 1; // 压缩图片时在宽度上限制
    public static final int RESIZE_MODE_HEIGHT_LIMIT = 2; // 压缩图片时在高度上限制


    /**
     * 等比例压缩图片
     * 1：如果图片的宽和高都小于目标压缩图片的宽和高，那么不做任何操作
     * 2：如果图片的宽和高任何一个大于目标压缩图片的宽和高，那么以最大边为基准来计算压缩比，然后等比例压缩图片
     *
     * @param srcFilePath    源图片的路径
     * @param resampleWidth  压缩的目标宽度
     * @param resampleHeight 压缩的目标高度
     * @return bitmap 缩小后的图片对象， 使用完要用recycle方法释放空间
     */
    public static Bitmap resizeBitmap(String srcFilePath, int resampleWidth, int resampleHeight) {
        if (srcFilePath == null)
            throw new IllegalArgumentException("Image file path should not be null");

        Bitmap taget = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPurgeable = true;
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcFilePath, opts);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = getClosestResampleSize(opts.outWidth, opts.outHeight, Math.max(resampleWidth, resampleHeight));
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(srcFilePath, opts);
            if (bitmap == null) {
                return taget;
            }
            // 拿到图片的旋转值：为了满足有的机型
//            int degree = getExifOrientation(srcFilePath);
            Matrix m = new Matrix();

            if (bitmap.getWidth() > resampleWidth || bitmap.getHeight() > resampleHeight) {
                float qw = ((float) resampleWidth) / bitmap.getWidth();
                float qh = ((float) resampleHeight) / bitmap.getHeight();
                if (qh < qw) {
                    float width01 = qh * bitmap.getWidth();
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) width01, (int) resampleHeight, true);
                } else {
                    float height01 = qw * bitmap.getHeight();
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) resampleWidth, (int) height01, true);
                }
            }

//            if (0 != degree) {
//                m.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
//            }
            taget = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (taget != bitmap) {
                safeReleaseBitmap(bitmap);
            }

        } catch (OutOfMemoryError e) {
            Log.e(TAG, "resizeBitmap -- outofMemoryError.", e);
        }
        return taget;
    }

    /**
     * 压缩图片
     *
     * @param srcFilePath
     * @param resampleWidth
     * @param resampleHeight
     * @return
     */
    public static Bitmap resizeBitmapForce(String srcFilePath, int resampleWidth, int resampleHeight) {
        if (srcFilePath == null)
            throw new IllegalArgumentException("Image file path should not be null");

        Bitmap taget = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPurgeable = true;
        opts.inJustDecodeBounds = true; // 为true那么将不返回实际的bitmap
        BitmapFactory.decodeFile(srcFilePath, opts);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = getClosestResampleSize(opts.outWidth, opts.outHeight, Math.max(resampleWidth, resampleHeight));
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(srcFilePath, opts);
            if (bitmap == null) {
                return taget;
            }
            // 拿到图片的旋转值：为了满足有的机型
            Matrix m = new Matrix();

            float qw = ((float) resampleWidth) / bitmap.getWidth();
            float qh = ((float) resampleHeight) / bitmap.getHeight();
            if (qh < qw) {
                float width01 = qh * bitmap.getWidth();
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) width01, (int) resampleHeight, true);
            } else {

                float height01 = qw * bitmap.getHeight();
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) resampleWidth, (int) height01, true);
            }
            taget = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (taget != bitmap) {
                safeReleaseBitmap(bitmap);
            }

        } catch (OutOfMemoryError e) {
            Log.e(TAG, "resizeBitmap outofMemoryError.", e);
        }
        return taget;
    }

    /*
     * 计算需要压缩的尺寸
     */
    private static int getClosestResampleSize(int cw, int ch, int maxDim) {
        int max = Math.max(cw, ch);

        int resample = 1;
        for (resample = 1; resample < Integer.MAX_VALUE; resample++) {
            if (resample * maxDim > max) {
                resample--;
                break;
            }
        }

        if (resample > 0) {
            return resample;
        }
        return 1;
    }

    /*
     * 检查图片是否需要旋转
     */
    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 将图片保存为文件,并返回图片的文件
     *
     * @param bmp         要保存的图片对象
     * @param dstFilePath 要保存的文件路径
     * @return File 被保存的图片文件
     */
    public static File saveBitmap(Bitmap bmp, String dstFilePath) throws IOException {
        if (bmp == null)
            throw new IllegalArgumentException("Image should not be null");

        if (dstFilePath == null)
            throw new IllegalArgumentException("File path should not be null");

        File file = new File(dstFilePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);

        if (bmp != null && fos != null) {
            BufferedOutputStream bos = new BufferedOutputStream(fos, 4096);
            bmp.compress(CompressFormat.JPEG, 100, bos);
            bos.close();
            fos.close();
            safeReleaseBitmap(bmp);
        }

        return file;
    }

    /**
     * 用于保存sticker的缩略图的图片信息
     *
     * @param bmp
     * @param dstFilePath
     * @return
     * @throws IOException
     */
    public static File saveStickerBitmap(Bitmap bmp, String dstFilePath) throws IOException {
        if (bmp == null)
            throw new IllegalArgumentException("Image should not be null");
        if (dstFilePath == null)
            throw new IllegalArgumentException("File path should not be null");

        File file = new File(dstFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);

        if (bmp != null && fos != null) {
            BufferedOutputStream bos = new BufferedOutputStream(fos, 4096);
            bmp.compress(CompressFormat.PNG, 100, bos);
            bos.close();
            fos.close();
        }
        return file;
    }

    /**
     * 安全释放Bitmap占用的资源
     *
     * @param bitmap
     */
    public static void safeReleaseBitmap(Bitmap bitmap) {
        if (null == bitmap)
            return;

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;
    }

    /**
     * 压缩并保存上传的图片（以原格式进行保存，jpg\png\bmp）,并返回文件
     *
     * @param path      保存图片的路径
     * @param imagePath 本地图片的路径
     * @return 图片文件对象
     */
    public static File saveUploadImage(String path, String imagePath) {
        return saveCommonImage(path, imagePath, 0, 0);
    }

    private static void writeWord(FileOutputStream stream, int value) throws IOException {
        byte[] b = new byte[2];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        stream.write(b);
    }

    private static void writeDword(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        b[2] = (byte) (value >> 16 & 0xff);
        b[3] = (byte) (value >> 24 & 0xff);
        stream.write(b);
    }

    private static void writeLong(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        b[2] = (byte) (value >> 16 & 0xff);
        b[3] = (byte) (value >> 24 & 0xff);
        stream.write(b);
    }

    /**
     * 压缩本地图片上传（以原格式进行保存，jpg\png\bmp）,并返回文件
     *
     * @param path      保存图片的路径
     * @param imagePath 本地图片的路径
     * @param with      图片宽度
     * @param height    图片高度
     * @return 图片文件对象
     */
    public static File saveCommonImage(String path, String imagePath, int with, int height) {
        File imgFile = new File(imagePath);
        String fileName = imgFile.getName();
        File fileSaved = null;
        Bitmap picBitmap = null;
        FileOutputStream fileOutputStream = null;
        if (with == 0 && height == 0) {
            picBitmap = resizeBitmap(imagePath, 720, 1280);
        } else {
            picBitmap = resizeBitmap(imagePath, with, height);
        }
        try {
            fileSaved = new File(path);
            if (!fileSaved.getParentFile().exists()) {
                fileSaved.getParentFile().mkdirs();
            }
            String ex = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
            fileOutputStream = new FileOutputStream(fileSaved);
            if (ex.equals("jpg") || ex.equals("jpeg")) {
                picBitmap.compress(CompressFormat.JPEG, 80, fileOutputStream);
            } else if (ex.equals("png")) {
                picBitmap.compress(CompressFormat.PNG, 80, fileOutputStream);
            } else if (ex.equals("bmp")) {
                // 位图大小
                int nBmpWidth = picBitmap.getWidth();
                int nBmpHeight = picBitmap.getHeight();
                // 图像数据大小
                int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

                int bfType = 0x4d42;
                long bfSize = 14 + 40 + bufferSize;
                int bfReserved1 = 0;
                int bfReserved2 = 0;
                long bfOffBits = 14 + 40;
                //保存bmp文件头
                writeWord(fileOutputStream, bfType);
                writeDword(fileOutputStream, bfSize);
                writeWord(fileOutputStream, bfReserved1);
                writeWord(fileOutputStream, bfReserved2);
                writeDword(fileOutputStream, bfOffBits);
                //bmp信息头
                long biSize = 40L;
                long biWidth = nBmpWidth;
                long biHeight = nBmpHeight;
                int biPlanes = 1;
                int biBitCount = 24;
                long biCompression = 0L;
                long biSizeImage = 0L;
                long biXpelsPerMeter = 0L;
                long biYPelsPerMeter = 0L;
                long biClrUsed = 0L;
                long biClrImportant = 0L;
                // 保存bmp信息头
                writeDword(fileOutputStream, biSize);
                writeLong(fileOutputStream, biWidth);
                writeLong(fileOutputStream, biHeight);
                writeWord(fileOutputStream, biPlanes);
                writeWord(fileOutputStream, biBitCount);
                writeDword(fileOutputStream, biCompression);
                writeDword(fileOutputStream, biSizeImage);
                writeLong(fileOutputStream, biXpelsPerMeter);
                writeLong(fileOutputStream, biYPelsPerMeter);
                writeDword(fileOutputStream, biClrUsed);
                writeDword(fileOutputStream, biClrImportant);
                //像素扫描
                byte bmpData[] = new byte[bufferSize];
                int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
                for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                    for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                        int clr = picBitmap.getPixel(wRow, nCol);
                        bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                        bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color.green(clr);
                        bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color.red(clr);
                    }
                fileOutputStream.write(bmpData);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                    fileOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != picBitmap && !picBitmap.isRecycled()) {
                safeReleaseBitmap(picBitmap);
            }
        }
        return fileSaved;
    }

}
