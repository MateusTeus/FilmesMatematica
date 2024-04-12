import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Conexao {
    public static void main(String[] args) throws SQLException {
        Connection conexao = null;
        try {
            Class.forName("org.postgresql.Driver");

            conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MatematicaDiscretaFilme", "postgres", "12345678");

            Scanner entrada = new Scanner(System.in);
            System.out.println("Qual tipo?");
            String dado = entrada.nextLine();

            ResultSet rs = null;

            if (dado.equals("filme")) {
                rs = conexao.createStatement().executeQuery("SELECT filme.id_filme, filme.nome_filme, genero.nome_genero\n" +
                        "FROM filme_genero\n" +
                        "INNER JOIN filme ON filme.id_filme = filme_genero.id_filme\n" +
                        "INNER JOIN genero ON genero.id_genero = filme_genero.id_genero\n");
            }

            while (rs.next()) {
                int id = rs.getInt("id_filme");
                String filme = rs.getString("nome_filme");
                String genero = rs.getString("nome_genero");

                System.out.println("ID: " + id + ", Nome do Filme: " + filme + ", Gênero: " + genero);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do banco de dados não localizado");
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco de dados: " + ex.getMessage());
        } finally {
            if (conexao != null) {
                conexao.close();
            }
        }
    }
}
