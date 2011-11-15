package custom.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CustomScrollView extends LinearLayout
{

	private int iLastMotionY;
	private int iTransferY;

	public CustomScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomScrollView(Context context)
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
