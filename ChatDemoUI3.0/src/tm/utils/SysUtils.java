package tm.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SysUtils {
	/**
	 * ״̬���߶��㷨
	 *
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
	/**
	 * 设置图片显示GridView高度的方法
	 *
	 * @param view gridView的view对象
	 */
	public static void setGridViewHight(GridView view) {
		ListAdapter adapter = view.getAdapter();
		if (adapter.isEmpty()) {
			//适配器为空
			return;
		}
		ViewGroup.LayoutParams params = view.getLayoutParams();
		int totalHeight = 0;
		int size = adapter.getCount();
		for (int i = 1; i <= size; i = i + 4) {
			View listItem = adapter.getView(0, null, view);
			listItem.measure(0, 0);
			totalHeight = totalHeight + listItem.getMeasuredHeight() + 20;
		}
		params.height = totalHeight + 15;
		view.setLayoutParams(params);
	}

	/**
	 * 设置ListView的高度的方法
	 */
	public void setListViewHight(ListView view) {
        ListAdapter adapter = view.getAdapter();
        if (adapter.isEmpty()) {
            //适配器为空
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int totalHeight = 0;
        int size = adapter.getCount();
        View listItem = adapter.getView(0, null, view);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight() * size;
        params.height = totalHeight + 15;
        view.setLayoutParams(params);
    }
}
