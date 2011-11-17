/**
 * 
 */
package custom.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import custom.android.widget.ScrollListView.ScrollListViewAdapter.ViewHolder;

/**
 * @author duminghui
 * 
 */
public class ScrollListView extends RelativeLayout
{
	private LinearLayout ll;
	private Context context;
	private int iFixViewWidth;
	private int iMoveableViewWidth;
	private View viewMoveableHeader;
	private List<View> viewMoveableListViews;
	private int iListViewHeight = -2;
	private Drawable listViewDivider = null;
	private int listViewDividerHeight = 1;
	private int moveSlop = 5;

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
		setBaseAttributes(context, attrs);
		initMainLayout();
	}

	private void setBaseAttributes(Context c, AttributeSet attrs)
	{
		TypedArray a = c.obtainStyledAttributes(attrs,
		        R.styleable.ScrollListView);
		iListViewHeight = a.getLayoutDimension(
		        R.styleable.ScrollListView_listview_height, -2);
		listViewDivider = a
		        .getDrawable(R.styleable.ScrollListView_listview_divider);
		listViewDividerHeight = a.getDimensionPixelSize(
		        R.styleable.ScrollListView_listview_dividerHeight, 1);
		a.recycle();
	}

	private void initMainLayout()
	{
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams scrollListLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		        LayoutParams.WRAP_CONTENT);
		addView(ll, scrollListLp);
	}

	private void setHeaderView(
	        ScrollListViewAdapter<? extends ViewHolder> adapter)
	{
		LinearLayout llHeader = new LinearLayout(context);
		View fixView = adapter.createTitleFixView(context);
		fixView.measure(
		        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		this.iFixViewWidth = fixView.getMeasuredWidth();
		View moveableView = adapter.createTitleMoveView(context);
		moveableView.measure(
		        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		this.iMoveableViewWidth = moveableView.getMeasuredWidth();
		llHeader.addView(fixView);
		viewMoveableHeader = moveableView;
		llHeader.addView(moveableView);
		ll.addView(llHeader);
	}

	public void setAdapter(ScrollListViewAdapter<? extends ViewHolder> adapter)
	{
		setHeaderView(adapter);
		ListView lv = new ListView(context);
		if (listViewDivider != null)
		{
			lv.setDivider(listViewDivider);
		}
		lv.setDividerHeight(listViewDividerHeight);
		lv.setCacheColorHint(0x00000000);
		lv.setVerticalScrollBarEnabled(false);
		lv.setFadingEdgeLength(0);
		ll.addView(lv, LayoutParams.FILL_PARENT, iListViewHeight);
		lv.setAdapter(adapter);
		viewMoveableListViews = adapter.getMoveViews();
		List<View> fixViews = adapter.getFixViews();
		for (View view : fixViews)
		{
			view.measure(
			        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
			        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			iFixViewWidth = Math.max(iFixViewWidth, view.getMeasuredWidth());
		}
		for (View view : viewMoveableListViews)
		{
			view.measure(
			        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
			        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			iMoveableViewWidth = Math.max(iMoveableViewWidth,
			        view.getMeasuredWidth());
		}
	}

	private int iLastMotionY;
	private int iLastMotionX;
	private int iTransferX;
	private int iStartDownX;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				iLastMotionX = (int) ev.getX();
				iLastMotionY = (int) ev.getY();
				iStartDownX = iLastMotionX;
				break;
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int offsetX = iLastMotionX - x;
				int offsetY = iLastMotionY - y;
				int offsetMoveX = iStartDownX - x;
				if (Math.abs(offsetX) > Math.abs(offsetY))
				{
					if (Math.abs(offsetMoveX) > moveSlop)
					{
						iTransferX += offsetX;
						srcoll(iTransferX);
						iLastMotionX = x;
						return true;
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				// endHorizontalScroll();
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				iLastMotionX = (int) event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getX();
				int offsetX = iLastMotionX - x;
				iTransferX += offsetX;
				srcoll(iTransferX);
				iLastMotionX = x;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				endHorizontalScroll();
				break;
		}
		return true;
	}

	private void srcoll(int iTransferX)
	{
		viewMoveableHeader.scrollTo(iTransferX, 0);
		for (View view : viewMoveableListViews)
		{
			view.scrollTo(iTransferX, 0);
		}
	}

	private void endHorizontalScroll()
	{

		int scrollRange = getHorizontalScrollRange();
		if (iTransferX < 0)
		{
			iTransferX = 0;
			srcoll(iTransferX);
		} else if (iTransferX > scrollRange)
		{
			iTransferX = scrollRange;
			srcoll(iTransferX);
		}
	}

	private int getHorizontalScrollRange()
	{
		int scrollRange = 0;
		scrollRange = Math
		        .max(0,
		                iMoveableViewWidth
		                        - (getWidth() - getPaddingLeft()
		                                - getPaddingRight() - iFixViewWidth));
		System.out.println(scrollRange + ":" + computeHorizontalScrollRange());
		return scrollRange;
	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec,
	        int parentHeightMeasureSpec)
	{
		ViewGroup.LayoutParams lp = child.getLayoutParams();

		int childWidthMeasureSpec;
		int childHeightMeasureSpec;

		// childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
		// getPaddingLeft() + getPaddingRight(), lp.width);

		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
		        MeasureSpec.UNSPECIFIED);

		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
		        MeasureSpec.UNSPECIFIED);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override
	protected void measureChildWithMargins(View child,
	        int parentWidthMeasureSpec, int widthUsed,
	        int parentHeightMeasureSpec, int heightUsed)
	{
		final MarginLayoutParams lp = (MarginLayoutParams) child
		        .getLayoutParams();

		final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
		        lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
		final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
		        lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
		System.out
		        .println(childWidthMeasureSpec + "," + childHeightMeasureSpec);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	public static abstract class ScrollListViewAdapter<T extends ViewHolder>
	        extends BaseAdapter
	{
		private List<View> lstMoveView = new ArrayList<View>();
		private List<View> lstFixView = new ArrayList<View>();

		public List<View> getMoveViews()
		{
			return lstMoveView;
		}

		public List<View> getFixViews()
		{
			return lstFixView;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			T viewHolder = null;
			if (convertView == null)
			{
				viewHolder = getViewHolder(position);
				LinearLayout ll = new LinearLayout(parent.getContext());
				View viewFix = createListFixView(position, viewHolder, ll);
				lstFixView.add(viewFix);
				ll.addView(viewFix);
				View viewMove = createListMoveView(position, viewHolder, ll);
				lstMoveView.add(viewMove);
				ll.addView(viewMove);
				convertView = ll;
				convertView.setTag(viewHolder);
			} else
			{
				viewHolder = (T) convertView.getTag();
				setListViewData(position, viewHolder);
			}
			return convertView;
		}

		protected abstract View createTitleFixView(Context context);

		protected abstract View createTitleMoveView(Context context);

		protected abstract void setListViewData(int position, T viewHolder);

		protected abstract View createListFixView(int position, T viewHolder,
		        ViewGroup parent);

		protected abstract View createListMoveView(int position, T viewHolder,
		        ViewGroup parent);

		protected abstract T getViewHolder(int position);

		public static abstract class ViewHolder
		{

		}
	}
}
