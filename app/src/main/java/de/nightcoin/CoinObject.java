package de.nightcoin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CoinObject {

	private String value;
	private String location;
	private String date;
    private String id;
	
	public CoinObject(){}
	
	public void setValue(String newValue){
		this.value = newValue;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void setDate(Date newDate){
		this.date = new SimpleDateFormat("dd. MMMM yyyy").format(newDate);
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setLocation(String newLocation){
		this.location = newLocation;
	}
	
	public String getLocation(){
		return this.location;
	}

    public void setId(String newId){
        this.id = newId;
    }

    public String getId(){
        return this.id;
    }
}
