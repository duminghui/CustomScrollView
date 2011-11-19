/**
 * 
 */
package custom.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
	private ListView lv;
	private int iFixViewWidth;
	private int iMoveableViewWidth;
	private View viewMoveableHeader;
	private List<View> viewMoveableListViews;
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
		moveSlop = a.getDimensionPixelSize(R.styleable.ScrollListView_moveSlop,
		        5);
		a.recycle();
	}

	private void initMainLayout()
	{
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		addView(ll, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public void setCreatorAndAdapter(ScrollListViewCreator creator,
	        ScrollListViewAdapter<? extends ViewHolder> adapter)
	{
		setCreator(creator);
		setAdapter(adapter);
	}

	private void setCreator(ScrollListViewCreator creator)
	{
		LinearLayout llHeader = new LinearLayout(context);
		View fixView = creator.createTitleFixView(context);
		this.iFixViewWidth = getViewMeasuredWidth(fixView);
		View moveableView = creator.createTitleMoveView(context);
		this.iMoveableViewWidth = getViewMeasuredWidth(moveableView);
		llHeader.addView(fixView);
		viewMoveableHeader = moveableView;
		llHeader.addView(moveableView);
		ll.addView(llHeader);
		lv = creator.createListView(context);
		ll.addView(lv);
	}

	private void setAdapter(ScrollListViewAdapter<? extends ViewHolder> adapter)
	{
		lv.setAdapter(adapter);
		viewMoveableListViews = adapter.getMoveViews();
		List<View> fixViews = adapter.getFixViews();
		for (View view : fixViews)
		{
			iFixViewWidth = Math.max(iFixViewWidth, getViewMeasuredWidth(view));
		}
		for (View view : viewMoveableListViews)
		{
			iMoveableViewWidth = Math.max(iMoveableViewWidth,
			        getViewMeasuredWidth(view));
		}
	}

	private int getViewMeasuredWidth(View view)
	{
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		return view.getMeasuredWidth();
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
		// System.out.println(scrollRange + ":" +
		// computeHorizontalScrollRange());
		return scrollRange;
	}

	// @Override
	// protected void measureChild(View child, int parentWidthMeasureSpec,
	// int parentHeightMeasureSpec)
	// {
	// ViewGroup.LayoutParams lp = child.getLayoutParams();
	//
	// int childWidthMeasureSpec;
	// int childHeightMeasureSpec;
	//
	// // childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
	// // getPaddingLeft() + getPaddingRight(), lp.width);
	//
	// childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
	// MeasureSpec.UNSPECIFIED);
	//
	// childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
	// MeasureSpec.UNSPECIFIED);
	//
	// child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	// }
	//
	// @Override
	// protected void measureChildWithMargins(View child,
	// int parentWidthMeasureSpec, int widthUsed,
	// int parentHeightMeasureSpec, int heightUsed)
	// {
	// final MarginLayoutParams lp = (MarginLayoutParams) child
	// .getLayoutParams();
	//
	// final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
	// lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
	// final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
	// lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
	// System.out
	// .println(childWidthMeasureSpec + "," + childHeightMeasureSpec);
	// child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	// }

	public static abstract class ScrollListViewCreator
	{
		/**
		 * 列头固定列
		 * 
		 * @param context
		 * @return
		 */
		protected abstract View createTitleFixView(Context context);

		/**
		 * 列头可移动列
		 * 
		 * @param context
		 * @return
		 */
		protected abstract View createTitleMoveView(Context context);

		/**
		 * 创建ListView
		 * 
		 * @return
		 */
		protected abstract ListView createListView(Context context);

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
				View viewFix = getListFixView(position, viewHolder, ll);
				lstFixView.add(viewFix);
				ll.addView(viewFix);
				View viewMove = getListMoveView(position, viewHolder, ll);
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

		/**
		 * 设置已经在ViewHolder存在的列表数据
		 * 
		 * @param position
		 * @param viewHolder
		 */
		protected abstract void setListViewData(int position, T viewHolder);

		/**
		 * 获取列表项中固定的列
		 * 
		 * @param position
		 * @param viewHolder
		 * @param parent
		 * @return
		 */
		protected abstract View getListFixView(int position, T viewHolder,
		        ViewGroup parent);

		/**
		 * 获取列表项中可移动的列
		 * 
		 * @param position
		 * @param viewHolder
		 * @param parent
		 * @return
		 */
		protected abstract View getListMoveView(int position, T viewHolder,
		        ViewGroup parent);

		/**
		 * 获取ViewHolder
		 * 
		 * @param position
		 * @return
		 */
		protected abstract T getViewHolder(int position);

		public static abstract class ViewHolder
		{

		}
	}
}
