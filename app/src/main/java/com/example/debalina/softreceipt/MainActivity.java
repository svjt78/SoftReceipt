package com.example.debalina.softreceipt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.sql.StatementEvent;


public class MainActivity extends Activity {

    ArrayList<String> list;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

// set an exit transition
        getWindow().setExitTransition(new Explode());
        getWindow().setReenterTransition(new Explode());

        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.action_Admin) {
            final Intent intent = new Intent(this, validateAdminPasscode.class);

            startActivityForResult(intent, 1);

            return true;
        }

        if (id == R.id.action_Mail) {
            final Intent intent = new Intent(this, eMailActivity.class);
            Toast.makeText(getApplicationContext(), "Sending message....", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Signin(View view) {

        // get pwd
        EditText et1 = (EditText) findViewById(R.id.editText1);
        String pwd = et1.getText().toString();

        //get user id and password for the Account
        DBHandler dbhandler = new DBHandler(MainActivity.this, null, null, 1);
        AccountProfile actProf = dbhandler.getAccountProfile();

        String SavedP = actProf.getpassword();

        if (pwd.equals(SavedP)) {
            et1.setText("");
            View iv = findViewById(R.id.applicon);
            iv.setTransitionName("test");
            final Intent intent = new Intent(this, ViewReceipt.class);
            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, iv, "test");
            startActivity(intent, option.toBundle());
        } else {
            Toast.makeText(getApplicationContext(), "invalid Login entered..", Toast.LENGTH_SHORT).show();
            et1.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final Intent intent1 = new Intent(this, AdminActivity.class);

                startActivityForResult(intent1, 2);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
              //  No action
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
        if (requestCode == 3) {
/*            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Click REFRESH to see the changes if made any..", Toast.LENGTH_LONG).show();
            } */
            if (resultCode == Activity.RESULT_OK) {
                //Write your code if there's no result
                String mailSent = data.getStringExtra("returnString");

                if (mailSent.equals("yes")) {
                    Toast.makeText(getApplicationContext(), "Message sent to registered email address", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Message sending failed", Toast.LENGTH_SHORT).show();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }

}