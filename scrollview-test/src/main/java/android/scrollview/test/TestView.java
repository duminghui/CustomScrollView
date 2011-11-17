/**
 * 
 */
package android.scrollview.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author duminghui
 * 
 */
public class TestView extends RelativeLayout
{

	private Context context;

	/**
	 * @param context
	 * @param attrs
	 */
	public TestView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		initMainLayout();
	}

	/**
	 * @param context
	 */
	public TestView(Context context)
	{
		super(context);
		this.context = context;
		initMainLayout();
	}

	private void initMainLayout()
	{
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams scrollListLp = new LayoutParams(LayoutParams.FILL_PARENT,
		        LayoutParams.FILL_PARENT);
		addView(ll, scrollListLp);
	}

}
