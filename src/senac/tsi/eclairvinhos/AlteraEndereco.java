package senac.tsi.eclairvinhos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senac.tsi.eclairvinhos.adapter.CarrinhoAdapter;
import senac.tsi.eclairvinhos.model.ItemPedido;
import senac.tsi.eclairvinhos.model.Singleton;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AlteraEndereco extends Fragment {
	// Log tag
	private static final String TAG = CarrinhoFragment.class.getSimpleName();

	// Movies json url
	//private static final String url = "http://pieclair.azurewebsites.net/4Sem/webservices/listarCategoria.php";
	private ProgressDialog pDialog;   
	Singleton sing;
	private ListView listView;
	private CarrinhoAdapter adapter;
	String tipoPagto;
	ArrayList<String> mylist = new ArrayList<String>();
	JSONArray jsonEdereco;
	String endereco;
	Spinner spinEnd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_finalizar, container, false);
		(new getEnderecos()).execute((Void)null);
		double totalPedido = 0;
		
		sing = Singleton.getInstance();
		List<ItemPedido> carr = sing.getCarrinho();
		
		for (ItemPedido itemPedido : carr) {
			totalPedido += itemPedido.getProduto().getPrecFinal() * itemPedido.getQtd();
		}
		
		TextView total = (TextView)view.findViewById(R.id.fin_total);
		total.setText("Total do pedido: R$" + String.format("%10.2f", totalPedido));
		
		Spinner spinner = (Spinner) view.findViewById(R.id.spin_pgto);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.forma_pgto, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		spinEnd = (Spinner)view.findViewById(R.id.spin_ende);
		ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mylist);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEnd.setAdapter(myAdapter1);
		
		tipoPagto = spinner.getSelectedItem().toString();
		
		
		Button enviar = (Button)view.findViewById(R.id.btn_finalizar);
		
		enviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				(new NetworkCall()).execute((Void)null);
			}
		});
		
		return view;
	}
	
	class NetworkCall extends AsyncTask<Void, Void, String> {
    	
    	
		@Override
		protected String doInBackground(Void... params) {
			SharedPreferences prefs = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE); 
			endereco = spinEnd.getSelectedItem().toString();
			if (prefs.getInt("idCliente", -1) == -1 ) {
				Toast tost = Toast.makeText(getActivity(), "Faça login antes de finalizar a compra", Toast.LENGTH_SHORT);
				tost.setGravity(Gravity.TOP, 0, 50);
				tost.show();
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivity(i);
				
			}
			
			AndroidHttpClient client = AndroidHttpClient.newInstance(null);
			sing = Singleton.getInstance();
            try{
            	
    			String idTipoPgto;
    			switch (tipoPagto) {
				case "Boleto":
					idTipoPgto = "2";
					break;
				case "pagSeguro":
					idTipoPgto = "3";
					break;
				case "PayPal":
					idTipoPgto = "4";
					break;
				default:
					idTipoPgto = "1";
					break;
				}
    			String idEndereco = "NULL";
    			for (int i= 0; i> jsonEdereco.length(); i++) {
    				JSONObject obj;
    				obj = jsonEdereco.getJSONObject(i);
    				if (obj!=null) {
    					if(obj.getString("nomeEndereco") == endereco){
        					idEndereco = String.valueOf(obj.getInt("idEndereco"));
        				}	
					}
    				
    			}
            	
            	
            	HttpPost post = new HttpPost("http://pieclair.azurewebsites.net/4Sem/webservices/cadastrarPedido.php");
                
            	           	
            	
            	JSONArray carrinho = new JSONArray();
            	
            	List<ItemPedido> lista = sing.getCarrinho();
            	
            	for (ItemPedido itemPedido : lista) {
					carrinho.put(new JSONObject("{idProduto:" +
							String.valueOf(itemPedido.getProduto().getIdProduto()) +
							", qtd:" +
							String.valueOf(itemPedido.getQtd()) +
							", precoVenda:"+
							String.valueOf(itemPedido.getQtd()*itemPedido.getProduto().getPrecFinal())+
							"}"));
				}
            	
            	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            	postParams.add(new BasicNameValuePair("idUsuario", String.valueOf(prefs.getInt("idCliente", -1))));
            	postParams.add(new BasicNameValuePair("statusPedido", "1"));
            	postParams.add(new BasicNameValuePair("TipoDePagamento", idTipoPgto));
            	postParams.add(new BasicNameValuePair("idEndereco", idEndereco));
            	postParams.add(new BasicNameValuePair("idAplicacao", "2"));
            	postParams.add(new BasicNameValuePair("carrinho", carrinho.toString()));
            	
            	
            	
            	
            	
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
					Toast tost = Toast.makeText(getActivity(), resposta.getString("mensagem"), Toast.LENGTH_SHORT);
					tost.setGravity(Gravity.TOP, 0, 50);
					tost.show();
				}else{
				
					//verificar preferences
					
					Toast toast = Toast.makeText(getActivity(), "Pedido efetuado com sucesso", Toast.LENGTH_SHORT);
					toast.show();
					
					Intent i = new Intent(getActivity(), MainActivity.class);
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

	class getEnderecos extends AsyncTask<Void, Void, String> {
    	
    	
		@Override
		protected String doInBackground(Void... params) {
			SharedPreferences prefs = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE); 
			
			AndroidHttpClient client = AndroidHttpClient.newInstance(null);
			sing = Singleton.getInstance();
            try{
            	HttpPost get = new HttpPost("http://pieclair.azurewebsites.net/4Sem/webservices/getEndereco.php?id="+prefs.getInt("idCliente", -1));
            	
            	HttpResponse response = client.execute(get);
            	
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
				jsonEdereco = new JSONArray(r);
				for (int i= 0; i> jsonEdereco.length(); i++) {
					JSONObject obj;
    				obj = jsonEdereco.getJSONObject(i);
    				if (obj!=null) {
    					mylist.add(obj.getString("nomeEndereco"));
    				}
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}	

}
