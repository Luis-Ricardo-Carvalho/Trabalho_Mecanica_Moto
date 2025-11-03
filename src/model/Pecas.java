package model;

public class Pecas {
	private int id;
	private String name;
	private String marca;
	private String categoria;
	private int quantidade;
	private User user;
	
	public Pecas(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}
	
	public void validate() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome da peça não pode ser vazio.");
		}
		
		if (marca == null || marca.isBlank()) {
			throw new IllegalArgumentException("A marca da peça não pode ser vazia.");
		}
		
		if (quantidade < 0) {
			throw new IllegalArgumentException("A quantidade da peça não pode ser menor que zero.");
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
