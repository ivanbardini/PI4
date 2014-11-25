package senac.tsi.eclairvinhos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends Activity {
	EditText nome ;
	EditText email ;
	EditText senha ;
	EditText cpf ;
	EditText telCel;
	EditText telRes ;
	EditText telCom ;
	EditText dataNasc;
	CheckBox newsLetter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);
		
		Button btnEnviar = (Button)findViewById(R.id.btn_cadastrar);
		nome = (EditText)findViewById(R.id.nomeCompleto);
		email = (EditText)findViewById(R.id.cad_email);
		senha = (EditText)findViewById(R.id.cad_senha);
		cpf = (EditText)findViewById(R.id.cad_cpf);
		telCel = (EditText)findViewById(R.id.cad_cel);
		telRes = (EditText)findViewById(R.id.cad_telRes);
		telCom = (EditText)findViewById(R.id.cad_telCom);
		dataNasc = (EditText)findViewById(R.id.cad_dataNasc);
		newsLetter = (CheckBox)findViewById(R.id.ckb_newsl);
		
		btnEnviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				(new NetworkCall()).execute((Void)null);
			}
		});
		
		
	}
	
	class NetworkCall extends AsyncTask<Void, Void, String> {
    	
    	
		@Override
		protected String doInBackground(Void... params) {
			AndroidHttpClient client = AndroidHttpClient.newInstance(null);
			
            try{
            	
    			// Showing progress dialog before making http request
            	nome = (EditText)findViewById(R.id.nomeCompleto);
        		email = (EditText)findViewById(R.id.cad_email);
        		senha = (EditText)findViewById(R.id.cad_senha);
        		cpf = (EditText)findViewById(R.id.cad_cpf);
        		telCel = (EditText)findViewById(R.id.cad_cel);
        		telRes = (EditText)findViewById(R.id.cad_telRes);
        		telCom = (EditText)findViewById(R.id.cad_telCom);
        		dataNasc = (EditText)findViewById(R.id.cad_dataNasc);
        		newsLetter = (CheckBox)findViewById(R.id.ckb_newsl);
            		
        		
            	
            	HttpPost post = new HttpPost("http://pieclair.azurewebsites.net/4Sem/webservices/webServPi.php");
                 
            	 String sendNome = nome.getText().toString() ;
				 String sendEmail = email.getText().toString();
				 String sendSenha = senha.getText().toString();
				 String sendCpf = cpf.getText().toString();
				 String sendTelCel = telCel.getText().toString();
				 String sendTelRes = telRes.getText().toString();
				 String sendTelCom = telCom.getText().toString();
				 String sendDataNasc = dataNasc.getText().toString();
				 
				 String[] data = sendDataNasc.split("/");
				 
//				 if (data.length!=3 || data[2].length()!=4) {
//				 	Toast tost = Toast.makeText(getApplicationContext(), "Preencha a data no formato dd/mm/aaaa", Toast.LENGTH_SHORT);
//					tost.setGravity(Gravity.TOP, 0, 50);
//					tost.show();
//					return null;
//				 }
				 
				 String newDate = "";
				 for (int i = data.length - 1; i >= 0 ; i--) {
					newDate += data[i];
					if(i!=0)
						newDate+="/";
				}
				 sendDataNasc = newDate;
				 
				 boolean sendNewsletter = newsLetter.isChecked();
            	
            	
            	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            	
            	if(!sendNome.isEmpty())
            		postParams.add(new BasicNameValuePair("nome", sendNome));
            	if(!sendEmail.isEmpty())
            		postParams.add(new BasicNameValuePair("email", sendEmail));
            	if(!sendSenha.isEmpty())
            		postParams.add(new BasicNameValuePair("senha", sendSenha));
            	if(!sendCpf.isEmpty())
            		postParams.add(new BasicNameValuePair("cpf", sendCpf));
            	if(!sendTelCel.isEmpty())
            		postParams.add(new BasicNameValuePair("celular", sendTelCel));
            	if(!sendTelCom.isEmpty())
            		postParams.add(new BasicNameValuePair("telCom", sendTelCom));
            	if(!sendTelRes.isEmpty())
            		postParams.add(new BasicNameValuePair("telRes", sendTelRes));
            	if(!sendDataNasc.isEmpty())
            		postParams.add(new BasicNameValuePair("dataNasc", sendDataNasc));
            	if (sendNewsletter) {
            		postParams.add(new BasicNameValuePair("newsletter", "on"));
				}
            	
            	
            	
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
						}if (usuario.getString("mensagem") == "Cadastro efetuado com sucesso!") {
							
						Intent i = new Intent(getApplicationContext(), CadastraEnderecoActivity.class);
						startActivity(i);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
