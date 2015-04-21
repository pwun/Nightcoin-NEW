package de.nightcoin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityEventItemView extends ActionBarActivity {

    Button ticketButton;

	String title;
    String objectId;
    private String location;
    private String dateString;
    private String priceString;
    private String description;
    private Date dateToFilterCoins;
    private ParseFile image;
    int tintColor;
    boolean ticketsEnabled;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_item_view);
		Intent i = getIntent();
		title = i.getStringExtra("title");
        objectId = i.getStringExtra("objectId");
		initView();
        getEventData();


    }
	
	private void initView(){
		TextView title = (TextView) findViewById(R.id.textViewEventItemViewTitle);
		title.setText(this.title);

	}

    private void initButtons() {
        Button filteredCoins = (Button) findViewById(R.id.buttonEventItemViewFilteredCoins);

        filteredCoins.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ActivityEventItemView.this, ActivityStandardList.class);
                System.out.println(dateToFilterCoins);
                i.putExtra("filterMode", "date");
                i.putExtra("dateToFilter", dateToFilterCoins.getTime());
                i.putExtra("input", "Coins");
                ActivityEventItemView.this.startActivity(i);
            }
        });

        final Button location = (Button) findViewById(R.id.buttonEventItemViewLocation);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityEventItemView.this, ActivityStandardItemView.class);
                i.putExtra("name", location.getText().toString());
                System.out.println(location.getText().toString());
                ActivityEventItemView.this.startActivity(i);
            }
        });

        //ticketButton = (Button) findViewById(R.id.buttonEventItemViewReservateTicket);
    }

    private void getEventData() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Events");
        query.fromLocalDatastore();
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                System.out.println("Creating " + object.getString("title"));
                title = object.getString("title");
                location = object.getString("location");
                priceString = "Eintritt: " + (String) object.get("price");
                dateString = new SimpleDateFormat("cccc, dd. MMMM, HH:mm").format(object.get("date")) + " Uhr";
                description = (String) object.get("details");
                image = object.getParseFile("image");
                normalizeDate((Date) object.get("date"));
                setTextViews();
                setImage();
                try {
                    image.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] stream, ParseException e) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(stream, 0, stream.length);
                            tintColor = getDominantColor(bmp);
                            ColorDrawable colorDrawable = new ColorDrawable(tintColor);
                            findViewById(R.id.buttonEventItemViewFilteredCoins).setBackgroundColor(tintColor);
                            findViewById(R.id.textViewEventItemViewDescriptionTitle).setBackgroundColor(tintColor);
                            Button locationButton = (Button) findViewById(R.id.buttonEventItemViewLocation);
                            locationButton.setTextColor(tintColor);

                            ActivityEventItemView.this.getSupportActionBar().setBackgroundDrawable(colorDrawable);

                            //layout.setBackgroundColor(getDominantColor(bmp));
                            bmp.recycle();
                            bmp = null;
                            System.gc();
                        }
                    });
                } catch (Exception ex) {
                    System.out.println("Error getting color");
                }
               /* // Tickets
                ticketsEnabled = (boolean)object.get("ticketsAvailable");
                if (ticketsEnabled) {
                    int tickets = (int)object.get("tickets");
                    int reservatedTickets = (int)object.get("reservatedTickets");
                    checkTickets(tickets, reservatedTickets);
                    System.out.println("Tickets" + tickets);
                } else {
                    ticketButton.setText("Keine Reservierung möglich");
                }*/
                initButtons();
            }
        });
    }


    private void setTextViews() {
        TextView titleTextView = (TextView) findViewById(R.id.textViewEventItemViewTitle);
        titleTextView.setText(title);
        TextView locationTextView = (TextView) findViewById(R.id.buttonEventItemViewLocation);
        locationTextView.setText(location);
        TextView dateTextView = (TextView) findViewById(R.id.textViewEventItemViewDate);
        dateTextView.setText(dateString);
        TextView priceTextView = (TextView) findViewById(R.id.textViewEventItemViewPrice);
        priceTextView.setText(priceString);
        TextView descriptionTextView = (TextView) findViewById(R.id.textViewEventItemViewDescription);
        descriptionTextView.setText(description);
    }

    private void setImage() {
        ParseImageView img = (ParseImageView) findViewById(R.id.imageViewEventItemView);
        img.setParseFile(image);
        img.loadInBackground();
    }

    /*private void checkTickets(int numberOfTickets, int reservatedTickets) {
        Button ticketButton = (Button)findViewById(R.id.buttonEventItemViewReservateTicket);
        if (reservatedTickets < numberOfTickets) {
            ticketButton.setText("Reservierung möglich");
        } else {
            ticketButton.setText("Keine Reservierung möglich");
        }
    }*/

    // gets the date of the event as parameter and sets it to 1 p.m.
    // then sets the Date ivar that's later used to filter coins
    private void normalizeDate (Date date) {
        Date newDate = new Date(date.getTime());
        newDate.setHours(12);
        newDate.setMinutes(0);
        dateToFilterCoins = newDate;
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
        if(luma > 180){
            System.out.println("Too light");
            //Set color to black
            r = 34;
            g = 34;
            b = 34;
            color = Color.rgb(r,g,b);
        }
        else{
            System.out.println("Color ok");
        }
        return color;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_item_view, menu);
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
}
