/**
 * 
 */
package custom.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
public class ScrollListView extends RelativeLayout {
	private LinearLayout ll;
	private Context context;
	private int iFixViewWidth;
	private int iMoveableViewWidth;
	private View viewMoveableHeader;
	private List<View> viewMoveableListViews;

	public ScrollListView(Context context) {
		super(context);
		this.context = context;
		initMainLayout();

	}

	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initMainLayout();
	}

	private void initMainLayout() {
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams scrollListLp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		addView(ll, scrollListLp);
	}

	public void setHeaderView(View fixView, View moveableView) {
		LinearLayout llHeader = new LinearLayout(context);
		fixView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		this.iFixViewWidth = fixView.getMeasuredWidth();
		moveableView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		this.iMoveableViewWidth = moveableView.getMeasuredWidth();
		llHeader.addView(fixView);
		viewMoveableHeader = moveableView;
		llHeader.addView(moveableView);
		ll.addView(llHeader);
	}

	public void setListAdapter(ScrollListViewAdapter adapter) {
		ListView lv = new ListView(context);
		lv.setCacheColorHint(0x00000000);
		lv.setVerticalScrollBarEnabled(false);
		lv.setFadingEdgeLength(0);
		ll.addView(lv);
		lv.setAdapter(adapter);
		viewMoveableListViews = adapter.getMoveViews();
		List<View> fixViews = adapter.getFixViews();
		for (View view : fixViews) {
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			iFixViewWidth = Math.max(iFixViewWidth, view.getMeasuredWidth());
		}
		for (View view : viewMoveableListViews) {
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			iMoveableViewWidth = Math.max(iMoveableViewWidth,
					view.getMeasuredWidth());
		}
	}

	private int iLastMotionX;
	private int iTransferX;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			iLastMotionX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int offsetX = iLastMotionX - x;
			iTransferX += offsetX;
			srcoll(iTransferX);
			iLastMotionX = (int) event.getX();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			endHorizontalScroll();
			break;
		}
		return true;
	}

	private void srcoll(int iTransferX) {
		viewMoveableHeader.scrollTo(iTransferX, 0);
		for (View view : viewMoveableListViews) {
			view.scrollTo(iTransferX, 0);
		}
	}

	private void endHorizontalScroll() {
		int scrollRange = getHorizontalScrollRange();
		if (iTransferX < 0) {
			iTransferX = 0;
			srcoll(iTransferX);
		} else if (iTransferX > scrollRange) {
			iTransferX = scrollRange;
			srcoll(iTransferX);
		}
	}

	private int getHorizontalScrollRange() {
		int scrollRange = 0;
		scrollRange = Math
				.max(0,
						iMoveableViewWidth
								- (getWidth() - getPaddingLeft()
										- getPaddingRight() - iFixViewWidth));
		return scrollRange;
	}

	public static abstract class ScrollListViewAdapter extends BaseAdapter {
		private List<View> lstMoveView = new ArrayList<View>();
		private List<View> lstFixView = new ArrayList<View>();

		public List<View> getMoveViews() {
			return lstMoveView;
		}

		public List<View> getFixViews() {
			return lstFixView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = getViewHolder();
				LinearLayout ll = new LinearLayout(parent.getContext());
				View viewFix = createFixListView(position, viewHolder, ll);
				lstFixView.add(viewFix);
				ll.addView(viewFix);
				View viewMove = createMoveListView(position, viewHolder, ll);
				lstMoveView.add(viewMove);
				ll.addView(viewMove);
				convertView = ll;
				convertView.setTag(viewHolder);
			} else {
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

		public static abstract class ViewHolder {

		}
	}
}
