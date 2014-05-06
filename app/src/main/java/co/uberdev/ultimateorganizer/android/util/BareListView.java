package co.uberdev.ultimateorganizer.android.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

/**
 * BareListView is an emulated list view that is intended to be used in a ScrollView, with other different views.
 */
public class BareListView extends LinearLayout implements BareListDataListener
{
	private ArrayAdapter adapter;
	private ViewGroup header;
	private ViewGroup listContainer;
	private ViewGroup footer;

	public BareListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// create the main list container
		listContainer = new LinearLayout(context);
		LinearLayout.LayoutParams listLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		listContainer.setLayoutParams(listLayoutParams);
		((LinearLayout)listContainer).setOrientation(LinearLayout.VERTICAL);

		header = new LinearLayout(context);
		header.setLayoutParams(listLayoutParams);

		footer = new LinearLayout(context);
		footer.setLayoutParams(listLayoutParams);

		setOrientation(LinearLayout.VERTICAL);

		addView(header);
		addView(listContainer);
		addView(footer);
	}

	public ViewGroup getHeader()
	{
		if(header.getChildCount() > 0)
			return (ViewGroup) header.getChildAt(0);
		else
			return null;
	}

	public void setHeader(ViewGroup headerView)
	{
		// remove the header view and add the new header to beginning
		this.header.removeAllViews();
		this.header.addView(headerView);
	}

	public ViewGroup getFooter()
	{
		if(footer.getChildCount() > 0)
			return (ViewGroup) footer.getChildAt(0);
		else
			return null;
	}

	public void setFooter(ViewGroup footerView)
	{
		this.footer.removeAllViews();
		this.footer.addView(footerView);
	}

	public void setHeaderVisible(boolean visible)
	{
		int flag = visible ? View.VISIBLE : View.GONE;
		this.header.setVisibility(flag);
	}

	public void setFooterVisible(boolean visible)
	{
		int flag = visible ? View.VISIBLE : View.GONE;
		this.header.setVisibility(flag);
	}

	public ArrayAdapter getAdapter()
	{
		return adapter;
	}

	public void setAdapter(ArrayAdapter adapter)
	{
		if(adapter == null)
			return;
		this.adapter = adapter;
		// listen data changes from the adapter.
		try
		{
			((BareListDataDelegate) adapter).setDataListener(this);
		}
		catch(ClassCastException e)
		{
			// Crash with a meaningful message
			throw new ClassCastException("Adapter for BareListView must implement BareListDataDelegate");
		}
	}

	/**
	 * Reloads the list by requesting a view for each item from the adapter
	 * Does not recycle views. Though this view is intented to be used in a ScrollView.
	 */
	public void reloadList()
	{
		listContainer.removeAllViews();

		if(adapter != null)
		{
			for(int i = 0; i < adapter.getCount(); i++)
			{
				listContainer.addView(adapter.getView(i, null, listContainer));
			}
		}
	}

	@Override
	public void onDataSetChanged()
	{
		reloadList();
	}

	@Override
	public void onSingleItemChanged(int position)
	{
		// replace view at position with its latest state
		listContainer.removeViewAt(position);
		listContainer.addView(adapter.getView(position, null, listContainer));
	}
}
