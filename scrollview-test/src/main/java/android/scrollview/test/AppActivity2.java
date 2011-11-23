package android.scrollview.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppActivity2 extends Activity {

	// private int[][] datas = { { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
	// { 21, 22, 23, 24, 25, 26, 27, 28, 29 },
	// { 31, 32, 33, 34, 35, 36, 37, 38, 39 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 51, 52, 53, 54, 55, 56, 57, 58, 59 },
	// { 61, 62, 63, 64, 65, 66, 67, 68, 69 },
	// { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
	// { 21, 22, 23, 24, 25, 26, 27, 28, 29 },
	// { 31, 32, 33, 34, 35, 36, 37, 38, 39 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 51, 52, 53, 54, 55, 56, 57, 58, 59 },
	// { 61, 62, 63, 64, 65, 66, 67, 68, 69 },
	// { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
	// { 21, 22, 23, 24, 25, 26, 27, 28, 29 },
	// { 31, 32, 33, 34, 35, 36, 37, 38, 39 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 41, 42, 43, 44, 45, 46, 47, 48, 49 },
	// { 51, 52, 53, 54, 55, 56, 57, 58, 59 },
	// { 61, 62, 63, 64, 65, 66, 67, 68, 69 } };

	private int[][] datas = { { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			{ 21, 22, 23, 24, 25, 26, 27, 28, 29 }, };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView lv = (ListView) findViewById(R.id.lv);
		final MyListAdapter adapter = new MyListAdapter();
		lv.setAdapter(adapter);
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int index = 0; index < datas.length; index++) {
					for (int index_ = 0; index_ < datas[index].length; index_++) {
						datas[index][index_] = datas[index][index_] + 1;
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.length;
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
			ViewHolder viewHolder = null;
			int size = datas[position].length;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LinearLayout ll = new LinearLayout(parent.getContext());
				TextView tv = null;
				viewHolder.tvs = new TextView[size];
				for (int index = 0; index < size; index++) {
					tv = new TextView(parent.getContext());
					ll.addView(tv, 100, ViewGroup.LayoutParams.WRAP_CONTENT);
					viewHolder.tvs[index] = tv;
				}
				convertView = ll;
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			for (int index = 0; index < size; index++) {
				viewHolder.tvs[index].setText("index:" + position + ","
						+ datas[position][index]);
			}
			return convertView;
		}

		private class ViewHolder {
			private TextView[] tvs;
		}
	}
}