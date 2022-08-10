package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private Connection conexao = null;

    public Connection getConexao(){			

        if(conexao == null){
            try {
                String url = "jdbc:mysql://localhost:3306/bdtccsp?user=root&password=&useUnicode=true&characterEncoding=UTF-8";
                conexao = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conexao;
    }
}