package de.nightcoin;

import com.parse.ParseFile;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StandardObject{

	private String name;
	private Bitmap image;
	private String[] weekplan = new String[7];
	private String[] opening = new String[7];
	private String[] closing = new String[7];
    private String location;
    private String date;
    private boolean isFavorite;
	
	public StandardObject(){}

    public void setFavorite(boolean status) {
        this.isFavorite = status;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setDate(Date date){
        String dateString = new SimpleDateFormat("dd. MMMM").format(date);
        this.date = dateString;
    }

    public String getDate(){
        return this.date;
    }

    public void setImage(ParseFile parseFile){
        if(parseFile != null){
            try {
                byte[] stream = parseFile.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(stream, 0, stream.length);
                Bitmap myImage = Bitmap.createScaledBitmap(bmp,1000,400,true);
                this.image = myImage;
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public Bitmap getImage(){
        return this.image;
    }
	
	public void setWeekplan(ArrayList<String> newWeekplan){
		for(int i = 0; i < weekplan.length; i++){
			this.weekplan[i] = newWeekplan.get(i);
		}
	}
	
	public String[] getWeekplan(){
		return this.weekplan;
	}
	
	public void setOpening(ArrayList<String> newOpening){
		for(int i = 0; i < weekplan.length; i++){
			this.opening[i] = newOpening.get(i);
		}
	}
	
	public String[] getOpening(){
		return this.opening;
	}
	
	public void setClosing(ArrayList<String> newClosing){
		for(int i = 1; i < weekplan.length; i++){
			this.closing[i-1] = newClosing.get(i);
		}
		this.closing[6] = newClosing.get(0);
	}
	
	public String[] getClosing(){
		return this.closing;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
	
	public String getName(){
		return this.name;
	}


    public void setLocation(String newLocation){
        this.location = newLocation;
    }

    public String getLocation(){
        return this.location;
    }

}
