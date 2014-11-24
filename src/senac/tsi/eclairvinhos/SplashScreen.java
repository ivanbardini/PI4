package senac.tsi.eclairvinhos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
				
            	String email = pref.getString("email", null);
            	String senha = pref.getString("senha", null);
            	
            	if (email== null || senha== null) {
            		Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
				}else{
					Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.putExtra("clEmail", email);
                    i.putExtra("clSenha", senha);
					startActivity(i);
					finish();
				}
            	
                //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                //startActivity(i);
 
                // close this activity
               // finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}