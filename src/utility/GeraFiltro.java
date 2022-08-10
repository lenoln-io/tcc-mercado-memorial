package utility;

public class GeraFiltro {

    public static String gerarSentenca(String sql, CriterioFiltro criterio){

        if(sql.contains("where")||sql.contains("WHERE")){

            if(criterio.getCampo().getTipoColuna().equals("int")){
                sql = sql + " AND " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" = "+criterio.getCriterio()+" "; 
            }

            if(criterio.getCampo().getTipoColuna().equals("string")){
                sql = sql + " AND " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like \"%"+criterio.getCriterio()+"%\" ";			
            }

            if(criterio.getCampo().getTipoColuna().equals("double")){
                sql = sql + " AND " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" = "+criterio.getCriterio()+" "; 
            }
            
            if(criterio.getCampo().getTipoColuna().equals("date")){
                sql = sql + " AND " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like '%"+criterio.getCriterio()+"%' ";
            }
            
            if(criterio.getCampo().getTipoColuna().equals("datetime")){
                sql = sql + " AND " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like '%"+criterio.getCriterio()+"%' ";
            }

        }else{

            if(criterio.getCampo().getTipoColuna().equals("int")){
                sql = sql + " WHERE " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" = "+criterio.getCriterio()+" "; 
            }

            if(criterio.getCampo().getTipoColuna().equals("double")){
                sql = sql + " WHERE " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" = "+criterio.getCriterio()+" "; 
            }

            if(criterio.getCampo().getTipoColuna().equals("string")){
                sql = sql + " WHERE " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like \"%"+criterio.getCriterio()+"%\" ";			
            }
            
            if(criterio.getCampo().getTipoColuna().equals("date")){
                sql = sql + " WHERE " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like '%"+criterio.getCriterio()+"%' "; 
            }
            
            if(criterio.getCampo().getTipoColuna().equals("datetime")){
                sql = sql + " WHERE " + criterio.getCampo().getNomeTabela()+"."+criterio.getCampo().getNomeColuna()+" like '%"+criterio.getCriterio()+"%' "; 
            }

        }

        return sql;
    }
    
}
