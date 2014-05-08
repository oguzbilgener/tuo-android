package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Tag;
import co.uberdev.ultimateorganizer.android.util.BareListDataDelegate;
import co.uberdev.ultimateorganizer.android.util.BareListDataListener;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreTags;

/**
 * Created by oguzbilgener on 02/05/14.
 */
public class TaskItemTagsListAdapter extends ArrayAdapter<Tag> implements BareListDataDelegate
{
	private Context context;
	private List<Tag> items;
	private LayoutInflater inflater;
	private int resourceId;

	private BareListDataListener dataListener;

	public TaskItemTagsListAdapter(Context context, int resource, List<Tag> objects)
	{
		super(context, resource, objects);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceId = resource;
		items = objects;


		setNotifyOnChange(true);
	}

	public static TaskItemTagsListAdapter fromCoreTags(Context context, int resource, CoreTags objects)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for(int i=0;i<objects.size();i++)
			tags.add((Tag)objects.get(i));

		Utils.log.d("ts "+tags.size());
		return new TaskItemTagsListAdapter(context, resource, tags);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Utils.log.d("getView "+position);
		ViewGroup itemView = (ViewGroup) inflater.inflate(resourceId, parent, false);

		RelativeLayout tagLayout = (RelativeLayout) itemView.findViewById(R.id.task_item_tag_layout);
		TextView tagTextView = (TextView) itemView.findViewById(R.id.task_item_tag);

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
}