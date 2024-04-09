import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Consulta {
    public static void main(String[] args) {
        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                Scanner entrada = new Scanner(System.in);
                System.out.println("Qual tipo?");
                String dado = entrada.nextLine();

                if (dado.equals("filme")) {
                    ResultSet tabelaFilme = conexao.createStatement().executeQuery("SELECT * FROM FILME");
                    while (tabelaFilme.next()) {
                        int id = tabelaFilme.getInt("id_filme");
                        String titulo = tabelaFilme.getString("nome_filme");
                        System.out.println("ID: " + id + ", Titulo: " + titulo);
                    }
                } else if (dado.equals("genero")) {
                    ResultSet tabelaGenero = conexao.createStatement().executeQuery("SELECT * FROM GENERO");
                    while (tabelaGenero.next()) {
                        int id = tabelaGenero.getInt("id_genero");
                        String nome = tabelaGenero.getString("nome_genero");
                        System.out.println("ID: " + id + ", Nome: " + nome);
                    }
                } else {
                    System.out.println("Tipo inválido!");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco: " + ex.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                System.out.println("Ocorreu um erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }
}
