package senac.tsi.eclairvinhos.adapter;

import java.util.List;
import java.util.zip.Inflater;

import senac.tsi.eclairvinhos.LojaCategoriaFragment;
import senac.tsi.eclairvinhos.LojaFragment;
import senac.tsi.eclairvinhos.ProdutoFragment;
import senac.tsi.eclairvinhos.R;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Movie;
import senac.tsi.eclairvinhos.model.Produto;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Produto> wineItems;
	ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Produto> wineItems) {
		this.activity = activity;
		this.wineItems = wineItems;
	}
	//@Override
//	public CustomListAdapter(LayoutInflater inflater, List<Produto> wineItems) {
//		this.inflater = inflater;
//		this.wineItems = wineItems;
//	}

	@Override
	public int getCount() {
		return wineItems.size();
	}

	@Override
	public Object getItem(int location) {
		return wineItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return wineItems.get(position).getIdProduto();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = VolleyController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView nome = (TextView) convertView.findViewById(R.id.nome);
		TextView preco = (TextView) convertView.findViewById(R.id.preco);
		TextView categoria = (TextView) convertView.findViewById(R.id.categoria);
		TextView desconto = (TextView) convertView.findViewById(R.id.desconto);

		Produto m = wineItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getUrlImage(), imageLoader);
		
		nome.setText(m.getNomeProduto());
		if(m.getDescontoPromocao()>0.00){
			preco.setText("De: R$" + String.format("%10.2f", m.getPrecProduto())+"\n por: R$"+ String.format("%10.2f", m.getPrecFinal()));
			desconto.setText("Desconto de R$"+String.format("%10.2f", m.getDescontoPromocao()));
		}
		else
			preco.setText("Por: R$"+ String.format("%10.2f", m.getPrecFinal()));
		categoria.setText(m.getNomeCategoria());
		
		final int pos = position;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				
				Fragment newFrag = new ProdutoFragment((int)getItemId(pos));
				
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