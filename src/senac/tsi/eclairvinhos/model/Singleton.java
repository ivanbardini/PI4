package senac.tsi.eclairvinhos.model;

import java.util.ArrayList;
import java.util.List;

import senac.tsi.eclairvinhos.MainActivity;

import android.app.Activity;

public class Singleton {
	private static final Singleton INSTANCE = new Singleton ();
	private Singleton (){}
	public static Singleton getInstance ()
	{
		return INSTANCE ;
	}
	
	private static List<ItemPedido> carrinho = new ArrayList<ItemPedido>();
	
	public void addProduto(Produto prod, int qtd)
	{
		boolean achou = false;
		for (ItemPedido p : carrinho) {
			if (p.getProduto().getIdProduto() == prod.getIdProduto()) {
				p.setQtd(p.getQtd()+qtd);
				achou = true;
			}
		}
		if(!achou)
			carrinho.add(new ItemPedido(prod, qtd));
	}
	
	public int getQtdTotal(){
		int qtdTotal = 0;
		for (ItemPedido p : carrinho) {
			qtdTotal += p.getQtd();
		}
		return qtdTotal;
	}
	
	public List<ItemPedido> getCarrinho(){
		return carrinho;
	}
	
	
	
}
