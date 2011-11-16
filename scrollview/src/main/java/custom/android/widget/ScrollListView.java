/**
 * 
 */
package custom.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * @author duminghui
 * 
 */
public class ScrollListView extends RelativeLayout
{
	private LinearLayout ll;
	private Context context;
	private View viewMoveableHeader;
	private List<View> viewMoveableListView;

	public ScrollListView(Context context)
	{
		super(context);
		this.context = context;
		initMainLayout();

	}

	public ScrollListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		initMainLayout();
	}

	private void initMainLayout()
	{
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams scrollListLp = new LayoutParams(LayoutParams.FILL_PARENT,
		        LayoutParams.FILL_PARENT);
		addView(ll, scrollListLp);
	}

	public void setHeaderView(View fixView, View moveableView,
	        int fixViewWidth, int moveableViewWidth, int itemHeight)
	{
		LinearLayout llHeader = new LinearLayout(context);
		llHeader.addView(fixView, fixViewWidth, itemHeight);
		viewMoveableHeader = moveableView;
		llHeader.addView(moveableView, moveableViewWidth, itemHeight);
		ll.addView(llHeader);

	}

	public void setListAdapter(ScrollListViewAdapter adapter)
	{
		ListView lv = new ListView(context);
		lv.setCacheColorHint(0x00000000);
		ll.addView(lv);
		lv.setAdapter(adapter);
		viewMoveableListView = adapter.getMoveViews();
	}

	public static abstract class ScrollListViewAdapter extends BaseAdapter
	{
		private List<View> lstMoveView = new ArrayList<View>();

		public List<View> getMoveViews()
		{
			return lstMoveView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder viewHolder = null;
			if (convertView == null)
			{
				viewHolder = getViewHolder();
				LinearLayout ll = new LinearLayout(parent.getContext());
				ll.addView(createFixListView(position, viewHolder, ll));
				View viewMove = createMoveListView(position, viewHolder, ll);
				lstMoveView.add(viewMove);
				ll.addView(viewMove);
				convertView = ll;
				convertView.setTag(viewHolder);
			} else
			{
				viewHolder = (ViewHolder) convertView.getTag();
				setListViewData(position, viewHolder);
			}
			return convertView;
		}

		protected abstract void setListViewData(int postion,
		        ViewHolder viewHolder);

		protected abstract View createFixListView(int postion,
		        ViewHolder viewHolder, ViewGroup parent);

		protected abstract View createMoveListView(int postion,
		        ViewHolder viewHolder, ViewGroup parent);

		protected abstract ViewHolder getViewHolder();

		public static abstract class ViewHolder
		{

		}
	}
}
