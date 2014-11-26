package senac.tsi.eclairvinhos;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SobreFragment extends Fragment {
	// Log tag
	private static final String TAG = CategoriasFragment.class.getSimpleName();

	// Movies json url
	private static final String url = "http://pieclair.azurewebsites.net/4Sem/webservices/listarCategoria.php";
	private ProgressDialog pDialog;   
	private ListView listView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_sobre, container, false);

		

		return view;
	
	}
}
