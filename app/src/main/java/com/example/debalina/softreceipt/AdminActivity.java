package com.example.debalina.softreceipt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window content transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

        // set an ENTER transition
/*        getWindow().setEnterTransition(new Explode());
        getWindow().setReturnTransition(new Explode());  */

        getWindow().setEnterTransition(new Fade());
        getWindow().setReturnTransition(new Fade());

        setContentView(R.layout.activity_admin);

        final EditText psw = (EditText) findViewById(R.id.textView1);

        final TextView str = (TextView) findViewById(R.id.tv);
        str.setVisibility(View.INVISIBLE);

        final ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        psw.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                String pss = psw.getText().toString();
                if (pss.length() != 0) {
                    PasswordValidityCheck ps = new PasswordValidityCheck();
                    String strength = ps.CheckValidPassword(pss);

                    if (strength.equals("invalid")) {
                        str.setVisibility(View.VISIBLE);
                        str.setText("Invalid");
                        pb.setVisibility(View.VISIBLE);
                        pb.setProgress(50);
                    }else if (strength.equals("valid")){
                        str.setVisibility(View.VISIBLE);
                        str.setText("Valid");
                        pb.setVisibility(View.VISIBLE);
                        pb.setProgress(100);
                    } else {
                        pb.setVisibility(View.INVISIBLE);
                        str.setVisibility(View.INVISIBLE);
                    }

                }else{
                    pb.setVisibility(View.INVISIBLE);
                    str.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    public void cancelSave (View view) {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void saveProcess (View view) {

        Boolean error_flag = false;

        String password = "";

        String email = "";

        EditText et1 = (EditText) findViewById(R.id.textView1);
        password = et1.getText().toString();
        if ((password.equals(null)) || (password.isEmpty()) || (password.trim().isEmpty())) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_LONG).show();
            et1.requestFocus();
            error_flag = true;
        } else {
            if (!passwordIsValid(password)) {
                Toast.makeText(getApplicationContext(), "Password must be 6 character long with atleast one " +
                                "alphabet in upper case, one in lower case, one number and one special character",
                        Toast.LENGTH_LONG).show();
                error_flag = true;
                et1.requestFocus();

            }
        }

        if (error_flag == false) {
            EditText et2 = (EditText) findViewById(R.id.textView2);
            email = et2.getText().toString();
            if ((email.equals(null)) || (email.isEmpty()) || (email.trim().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Enter email ID", Toast.LENGTH_LONG).show();
                et2.requestFocus();
                error_flag = true;
            }else {
                if (!emailIsValid(email)) {
                    Toast.makeText(getApplicationContext(), "Invalid email Id", Toast.LENGTH_LONG).show();
                    error_flag = true;
                    et2.requestFocus();
                }

            }
        }

//save data in database via AccountProfile object
        if (error_flag == false) {

            AccountProfile accountProfile = new AccountProfile(password, email);

            DBHandler dbhandler = new DBHandler(this, null, null, 1);
            String operation_flag = dbhandler.saveAdmin(accountProfile);

            if (operation_flag == "update") {
                Toast.makeText(getApplicationContext(), "Existing profile updated", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "New profile created", Toast.LENGTH_LONG).show();
            }
        }

/*        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish(); */

    }

    public Boolean passwordIsValid(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private Boolean emailIsValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

}
