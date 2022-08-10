package utility;

public class CampoFiltro {
	
    private String nomeCampo;
    private String nomeTabela;
    private String nomeColuna;
    private String tipoColuna;

    public CampoFiltro(String nomeCampo ,String nomeTabela, String nomeColuna, String tipoColuna) {
        this.nomeCampo = nomeCampo;
        this.nomeTabela = nomeTabela;
        this.nomeColuna = nomeColuna;
        this.tipoColuna = tipoColuna;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public String getNomeColuna() {
        return nomeColuna;
    }

    public void setNomeColuna(String nomeColuna) {
        this.nomeColuna = nomeColuna;
    }

    public String getTipoColuna() {
        return tipoColuna;
    }

    public void setTipoColuna(String tipoColuna) {
        this.tipoColuna = tipoColuna;
    }

    @Override
    public String toString() {
        return this.nomeCampo;
    }
	
}
