package custom.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CustomScrollView_OneC extends LinearLayout
{

	private int iLastMotionY;
	private int iTransferY;

	public CustomScrollView_OneC(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomScrollView_OneC(Context context)
	{
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				iLastMotionY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int y = (int) event.getY();
				int offsetY = iLastMotionY - y;
				iTransferY += offsetY;
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

	private void endScroll()
	{
		int scrollRange = getScrollRange();
		if (iTransferY < 0)
		{
			iTransferY = 0;
			scrollTo(0, iTransferY);
		} else if (iTransferY > scrollRange)
		{
			iTransferY = scrollRange;
			scrollTo(0, iTransferY);
		}
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//	{
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		if (heightMode == MeasureSpec.UNSPECIFIED)
//		{
//			return;
//		}
//
//		if (getChildCount() > 0)
//		{
//			final View child = getChildAt(0);
//			int height = getMeasuredHeight();
//			if (child.getMeasuredHeight() < height)
//			{
//				final FrameLayout.LayoutParams lp = (LayoutParams) child
//				        .getLayoutParams();
//
//				int childWidthMeasureSpec = getChildMeasureSpec(
//				        widthMeasureSpec, getPaddingLeft() + getPaddingRight(),
//				        lp.width);
//				height -= getPaddingTop();
//				height -= getPaddingBottom();
//				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//				        height, MeasureSpec.EXACTLY);
//
//				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//			}
//		}
//	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec,
	        int parentHeightMeasureSpec)
	{
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
	        int parentHeightMeasureSpec, int heightUsed)
	{
		final MarginLayoutParams lp = (MarginLayoutParams) child
		        .getLayoutParams();

		final int childWidthMeasureSpec = getChildMeasureSpec(
		        parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight()
		                + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
		final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
		        lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	private int getScrollRange()
	{
		int scrollRange = 0;
		for (int index = 0; index < getChildCount(); index++)
		{
			scrollRange += getChildAt(index).getHeight();
		}
		scrollRange = Math.max(0, scrollRange
		        - (getHeight() - getPaddingBottom() - getPaddingTop()));
		return scrollRange;
	}

}
