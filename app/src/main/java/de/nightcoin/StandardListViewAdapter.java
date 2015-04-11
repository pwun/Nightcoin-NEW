package de.nightcoin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class StandardListViewAdapter extends BaseAdapter {

	Context listContext;
	LayoutInflater inflater;
	ArrayList<StandardObject> list;
	String mode;

	public StandardListViewAdapter(Context context,
			List<StandardObject> objectList, String mode) {
		super();
		this.mode = mode;
		listContext = context;
		inflater = LayoutInflater.from(listContext);
		list = new ArrayList<StandardObject>();
		list.addAll(objectList);
	}

	public class ViewHolder {
		TextView name;
		ParseImageView image;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.standard_list_view_adapter,
					null);
			holder.name = (TextView) convertView
					.findViewById(R.id.textViewStandardListViewAdapterName);
			holder.image = (ParseImageView) convertView
					.findViewById(R.id.imageViewStandardListViewAdapter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.image.setParseFile(list.get(position).getImage());
		holder.image.loadInBackground();

		if (mode.equals("event")) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(listContext,
							ActivityEventItemView.class);

					intent.putExtra("title", list.get(position).getName());
					listContext.startActivity(intent);
				}
			});
		} else {

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(listContext,
							ActivityStandardItemView.class);

					intent.putExtra("name", list.get(position).getName());
					listContext.startActivity(intent);
				}
			});
		}

		return convertView;
	}

}
