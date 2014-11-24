package senac.tsi.eclairvinhos.adapter;

import java.util.List;

import senac.tsi.eclairvinhos.HomeFragment;
import senac.tsi.eclairvinhos.LojaCategoriaFragment;
import senac.tsi.eclairvinhos.R;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Categoria;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CategoriaAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Categoria> categoriaItems;
	ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();

	public CategoriaAdapter(Activity activity, List<Categoria> categoriaItems) {
		this.activity = activity;
		this.categoriaItems = categoriaItems;
	}
	//@Override
//	public CategoriaAdapter(LayoutInflater inflater, List<Categoria> categoriaItems) {
//		this.inflater = inflater;
//		this.categoriaItems = categoriaItems;
//	}

	@Override
	public int getCount() {
		return categoriaItems.size();
	}

	@Override
	public Object getItem(int location) {
		return categoriaItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return categoriaItems.get(position).getIdCategoria();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.cat_list_row, null);

		if (imageLoader == null)
			imageLoader = VolleyController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView nome = (TextView) convertView.findViewById(R.id.cat_nome);
		TextView descricao = (TextView) convertView.findViewById(R.id.cat_descricao);
		

		// getting movie data for the row
		Categoria m = categoriaItems.get(position);

		nome.setText(m.getNomeCategoria());
		descricao.setText(m.getDescCategoria());
		
		final int pos = position;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				
				Fragment newFrag = new LojaCategoriaFragment((int)getItemId(pos));
				
				FragmentTransaction trans = activity.getFragmentManager().beginTransaction();
				
				trans.replace(R.id.frame_container, newFrag);
				trans.addToBackStack(null);
				
				trans.commit();
				}catch(Exception e){}
			}
		});
		
		return convertView;
	}

}