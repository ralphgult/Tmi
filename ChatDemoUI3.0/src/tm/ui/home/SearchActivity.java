package tm.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.tmdemo.R;
import com.hyphenate.tmdemo.ui.BaseActivity;



/***
 * 搜索
 * 
 * @author lenovo
 * 
 */
public class SearchActivity extends BaseActivity {

	private TextView search;
	private EditText guanjian;
	private String guanjianzi;

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
		search = (TextView)findViewById(R.id.tv_search);
		guanjian = (EditText)findViewById(R.id.et_key);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				guanjianzi=guanjian.getText().toString().trim();
				if (guanjianzi.equals("")) {
					Toast.makeText(SearchActivity.this,"搜索为空",Toast.LENGTH_SHORT).show();
		      }else{
					Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
					intent.putExtra("companyName",guanjianzi );
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void back(View view) {
		finish();
	}
}