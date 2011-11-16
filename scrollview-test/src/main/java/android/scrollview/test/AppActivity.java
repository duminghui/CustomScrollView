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

public class AppActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		ScrollListView slv = (ScrollListView) findViewById(R.id.slv);
		TextView tv = new TextView(this);
		tv.setText("AAAAAAAAAAAAAAAA");
		tv.setBackgroundColor(Color.GREEN);
		TextView tv2 = new TextView(this);
		tv2.setText("vBBBBBBBBBBBBBBBBBBBBBBCCCCCCCCCCCCCCCCCCC");
		tv2.setBackgroundColor(Color.YELLOW);
		slv.setHeaderView(tv, tv2, 150, 250, 50);
		slv.setListAdapter(new ListAdpater());
	}

	public class ListAdpater extends ScrollListViewAdapter
	{
		@Override
		public int getCount()
		{
			return 1;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected void setListViewData(int postion, ViewHolder viewHolder)
		{

		}

		@Override
		protected View createFixListView(int postion, ViewHolder viewHolder,
		        ViewGroup parent)
		{
			TextView tv = new TextView(parent.getContext());
			tv.setText("AAAAAAAAAAB");
			return tv;
		}

		@Override
		protected View createMoveListView(int postion, ViewHolder viewHolder,
		        ViewGroup parent)
		{
			TextView tv = new TextView(parent.getContext());
			tv.setText("aaaaAAAAAAAAAACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.addView(tv, 400, LayoutParams.WRAP_CONTENT);
			return ll;
		}

		@Override
		protected ViewHolder getViewHolder()
		{
			// TODO Auto-generated method stub
			return null;
		}

	}
}