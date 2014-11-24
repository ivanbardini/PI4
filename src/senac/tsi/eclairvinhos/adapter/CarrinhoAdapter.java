package senac.tsi.eclairvinhos.adapter;

import java.util.List;

import senac.tsi.eclairvinhos.LojaCategoriaFragment;
import senac.tsi.eclairvinhos.MainActivity;
import senac.tsi.eclairvinhos.R;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.Categoria;
import senac.tsi.eclairvinhos.model.ItemPedido;
import senac.tsi.eclairvinhos.model.Singleton;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CarrinhoAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ItemPedido> carrinho;
	ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();
	TextView quantidade;
	TextView preco;
	int quant =0;
	String precFin;

	public CarrinhoAdapter(Activity activity, List<ItemPedido> carrinho) {
		this.activity = activity;
		this.carrinho = carrinho;
	}

	@Override
	public int getCount() {
		return carrinho.size();
	}

	@Override
	public Object getItem(int location) {
		return carrinho.get(location);
	}

	@Override
	public long getItemId(int position) {
		return carrinho.get(position).getProduto().getIdProduto();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.carr_list_row, null);

		if (imageLoader == null)
			imageLoader = VolleyController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.item_img);
		TextView nome = (TextView) convertView.findViewById(R.id.item_nome);
		preco = (TextView) convertView.findViewById(R.id.item_preco);
		quantidade = (TextView) convertView.findViewById(R.id.item_qtd);
		

		// getting movie data for the row
		ItemPedido m = carrinho.get(position);

		thumbNail.setImageUrl(m.getProduto().getUrlImage(), imageLoader);
		nome.setText(m.getProduto().getNomeProduto());
		quantidade.setText(String.valueOf(m.getQtd()));
		precFin = String.valueOf(m.getProduto().getPrecFinal() * m.getQtd());
		preco.setText(precFin);
//		Button add = (Button)convertView.findViewById(R.id.item_add);
//		Button rem = (Button)convertView.findViewById(R.id.item_rem);
//		
//		add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				if (quant < 5) {
//					quant++;
//					quantidade.setText(String.valueOf(quant));	
//					precFin = String.valueOf(m.getProduto().getPrecFinal() * m.getQtd());
//					preco.setText(precFin);
//				}
//			}
//		});
//		rem.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (quant > 1) {
//					quant--;
//					quantidade.setText(String.valueOf(quant));						
//				}
//			}
//		});
		
		return convertView;
	}

}