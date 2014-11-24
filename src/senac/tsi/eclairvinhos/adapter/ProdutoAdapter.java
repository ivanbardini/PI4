package senac.tsi.eclairvinhos.adapter;

import java.util.List;

import senac.tsi.eclairvinhos.MainActivity;
import senac.tsi.eclairvinhos.ProdutoFragment;
import senac.tsi.eclairvinhos.R;
import senac.tsi.eclairvinhos.controller.VolleyController;
import senac.tsi.eclairvinhos.model.NavDrawerItem;
import senac.tsi.eclairvinhos.model.Produto;
import senac.tsi.eclairvinhos.model.Singleton;
import android.annotation.SuppressLint;
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

public class ProdutoAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private Produto wine;
	ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();

	public ProdutoAdapter(Activity activity, Produto wine) {
		this.activity = activity;
		this.wine = wine;
	}
	//@Override
//	public CustomListAdapter(LayoutInflater inflater, List<Produto> wineItems) {
//		this.inflater = inflater;
//		this.wineItems = wineItems;
//	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int location) {
		return wine;
	}

	@Override
	public long getItemId(int position) {
		return wine.getIdProduto();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.fragment_produto, null);

		if (imageLoader == null)
			imageLoader = VolleyController.getInstance().getImageLoader();
		 	imageLoader = VolleyController.getInstance().getImageLoader();
			NetworkImageView thumbNail = (NetworkImageView) convertView
					.findViewById(R.id.prod_img);
			TextView nome = (TextView) convertView.findViewById(R.id.prod_nome);
			TextView preco = (TextView) convertView.findViewById(R.id.prod_preco);
			TextView categoria = (TextView) convertView.findViewById(R.id.prod_categoria);
			TextView descricao = (TextView) convertView.findViewById(R.id.prod_descricao);


			// thumbnail image
			thumbNail.setImageUrl(wine.getUrlImage(), imageLoader);
			
			nome.setText(wine.getNomeProduto());
			
			if(wine.getDescontoPromocao()>0.00)
				preco.setText("De: R$" + String.format("%10.2f", wine.getPrecProduto())+"\n por: R$"+ String.format("%10.2f", wine.getPrecFinal()));
			else
				preco.setText("Por: R$"+ String.format("%10.2f", wine.getPrecFinal()));
			categoria.setText(wine.getNomeCategoria());
			
			descricao.setText(wine.getDescProduto());
			
			Button comprar = (Button)convertView.findViewById(R.id.btn_comprar);
			
			Button add = (Button)convertView.findViewById(R.id.prod_add);
			Button rem = (Button)convertView.findViewById(R.id.prod_rem);
			prod_qtd = (TextView) convertView.findViewById(R.id.num_qtd);
			
			quant = Integer.parseInt(prod_qtd.getText().toString());
			
			add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if (quant < 5) {
						quant++;
						prod_qtd.setText(String.valueOf(quant));		
					}
				}
			});
			rem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (quant > 1) {
						quant--;
						prod_qtd.setText(String.valueOf(quant));						
					}
				}
			});
			
			comprar.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Singleton sing = Singleton.getInstance();
					sing.addProduto(wine, Integer.parseInt(prod_qtd.getText().toString()));
					Toast toast = Toast.makeText(activity.getApplicationContext(), "Produto adicionado ao Carrinho", Toast.LENGTH_SHORT);
					toast.show();
					MainActivity.setCarrinhoCount();
					
					}
			});

		return convertView;
	}
	public TextView prod_qtd;
	public int quant;
}