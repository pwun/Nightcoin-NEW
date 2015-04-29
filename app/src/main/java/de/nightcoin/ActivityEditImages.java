package de.nightcoin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.widget.Toast;
import com.parse.*;

import java.io.ByteArrayOutputStream;


public class ActivityEditImages extends ActionBarActivity {

    ImageButton image1;
    ImageButton image2;
    ImageButton image3;
    ImageButton image4;
    ImageButton image5;

    boolean[] changed = {false, false, false, false, false};

    ProgressDialog progressDialog;

    ParseObject serverObject;

    int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_images);
        init();
        queryForData();
    }

    @Override
    public void onBackPressed(){
        boolean changes = false;

        for(int i = 0; i < 5 ; i++){
            if(changed[i] == true){
                changes = true;
            }
        }

        if(!changes){
            super.onBackPressed();
        }
        else{
            //Dialog
            new AlertDialog.Builder(ActivityEditImages.this)
                    .setTitle("Änderungen speichern?")
//                    .setMessage("Wir arbeiten daran, dies schnellstmöglich innerhalb der App zu ermöglichen.")
                    .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveAll();
                            ActivityEditImages.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Verwerfen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            ActivityEditImages.super.onBackPressed();
                        }
                    })
                    .show();
        }
    }

    private void saveAll(){
        for (int i = 0; i < 5; i++) {
            if (changed[i] == true){
                System.out.println("Saved Picture Nr."+(i+1)+"!");
                //uploadImage
                ImageButton img;
                String imageString;
                switch (i){
                    case 0: img = image1;
                        imageString = "image";
                        break;
                    case 1: img= image2;
                        imageString = "image2";
                        break;
                    case 2: img = image3;
                        imageString = "image3";
                        break;
                    case 3: img = image4;
                        imageString = "image4";
                        break;
                    case 4: img = image5;
                        imageString = "image5";
                        break;
                    default:img = image1;
                        imageString = "image";
                }
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();

                ParseFile pf = new ParseFile(bitmapdata);
                serverObject.put(imageString, pf);
                //Upload
                serverObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast t = Toast.makeText(ActivityEditImages.this, "Bilder gespeichert", Toast.LENGTH_SHORT);
                        t.show();
                    }
                });

            }
        }
    }

    private void init() {
        image1=(ImageButton) findViewById(R.id.imageViewActivityEditImages1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromDevice(1);
            }
        });

        image2=(ImageButton) findViewById(R.id.imageViewActivityEditImages2);
        image2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getImageFromDevice(2);
             }
        });

        image3=(ImageButton) findViewById(R.id.imageViewActivityEditImages3);
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromDevice(3);
            }
        });

        image4=(ImageButton) findViewById(R.id.imageViewActivityEditImages4);
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromDevice(4);
            }
        });

        image5=(ImageButton) findViewById(R.id.imageViewActivityEditImages5);
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromDevice(5);
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
                if(parseObject.get("image") != null) {
                    ParseFile file1 = (ParseFile) parseObject.get("image");
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
                }
                if(parseObject.get("image2") != null) {
                    ParseFile file2 = (ParseFile) parseObject.get("image2");
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
                }
                if(parseObject.get("image3") != null) {
                    ParseFile file3 = (ParseFile) parseObject.get("image3");
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
                }
                if(parseObject.get("image4") != null) {
                    ParseFile file4 = (ParseFile) parseObject.get("image4");
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
                }
                if(parseObject.get("image5") != null) {
                    ParseFile file5 = (ParseFile) parseObject.get("image5");
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
                                //progressDialog.dismiss();
                            } else {
                                Log.d("test",
                                        "There was a problem downloading the data.");
                            }
                        }
                    });
                }
                progressDialog.dismiss();
            }
        });
    }


    private void getImageFromDevice(int imageViewIndex) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);

        imageId = imageViewIndex;


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri chosenImageUri;
            if(data!= null) {
                chosenImageUri = data.getData();
                if (imageId == 1) {
                    image1.setImageURI(chosenImageUri);
                    changed[0] = true;
                }
                if (imageId == 2) {
                    image2.setImageURI(chosenImageUri);
                    changed[1] = true;
                }
                if (imageId == 3) {
                    image3.setImageURI(chosenImageUri);
                    changed[2] = true;
                }
                if (imageId == 4) {
                    image5.setImageURI(chosenImageUri);
                    changed[3] = true;
                }
                if (imageId == 5) {
                    image5.setImageURI(chosenImageUri);
                    changed[4] = true;
                }
            }
        }
    }

    public void setImage(Bitmap map, int imageIndex) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        map.compress(Bitmap.CompressFormat.JPEG, 10, stream);
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
        if (id == R.id.editImage_action_save) {
            //save all images that changed
            saveAll();

            //Close Activity
            Intent i = new Intent(ActivityEditImages.this, ActivityUser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityEditImages.this.startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
