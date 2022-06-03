package Server;

public class Carte {

	private int cod;
	private String titlu;
	private String autor;
	private int nrPagini;
	
	public Carte(int cod, String titlu, String autor, int nrPagini) {
		super();
		this.cod = cod;
		this.titlu = titlu;
		this.autor = autor;
		this.nrPagini = nrPagini;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getNrPagini() {
		return nrPagini;
	}

	public void setNrPagini(int nrPagini) {
		this.nrPagini = nrPagini;
	}

	@Override
	public String toString() {
		return "Carte [cod=" + cod + ", titlu=" + titlu + ", autor=" + autor + ", nrPagini=" + nrPagini + "]";
	}

	
	
	
	
}
