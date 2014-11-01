package com.yhacks2014.hive;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by andrew on 31/10/14.
 */
public class LoginActivity extends Activity {
    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }


}
