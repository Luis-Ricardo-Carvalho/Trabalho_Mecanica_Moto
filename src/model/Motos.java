package model;

import java.util.List;

public class Motos {
	private int id;
	private String modelo;
	private String marca;
	private String descricao;
	private List<Motos> motos;
    private Pecas pecas; 
	
	public Motos(int id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

    public Pecas getPeca() {
    	return pecas; 
    }
    
    public void setPeca(Pecas peca) {
    	this.pecas = peca;
    }

	public List<Motos> getMotos() {
		return motos;
	}

	public void setMotos(List<Motos> motos) {
		this.motos = motos;
	}

	public int getId() {
		return id;
	}
	
	public void validate() {
		if (modelo == null || modelo.isBlank()) {
			throw new IllegalArgumentException("O modelo da moto não pode ser vazio.");
		}
		
		if (marca == null || marca.isBlank()) {
			throw new IllegalArgumentException("A marca da moto não pode ser vazia.");
		}
	}

	@Override
	public String toString() {
		return modelo;
	}
}
