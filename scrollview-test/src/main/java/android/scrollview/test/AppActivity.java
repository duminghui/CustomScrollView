package android.scrollview.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import custom.android.widget.ScrollListView;
import custom.android.widget.ScrollListView.ScrollListViewAdapter;

public class AppActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		ScrollListView slv = (ScrollListView) findViewById(R.id.slv);
		TextView tv = new TextView(this);
		tv.setText("AAAAAAAAAAAAAAAA");
		tv.setBackgroundColor(Color.GREEN);
		LinearLayout ll2 = new LinearLayout(this);
		ll2.addView(tv, 100, LayoutParams.WRAP_CONTENT);
		LinearLayout ll = new LinearLayout(this);
		TextView tv2 = new TextView(this);
		tv2.setText("vBBBBBBBBBBBBBBBBBBBBBBCCCCCCCCCCCCCCCCCCC");
		tv2.setBackgroundColor(Color.YELLOW);
		ll.addView(tv2, 500, LayoutParams.WRAP_CONTENT);
		slv.setHeaderView(ll2, ll);
		slv.setListAdapter(new ListAdpater());
	}

	public class ListAdpater extends ScrollListViewAdapter {
		@Override
		public int getCount() {
			return 50;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void setListViewData(int postion, ViewHolder viewHolder) {

		}

		@Override
		protected View createFixListView(int postion, ViewHolder viewHolder,
				ViewGroup parent) {
			LinearLayout ll = new LinearLayout(parent.getContext());

			TextView tv = new TextView(parent.getContext());
			tv.setText("AAAAAAAAAAB");
			ll.addView(tv, 100, LayoutParams.WRAP_CONTENT);
			return ll;
		}

		@Override
		protected View createMoveListView(int postion, ViewHolder viewHolder,
				ViewGroup parent) {
			TextView tv = new TextView(parent.getContext());
			tv.setText("aaaaAAAAAAAAAACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
			tv.setBackgroundColor(Color.BLUE);
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.addView(tv, 400, LayoutParams.WRAP_CONTENT);
			return ll;
		}

		@Override
		protected ViewHolder getViewHolder() {
			return null;
		}

	}
}