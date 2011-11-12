package android.scrollview.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new MyListAdapter());
	}

	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			TextView tv = null;
			for (int index = 0; index < 10; index++) {
				tv = new TextView(parent.getContext());
				tv.setText("index:" + index);
				ll.addView(tv, 50, ViewGroup.LayoutParams.WRAP_CONTENT);

			}
			return ll;
		}

	}
}