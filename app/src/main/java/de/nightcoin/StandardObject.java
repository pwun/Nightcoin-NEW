package de.nightcoin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private boolean isOpen;
    private String tel;
    private String adr;
	
	public StandardObject(){}

    public String getTel(){
        return this.tel;
    }

    public void setTel(String tel){
        this.tel = tel;
    }

    public String getAdr(){
        return this.adr;
    }

    public void setAdr(String newAdr){
        this.adr = newAdr;
    }

    public String getOpeningToday(){
        String open = "";
        String closed = "";
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int day = cal.DAY_OF_WEEK;
        switch (day)
        {
            case Calendar.MONDAY:	open=opening[1];
                closed=closing[1];
                break;
            case Calendar.TUESDAY:	open=opening[2];
                closed=closing[2];
                break;
            case Calendar.WEDNESDAY:open=opening[3];
                closed=closing[3];
                break;
            case Calendar.THURSDAY:	open=opening[4];
                closed=closing[4];
                break;
            case Calendar.FRIDAY:	open=opening[5];
                closed=closing[5];
                break;
            case Calendar.SATURDAY:	open=opening[6];
                closed=closing[6];
                break;
            case Calendar.SUNDAY:	open=opening[0];
                closed=closing[0];
                break;
        }
        if(open.equals("")){
            return "";
        }
        if(open.equals("-")){
            return "Heute geschlossen";
        }
        if(/*Geöffnet*/openToday(open, closed, now.getHours())){
            return "Geöffnet";
        }
        return "Öffnet um "+ open+ " Uhr";
    }

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

    public void setFavorite(boolean status) {
        this.isFavorite = status;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return this.isOpen;
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
                Bitmap myImage = Bitmap.createScaledBitmap(bmp,bmp.getWidth()/2,bmp.getHeight()/2,true);
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
