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
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StandardListViewAdapter extends ParseQueryAdapter {
    Context listContext;
    LayoutInflater inflater;
    ArrayList<ParseObject> list;
    String mode;
    boolean usermode = false;
    GPSTracker gps;
    double longitude;
    double latitude;
    boolean edit;
    ParseGeoPoint geo;
    ImageLoader imageLoader;
    public StandardListViewAdapter(Context context,
                                   com.parse.ParseQueryAdapter.QueryFactory<ParseObject> queryFactory, boolean usermode) {
        super(context, queryFactory);
        this.edit = edit;
        listContext = context;
        initGPS();
        this.usermode = usermode;
        imageLoader = new ImageLoader(context);
        setObjectsPerPage(25);
    }

    private void initGPS(){
                    gps = new GPSTracker(listContext);
        if(gps.canGetLocation()) {
            try{
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                geo = new ParseGeoPoint(latitude,longitude);
            } catch (Exception e){
                System.out.println("Fehler, Location nicht bekommen");
                geo = new ParseGeoPoint(0,0);
            }
        }
        else {
            System.out.println("Fehler, GPS nicht verfügbar");
            geo = new ParseGeoPoint(0,0);
        }
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

    /*
         NSString *opensAt = [object[@"opensAt"] objectAtIndex:[self getWeekday]];
     NSString *closesAt = [object[@"closesAt"] objectAtIndex:[self getWeekday]];

     if ([self locationIsOpen:opensAt closingAt:closesAt]) {
         cell.openLabel.text = @"Geöffnet";
         cell.openLabel.textColor = [UIColor colorWithRed:0.3 green:0.85 blue:0.5 alpha:1.0];
         [openLocations addObject:object[self.textKey]];
     } else {
         if ([opensAt isEqualToString:@"-"]) {
             cell.openLabel.text = [NSString stringWithFormat:@"Heute geschlossen"];
             cell.openLabel.textColor = [UIColor redColor];
         } else {
             cell.openLabel.text = [NSString stringWithFormat:@"öffnet um %@ Uhr", opensAt];
             cell.openLabel.textColor = [UIColor redColor];
         }
     }

     - (BOOL)locationIsOpen:(NSString *)opensAtString closingAt:(NSString *)closesAtString {

    NSDateComponents *components = [[NSCalendar currentCalendar] components:NSCalendarUnitHour fromDate:[NSDate date]];
    NSInteger hour = [components hour];

    NSInteger opensAt = [opensAtString integerValue];
    NSInteger closesAt = [closesAtString integerValue];

    if (opensAt == 0 && closesAt == 0) {
        return NO;
    }

    if (hour >= opensAt || hour < closesAt) {
        return YES;
    } else {
        return NO;
    }
}
     */

    private String hoursSring(ParseObject object) {
        ArrayList<String> opening = (ArrayList<String>) object.get("opensAt");
        ArrayList<String> closing = (ArrayList<String>) object.get("closesAt");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(Calendar.DAY_OF_WEEK);
        String open = "";
        String closed = "";

        switch (today)
        {
            case Calendar.MONDAY:
                open=opening.get(0);
                closed=closing.get(1);
                break;
            case Calendar.TUESDAY:
                open=opening.get(1);
                closed=closing.get(2);
                break;
            case Calendar.WEDNESDAY:
                open=opening.get(2);
                closed=closing.get(3);
                break;
            case Calendar.THURSDAY:
                open=opening.get(3);
                closed=closing.get(4);
                break;
            case Calendar.FRIDAY:
                open=opening.get(4);
                closed=closing.get(5);
                break;
            case Calendar.SATURDAY:
                open=opening.get(5);
                closed=closing.get(6);
                break;
            case Calendar.SUNDAY:
                open=opening.get(6);
                closed=closing.get(0);
                break;
        }

        if (open.equals("-")) {
            return "Heute geschlossen";
        } else {
            int opensAt;
            if(open.length()>2){
                opensAt = Integer.parseInt(""+open.charAt(0))*10+Integer.parseInt(""+open.charAt(1));
            }
            else{opensAt = Integer.parseInt(open);}
            int closesAt;
            if(closed.length()>2){
                closesAt = Integer.parseInt(""+closed.charAt(0))*10+Integer.parseInt(""+closed.charAt(1));
            }
            else{closesAt = Integer.parseInt(closed);}

            if (locationIsOpen(opensAt, closesAt)) {
                return "Geöffnet";
            } else {
                return "Öffnet um " + open + " Uhr";
            }

        }
    }

    private boolean locationIsOpen(int opensAt, int closesAt) {
        int hour = new Date().getHours();
        if (hour >= opensAt || hour < closesAt) {
            return true;
        } else {
            return false;
        }
    }




    private View setupLocationItemView(final ParseObject object, View v) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.standard_list_view_adapter, null);
        }

        TextView distanceView = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterDistance);
        TextView openingView = (TextView) v.findViewById(R.id.textViewStandardListViewAdapterOpening);

        ParseGeoPoint geoLocation = (ParseGeoPoint)object.get("geoData");
        double distance = geo.distanceInKilometersTo(geoLocation);
        if (geo.getLatitude()==0&&geo.getLongitude()==0){
            distanceView.setText("kein GPS verfügbar");
        }
        else {
            DecimalFormat df = new DecimalFormat("###.##");
            distanceView.setText(""+ df.format(distance) +" km");
        }

        openingView.setText(hoursSring(object));
        if( openingView.getText().toString().equals("Geöffnet")) {
            openingView.setTextColor(listContext.getResources().getColor(R.color.green));
        } else {
            openingView.setTextColor(listContext.getResources().getColor(R.color.dark_red));
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


    /*
    ArrayList<String> opening = (ArrayList<String>) object.get("opensAt");
        ArrayList<String> closing = (ArrayList<String>) object.get("closesAt");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int today = cal.get(Calendar.DAY_OF_WEEK);
        String open = "";
        String closed = "";
        switch (today)
        {
            case Calendar.MONDAY:	open=opening.get(0);
                closed=closing.get(1);
                break;
            case Calendar.TUESDAY:	open=opening.get(1);
                closed=closing.get(2);
                break;
            case Calendar.WEDNESDAY:open=opening.get(2);
                closed=closing.get(3);
                break;
            case Calendar.THURSDAY:	open=opening.get(3);
                closed=closing.get(4);
                break;
            case Calendar.FRIDAY:	open=opening.get(4);
                closed=closing.get(5);
                break;
            case Calendar.SATURDAY:	open=opening.get(5);
                closed=closing.get(6);
                break;
            case Calendar.SUNDAY:	open=opening.get(6);
                closed=closing.get(0);
                break;
        }

        if(open.equals("-")){
            openingView.setText("Heute geschlossen");
            openingView.setTextColor(listContext.getResources().getColor(R.color.dark_red));
        }
        if(openToday(open, closed, new Date().getHours())){
        openingView.setText("Geöffnet");
        openingView.setTextColor(listContext.getResources().getColor(R.color.green));
    }
    else{
        openingView.setText("Öffnet um "+ open+ " Uhr");
        openingView.setTextColor(listContext.getResources().getColor(R.color.dark_red));
    }

    */

    private boolean openToday (String open, String closed, int hour){
        int o = 1;
        if(open.length()>1) {
            String onew = "" + open.charAt(0) + open.charAt(1);
            o = Integer.parseInt(onew);
        }
        int c = 1;
        if(closed.length()>1) {
            String cnew = "" + closed.charAt(0) + closed.charAt(1);
            c = Integer.parseInt(cnew);
        }


        if (hour >= o || hour < c) {
            return true;
        } else {
            return false;
        }

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
            int cashedInCoins = object.getInt("cashedInAmount");
            int coinsLeft = totalAmount - cashedInCoins;
            amount.setText("noch " + coinsLeft + " Stück");
        } else {
            amount.setText("Unbegrenzt");
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ParseUser: " + ParseUser.getCurrentUser());
                if(ParseUser.getCurrentUser().get("location").equals(object.get("location"))&&usermode){//Nur wenn usermode
                    Intent i = new Intent(listContext, ActivityEditCoin.class);
                    i.putExtra("isNewCoin", false);
                    i.putExtra("id", object.getObjectId());
                    listContext.startActivity(i);
                }
                else{
                    Intent i = new Intent(listContext, ActivityCoinItemView.class);
                    i.putExtra("id", object.getObjectId());
                    if(listContext!=null) {
                        listContext.startActivity(i);
                    }
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