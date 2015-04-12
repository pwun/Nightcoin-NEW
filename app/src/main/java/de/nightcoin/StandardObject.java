package de.nightcoin;

import com.parse.ParseFile;

import java.util.ArrayList;

public class StandardObject{

	private String name;
	private ParseFile image;
	private String[] weekplan = new String[7];
	private String[] opening = new String[7];
	private String[] closing = new String[7];
    private String location;
    private boolean isFavorite;
	
	public StandardObject(){}

    public void setFavorite(boolean status) {
        this.isFavorite = status;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }
	
	public void setImage(ParseFile parseFile){
		this.image = parseFile;
	}
	
	public ParseFile getImage(){
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
        this.location = location;
    }

    public String getLocation(){
        return this.location;
    }

}
