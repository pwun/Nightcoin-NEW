package de.nightcoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;


public class ActivityEditImages extends ActionBarActivity {

    ImageButton image1;
    ImageButton image2;
    ImageButton image3;
    ImageButton image4;
    ImageButton image5;

    ProgressDialog progressDialog;

    ParseObject serverObject;

    int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_images);
        queryForData();
    }

    private void init() {
        image1=(ImageButton) findViewById(R.id.imageViewActivityEditImages1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getImageFromDevice();*/
            }
        });

        image2=(ImageButton) findViewById(R.id.imageViewActivityEditImages2);
        image2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
/*
                 getImageFromDevice();
*/
             }
        });

        image3=(ImageButton) findViewById(R.id.imageViewActivityEditImages3);
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                getImageFromDevice();
*/
            }
        });

        image4=(ImageButton) findViewById(R.id.imageViewActivityEditImages4);
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                getImageFromDevice();
*/
            }
        });

        image5=(ImageButton) findViewById(R.id.imageViewActivityEditImages5);
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                getImageFromDevice();
*/
            }
        });
    }

    private void queryForData() {
        progressDialog = ProgressDialog.show(ActivityEditImages.this, "", "Lädt Bilder...", true);
        // Locate the class table named "Footer" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Locations");
        query.setLimit(1);
        query.whereEqualTo("name", ParseUser.getCurrentUser().get("location").toString());
        // Locate the objectId from the class
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                serverObject = parseObject;
                ParseFile file1 = (ParseFile) parseObject.get("image");
                ParseFile file2 = (ParseFile) parseObject.get("image2");
                ParseFile file3 = (ParseFile) parseObject.get("image3");
                ParseFile file4 = (ParseFile) parseObject.get("image4");
                ParseFile file5 = (ParseFile) parseObject.get("image5");
                file1.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test",
                                    "We've got data in data.");
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            // Set the Bitmap into the
                            // ImageView
                            image1.setImageBitmap(bmp);
                        } else {
                            Log.d("test",
                                    "There was a problem downloading the data.");
                        }
                    }
                });
                file2.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test",
                                    "We've got data in data.");
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            // Set the Bitmap into the
                            // ImageView
                            image2.setImageBitmap(bmp);
                        } else {
                            Log.d("test",
                                    "There was a problem downloading the data.");
                        }
                    }
                });
                file3.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test",
                                    "We've got data in data.");
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            // Set the Bitmap into the
                            // ImageView
                            image3.setImageBitmap(bmp);
                        } else {
                            Log.d("test",
                                    "There was a problem downloading the data.");
                        }
                    }
                });
                file4.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test",
                                    "We've got data in data.");
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            // Set the Bitmap into the
                            // ImageView
                            image4.setImageBitmap(bmp);
                        } else {
                            Log.d("test",
                                    "There was a problem downloading the data.");
                        }
                    }
                });
                file5.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        if (e == null) {
                            Log.d("test",
                                    "We've got data in data.");
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            // Set the Bitmap into the
                            // ImageView
                            image5.setImageBitmap(bmp);
                            // Close progress dialog
                            progressDialog.dismiss();
                        } else {
                            Log.d("test",
                                    "There was a problem downloading the data.");
                        }
                    }
                });
            }
        });
    }


    private void getImageFromDevice() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
/*
        imageId = imageViewIndex;
*/

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri chosenImageUri = data.getData();
/*            if (imageId == 1) {
                image1.setImageURI(chosenImageUri);
            }
            if (imageId == 2) {
                image2.setImageURI(chosenImageUri);
            }
            if (imageId == 3) {
                image3.setImageURI(chosenImageUri);
            }
            if (imageId == 4) {
                image5.setImageURI(chosenImageUri);
            }
            if (imageId == 5) {
                image5.setImageURI(chosenImageUri);
            }*/
        }
    }

    public void setImage(Bitmap map, int imageIndex) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        map.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file  = new ParseFile("image.jpeg", image);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
