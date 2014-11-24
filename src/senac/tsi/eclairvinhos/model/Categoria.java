package senac.tsi.eclairvinhos.model;

public class Categoria {
	private int idCategoria;
	private String nomeCategoria;
	private String descCategoria;
	
	public int getIdCategoria(){return idCategoria;};
	public String getNomeCategoria(){return nomeCategoria;};
	public String getDescCategoria(){return descCategoria;};
	
	public void setIdCategoria(int id){idCategoria = id;};
	public void setNomeCategoria(String nome){nomeCategoria = nome;};
	public void setDescCategoria(String cat){descCategoria = cat;};
	
}
