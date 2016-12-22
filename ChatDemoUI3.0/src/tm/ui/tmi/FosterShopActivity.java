package tm.ui.tmi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.xbh.tmi.R;

/**
 * Created by Lking on 2016/12/22.
 */

public class FosterShopActivity extends Activity {
    String name;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_shopping_activity);

        getIntentString();
        Toast.makeText(this,"m = "+name+",p ="+price,Toast.LENGTH_SHORT).show();
    }

    private void getIntentString(){
        if(null != getIntent().getExtras()){
            name  = getIntent().getExtras().getString("name");
            price  = getIntent().getExtras().getString("price");
        }
    }
}
