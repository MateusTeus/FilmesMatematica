import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Quantos gêneros de filmes você deseja escolher?");
        int qntGeneros = entrada.nextInt();

        int[] generosEscolhidos = new int[qntGeneros];

        System.out.println("Escolha os gêneros de filmes:");

        for (int i = 0; i < qntGeneros; i++) {
            System.out.println("Escolha o " + (i + 1) + "º gênero:");
            generosEscolhidos[i] = entrada.nextInt();
        }

        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                // Mapeia os IDs dos gêneros para seus nomes
                Map<Integer, String> generosMap = new HashMap<>();
                String queryGeneros = "SELECT * FROM genero";
                ResultSet rsGeneros = conexao.createStatement().executeQuery(queryGeneros);
                while (rsGeneros.next()) {
                    int idGenero = rsGeneros.getInt("id_genero");
                    String nomeGenero = rsGeneros.getString("nome_genero");
                    generosMap.put(idGenero, nomeGenero);
                }

                String query = "SELECT filme.id_filme, filme.nome_filme\n" +
                        "FROM filme_genero\n" +
                        "INNER JOIN filme ON filme.id_filme = filme_genero.id_filme\n" +
                        "WHERE filme_genero.id_genero IN (";

                for (int i = 0; i < qntGeneros; i++) {
                    if (i > 0) {
                        query += ",";
                    }
                    query += generosEscolhidos[i];
                }
                query += ")\n" +
                        "GROUP BY filme.id_filme, filme.nome_filme\n" +
                        "HAVING COUNT(DISTINCT filme_genero.id_genero) = " + qntGeneros;

                ResultSet rs = conexao.createStatement().executeQuery(query);

                while (rs.next()) {
                    int id_filme = rs.getInt("id_filme");
                    String nomeFilme = rs.getString("nome_filme");

                    System.out.println("ID filme: " + id_filme + ", Nome do Filme: " + nomeFilme);
                    // Exibe os nomes dos gêneros
                    for (int i = 0; i < qntGeneros; i++) {
                        int idGenero = generosEscolhidos[i];
                        System.out.println("Gênero: " + generosMap.get(idGenero));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco de dados: " + ex.getMessage());
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
