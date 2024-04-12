import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class Teste {
    static Connection conexao = null;

    public static void main(String[] args) {
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                Menu();
                //pesquisarFilmesPorGenero();
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
        System.out.println("1\t\"Ação\"\n" +
                "2\t\"Aventura\"\n" +
                "3\t\"Cinema de arte\"\n" +
                "4\t\"Chanchada\"\n" +
                "5\t\"Comédia\"\n" +
                "6\t\"Comédia de ação\"\n" +
                "7\t\"Comédia de terror\"\n" +
                "8\t\"Comédia dramática\"\n" +
                "9\t\"Comédia romântica\"\n" +
                "10\t\"Dança\"\n" +
                "11\t\"Documentário\"\n" +
                "12\t\"Docuficção\"\n" +
                "13\t\"Drama\"\n" +
                "14\t\"Espionagem\"\n" +
                "15\t\"Faroeste\"\n" +
                "16\t\"Fantasia\"\n" +
                "17\t\"Fantasia científica\"\n" +
                "18\t\"Ficção científica\"\n" +
                "19\t\"Filmes com truques\"\n" +
                "20\t\"Filmes de guerra\"\n" +
                "21\t\"Mistério\"\n" +
                "22\t\"Musical\"");

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
        System.out.println("Deseja pesquisar ator por:");
        System.out.println("[1] - Nome de um ator");//LIGAÇÃO (UNIÃO)
        System.out.println("[2] - Atores que atuaram juntos em um filme");//INTERSEÇÃO
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
                                        "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR\n" +
                                        "WHERE ator.nome = '" + nomeAtor + "'");

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
                         consultaFilmeAtor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, filme.ano_lancamento, diretor.nome AS nome_diretor\n" +
                                "FROM FILME\n" +
                                "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                "JOIN (\n" +
                                "    SELECT fa1.ID_FILME\n" +
                                "    FROM FILME_ATOR fa1\n" +
                                "    JOIN ATOR a1 ON fa1.ID_ATOR = a1.ID_ATOR\n" +
                                "    WHERE a1.nome = '" + primeiroAtor + "'" +
                                "    INTERSECT\n" +
                                "    SELECT fa2.ID_FILME\n" +
                                "    FROM FILME_ATOR fa2\n" +
                                "    JOIN ATOR a2 ON fa2.ID_ATOR = a2.ID_ATOR\n" +
                                "    WHERE a2.nome ='" + segundoAtor + "'" +
                                ") AS filmes_ator ON FILME.ID_FILME = filmes_ator.ID_FILME\n" +
                                "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR;\n");
                        while (consultaFilmeAtor.next()) {
                            String nome_filme = consultaFilmeAtor.getString("nome_filme"); // Corrigido para pegar o ID do filme
                            String ano_lancamento = consultaFilmeAtor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeAtor.getString("nome_diretor");

                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor " +diretor_nome);

                        }
                        break;
                    case 3:
                        System.out.println("Digite o nome do ator a ser excluído:");
                        String atorExcluir = leia.next();
                        consultaFilmeAtor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, filme.ano_lancamento, ator.id_ator, diretor.nome AS nome_diretor, ator.nome AS nome_ator\n" +
                                "FROM FILME\n" +
                                "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                "JOIN FILME_ATOR ON FILME_ATOR.ID_FILME = FILME.ID_FILME\n" +
                                "JOIN ATOR ON FILME_ATOR.ID_ATOR = ATOR.ID_ATOR\n" +
                                "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR\n" +
                                "WHERE ator.nome <> '" + atorExcluir + "'");
                        while (consultaFilmeAtor.next()) {
                            String nome_filme = consultaFilmeAtor.getString("nome_filme");
                            String ano_lancamento = consultaFilmeAtor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeAtor.getString("nome_diretor");
                            String nome_ator = consultaFilmeAtor.getString("nome_ator");

                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor: " + diretor_nome + ", Ator: " + nome_ator);
                        }
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

    static void pesquisarDiretor(){
        Scanner leia = new Scanner(System.in);
        System.out.println("Deseja pesquisar por:");
        System.out.println("[1] - Nome de um diretor");//LIGAÇÃO (UNIÃO)
        System.out.println("[2] - Junção de dois diretores");//INTERSEÇÃO
        System.out.println("[3] - Todos os diretores exceto um");//EXCLUSÃO CONJUNTO UNIVERSO
        int respostaUser = leia.nextInt();
        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                switch (respostaUser) {
                    case 1:
                        System.out.println("Digite o nome do diretor:");
                        String nomeDiretor = leia.next();
                        ResultSet consultaFilmeDiretor = conexao.createStatement().executeQuery(
                                "SELECT filme.nome_filme, filme.ano_lancamento, diretor.nome AS nome_diretor\n" +
                                        "FROM FILME\n" +
                                        "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                        "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR\n" +
                                        "WHERE diretor.nome ='" + nomeDiretor + "'");

                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme"); // Corrigido para pegar o ID do filme
                            String ano_lancamento = consultaFilmeDiretor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeDiretor.getString("nome_diretor");
                            String nome_diretor = consultaFilmeDiretor.getString("nome_diretor");
                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor " +diretor_nome + ", Ator: " + nome_diretor);

                        }                     break;
                    case 2:
                        System.out.println("Digite o nome do primeiro diretor:");
                        String primeiroDiretor = leia.next();
                        System.out.println("Digite o nome do segundo diretor:");
                        String segundoDiretor = leia.next();
                        consultaFilmeDiretor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, filme.ano_lancamento, diretor.nome AS nome_diretor\n" +
                                "FROM FILME\n" +
                                "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR\n"+
                                "WHERE diretor.nome ='" + primeiroDiretor + "' OR diretor.nome ='" + segundoDiretor + "'");

                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme"); // Corrigido para pegar o ID do filme
                            String ano_lancamento = consultaFilmeDiretor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeDiretor.getString("nome_diretor");

                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor " +diretor_nome);

                        }
                        break;
                    case 3:
                        System.out.println("Digite o nome do ator a ser excluído:");
                        String diretorExcluir = leia.next();
                        consultaFilmeDiretor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, filme.ano_lancamento, diretor.nome AS nome_diretor\n" +
                                "FROM FILME\n" +
                                "JOIN FILME_DIRETOR ON FILME_DIRETOR.ID_FILME = FILME.ID_FILME\n" +
                                "JOIN DIRETOR ON FILME_DIRETOR.ID_DIRETOR = DIRETOR.ID_DIRETOR\n" +
                                "WHERE diretor.nome <> '" + diretorExcluir + "'");
                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme");
                            String ano_lancamento = consultaFilmeDiretor.getString("ano_lancamento");
                            String diretor_nome = consultaFilmeDiretor.getString("nome_diretor");
                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor: " + diretor_nome);
                        }
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
    public static void pesquisarFilmesPorGenero() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o ID do gênero:");
        int idGenero = entrada.nextInt();

        try {
            String query = "SELECT filme.id_filme, filme.nome_filme, genero.nome_genero, COUNT(ranking_filme.estrelas) AS curtidas\n" +
                    "FROM filme_genero\n" +
                    "INNER JOIN filme ON filme.id_filme = filme_genero.id_filme\n" +
                    "INNER JOIN genero ON genero.id_genero = filme_genero.id_genero\n" +
                    "LEFT JOIN ranking_filme ON filme.id_filme = ranking_filme.id_filme\n" +
                    "WHERE filme_genero.id_genero = ?\n" +
                    "GROUP BY filme.id_filme, filme.nome_filme, genero.nome_genero\n" +
                    "ORDER BY COUNT(ranking_filme.estrelas) DESC";

            PreparedStatement statement = conexao.prepareStatement(query);
            statement.setInt(1, idGenero);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int idFilme = rs.getInt("id_filme");
                String nomeFilme = rs.getString("nome_filme");
                String nomeGenero = rs.getString("nome_genero");
                int curtidas = rs.getInt("curtidas");

                System.out.println("ID Filme: " + idFilme + ", Nome do Filme: " + nomeFilme +
                        ", Gênero: " + nomeGenero + ", Curtidas: " + curtidas);
            }
        } catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }

    static void avaliarFilme(){
        Scanner leia = new Scanner(System.in);

        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                        System.out.println("Digite o nome do filme que deseja avaliar:");
                        String nomeFilme = leia.next();
                ResultSet consultaFilme = conexao.createStatement().executeQuery("SELECT filme.id_filme, filme.nome_filme, filme.ano_lancamento, diretor.nome AS nome_diretor, diretor.sobrenome\n" +
                        "FROM FILME\n" +
                        "JOIN filme_diretor ON filme_diretor.id_filme = filme.id_filme\n" +
                        "JOIN diretor ON diretor.id_diretor = filme_diretor.id_diretor\n" +
                        "WHERE filme.nome_filme = '" + nomeFilme + "'");

                        while (consultaFilme.next()) {
                            String nome_filme = consultaFilme.getString("nome_filme"); // Corrigido para pegar o ID do filme
                            String ano_lancamento = consultaFilme.getString("ano_lancamento");
                            String diretor_nome = consultaFilme.getString("nome_diretor");
                            int idFilme = consultaFilme.getInt("id_filme");
                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento + ", Diretor " +diretor_nome);

                            System.out.println("De uma nota de 0 a 5:");
                            int nota = leia.nextInt();

                        // Calcula a nota final
                            int notaProcesso = nota+1;
                            int notaMultiplicado = nota * notaProcesso;
                            int notaFinal = (int) Math.ceil(nota / 2.0);

                            // Insere a avaliação com a nota final no banco de dados
                            String sqlInsert = "INSERT INTO ranking_filme (estrelas, id_filme) VALUES (?, ?)";
                            PreparedStatement statementInsert = conexao.prepareStatement(sqlInsert);
                            statementInsert.setInt(1, notaFinal); // Insere a nota final
                            statementInsert.setInt(2, idFilme);   // Use o id do filme obtido anteriormente
                            int linhasAfetadas = statementInsert.executeUpdate();
                            System.out.println("Linhas afetadas pela inserção: " + linhasAfetadas);
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
    public static void verAvaliacoes() {
        Scanner leia = new Scanner(System.in);
        System.out.println("Deseja pesquisar avaliações:");
        System.out.println("[1] - Todos os filmes");//LIGAÇÃO (UNIÃO)
        System.out.println("[2] - Filme especifico");//INTERSEÇÃO
        System.out.println("[3] - Todos os filmes exceto um");//EXCLUSÃO CONJUNTO UNIVERSO
        int respostaUser = leia.nextInt();
        Connection conexao = null;
        try {
            conexao = ConexaoBanco.conectar();
            if (conexao != null) {
                ResultSet consultaFilmeDiretor;
                switch (respostaUser) {
                    case 1:
                        consultaFilmeDiretor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, AVG(ranking_filme.estrelas) AS media_avaliacao\n" +
                                "FROM filme\n" +
                                "JOIN ranking_filme ON ranking_filme.id_filme = filme.id_filme\n" +
                                "GROUP BY filme.nome_filme");
                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme");
                            String ranking_filme = consultaFilmeDiretor.getString("media_avaliacao");
                            System.out.println("Nome filme: " + nome_filme + ", Média de Avaliação: " + ranking_filme);
                        }
                        break;
                    case 2:
                        System.out.println("Digite o nome do filme:");
                        String nomeFilme = leia.next();
                        consultaFilmeDiretor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, AVG(ranking_filme.estrelas) AS media_avaliacao\n" +
                                "FROM filme\n" +
                                "JOIN ranking_filme ON ranking_filme.id_filme = filme.id_filme\n" +
                                "WHERE filme.nome_filme = '" + nomeFilme + "'\n" +
                                "GROUP BY filme.nome_filme");
                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme");
                            String ranking_filme = consultaFilmeDiretor.getString("media_avaliacao");
                            System.out.println("Nome filme: " + nome_filme + ", Média de Avaliação: " + ranking_filme);
                        }
                        break;
                    case 3:
                        System.out.println("Digite o nome do filme a ser excluído:");
                        String filmeExcluir = leia.next();
                        consultaFilmeDiretor = conexao.createStatement().executeQuery("SELECT filme.nome_filme, AVG(ranking_filme.estrelas) AS media_avaliacao\n" +
                                        "FROM filme\n" +
                                        "JOIN ranking_filme ON ranking_filme.id_filme = filme.id_filme\n" +
                                        "WHERE filme.nome_filme <> '" + filmeExcluir + "'\n" +
                                        " GROUP BY filme.nome_filme");


                        while (consultaFilmeDiretor.next()) {
                            String nome_filme = consultaFilmeDiretor.getString("nome_filme");
                            String ano_lancamento = consultaFilmeDiretor.getString("ano_lancamento");
                            System.out.println("Nome filme: " + nome_filme + ", Ano: " + ano_lancamento);
                        }
                        break;
                    default:
                        System.out.println("Opção inválida.");
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


    static void Menu() {
        Scanner leia = new Scanner(System.in);
        int respostaUser;

        do {
            //conjunto - relação - função
            System.out.println("Escolha qual relação você deseja para obter filme");
            System.out.println("[1] - Baseado em gênero de filme");
            System.out.println("[2] - Baseado em atores");
            System.out.println("[3] - Baseado em diretores");
            System.out.println("[4] Adicionar avaliação");
            System.out.println("[5] Ver avaliações");
            System.out.println("[6] Ver curtidas dos filmes");


            System.out.println("[0] - Sair");

            respostaUser = leia.nextInt();

            switch (respostaUser) {
                case 1:
                    pesquisaGenero();
                    respostaUser = 0;
                    break;
                case 2:
                    pesquisarAtores();
                    respostaUser = 0;
                    break;
                case 3:
                    pesquisarDiretor();
                    respostaUser = 0;
                    break;
                case 4:
                    avaliarFilme();
                    respostaUser = 0;
                    break;
                case 5:
                    verAvaliacoes();
                    respostaUser = 0;
                    break;
                case 6:
                    pesquisarFilmesPorGenero();
                    respostaUser = 0;
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
