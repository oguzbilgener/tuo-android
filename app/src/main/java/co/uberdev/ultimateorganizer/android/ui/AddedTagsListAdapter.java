package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.BareListDataDelegate;
import co.uberdev.ultimateorganizer.android.util.BareListDataListener;
import co.uberdev.ultimateorganizer.core.CoreTag;

/**
 * Created by oguzbilgener on 02/05/14.
 */
public class AddedTagsListAdapter extends ArrayAdapter<CoreTag>
		implements BareListDataDelegate, View.OnClickListener
{
	private Context context;
	private List<CoreTag> items;
	private LayoutInflater inflater;
	private int resourceId;

	private BareListDataListener dataListener;

	private OnItemRemoveClickListener itemRemoveClickListener;

	public AddedTagsListAdapter(Context context, int resource, List<CoreTag> objects)
	{
		super(context, resource, objects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceId = resource;
		items = objects;


		setNotifyOnChange(true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);

		ImageButton removeButton =  (ImageButton) itemView.findViewById(R.id.button_remove_tag);
		// save position info in button to distinguish remove button clicks
		removeButton.setTag(R.id.add_task_tag_remove_index, position);
		// listen for remove button clicks first, distinguish them and pass them to the listener
		removeButton.setOnClickListener(this);

		LinearLayout tagLayout = (LinearLayout) itemView.findViewById(R.id.layout_add_task_tag);
		TextView tagTextView = (TextView) itemView.findViewById(R.id.text_add_task_tag);

		tagTextView.setText(items.get(position).getName());
		tagLayout.setBackgroundColor(items.get(position).getColor());

		return itemView;
	}

	@Override
	public void setDataListener(BareListDataListener dataListener)
	{
		this.dataListener = dataListener;
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
		if(dataListener != null)
		{
			dataListener.onDataSetChanged();
		}
	}

	public void setItemRemoveClickListener(OnItemRemoveClickListener itemRemoveClickListener)
	{
		this.itemRemoveClickListener = itemRemoveClickListener;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.button_remove_tag)
		{
			try
			{
				int position = (Integer)v.getTag(R.id.add_task_tag_remove_index);

				// pass this event to the ItemRemoveClickListener, if exists.
				if(itemRemoveClickListener != null)
				{
					itemRemoveClickListener.onTagRemoveClick(v, position);
				}
			}
			catch(Exception e)
			{
				// button has no tag. do nothing.
			}
		}
	}


	public interface OnItemRemoveClickListener
	{
		public void onTagRemoveClick(View view, int position);
	}
}