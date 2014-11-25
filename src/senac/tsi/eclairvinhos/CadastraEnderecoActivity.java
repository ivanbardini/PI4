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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senac.tsi.eclairvinhos.FinalizarPedidoFragment.NetworkCall;
import senac.tsi.eclairvinhos.model.ItemPedido;

import android.app.Activity;
import android.content.Context;
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

public class CadastraEnderecoActivity extends Activity {
	
	String nomeEnd;
	String logra;
	String num;
	String cep;
	String compl;
	String cidade;
	String pais;
	String uf;
	int idCiente;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastra_endereco);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastra_endereco, menu);
		
		nomeEnd = ((EditText)findViewById(R.id.end_nome)).getText().toString();
		logra = ((EditText)findViewById(R.id.end_logradouro)).getText().toString();
		num = ((EditText)findViewById(R.id.end_num)).getText().toString();
		cep = ((EditText)findViewById(R.id.end_cep)).getText().toString();
		compl = ((EditText)findViewById(R.id.end_complem)).getText().toString();
		cidade = ((EditText)findViewById(R.id.end_cidade)).getText().toString();
		pais = ((EditText)findViewById(R.id.end_pais)).getText().toString();
		uf = ((EditText)findViewById(R.id.end_uf)).getText().toString();
		
		Button enviar = (Button)findViewById(R.id.btn_cadastrarEnd);
		
		enviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				(new NetworkCall()).execute((Void)null);
			}
		});
		
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
	
class NetworkCall extends AsyncTask<Void, Void, String> {
    	
    	
		@Override
		protected String doInBackground(Void... params) {
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE); 
			
			if (prefs.getInt("idCliente", -1) == -1 ) {
				Toast tost = Toast.makeText(getApplicationContext(), "Faça login antes de cadastrsr endereco", Toast.LENGTH_SHORT);
				tost.setGravity(Gravity.TOP, 0, 50);
				tost.show();
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				
			}
			
			AndroidHttpClient client = AndroidHttpClient.newInstance(null);
			
            try{
            	nomeEnd = ((EditText)findViewById(R.id.end_nome)).getText().toString();
        		logra = ((EditText)findViewById(R.id.end_logradouro)).getText().toString();
        		num = ((EditText)findViewById(R.id.end_num)).getText().toString();
        		cep = ((EditText)findViewById(R.id.end_cep)).getText().toString();
        		compl = ((EditText)findViewById(R.id.end_complem)).getText().toString();
        		cidade = ((EditText)findViewById(R.id.end_cidade)).getText().toString();
        		pais = ((EditText)findViewById(R.id.end_pais)).getText().toString();
        		uf = ((EditText)findViewById(R.id.end_uf)).getText().toString();

            	HttpPost post = new HttpPost("http://pieclair.azurewebsites.net/4Sem/webservices/cadastrarEndereco.php");

            	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            	postParams.add(new BasicNameValuePair("idUsuario", String.valueOf(prefs.getInt("idCliente", -1))));
            	postParams.add(new BasicNameValuePair("nomeEndereco", nomeEnd));
            	postParams.add(new BasicNameValuePair("lugradouroEndereco", logra));
            	postParams.add(new BasicNameValuePair("numeroEndereco", num));
            	postParams.add(new BasicNameValuePair("cepEndereco", cep));
            	postParams.add(new BasicNameValuePair("complementoEndereco", compl));
            	postParams.add(new BasicNameValuePair("cidadeEndereco", cidade));
            	postParams.add(new BasicNameValuePair("paisEndereco", pais));
            	postParams.add(new BasicNameValuePair("UFEndereco", uf));
            	
            	
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
			try {
				JSONObject resposta = new JSONObject(r);
				
				if (resposta.has("mensagem") ) {
					Toast tost = Toast.makeText(getApplicationContext(), resposta.getString("mensagem"), Toast.LENGTH_SHORT);
					tost.setGravity(Gravity.TOP, 0, 50);
					tost.show();
				}else{
				
					//verificar preferences
					
					Toast toast = Toast.makeText(getApplicationContext(), "Endereco cadastrado com sucesso", Toast.LENGTH_SHORT);
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
}
}
