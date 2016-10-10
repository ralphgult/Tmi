package tm.ui.home;

import android.os.Bundle;
import android.view.View;
import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;



/***
 * 搜索
 * 
 * @author lenovo
 * 
 */
public class SearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tm_search_aty);
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void init() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void back(View view) {
		finish();
	}
}