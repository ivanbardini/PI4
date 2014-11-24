package senac.tsi.eclairvinhos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import senac.tsi.eclairvinhos.adapter.CarrinhoAdapter;
import senac.tsi.eclairvinhos.adapter.CategoriaAdapter;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Categoria;
import senac.tsi.eclairvinhos.model.Singleton;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

public class CarrinhoFragment extends Fragment {
	// Log tag
	private static final String TAG = CarrinhoFragment.class.getSimpleName();

	// Movies json url
	//private static final String url = "http://pieclair.azurewebsites.net/4Sem/webservices/listarCategoria.php";
	private ProgressDialog pDialog;   
	Singleton sing = Singleton.getInstance();
	private ListView listView;
	private CarrinhoAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

		listView = (ListView) view.findViewById(R.id.list);
		adapter = new CarrinhoAdapter(getActivity(), sing.getCarrinho());
		listView.setAdapter(adapter);

//		pDialog = new ProgressDialog(getActivity());
//		// Showing progress dialog before making http request
//		pDialog.setMessage("Loading...");
//		pDialog.show();

		adapter.notifyDataSetChanged();
		
		Button comprar = (Button)view.findViewById(R.id.btn_comprar);
		Button cont = (Button)view.findViewById(R.id.btn_continuar);
		
		comprar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = new FinalizarPedidoFragment();
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		});
		
		cont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment = new LojaFragment();
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		});
		

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
