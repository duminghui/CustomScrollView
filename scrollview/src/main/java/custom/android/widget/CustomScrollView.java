package custom.android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CustomScrollView extends FrameLayout {

	private int iLastMotionY;
	private int iTransferY;

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
//			 debug(0);
			Rect rect = new Rect();
			getWindowVisibleDisplayFrame(rect);
			System.out.println(rect.toShortString());
			showRect(this, 0);
			System.out.println(ev.getX() + "," + ev.getY());
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void showRect(View view, int depth) {
		Rect rect = new Rect();
		String str = debugIndent(depth);
		str += view.toString();
		str += (String.format("[%s,%s,%s,%s|%s,%s]", view.getPaddingLeft(),
				view.getPaddingTop(), view.getPaddingRight(),
				view.getPaddingBottom(), view.getWidth(), view.getHeight()));
		str += "{";
		System.out.println(str);
		// view在父控件中的rect位置
		view.getHitRect(rect);
		str = debugIndent(depth);
		str += "hr:" + rect.toShortString();
		System.out.println(str);
		// 如果view在屏幕中显示，则以该控件显示的区域作为结果，如果没有在屏幕中显示（即移动出屏屏幕）,则以该view在父View中的区域作为结果
		view.getLocalVisibleRect(rect);
		str = debugIndent(depth);
		str += "lvr:" + rect.toShortString();
		System.out.println(str);
		//该view在父View中的区域
		view.getGlobalVisibleRect(rect);
		str = debugIndent(depth);
		str += "gvr:" + rect.toShortString();
		System.out.println(str);
		view.getDrawingRect(rect);
		str = debugIndent(depth);
		str += "dr:" + rect.toShortString() + "}";
		System.out.println(str);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int index = 0; index < vg.getChildCount(); index++) {
				showRect(vg.getChildAt(index), depth + 1);
			}
		}

	}

	protected static String debugIndent(int depth) {
		StringBuilder spaces = new StringBuilder((depth * 2 + 3) * 2);
		for (int i = 0; i < (depth * 2) + 3; i++) {
			spaces.append(' ').append(' ');
		}
		return spaces.toString();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			iLastMotionY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int y = (int) event.getY();
			int offsetY = iLastMotionY - y;
			if (iTransferY <= 0 || iTransferY >= getScrollRange()) {
				iTransferY += (offsetY / 2);
			} else {
				iTransferY += offsetY;
			}
			scrollTo(0, iTransferY);
			iLastMotionY = (int) event.getY();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			endScroll();
			break;
		}
		return true;
	}

	private void endScroll() {
		int scrollRange = getScrollRange();
		if (iTransferY < 0) {
			iTransferY = 0;
			scrollTo(0, iTransferY);
		} else if (iTransferY > scrollRange) {
			iTransferY = scrollRange;
			scrollTo(0, iTransferY);
		}
	}

	private int getScrollRange() {
		int scrollRange = 0;
		for (int index = 0; index < getChildCount(); index++) {
			scrollRange += getChildAt(index).getHeight();
		}
		scrollRange = Math.max(0, scrollRange
				- (getHeight() - getPaddingBottom() - getPaddingTop()));
		return scrollRange;
	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec,
			int parentHeightMeasureSpec) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();

		int childWidthMeasureSpec;
		int childHeightMeasureSpec;

		childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
				getPaddingLeft() + getPaddingRight(), lp.width);

		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override
	protected void measureChildWithMargins(View child,
			int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		final MarginLayoutParams lp = (MarginLayoutParams) child
				.getLayoutParams();

		final int childWidthMeasureSpec = getChildMeasureSpec(
				parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight()
						+ lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
		final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
				lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
		System.out
				.println(childWidthMeasureSpec + "," + childHeightMeasureSpec);
		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

}
