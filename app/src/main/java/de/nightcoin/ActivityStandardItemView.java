package de.nightcoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityStandardItemView extends ActionBarActivity {

	String name;

    ParseObject serverObject;
	StandardObject obj = new StandardObject();
    ArrayList<String> favorites = new ArrayList<String>();
    Menu menu;
    int dominantColor;
    MenuItem menuItemFavorites;
    ImageColor imageColor;
    int tintColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard_item_view);
		Intent i = getIntent();
		name = i.getStringExtra("name");
        setTitle(name);
        getData(name);
        initButtons();
        System.out.println("ID: " + ParseInstallation.getCurrentInstallation().getInstallationId());
        //setActionBarColor(null);
    }

    @Override
    public void onBackPressed() {
        obj.getImage().recycle();
        obj.setImage(null);
        obj = null;
        serverObject = null;
        System.gc();
        super.onBackPressed();
    }


    private void updateLocationStatistics(ParseObject object) {
        object.increment("numberOfOpens");
        object.saveEventually();
    }



	private void initButtons() {
        Button call = (Button)findViewById(R.id.buttonStandardItemViewCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = obj.getTel();

                String uri = "tel:" + tel.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

		Button weekplan = (Button) findViewById(R.id.buttonStandardItemViewWeekplan);

		weekplan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityWeekplan.class);
				i.putExtra("name", name);
                i.putExtra("color", dominantColor);
				ActivityStandardItemView.this.startActivity(i);
			}
		});

		Button nextEvents = (Button) findViewById(R.id.buttonStandardItemViewNextEvents);

		nextEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityStandardList.class);
				i.putExtra("name", name);
				i.putExtra("input", "nextEvents");
				ActivityStandardItemView.this.startActivity(i);
			}
		});
		
		Button nextCoins = (Button) findViewById(R.id.buttonStandardItemViewNextCoins);

		nextCoins.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ActivityStandardItemView.this,ActivityStandardList.class);
				i.putExtra("name", name);
				i.putExtra("input", "nextCoins");
				ActivityStandardItemView.this.startActivity(i);
			}
		});

        Button map = (Button)findViewById(R.id.buttonStandardItemViewMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = obj.getLat();
                double longitude = obj.getLong();
                try{
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude+"?q="+latitude+","+longitude+"("+obj.getName()+")"));

                    ActivityStandardItemView.this.startActivity(intent);
                    System.out.println("Lon/Lat = "+ latitude + "/" + longitude);
                }catch(Exception e){
                    System.out.println("FEHLER! Lon/Lat = "+ latitude + "/" + longitude);
                }
            }
        });
	}

	private void getData(String name) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
		query.whereEqualTo("name", name);
		query.setLimit(1);
		query.fromLocalDatastore();
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				System.out.println("Creating " + objects.get(0).get("name"));
				serverObject = objects.get(0);
				obj.setName((String) serverObject.get("name"));
				obj.setImage((ParseFile) serverObject.getParseFile("image"));
                obj.setAdr((String) serverObject.get("address"));
                obj.setTel((String) serverObject.get("phone"));
                ParseGeoPoint geo = (ParseGeoPoint) serverObject.get("geoData");
                obj.setLat(geo.getLatitude());
                obj.setLong(geo.getLongitude());
				obj.setWeekplan((ArrayList<String>) serverObject.get("weekplan"));
				obj.setOpening((ArrayList<String>) serverObject.get("opensAt"));
				obj.setClosing((ArrayList<String>) serverObject.get("closesAt"));
                favorites = ((ArrayList<String>) serverObject.get("favorites"));

                TextView tel = (TextView)findViewById(R.id.textViewStandardItemViewTel);
                if(obj.getTel()!=null){
                    tel.setText(""+obj.getTel());
                    System.out.println(obj.getTel());
                }
                else{System.out.println("Fehler bei Telefonnummer");}
                TextView adr = (TextView)findViewById(R.id.textViewStandardItemViewAdress);
                if(obj.getAdr() != null){
                    adr.setText(""+obj.getAdr());
                    System.out.println(obj.getAdr());
                }
                else{
                    System.out.println("Fehler bei der Adresse");
                }

                Button weekplan = (Button) findViewById(R.id.buttonStandardItemViewWeekplan);



                try {
                    byte[] stream = serverObject.getParseFile("image").getData();
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream, 0, stream.length);
                    dominantColor = getDominantColor(bmp);
                    ColorDrawable colorDrawable = new ColorDrawable(dominantColor);

                    /*RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutStandardItemViewBackground);
                    TextView hours = (TextView) findViewById(R.id.textViewStandardItemViewHours);
                    TextView contact = (TextView) findViewById(R.id.textViewStandardItemViewContact);
                    Button nextCoins = (Button) findViewById(R.id.buttonStandardItemViewNextCoins);
                    Button nextEvents = (Button) findViewById(R.id.buttonStandardItemViewNextEvents);
                    Button weekPlan = (Button) findViewById(R.id.buttonStandardItemViewWeekplan);
                    Button map = (Button)findViewById(R.id.buttonStandardItemViewMap);
                    Button call = (Button)findViewById(R.id.buttonStandardItemViewCall);*/
                    findViewById(R.id.buttonStandardItemViewNextCoins).setBackgroundColor(dominantColor);
                    findViewById(R.id.buttonStandardItemViewNextEvents).setBackgroundColor(dominantColor);
                    findViewById(R.id.buttonStandardItemViewWeekplan).setBackgroundColor(dominantColor);
                    findViewById(R.id.ViewstandardItemViewSeperator).setBackgroundColor(dominantColor);
                    /*call.setBackgroundColor(dominantColor);
                    map.setBackgroundColor(dominantColor);*/

                    ActivityStandardItemView.this.getSupportActionBar().setBackgroundDrawable(colorDrawable);
                    findViewById(R.id.textViewStandardItemViewHours).setBackgroundColor(dominantColor);
                    findViewById(R.id.textViewStandardItemViewContact).setBackgroundColor(dominantColor);
                    Button map = (Button)findViewById(R.id.buttonStandardItemViewMap);
                    map.setTextColor(dominantColor);
                    Button call = (Button)findViewById(R.id.buttonStandardItemViewCall);
                    call.setTextColor(dominantColor);
                    //layout.setBackgroundColor(getDominantColor(bmp));
                    stream = null;
                    bmp.recycle();
                    bmp = null;
                    System.gc();
                    //System.out.println(getSecundaryColorFromColor(getDominantColor(bmp)));
                } catch (Exception ex) {
                    System.out.println("Error getting color");
                }


				ImageView img = (ImageView) findViewById(R.id.imageViewStandardItemView);
				img.setImageBitmap(obj.getImage());
				//img.loadInBackground();

				getOpening();
                updateLocationStatistics(serverObject);

                if(menu!= null){
                    checkIfFavorite();
                }
                else{
                    System.out.println("Menu still null");
                }

			}
		});

		getDailyData();
	}

    private int getSecundaryColorFromColor(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        String output = "-";
        for (int i = 0; i < hexColor.length(); i++) {
            int colorValue = Character.getNumericValue(hexColor.charAt(i));
            if (colorValue < 12) {
                colorValue += 3;
            }
            output = output + colorValue;
        }
        System.out.println("Output" + output);
        return Integer.parseInt(output);
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        int color = bitmap1.getPixel(0, 0);
        System.out.println("Dominant Color = "+color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        double luma = 0.2126 * r + 0.7152 * g + 0.0722 * b; // per ITU-R BT.709
        System.out.println("Luma: " + luma);
        if(luma > 170){
            System.out.println("Too light");
            color = -7829368;
        }
        else{
            System.out.println("Color ok");
        }
        return color;
    }

	private void getOpening() {
		String[] opening = new String[7];
		String[] closing = new String[7];
		opening = obj.getOpening();
		closing = obj.getClosing();

		TextView moOp = (TextView) findViewById(R.id.textViewStandardItemViewMoOpening);
		TextView diOp = (TextView) findViewById(R.id.textViewStandardItemViewDiOpening);
		TextView miOp = (TextView) findViewById(R.id.textViewStandardItemViewMiOpening);
		TextView doOp = (TextView) findViewById(R.id.textViewStandardItemViewDoOpening);
		TextView frOp = (TextView) findViewById(R.id.textViewStandardItemViewFrOpening);
		TextView saOp = (TextView) findViewById(R.id.textViewStandardItemViewSaOpening);
		TextView soOp = (TextView) findViewById(R.id.textViewStandardItemViewSoOpening);
		TextView moCl = (TextView) findViewById(R.id.textViewStandardItemViewMoClosing);
		TextView diCl = (TextView) findViewById(R.id.textViewStandardItemViewDiClosing);
		TextView miCl = (TextView) findViewById(R.id.textViewStandardItemViewMiClosing);
		TextView doCl = (TextView) findViewById(R.id.textViewStandardItemViewDoClosing);
		TextView frCl = (TextView) findViewById(R.id.textViewStandardItemViewFrClosing);
		TextView saCl = (TextView) findViewById(R.id.textViewStandardItemViewSaClosing);
		TextView soCl = (TextView) findViewById(R.id.textViewStandardItemViewSoClosing);

		moOp.setText(opening[0]);
		diOp.setText(opening[1]);
		miOp.setText(opening[2]);
		doOp.setText(opening[3]);
		frOp.setText(opening[4]);
		saOp.setText(opening[5]);
		soOp.setText(opening[6]);
		moCl.setText(closing[0]);
		diCl.setText(closing[1]);
		miCl.setText(closing[2]);
		doCl.setText(closing[3]);
		frCl.setText(closing[4]);
		saCl.setText(closing[5]);
		soCl.setText(closing[6]);

	}

	private int normalizeWeekday(Date date) {
		// Stimmt das auch?
		// 1 = Montag So = 0 oder 7?
		if (date.getDay() == 0) {
			return 6;
		} else {
			return date.getDay() - 1;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_standard_item_view, menu);
        this.menu = menu;
        checkIfFavorite();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_favorites) {
            menuItemFavorites = item;
			updateFavoriteStatus(item);
		}
		return super.onOptionsItemSelected(item);
	}

    private boolean isFavorite() {
        return (favorites.contains(ParseInstallation.getCurrentInstallation().getInstallationId()));
    }

    private void checkIfFavorite() {
        if (isFavorite()) {
            System.out.println("is favorites");
            menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_action_important));
        } else {
            System.out.println("no favorite");
            menu.findItem(R.id.action_favorites).setIcon(getResources().getDrawable(R.drawable.ic_action_not_important));
        }
    }

    private void updateFavoriteStatus(MenuItem item) {
       if (isFavorite()) {
           removeFromFavorites();
           item.setIcon(getResources().getDrawable(R.drawable.ic_action_not_important));
           Toast t = Toast.makeText(ActivityStandardItemView.this, "Aus Favoriten entfernt", Toast.LENGTH_SHORT);
           t.show();
       } else {
           addToFavorites();
           item.setIcon(getResources().getDrawable(R.drawable.ic_action_important));
           Toast t = Toast.makeText(ActivityStandardItemView.this, "Zu Favoriten hinzugefügt", Toast.LENGTH_SHORT);
           t.show();
       }

        System.out.println("isFavorite: " + isFavorite());
    }

    private void addToFavorites() {
        System.out.println("AddToFavorites Saving..");
        serverObject.add("favorites", ParseInstallation.getCurrentInstallation().getInstallationId().toString());
        favorites.add(ParseInstallation.getCurrentInstallation().getInstallationId());

        serverObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Favorit hinzufügen GESPEICHERT!!");
            }
        });
        //menuItemFavorites.setIcon(getResources().getDrawable(R.drawable.ic_action_important));
    }

    private void removeFromFavorites() {
        System.out.println("RemoveFromFavorites Saving..");
        favorites.remove(ParseInstallation.getCurrentInstallation().getInstallationId());
        ((ArrayList<String>) serverObject.get("favorites")).remove(ParseInstallation.getCurrentInstallation().getInstallationId().toString());
        serverObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Favorit löschen GESPEICHERT!");
            }
        });
       // menuItemFavorites.setIcon(getResources().getDrawable(R.drawable.ic_action_important));
    }

	private void getDailyData() {
		Date d = new Date();
		d.setHours(1);
		d.setMinutes(0);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Events");
		query.whereEqualTo("location", name);
		query.whereGreaterThan("date", d);
		query.orderByAscending("date");
		query.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(new Date());
					cal2.setTime(object.getDate("date"));
					boolean sameDay = cal1.get(Calendar.YEAR) == cal2
							.get(Calendar.YEAR)
							&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
									.get(Calendar.DAY_OF_YEAR);

					if (sameDay) {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
						title.setText((CharSequence) object.get("title"));
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
						detail.setText("Eintritt: " + object.get("price"));
					} else {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
						title.setText("jeden "
								+ new SimpleDateFormat("EEEE").format(cal1
										.getTime()) + ":");
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
						detail.setText(obj.getWeekplan()[normalizeWeekday(cal1
								.getTime())]);
					}
				} else {
					TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEvents);
					title.setText("jeden "
							+ new SimpleDateFormat("EEEE").format(new Date())
							+ ":");
					TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysEventsDetail);
					detail.setText(obj.getWeekplan()[normalizeWeekday(new Date())]);
					System.out.println("Object == null");
				}
			}
		});

		query = new ParseQuery<ParseObject>("Coupons");
		query.whereEqualTo("location", name);
		query.whereGreaterThan("date", d);
		query.orderByAscending("date");
		query.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (object != null) {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(new Date());
					cal2.setTime(object.getDate("date"));
					boolean sameDay = cal1.get(Calendar.YEAR) == cal2
							.get(Calendar.YEAR)
							&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
									.get(Calendar.DAY_OF_YEAR);

					if (sameDay) {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
						title.setText("1 Coin verfügbar");
						TextView detail = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoinsDetail);
						detail.setText("" + object.get("value"));
					} else {
						TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
						title.setText("kein Coin verfügbar");
					}

				} else {
					TextView title = (TextView) findViewById(R.id.textViewStandardItemViewTodaysCoins);
					title.setText("kein Coin verfügbar");
				}
			}
		});
	}
}
