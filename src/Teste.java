import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Teste {
    static Connection conexao = null;

    public static void main(String[] args) {
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                Menu();
            } else {
                System.out.println("Não foi possível conectar ao banco de dados.");
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao conectar ao banco de dados: " + ex.getMessage());
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

    static void pesquisaGenero() {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Quantos gêneros de filmes você deseja escolher?");
        int qntGeneros = entrada.nextInt();

        int[] generosEscolhidos = new int[qntGeneros];

        System.out.println("Escolha os gêneros de filmes:");

        for (int i = 0; i < qntGeneros; i++) {
            System.out.println("Escolha o " + (i + 1) + "º gênero:");
            generosEscolhidos[i] = entrada.nextInt();
        }

        try {
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
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }

    public static void pesquisarAtores() {
        Scanner leia = new Scanner(System.in);
        System.out.println("Deseja pesquisar por:");
        System.out.println("[1] - Nome de um ator");//LIGAÇÃO (UNIÃO)
        System.out.println("[2] - Atores que atuam juntos em um filme");//INTERSEÇÃO
        System.out.println("[3] - Todos os atores exceto um");//EXCLUSÃO CONJUNTO UNIVERSO
        int respostaUser = leia.nextInt();
        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                switch (respostaUser) {
                    case 1:
                        System.out.println("Digite o nome do ator:");
                        String nomeAtor = leia.next();
                        ResultSet consultaFilmeAtor = conexao.createStatement().executeQuery(
                                "SELECT filme.nome_filme, filme.ano_lancamento, ator.id_ator, diretor.nome AS nome_diretor, ator.nome AS nome_ator\n" +
                                        "FROM FILME\n" +
                                        "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                        "JOIN FILME_ATOR ON FILME_ATOR.ID_FILME =FILME.ID_FILME\n" +
                                        "JOIN ATOR ON FILME_ATOR.ID_ATOR = ATOR.ID_ATOR\n" +
                                        "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR;");

                        while (consultaFilmeAtor.next()) {
                            String nome_filme = consultaFilmeAtor.getString("nome_filme"); // Corrigido para pegar o ID do filme
                            String ano_lancamento = consultaFilmeAtor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeAtor.getString("nome_diretor");
                            String nome_ator = consultaFilmeAtor.getString("nome_ator");

                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor " +diretor_nome + ", Ator: " + nome_ator);

                        }                     break;
                    case 2:
                        System.out.println("Digite o nome do primeiro ator:");
                        String primeiroAtor = leia.next();
                        System.out.println("Digite o nome do segundo ator:");
                        String segundoAtor = leia.next();
                        break;
                    case 3:
                        System.out.println("Digite o nome do ator a ser excluído:");
                        String atorExcluir = leia.next();
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco: " + ex.getMessage());
        }finally {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao fechar a conexão: " + ex.getMessage());
        }
    }

    }

    static void Menu() {
        Scanner leia = new Scanner(System.in);
        int respostaUser;


        do {
            System.out.println("Escolha qual relação você deseja para obter filme");
            System.out.println("[1] - Baseado em gênero de filme");
            System.out.println("[2] - Baseado em atores");
            System.out.println("[3] - Baseado em diretores");
            //UNIÃO genero E ATOR (NOMES)
            System.out.println("[6] - Ver numero total de filmes");// indução
            System.out.println("[8] - Ver numero total de atores");//indução
            System.out.println("[9] - Ver numero total de diretores");//indução
            System.out.println("[0] - Sair");
            respostaUser = leia.nextInt();

            switch (respostaUser) {
                case 1:
                    pesquisaGenero();
                    respostaUser = 0;
                    break;
                case 2:
                    pesquisarAtores();
                    break;
                case 3:
                    // Implemente a lógica para pesquisa baseada em diretores
                    break;
                case 4:
                    // Implemente a lógica para pesquisa baseada em ano de lançamento
                    break;
                case 5:
                    // Implemente a lógica para pesquisa baseada em ranking
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (respostaUser != 0);
    }
}
