package senac.tsi.eclairvinhos;

import org.json.JSONObject;

import senac.tsi.eclairvinhos.adapter.ProdutoAdapter;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Produto;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class ProdutoFragment  extends Fragment {
	// Log tag
	private static final String TAG = ProdutoFragment.class.getSimpleName();
	private int idProduto;
	// Movies json url
	private static final String url = "http://pieclair.azurewebsites.net/4Sem/webservices/getDetalheProduto.php?id=";
	private ProgressDialog pDialog;   
	private Produto wine = new Produto();
	private ProdutoAdapter adapter;
	private ListView listView;

	public ProdutoFragment(int id) {
		idProduto = id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_loja, container, false);

		adapter = new ProdutoAdapter(getActivity(), wine);
		listView = (ListView) view.findViewById(R.id.list);
		listView.setAdapter(adapter);
		
		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// changing action bar color
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
		String newUrl = url + idProduto;
		// Creating volley request obj
		JsonObjectRequest movieReq = new JsonObjectRequest(newUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						
						// Parsing json
						//for (int i = 0; i < response.length(); i++) {
							try {
								JSONObject obj = response;
								//Produto vinho = new Produto();
								wine.setIdProduto(Integer.parseInt(obj.get("idProduto").toString()));
								wine.setNomeProduto(obj.getString("nomeProduto"));
								wine.setPrecProduto(Double.parseDouble(obj.get("precProduto").toString()));
								wine.setDescontoPromocao(Double.parseDouble(obj.get("descontoPromocao").toString()));
								wine.setPrecFinal(Double.parseDouble(obj.get("precFinal").toString()));
								wine.setDescProduto(obj.getString("descProduto"));
								wine.setNomeCategoria(obj.getString("nomeCategoria"));
							} catch (Exception e) {
								e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						//System.out.println(error.getMessage());
						hidePDialog();

					}
				});
		movieReq.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		VolleyController.getInstance().addToRequestQueue(movieReq);
		
		return view;
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