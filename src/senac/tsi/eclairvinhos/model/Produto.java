package senac.tsi.eclairvinhos.model;

public class Produto {
	
	private int idProduto;
	private String nomeProduto;
	private double precProduto;
	private double descontoPromocao;
	private double precFinal;
	private String nomeCategoria;
	private String descProduto;
	//private String urlImage;
	
	public int getIdProduto(){return idProduto;}
	public String getNomeProduto(){return nomeProduto;}
	public double getPrecProduto(){return precProduto;}
	public double getDescontoPromocao(){return descontoPromocao;}
	public double getPrecFinal(){return precFinal;}
	public String getNomeCategoria(){return nomeCategoria;}
	public String getDescProduto(){return descProduto;}
	public String getUrlImage(){return "http://pieclair.azurewebsites.net/imagens/"+idProduto+".jpg";}

	public void setIdProduto(int id){idProduto = id;}
	public void setNomeProduto(String nome){nomeProduto = nome;}
	public void setPrecProduto(double preco){precProduto = preco;}
	public void setDescontoPromocao(double desc){descontoPromocao = desc;}
	public void setPrecFinal(double preco){precFinal = preco;}
	public void setNomeCategoria(String nome){nomeCategoria = nome;}
	public void setDescProduto(String desc){descProduto = desc;}
	//public void setUrlImage(String url){urlImage = url;}
	
}
