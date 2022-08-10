package utility;

public class CriterioFiltro {
	
    private CampoFiltro campo;
    private String criterio;

    public CriterioFiltro() {
    }

    public CriterioFiltro(CampoFiltro campo, String criterio) {
        this.campo = campo;
        this.criterio = criterio;
    }

    public CampoFiltro getCampo() {
        return campo;
    }

    public void setCampo(CampoFiltro campo) {
        this.campo = campo;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

}
