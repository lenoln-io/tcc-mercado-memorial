package entity;

public class UsuarioAcaoEntity {

    private long id;
    private UsuarioEntity usuario;
    private AcaoEntity acao;
    private String horario;
    private String descricao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public AcaoEntity getAcao() {
        return acao;
    }

    public void setAcao(AcaoEntity acao) {
        this.acao = acao;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public UsuarioAcaoEntity getClone(){
        UsuarioAcaoEntity u = new UsuarioAcaoEntity();
        u.setId(this.getId());
        u.setUsuario(this.getUsuario());
        u.setAcao(this.getAcao());
        u.setHorario(this.getHorario());
        u.setDescricao(this.getDescricao());
        return u;
    }
    
}
