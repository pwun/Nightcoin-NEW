package de.nightcoin;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import java.text.SimpleDateFormat;
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
        listContext = context;
        imageLoader = new ImageLoader(context);
        setObjectsPerPage(25);
    }
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        String objectType = object.getClassName();
        if (objectType.equals("Locations")) {
            return setupLocationItemView(object, v);
        }
        if (objectType.equals("Events")) {
            return setupEventItemView(object, v);
        }
        if (objectType.equals("Coupons")) {
            return setupCoinItemView(object, v);
        }
        return v;
    }

    private View setupLocationItemView(final ParseObject object, View v) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.standard_list_view_adapter, null);
        }
        final TextView location = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterName);
        location.setText(object.getString("name"));
        ParseFile imageFile = object.getParseFile("image");
        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewStandardListViewAdapter);
        imageLoader.DisplayImage(imageFile.getUrl(), imageView);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startRequestedIntent(object);
            }
        });
        return v;
    }

    private View setupEventItemView(final ParseObject object, View v) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.standard_list_view_adapter, null);
        }
        TextView title = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterName);
        title.setText(object.getString("title"));
        TextView location = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterOpening);
        location.setText(object.getString("location"));
        location.setTextColor(listContext.getResources().getColor(R.color.white));
        TextView date = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterDistance);
        date.setText(new SimpleDateFormat("dd. MMMM").format(object.getDate("date")));
        ParseFile imageFile = object.getParseFile("image");
        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewStandardListViewAdapter);
        imageLoader.DisplayImage(imageFile.getUrl(), imageView);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(listContext,
                        ActivityEventItemView.class);
                i.putExtra("objectId", object.getObjectId());
                if(listContext!=null) {
                    listContext.startActivity(i);
                }
            }
        });
        return v;
    }

    private View setupCoinItemView(final ParseObject object, View v) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.coin_list_view_adapter, null);
        }
        TextView location = (TextView) v.findViewById(R.id.textViewCoinListViewAdapterLocation);
        location.setText(object.getString("location"));
        TextView value = (TextView) v.findViewById(R.id.textViewCoinListViewAdapterValue);
        value.setText(object.getString("value"));
        TextView date = (TextView) v.findViewById(R.id.textViewCoinListViewAdapterDate);
        date.setText(new SimpleDateFormat("EEEE, dd. MMMM").format(object.getDate("date")));
        TextView amount = (TextView) v.findViewById(R.id.textViewCoinListViewAdapterAmount);
        if (object.getBoolean("limited")) {
            int totalAmount = object.getInt("amount");
            int cashedInCoins = object.getInt("cashedIn");
            int coinsLeft = totalAmount - cashedInCoins;
            amount.setText("noch " + coinsLeft + " Stück");
        } else {
            amount.setText("Unbegrenzt");
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(listContext,
                        ActivityCoinItemView.class);
                i.putExtra("id", object.getObjectId());
                if(listContext!=null) {
                    listContext.startActivity(i);
                }
            }
        });
        return v;
    }

    private void startRequestedIntent(ParseObject object) {
        String category = object.getString("category");
        if (category.equals("Food")) {
            Intent i = new Intent(listContext, ActivityFoodItemView.class);
            i.putExtra("name", object.getString("name"));
            if(listContext!=null) {
                listContext.startActivity(i);
            }
        } else {
            Intent i = new Intent(listContext,
                    ActivityStandardItemView.class);
            i.putExtra("name", object.getString("name"));
            if(listContext!=null) {
                listContext.startActivity(i);
            }
        }
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            // R.layout.adapter_next_page contains an ImageView with a custom graphic
            // and a TextView.
            v = View.inflate(getContext(), R.layout.adapter_load_more, null);
        }
        TextView textView = (TextView) v.findViewById(R.id.textViewAdapterLoadMore);
        textView.setText(getCount() + " Coins geladen! Mehr laden...");
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