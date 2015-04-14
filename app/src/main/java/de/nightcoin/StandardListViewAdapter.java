package de.nightcoin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

public class StandardListViewAdapter extends BaseAdapter {

	Context listContext;
	LayoutInflater inflater;
	ArrayList<StandardObject> list;
	String mode;
    GPSTracker gps;
    double longitude;
    double latitude;
    ParseGeoPoint geo;

	public StandardListViewAdapter(Context context,
			List<StandardObject> objectList, String mode) {
		super();
		this.mode = mode;
		listContext = context;
		inflater = LayoutInflater.from(listContext);
		list = new ArrayList<StandardObject>();
		list.addAll(objectList);
        gps = new GPSTracker(listContext);

        if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            geo = new ParseGeoPoint(latitude,longitude);
            Toast.makeText(listContext,
                    "Your Location is -\nLat: " + latitude + "\nLong: "
                            + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }

    }

	public class ViewHolder {
		TextView name;
        TextView topDetail;
        TextView bottomDetail;
		ImageView image;
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
			holder.image = (ImageView) convertView
					.findViewById(R.id.imageViewStandardListViewAdapter);
            holder.topDetail = (TextView) convertView
                    .findViewById(R.id.textViewStandardListViewAdapterDistance);
            holder.bottomDetail = (TextView) convertView
                    .findViewById(R.id.textViewStandardListViewAdapterOpening);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.image.setImageBitmap(list.get(position).getImage());
		//holder.image.loadInBackground();

		if (mode.equals("event")) {
            holder.name.setTextSize(20);
            holder.topDetail.setText(list.get(position).getDate());
            holder.bottomDetail.setText(list.get(position).getLocation());
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
		}
        else if(mode.equals("taxi")){
            holder.topDetail.setVisibility(View.INVISIBLE);
            holder.bottomDetail.setVisibility(View.INVISIBLE);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(listContext,
                            ActivityTaxiItemView.class);

                    intent.putExtra("title", list.get(position).getName());
                    listContext.startActivity(intent);
                }
            });
        }
        else if(mode.equals("food")){
            double lat = list.get(position).getLat();
            double lon = list.get(position).getLong();
            ParseGeoPoint geoLocation = new ParseGeoPoint(lat, lon);
            System.out.println(geoLocation);
            System.out.println(geo);
            double distance = geo.distanceInKilometersTo(geoLocation);
            distance = (int)distance*100;
            distance /=100;
            holder.topDetail.setText(""+ distance +" km");
            holder.bottomDetail.setText(list.get(position).getOpeningToday());
            if(holder.bottomDetail.getText().equals("Geöffnet")){//list.get(position).getOpeningToday().equals("Geöffnet")) {
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.green));
            }
            else{
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.dark_red));
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(listContext,
                            ActivityFoodItemView.class);

                    intent.putExtra("title", list.get(position).getName());
                    listContext.startActivity(intent);
                }
            });
        }
        else {
            double lat = list.get(position).getLat();
            double lon = list.get(position).getLong();
            ParseGeoPoint geoLocation = new ParseGeoPoint(lat, lon);
            System.out.println(geoLocation);
            System.out.println(geo);
            double distance = geo.distanceInKilometersTo(geoLocation);
            distance = (int)distance*100;
            distance /=100;
            holder.topDetail.setText(""+ distance +" km");
            holder.bottomDetail.setText(list.get(position).getOpeningToday());
            if(holder.bottomDetail.getText().equals("Geöffnet")){//list.get(position).getOpeningToday().equals("Geöffnet")) {
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.green));
            }
            else{
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.dark_red));
            }
/*            try {
                if (list.get(position).isOpen()) {
                    holder.bottomDetail.setText("Geöffnet");
                }
            } catch(Exception e) {
                System.out.println("Ich bin die Filmbühne und ich bin blöd.");
            }*/

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
