package senac.tsi.eclairvinhos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import senac.tsi.eclairvinhos.adapter.CustomListAdapter;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Produto;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

public class LojaFragment extends Fragment {
	// Log tag
	private static final String TAG = LojaFragment.class.getSimpleName();

	// Movies json url
	private static final String url = "http://pieclair.azurewebsites.net/4Sem/webservices/listaProduto.php";
	private ProgressDialog pDialog;   
	private List<Produto> wineList = new ArrayList<Produto>();
	private ListView listView;
	private CustomListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		Fragment frag = getActivity().getFragmentManager().findFragmentByTag(TAG);
		
		if(frag!=null){
			FragmentTransaction trans = getActivity().getFragmentManager().beginTransaction();
			
			trans.replace(R.id.frame_container, frag);
			trans.addToBackStack(null);
			
			trans.commit();
		}
		
		View view = inflater.inflate(R.layout.fragment_loja, container, false);

		listView = (ListView) view.findViewById(R.id.list);
		adapter = new CustomListAdapter(getActivity(), wineList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// changing action bar color
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));

		// Creating volley request obj
		StringRequest movieReq = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						
						// Parsing json
						//for (int i = 0; i < response.length(); i++) {
							try {
								response = response.replace("}", "},");
								response = "["+response+"]";
								response = response.replace("},]","}]");
								JSONArray jsonArr = new JSONArray(response);
								
								for (int i = 0; i < jsonArr.length(); i++) {
								JSONObject obj = jsonArr.getJSONObject(i);
								Produto vinho = new Produto();
								vinho.setIdProduto(Integer.parseInt(obj.get("idProduto").toString()));
								vinho.setNomeProduto(obj.getString("nomeProduto"));
								vinho.setNomeCategoria(obj.getString("nomeCategoria"));
								vinho.setPrecProduto(Double.parseDouble(obj.get("precProduto").toString()));
								vinho.setDescontoPromocao(Double.parseDouble(obj.get("descontoPromocao").toString()));
								vinho.setPrecFinal(Double.parseDouble(obj.get("precFinal").toString()));

								// adding movie to movies array
								wineList.add(vinho);
								}
							} catch (Exception e) {
								e.printStackTrace();
							

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
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
