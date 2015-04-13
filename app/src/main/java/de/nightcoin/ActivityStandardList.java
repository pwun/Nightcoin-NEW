package de.nightcoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

    private int normalizeWeekday(Date date) {
        // Stimmt das auch?
        // 1 = Montag So = 0 oder 7?
        if (date.getDay() == 0) {
            return 6;
        } else {
            return date.getDay() - 1;
        }
    }

    private boolean locationIsOpen(StandardObject object) {

        int opensAt = Integer.parseInt(object.getOpening()[normalizeWeekday(new Date())]);
        int closesAt = Integer.parseInt(object.getClosing()[normalizeWeekday(new Date())]);

        if (opensAt == 0 && closesAt == 0) {
            return false;
        }
        if (new Date().getHours() >= opensAt || new Date().getHours() < closesAt) {
            return true;
        } else {
            return false;
        }



        /*
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
         */
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

        private boolean locationIsOpen(StandardObject object) {

            int opensAt = 0;
            int closesAt = 0;

            if (!(object.getOpening()[normalizeWeekday(new Date())].equals("-"))) {
                opensAt = Integer.parseInt(object.getOpening()[normalizeWeekday(new Date())]);
            }
            if (!(object.getClosing()[normalizeWeekday(new Date())].equals("-"))) {
                closesAt = Integer.parseInt(object.getClosing()[normalizeWeekday(new Date())]);
            }

            if (opensAt == 0 && closesAt == 0) {
                return false;
            }
            if (new Date().getHours() >= opensAt || new Date().getHours() < closesAt) {
                return true;
            } else {
                return false;
            }
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
                        obj.setLimited((Boolean) data.get("limited"));
                        try{
                        obj.setCashedIn((Integer) data.get("cashedInAmount"));}
                        catch (Exception e){
                            obj.setCashedIn(0);
                        }
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
                        obj.setAmount((Integer) data.get("amount"));
                        obj.setId((String) data.getObjectId());
                        obj.setLimited((Boolean) data.get("limited"));
                        try{
                            obj.setCashedIn((Integer) data.get("cashedInAmount"));}
                        catch (Exception e){
                            obj.setCashedIn(0);
                        }
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
                    Date maxDate = (Date)dateToFilter.clone();
                    maxDate.setHours(23);
                    maxDate.setMinutes(50);

                    System.out.println(dateToFilter);
                    System.out.println(maxDate);

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
                        obj.setAmount((Integer) data.get("amount"));
                        obj.setId((String) data.getObjectId());
                        obj.setLimited((Boolean) data.get("limited"));
                        try{
                            obj.setCashedIn((Integer) data.get("cashedInAmount"));}
                        catch (Exception e){
                            obj.setCashedIn(0);
                        }
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
						obj.setImage(data.getParseFile("image"));
                        obj.setDate((Date)data.get("date"));
                        obj.setLocation((String) data.get("location"));
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));
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
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));

                        if(data.getParseFile("image")!= null){
                            obj.setImage(data.getParseFile("image"));
                        }
                        try{
                            obj.setOpening((ArrayList<String>)data.get("opensAt"));
                            obj.setClosing((ArrayList<String>)data.get("closesAt"));
                        }
                        catch(Exception e){
                            System.out.println("Keine öffnungszeiten gefunden");
                        }

/*
                        obj.setOpen(locationIsOpen(obj));
*/
                        try {
                            System.out.println("Objekt: " + obj);
                            System.out.println("Location is open: " + locationIsOpen(obj));
                        } catch (Exception e){
                            System.out.println("Nope");
                        }


                        list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());	
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH BARS");
                    System.out.println(e.getStackTrace());
				}
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
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));
						//set every needed value here
                        System.out.println("Checke Öffnungszeiten...");
                        try{
                            obj.setOpening((ArrayList<String>)data.get("opensAt"));
                            obj.setClosing((ArrayList<String>)data.get("closesAt"));
                        }
                        catch(Exception e){
                            System.out.println("Keine öffnungszeiten gefunden");
                        }
						
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
						obj.setImage(data.getParseFile("image"));
                        obj.setDate((Date)data.get("date"));
                        obj.setLocation((String) data.get("location"));
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));
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
						obj.setImage(data.getParseFile("image"));
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));
                        try{
                            obj.setOpening((ArrayList<String>)data.get("opensAt"));
                            obj.setClosing((ArrayList<String>)data.get("closesAt"));
                        }
                        catch(Exception e){
                            System.out.println("Keine öffnungszeiten gefunden");
                        }
						//set every needed value here

						list.add(obj);
						System.out.println("Location fetched: "+ obj.getName());
					}
				}
				catch(Exception e){
					System.out.println("ERROR, CANT FETCH FAVORITES");
				}
			}

            if(mode.equals("Food")){
                try{
                    query = new ParseQuery<ParseObject>("Locations");
                    query.whereEqualTo("category", "Food");
                    parseList = query.find();
                    for (ParseObject data : parseList) {
                        StandardObject obj = new StandardObject();
                        obj.setName((String) data.get("name"));
                        //obj.setAdr((String) data.get("adress"));
                        //obj.setTel((String) data.get("phone"));
                        try{obj.setImage(data.getParseFile("image"));}
                        catch (Exception e){
                            System.out.println("Image setzen fehler");
                        }
                        try{
                            obj.setOpening((ArrayList<String>)data.get("opensAt"));
                            obj.setClosing((ArrayList<String>)data.get("closesAt"));
                        }
                        catch(Exception e){
                            System.out.println("Keine öffnungszeiten gefunden");
                        }
                        //set every needed value here

                        list.add(obj);
                        System.out.println("Food fetched: "+ obj.getName());
                    }
                }
                catch(Exception e){
                    System.out.println("ERROR, CANT FETCH FAVORITES");
                }
            }

            if(mode.equals("Taxi")){
                try{
                    query = new ParseQuery<ParseObject>("Locations");
                    query.whereEqualTo("category", "Taxi");
                    parseList = query.find();
                    for (ParseObject data : parseList) {
                        StandardObject obj = new StandardObject();
                        obj.setName((String) data.get("name"));
                        obj.setAdr((String) data.get("adress"));
                        obj.setTel((String) data.get("phone"));
                        try{obj.setImage(data.getParseFile("image"));}
                        catch (Exception e){
                            System.out.println("Image setzen fehler");
                        }
                        //set every needed value here

                        list.add(obj);
                        System.out.println("Taxi fetched: "+ obj.getName());
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
				if(mode.equals("Favoriten")){
					ParseObject.unpinAllInBackground("favorites", new DeleteCallback(){

						@Override
						public void done(ParseException e) {
							ParseObject.pinAllInBackground("favorites", parseList);
						}
					});
					ListView listview = (ListView) findViewById(R.id.listViewStandardList);
				    StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "location");
				    listview.setAdapter(adapter);
				}

                if(mode.equals("Taxi")){
                    ParseObject.unpinAllInBackground("taxis", new DeleteCallback(){

                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground("taxis", parseList);
                        }
                    });
                    ListView listview = (ListView) findViewById(R.id.listViewStandardList);
                    StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "taxi");
                    listview.setAdapter(adapter);
                }

                if(mode.equals("Food")){
                    ParseObject.unpinAllInBackground("food", new DeleteCallback(){

                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground("food", parseList);
                        }
                    });
                    ListView listview = (ListView) findViewById(R.id.listViewStandardList);
                    StandardListViewAdapter adapter = new StandardListViewAdapter(ActivityStandardList.this, list, "food");
                    listview.setAdapter(adapter);
                }
				
			}
			
			dialog.dismiss();
		}
		
	}
}