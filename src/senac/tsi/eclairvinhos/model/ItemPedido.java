package senac.tsi.eclairvinhos.model;

public class ItemPedido {
	
	private Produto produto;
	private int qtd;
	
	public ItemPedido(Produto produto, int qtd){
		this.produto = produto;
		this.qtd = qtd;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Produto getProduto() {
		return produto;
	}
	
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
	public int getQtd() {
		return qtd;
	}
	
}
