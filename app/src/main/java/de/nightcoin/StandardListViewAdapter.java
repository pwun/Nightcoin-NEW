
package de.nightcoin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

public class StandardListViewAdapter extends ParseQueryAdapter {

    Context listContext;
    LayoutInflater inflater;
    ArrayList<ParseObject> list;
    String mode;
    GPSTracker gps;
    double longitude;
    double latitude;
    ParseGeoPoint geo;
    ImageLoader imageLoader;

    public StandardListViewAdapter(Context context,
    com.parse.ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
        imageLoader = new ImageLoader(context);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.standard_list_view_adapter, null);
        }

        TextView location = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterName);
        location.setText(object.getString("name"));

        ParseFile imageFile = object.getParseFile("image");
        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewStandardListViewAdapter);
        imageLoader.DisplayImage(imageFile.getUrl(), imageView);
        return v;
    }

}














































































/*	public StandardListViewAdapter(Context context,
			List<ParseObject> objectList, String mode) {
		this.mode = mode;
		listContext = context;
		inflater = LayoutInflater.from(listContext);
		list = new ArrayList<ParseObject>();
		list.addAll(objectList);
        gps = new GPSTracker(listContext);

        if(gps.canGetLocation()) {
            try{
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            geo = new ParseGeoPoint(latitude,longitude);}
            catch (Exception e){
                System.out.println("Fehler, Location nicht bekommen");
                geo = new ParseGeoPoint(0,0);
            }
        } else {
            //gps.showSettingsAlert();
            System.out.println("Fehler, GPS nicht verfügbar");
            geo = new ParseGeoPoint(0,0);
        }

    }*//*


	public class ViewHolder {
		TextView name;
        TextView topDetail;
        TextView bottomDetail;
		ImageView image;
	}

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.adapter_item, null);
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.description);
        descriptionView.setText(object.getString("description"));
        return v;
    }

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


*/
/*	@Override
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
		holder.name.setText(list.get(position).get("name").toString());
		holder.image*//*

		//holder.image.loadInBackground();

		*/
/*if (mode.equals("event")) {
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
            double distance = geo.distanceInKilometersTo(geoLocation);
            if(distance < 100){
                DecimalFormat df = new DecimalFormat("###.##");
                holder.topDetail.setText(""+ df.format(distance) +" km");
            } else if (geo.getLatitude()==0&&geo.getLongitude()==0){
                holder.topDetail.setText("kein GPS verfügbar");
            }
            else{//kein GPS
                holder.topDetail.setText("nicht in deiner Nähe");
            }
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
            double distance = geo.distanceInKilometersTo(geoLocation);
            if(distance < 100){
                DecimalFormat df = new DecimalFormat("###.##");
                holder.topDetail.setText(""+ df.format(distance) +" km");
            }
            else if (geo.getLatitude()==0&&geo.getLongitude()==0){
                holder.topDetail.setText("kein GPS verfügbar");
            }
            else{//kein GPS
                holder.topDetail.setText("nicht in deiner Nähe");
            }
            holder.bottomDetail.setText(list.get(position).getOpeningToday());
            if(holder.bottomDetail.getText().equals("Geöffnet")){//list.get(position).getOpeningToday().equals("Geöffnet")) {
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.green));
            }
            else{
                holder.bottomDetail.setTextColor(listContext.getResources().getColor(R.color.dark_red));
            }
*//*
*/
/*            try {
                if (list.get(position).isOpen()) {
                    holder.bottomDetail.setText("Geöffnet");
                }
            } catch(Exception e) {
                System.out.println("Ich bin die Filmbühne und ich bin blöd.");
            }*//*


			*/
/*convertView.setOnClickListener(new OnClickListener() {

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
	}*//*


}
*/
