package senac.tsi.eclairvinhos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText email;
	EditText senha;
	private ProgressDialog pDialog; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		
		pDialog = new ProgressDialog(this);
    	
		
		Intent i = getIntent();
		
		String clEmail = i.getStringExtra("clEmail");
		String clSenha = i.getStringExtra("clSenha");
		if (clEmail!= null && clSenha!= null) {		
			pDialog.setMessage("Conectando...");
			pDialog.show();
			chamadaPost(clEmail, clSenha);			
		}
		
		email = (EditText)findViewById(R.id.email);
		senha = (EditText)findViewById(R.id.senha);
		Button entrar = (Button)findViewById(R.id.btn_enviar);
		Button cadastro = (Button)findViewById(R.id.btn_cadastro);
		Button depois = (Button)findViewById(R.id.btn_depois);
		
		entrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pDialog.setMessage("Conectando...");
				pDialog.show();			
				chamadaPost(email.getText().toString(), senha.getText().toString());
			}
		});
		cadastro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), CadastroActivity.class);
				startActivity(i);
			}
		});
		depois.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.camera) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void chamadaPost(final String email,final String senha){
		class NetworkCall extends AsyncTask<Void, Void, String> {
	    	
	    	
			@Override
			protected String doInBackground(Void... params) {
				AndroidHttpClient client = AndroidHttpClient.newInstance(null);
	            
	            try{
	            	
	    			// Showing progress dialog before making http request
	            	
	            	HttpPost post = new HttpPost("http://pieclair.azurewebsites.net/4Sem/webservices/login.php");
	                
	            	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	            	postParams.add(new BasicNameValuePair("email", email));
	            	postParams.add(new BasicNameValuePair("senha", senha));
	            	
	            	UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
	            	
	            	post.setEntity(ent);
	            	
	            	HttpResponse response = client.execute(post);
	            	
	            	BufferedReader reader = new BufferedReader(new InputStreamReader(
	            			response.getEntity().getContent()));

	            	StringBuilder responseStrBuilder = new StringBuilder();

	            	String inputStr;
	            	while ((inputStr = reader.readLine()) != null)
	            	    responseStrBuilder.append(inputStr);
	            	
	            	//JSONObject json = new JSONObject(responseStrBuilder.toString());
	            	return responseStrBuilder.toString();
	                
	            } catch (Exception e) {
	            	e.printStackTrace();
	            } finally {
	            	client.close();
	            }
	            
				return null;
			}
	    	
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				final String r = result;
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						try {
							JSONObject usuario = new JSONObject(r);
							
							if (usuario.has("mensagem") ) {
								Toast tost = Toast.makeText(getApplicationContext(), usuario.getString("mensagem"), Toast.LENGTH_SHORT);
								tost.setGravity(Gravity.TOP, 0, 50);
								tost.show();
							}else{
							
								//verificar preferences
								SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
								SharedPreferences.Editor editor = pref.edit();
								
								editor.putString("nome", usuario.getString("nomeCompletoCliente"));
								editor.putInt("idCliente", usuario.getInt("idCliente"));
								editor.putString("email", usuario.getString("emailCliente"));
								editor.putString("senha", usuario.getString("senhaCliente"));
								
								editor.commit();
								
								Toast toast = Toast.makeText(getApplicationContext(), "Bem vindo,  "+pref.getString("nome", "visitante"), Toast.LENGTH_SHORT);
								toast.show();
								
								Intent i = new Intent(getApplicationContext(), MainActivity.class);
								startActivity(i);
								
								
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
							//pDialog.dismiss();
							 }
					}
				});
			}
		}
		(new NetworkCall()).execute((Void)null);
	}
}

