package de.nightcoin;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {
	 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "0hdzhUQPWYezu2nS1Y32MMbUonkbxjcvzIYHYOOC", "wMZJWwwbJzRgDMbETu60iC25epVArC0AR6cDRoGv");
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}
