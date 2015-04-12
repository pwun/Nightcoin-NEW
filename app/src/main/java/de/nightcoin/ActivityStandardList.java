package de.nightcoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityStandardList extends ActionBarActivity {

	Intent i;
	String mode;
	ProgressDialog dialog;
	List<ParseObject> parseList;
	List<StandardObject> list = null;
	ArrayList<CoinObject> coinlist;
	Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standard_list);
        i = getIntent();
		mode = i.getStringExtra("input");
        if(mode.equals("Coins")||mode.equals("nextCoins")||mode.equals("filteredCoins")){
            setTitle("Nightcoins");
        }
        else if(mode.equals("Events")||mode.equals("nextEvents")){
            setTitle("Events");
        }
        else if(mode.equals("Clubs")){
            setTitle("Clubs");
        }
        else if(mode.equals("Bars")){
            setTitle("Bars");
        }
        else if(mode.equals("Favoriten")){
            setTitle("Favoriten");
        }
        else if(mode.equals("Taxi")){
            setTitle("Taxi");
        }
        else if(mode.equals("Food")){
            setTitle("Food");
        }
		System.out.println("Creating "+ mode + "list...");
		//Execute Async Task
		new RemoteDataTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_standard_list, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ActivityStandardList.this);
			dialog.setTitle(mode);
			dialog.setMessage("Loading...");
			dialog.setIndeterminate(false);
			dialog.show();
		}

        private Date normalizedDate (Date date) {
            date.setHours(1);
            date.setMinutes(0);
            return date;
        }
		
		@Override
		protected Void doInBackground(Void... params) {
			// Unterscheidung nach mode um query anzupassen
			
			ParseQuery<ParseObject> query = null;
			list = new ArrayList<StandardObject>();
			coinlist = new ArrayList<CoinObject>();
			
			if(mode.equals("Coins")){
				try{
					query = new ParseQuery<ParseObject>("Coupons");
                    query.whereGreaterThan("date", normalizedDate(new Date()));
					query.orderByAscending("date");
					parseList = query.find();
					for (ParseObject data : parseList) {
						CoinObject obj = new CoinObject();
						obj.setValue((String) data.get("value"));
						obj.setLocation((String) data.get("location"));
						obj.setDate((Date) data.get("date"));
                        obj.setId((String) data.getObjectId());
                        obj.setAmount((Integer) data.get("amount"));
                        /*obj.setLimited((Boolean) data.get("limited"));
                        obj.setCashedInAmount((Integer) data.get("cashedInAmount"));*/
						//set every needed value here
						
						coinlist.add(obj);
						System.out.println("Coin fetched: "+ obj.getValue());
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH COINS");
				}
			}
			
			if(mode.equals("nextCoins")){
				try{
					query = new ParseQuery<ParseObject>("Coupons");
					query.whereEqualTo("location", i.getStringExtra("name"));
                    query.whereGreaterThan("date", normalizedDate(new Date()));
                    query.orderByAscending("date");
					parseList = query.find();
					for (ParseObject data : parseList) {
						CoinObject obj = new CoinObject();
						obj.setValue((String) data.get("value"));
						obj.setLocation((String) data.get("location"));
						obj.setDate((Date) data.get("date"));
                        obj.setId((String) data.getObjectId());
						//set every needed value here
						
						coinlist.add(obj);
						System.out.println("Location fetched: "+ obj.getValue());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH CLUBS");
				}
			}

            if(mode.equals("filteredCoins")){
                try{

                    // normalized Date from Event
                    Date dateToFilter = new Date();
                    dateToFilter.setTime(getIntent().getLongExtra("dateToFilter", -1));

                    // date from Event set to 23:50 Uhr
                    Date maxDate = dateToFilter;
                    maxDate.setHours(23);
                    maxDate.setMinutes(50);

                    query = new ParseQuery<ParseObject>("Coupons");
                    query.whereGreaterThan("date", dateToFilter);
                    query.whereLessThan("date", maxDate);
                    query.orderByAscending("date");
                    parseList = query.find();
                    for (ParseObject data : parseList) {
                        CoinObject obj = new CoinObject();
                        obj.setValue((String) data.get("value"));
                        obj.setLocation((String) data.get("location"));
                        obj.setDate((Date) data.get("date"));
                        obj.setId((String) data.getObjectId());
                        //set every needed value here

                        coinlist.add(obj);
                        System.out.println("Coin fetched: " + obj.getDate());
                    }
                   }
                catch(Exception e){
                    System.out.println("ERROR, CANT FETCH COINS");
                }
            }
			
			if(mode.equals("nextEvents")){
				try{
					query = new ParseQuery<ParseObject>("Events");
					query.whereEqualTo("location", i.getStringExtra("name"));
					parseList = query.find();
					for (ParseObject data : parseList) {
						StandardObject obj = new StandardObject();
						obj.setName((String) data.get("title"));
						obj.setImage((ParseFile) data.getParseFile("image"));
						//set every needed value here
						
						list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH EVENTS");
				}
			}
			
			if (mode.equals("Bars")){
				try{
					query = new ParseQuery<ParseObject>("Locations");
					query.whereEqualTo("category", "Bar");
					parseList = query.find();
					for (ParseObject data : parseList) {
						StandardObject obj = new StandardObject();
						obj.setName((String) data.get("name"));
						obj.setImage((ParseFile) data.getParseFile("image"));
						
						list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH BARS");
				}
				
				
				//ParseObject.unpinAllInBackground("bars");
				
				
				//Local Versuch
				/*query = new ParseQuery<ParseObject>("Locations");
				query.whereEqualTo("category", "Bar");
				query.findInBackground(new FindCallback<ParseObject>(){

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						ParseObject.pinAllInBackground("bars", objects);
						for (ParseObject data : parseList) {
							StandardObject obj = new StandardObject();
							obj.setName((String) data.get("name"));
							obj.setImage((ParseFile) data.getParseFile("image"));
							list.add(obj);
						}
					}});*/
				
					
			}
			if(mode.equals("Clubs")){
				try{
					query = new ParseQuery<ParseObject>("Locations");
					query.whereEqualTo("category", "Club");
					parseList = query.find();
					for (ParseObject data : parseList) {
						StandardObject obj = new StandardObject();
						obj.setName((String) data.get("name"));
						obj.setImage((ParseFile) data.getParseFile("image"));
						//set every needed value here
						
						list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH CLUBS");
				}
			}
			
			if(mode.equals("Events")){
				try{
					query = new ParseQuery<ParseObject>("Events");
					parseList = query.find();
					for (ParseObject data : parseList) {
						StandardObject obj = new StandardObject();
						obj.setName((String) data.get("title"));
						obj.setImage((ParseFile) data.getParseFile("image"));
						//set every needed value here
						
						list.add(obj);
						System.out.println("Events fetched: "+ obj.getName());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH EVENTS");
				}
			}
			
			if(mode.equals("Favoriten")){
				try{
					query = new ParseQuery<ParseObject>("Locations");
                    query.whereEqualTo("favorites", ParseInstallation.getCurrentInstallation().getInstallationId());
					parseList = query.find();
					for (ParseObject data : parseList) {
						StandardObject obj = new StandardObject();
						obj.setName((String) data.get("name"));
						obj.setImage((ParseFile) data.getParseFile("image"));
						//set every needed value here

						list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH FAVORITES");
				}
			}
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mode.equals("Coins")||mode.equals("nextCoins")||mode.equals("filteredCoins")){
                ParseObject.unpinAllInBackground("coins", new DeleteCallback(){

                    @Override
                    public void done(ParseException e) {
                        ParseObject.pinAllInBackground("coins", parseList);
                    }
                });
				ListView listview = (ListView) findViewById(R.id.listViewStandardList);
				CoinListViewAdapter adapter = new CoinListViewAdapter(ActivityStandardList.this, coinlist);
				listview.setAdapter(adapter);
			}
			
			else{
				if(mode.equals("Bars")){
					ParseObject.unpinAllInBackground("bars", new DeleteCallback(){

						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("bars", parseList);
						}
					});
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
					StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "location");
					listview.setAdapter(adapter);
				}
				if(mode.equals("Clubs")){
					ParseObject.unpinAllInBackground("clubs", new DeleteCallback(){

						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("clubs", parseList);
						}
					});
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
					StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "location");
					listview.setAdapter(adapter);
				}
				if(mode.equals("Events")){
					ParseObject.unpinAllInBackground("events", new DeleteCallback(){

						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("events", parseList);
						}
					});
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
					StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "event");
					listview.setAdapter(adapter);
				}
				
				if(mode.equals("nextEvents")){
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
					StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "event");
					listview.setAdapter(adapter);
				}
				/*if(mode.equals("Favoriten")){
					ParseObject.unpinAllInBackground("favorites", new DeleteCallback(){

						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("favorites", parseList);
						}
					});
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
				StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "location");
				listview.setAdapter(adapter);
				}*/
				
			}
			
			dialog.dismiss();
		}
		
	}
}
