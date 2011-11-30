package android.scrollview.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import custom.android.widget.ScrollListView;
import custom.android.widget.ScrollListView.ScrollListViewAdapter;
import custom.android.widget.ScrollListView.ViewHolder;

public class AppActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		ScrollListView slv = (ScrollListView) findViewById(R.id.slv);
		slv.setAdapter(new ListAdpater(this));
	}

	public static class ListAdpater extends
	        ScrollListViewAdapter<ListViewHolder>
	{
		private Context context;

		public ListAdpater(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount()
		{
			return 50;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		protected void setListViewData(int postion, ListViewHolder viewHolder)
		{

		}

		private ListView lv = null;

		@Override
		protected View createHeaderFixView()
		{
			TextView tv = new TextView(context);
			tv.setText("AAAAAAAAAAAAAAAA");
			tv.setBackgroundColor(Color.GREEN);
			LinearLayout ll2 = new LinearLayout(context);
			ll2.addView(tv, 100, LayoutParams.WRAP_CONTENT);
			return ll2;
		}

		@Override
		protected View createHeaderMoveView()
		{
			LinearLayout ll = new LinearLayout(context);
			TextView tv2 = null;
			for (int index = 0; index < 5; index++)
			{
				tv2 = new TextView(context);
				tv2.setText("title:" + index);
				tv2.setBackgroundColor(Color.YELLOW);
				ll.addView(tv2, 150, LayoutParams.WRAP_CONTENT);
			}

			return ll;
		}

		@Override
		protected ListView createListView()
		{
			lv = new ListView(context);
			lv.setDividerHeight(1);
			lv.setCacheColorHint(0x00000000);
			lv.setVerticalScrollBarEnabled(false);
			lv.setFadingEdgeLength(0);
			LayoutParams lp = lv.getLayoutParams();
			if (lp == null)
			{
				lp = new LayoutParams(LayoutParams.FILL_PARENT, 100);
				lv.setLayoutParams(lp);
			}

			return lv;
		}

		@Override
		protected View createListItemFixView(int postion,
		        ListViewHolder viewHolder)
		{
			LinearLayout ll = new LinearLayout(context);

			TextView tv = new TextView(context);
			tv.setText("Text1");
			ll.addView(tv, 100, LayoutParams.WRAP_CONTENT);
			return ll;
		}

		@Override
		protected View createListItemMoveView(int postion,
		        ListViewHolder viewHolder)
		{
			LinearLayout ll = new LinearLayout(context);
			TextView tv2 = null;
			for (int index = 0; index < 5; index++)
			{
				tv2 = new TextView(context);
				tv2.setText("content:" + index);
				tv2.setBackgroundColor(Color.YELLOW);
				ll.addView(tv2, 150, LayoutParams.WRAP_CONTENT);
			}
			return ll;
		}

		@Override
		protected ListViewHolder getViewHolder(int position)
		{
			return null;
		}

		@Override
		protected ViewGroup createHeaderLayout()
		{
			LinearLayout ll = new LinearLayout(context);
			return ll;
		}

		@Override
		protected ViewGroup createListItemLayout()
		{
			LinearLayout ll = new LinearLayout(context);
			return ll;
		}
	}

	public static class ListViewHolder extends ViewHolder
	{

	}
}