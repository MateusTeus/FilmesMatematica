import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public static Connection conectar() throws SQLException {
        Connection conexao = null;
        try {
            Class.forName("org.postgresql.Driver");//MatematicaDiscretaFilme MatematicaTeste
            conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MatematicaTeste", "postgres", "12345678");
        } catch (ClassNotFoundException e) {
            System.out.println("Drive do banco de dados não localizado");
        }
        return conexao;
    }
}
