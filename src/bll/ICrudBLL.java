package bll;

import java.util.List;
import utility.CriterioFiltro;

public interface ICrudBLL<Entity> {

    public Entity salvar(Entity entity);
    public boolean excluir(Entity entity);
    public List<Entity> listarTodos();
    public Entity pesquisar(long id);
    public List<Entity> pesquisaPersonalizada(String sql);
    public List<Entity> filtrar(CriterioFiltro criterio);
    public List<Entity> sqlDeConsultaPersonalizada(String sql);
    public List<Entity> filtrar(String sql, CriterioFiltro criterio);
    
}
