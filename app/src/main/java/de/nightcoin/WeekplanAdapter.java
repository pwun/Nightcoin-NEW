package de.nightcoin;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseImageView;

public class WeekplanAdapter extends BaseAdapter {

	Context listContext;
	LayoutInflater inflater;
	ArrayList<String> list;
    int color;

	public WeekplanAdapter(Context context, ArrayList<String> weeklist, int color) {
		super();
		listContext = context;
		inflater = LayoutInflater.from(listContext);
		list = new ArrayList<String>();
		list.addAll(weeklist);
        this.color = color;
	}

	public class ViewHolder {
		TextView weekday;
		TextView content;
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
			convertView = inflater.inflate(R.layout.weekplan_adapter, null);
			holder.weekday = (TextView) convertView
					.findViewById(R.id.textViewWeekplanTitle);
            holder.weekday.setBackgroundColor(color);
			holder.content = (TextView) convertView
					.findViewById(R.id.textViewWeekplanContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.content.setText(list.get(position));

		switch (position) {
		case 0:
			holder.weekday.setText("Montag");
			break;
		case 1:
			holder.weekday.setText("Dienstag");
			break;
		case 2:
			holder.weekday.setText("Mittwoch");
			break;
		case 3:
			holder.weekday.setText("Donnerstag");
			break;
		case 4:
			holder.weekday.setText("Freitag");
			break;
		case 5:
			holder.weekday.setText("Samstag");
			break;
		case 6:
			holder.weekday.setText("Sonntag");
			break;
		}

		return convertView;
	}

}
