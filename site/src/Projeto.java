import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Projeto extends JFrame {

    private boolean usuarioLogado = false;

    private JPanel painelPrincipal;
    private JLabel statusLabel;
    private Color corFundo = new Color(245, 245, 245);
    private Color corPrimaria = new Color(102, 204, 0);
    private Color corSecundaria = new Color(255, 255, 255);
    private Color corTexto = new Color(51, 51, 51);
    private Color corBotao = new Color(255, 200, 0);
    private Font fonteTitulo = new Font("Montserrat", Font.BOLD, 28);
    private Font fonteTexto = new Font("Open Sans", Font.PLAIN, 14);
    private JPanel painelConteudo;
    private Connection conexao;

    public Projeto() {
        super("Sistema de Informações sobre Febre Amarela");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        conectarBancoDados();
        this.configurarJanela();
        this.criarInterface();
    }

    private void conectarBancoDados() {
        String url = "jdbc:mysql://localhost:3306/febre_amarela";
        String usuario = "root";
        String senha = "2315";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão com o banco de dados estabelecida!");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver JDBC do MySQL: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao carregar o driver JDBC do MySQL: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarJanela() {
        this.setDefaultCloseOperation(3);
        this.setSize(1024, 768);
        this.setLocationRelativeTo((Component) null);
        this.setResizable(true);
        this.getContentPane().setBackground(this.corFundo);
    }

    private void criarInterface() {
        this.painelPrincipal = new JPanel(new BorderLayout(10, 10));
        this.painelPrincipal.setBackground(this.corFundo);
        this.painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.painelPrincipal.add(this.criarPainelTitulo(), "North");
        this.painelPrincipal.add(this.criarPainelMenu(), "West");
        this.painelPrincipal.add(this.criarPainelConteudo(), "Center");
        this.painelPrincipal.add(this.criarBarraStatus(), "South");
        this.add(this.painelPrincipal);
    }

    private JPanel criarPainelTitulo() {
        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(corPrimaria);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("FEBRE AMARELA");
        titulo.setFont(fonteTitulo);
        titulo.setForeground(corSecundaria);

        JPanel painelTituloConteudo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTituloConteudo.setBackground(corPrimaria);
        painelTituloConteudo.add(titulo);

        painelTitulo.add(painelTituloConteudo, BorderLayout.CENTER);

        return painelTitulo;
    }

    private JPanel criarPainelMenu() {
        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new BoxLayout(painelMenu, 1));
        painelMenu.setBackground(corSecundaria);
        painelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] opcoesMenu = new String[]{"O que é", "Sintomas", "Prevenção", "Vacinação", "Mitos e Verdades", "Login", "Cadastro"};
        for (String opcao : opcoesMenu) {
            JButton botao = this.criarBotaoMenu(opcao);
            painelMenu.add(botao);
            painelMenu.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        return painelMenu;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(this.fonteTexto);
        botao.setBackground(this.corBotao);
        botao.setMaximumSize(new Dimension(150, 40));
        botao.setFocusPainted(false);
        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarConteudo(texto);
            }
        });
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botao.setBackground(new Color(255, 180, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                botao.setBackground(corBotao);
            }
        });
        return botao;
    }

    private JPanel criarPainelConteudo() {
        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout());
        painelConteudo.setBackground(corSecundaria);
        painelConteudo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JTextArea areaTexto = new JTextArea();
        areaTexto.setFont(this.fonteTexto);
        areaTexto.setForeground(corTexto);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setEditable(false);
        areaTexto.setMargin(new Insets(10, 10, 10, 10));
        areaTexto.setText("Selecione uma opção no menu lateral para visualizar as informações.");
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        painelConteudo.add(scrollPane, "Center");
        return painelConteudo;
    }

    private JPanel criarBarraStatus() {
        JPanel barraStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraStatus.setBackground(corFundo);
        this.statusLabel = new JLabel("Pronto");
        this.statusLabel.setFont(fonteTexto);
        this.statusLabel.setForeground(corTexto);
        barraStatus.add(this.statusLabel);
        return barraStatus;
    }

    private void mostrarConteudo(String opcao) {
        painelConteudo.removeAll();
        switch (opcao) {
            case "O que é":
                mostrarTexto("A febre amarela é uma doença infecciosa causada por um vírus do gênero Flavivirus\ntransmitido principalmente pela picada de mosquitos infectados, como o Aedes aegypti\nem áreas urbanas e o Haemagogus em áreas silvestres. É uma enfermidade endêmica em\nregiões tropicais da África e da América do Sul, incluindo o Brasil.\n" + //
                                        "\n\nA doença apresenta um ciclo silvestre e um ciclo urbano. No ciclo silvestre, o vírus circula\nentre primatas e mosquitos, podendo eventualmente infectar humanos que adentram\nessas áreas. No ciclo urbano, o ser humano é o principal hospedeiro, e a transmissão \nocorre entre pessoas por meio do mosquito. ");
                break;
            case "Sintomas":
                mostrarGraficoSintomas();
                break;
            case "Prevenção":
                mostrarTexto("A prevenção da febre amarela é fundamental para evitar a propagação da doença e proteger a população, especialmente em áreas onde o vírus é comum. A principal forma de prevenção é a vacinação, que é segura, eficaz e oferece proteção por toda a vida após uma única dose. Ela é recomendada para pessoas que vivem, trabalham ou viajam para regiões com risco de transmissão"+
                    "\n\nAlém da vacina, outra medida essencial é o controle dos mosquitos transmissores, como o Aedes aegypti e o Haemagogus. Isso inclui eliminar locais com água parada, onde os mosquitos se reproduzem, como pneus, vasos de plantas, garrafas e caixas d’água abertas.\n" + //
                                                "\n\nO uso de repelentes, roupas compridas e mosquiteiros também ajuda a reduzir o risco de picadas, especialmente em áreas de mata ou rurais. A conscientização da população, o monitoramento de casos suspeitos e campanhas de vacinação são estratégias importantes adotadas pelos serviços de saúde para prevenir surtos.");
                break;
            case "Vacinação":
                mostrarTexto("A vacinação contra a febre amarela é a principal forma de prevenção. A vacina é segura e eficaz, e está disponível gratuitamente no SUS.\n\nRecomenda-se a vacinação para pessoas que vivem ou viajam para áreas de risco.");
                break;
            case "Mitos e Verdades":
                mostrarTexto("Mitos e verdades:A febre amarela é uma doença que gera muitas dúvidas, e com isso surgem diversos mitos que podem confundir a população. Entender o que é verdade e o que é falso é essencial para a prevenção e o controle da doença\n\n"+
                    "Mito: A vacina contra febre amarela causa a doença.\n\n" + //
                    "Verdade: A vacina é feita com o vírus enfraquecido, e não causa a doença. Em casos raros, pode provocar reações leves, como febre ou dor no local da aplicação.\n\n" + //
                    "Mito: Só pega febre amarela quem vive na zona rural.\n\n" + //
                    "Verdade: Apesar de ser mais comum em áreas silvestres, a febre amarela também pode surgir em áreas urbanas, se houver mosquitos transmissores e pessoas não vacinadas.\n\n" + //
                    "Mito: A febre amarela passa de pessoa para pessoa.\n\n" + //
                    "Verdade: A doença não é contagiosa entre humanos. A transmissão acontece apenas pela picada do mosquito infectado.\n\n" + //
                    "Mito: Quem já tomou a vacina precisa tomar de novo.\n\n" + //
                    "Verdade: Desde 2017, uma única dose da vacina é suficiente para garantir proteção por toda a vida, segundo a Organização Mundial da Saúde (OMS).\n\n\n" + //
                    "Esclarecer esses pontos ajuda a combater o medo e a desinformação, promovendo atitudes corretas de prevenção.");
                break;
            case "Login":
                mostrarLogin();
                break;
            case "Cadastro":
                mostrarCadastro();
                break;
        }
        painelConteudo.revalidate();
        painelConteudo.repaint();
        statusLabel.setText("Visualizando: " + opcao);
    }

    private void mostrarTexto(String texto) {
        JTextArea areaTexto = new JTextArea(texto);
        areaTexto.setFont(this.fonteTexto);
        areaTexto.setForeground(corTexto);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setEditable(false);
        areaTexto.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        painelConteudo.add(scrollPane, BorderLayout.CENTER);
    }

    private void mostrarGraficoSintomas() {
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        graphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Maior Frequencia de Sintomas");
        titleLabel.setFont(fonteTitulo.deriveFont(Font.PLAIN, 20));
        titleLabel.setForeground(corTexto);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        graphPanel.add(titleLabel);

        try {
            String sql = "SELECT sintoma, porcentagem FROM sintomas";
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String sintoma = rs.getString("sintoma");
                int porcentagem = rs.getInt("porcentagem");

                JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                barPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
                barPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o conteúdo na horizontal

                JPanel bar = new JPanel();
                bar.setBackground(corPrimaria);
                bar.setPreferredSize(new Dimension(porcentagem * 3, 20));
                barPanel.add(bar);

                JLabel label = new JLabel(sintoma + " (" + porcentagem + "%)");
                barPanel.add(label);

                graphPanel.add(barPanel);

                JTextArea descricaoSintoma = new JTextArea();
                descricaoSintoma.setFont(fonteTexto);
                descricaoSintoma.setForeground(corTexto);
                descricaoSintoma.setLineWrap(true);
                descricaoSintoma.setWrapStyleWord(true);
                descricaoSintoma.setEditable(false);
                descricaoSintoma.setMargin(new Insets(5, 5, 5, 5));
                descricaoSintoma.setAlignmentX(Component.CENTER_ALIGNMENT);

                String descricao = "";
                switch (sintoma) {
                    case "Febre":
                        descricao = "Aumento da temperatura corporal, geralmente acima de 37,8°C.";
                        break;
                    case "Dor de cabeça":
                        descricao = "Dor na região da cabeça, podendo ser latejante ou constante.";
                        break;
                    case "Dores musculares":
                        descricao = "Dores nos músculos do corpo, podendo ser generalizadas ou localizadas.";
                        break;
                    case "Náuseas":
                        descricao = "Sensação de enjoo, podendo levar ao vômito.";
                        break;
                    case "Febre alta":
                        descricao = "Temperatura corporal elevada, geralmente acima de 38,5°C.";
                        break;
                    case "Icterícia":
                        descricao = "Amarelamento da pele e dos olhos, indicando problemas no fígado.";
                        break;
                    case "Sangramentos":
                        descricao = "Sangramentos espontâneos, como sangramento nasal ou gengival.";
                        break;
                    case"vômitos":
                        descricao = "Eliminação de conteúdo gástrico pela boca, podendo ser frequente.";
                        break;  
                    default:
                        descricao = "Eliminação de conteúdo gástrico pela boca, podendo ser frequente.";
                        break;
                }
                descricaoSintoma.setText(descricao);
		        graphPanel.add(descricaoSintoma);

                graphPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao exibir gráfico de sintomas: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao exibir gráfico de sintomas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(graphPanel);
        painelConteudo.add(scrollPane, BorderLayout.CENTER);
    }

    private void mostrarLogin() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(10);

        JLabel senhaLabel = new JLabel("Senha:");
        JPasswordField senhaField = new JPasswordField(10);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());

                try {
                    String sql = "SELECT * FROM usuarios WHERE email = '" + email + "' AND senha = '" + senha + "'";
                    Statement stmt = conexao.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    if (rs.next()) {
                        usuarioLogado = true;
                        JOptionPane.showMessageDialog(Projeto.this, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        mostrarConteudo("O que é");
                        statusLabel.setText("Logado como: " + email); // Exibe o email do usuário logado
                    } else {
                        JOptionPane.showMessageDialog(Projeto.this, "Email ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                    rs.close();
                    stmt.close();
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar login: " + ex.getMessage());
                    JOptionPane.showMessageDialog(Projeto.this, "Erro ao realizar login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(senhaLabel);
        loginPanel.add(senhaField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton esqueciSenhaButton = new JButton("Esqueceu a senha?");
        esqueciSenhaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JOptionPane.showInputDialog(Projeto.this, "Digite seu email:", "Esqueceu a senha?", JOptionPane.QUESTION_MESSAGE);
                if (email != null && !email.isEmpty()) {
                    try {
                        String sql = "SELECT * FROM usuarios WHERE email = '" + email + "'";
                        Statement stmt = conexao.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            JOptionPane.showMessageDialog(Projeto.this, "As instruções para redefinir a senha foram enviadas para o seu email.", "Esqueceu a senha?", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Projeto.this, "Email não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                        rs.close();
                        stmt.close();
                    } catch (SQLException ex) {
                        System.err.println("Erro ao verificar email: " + ex.getMessage());
                        JOptionPane.showMessageDialog(Projeto.this, "Erro ao verificar email: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        loginPanel.add(esqueciSenhaButton);
        loginPanel.add(loginButton);

        painelConteudo.add(loginPanel, BorderLayout.CENTER);
    }

    private void mostrarCadastro() {
        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new BoxLayout(cadastroPanel, BoxLayout.Y_AXIS));
        cadastroPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(3);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(3);

        JLabel senhaLabel = new JLabel("Senha:");
        JPasswordField senhaField = new JPasswordField(3);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());

                try {
                    String sql = "INSERT INTO usuarios (nome, email, senha) VALUES ('" + nome + "', '" + email + "', '" + senha + "')";
                    Statement stmt = conexao.createStatement();
                    stmt.executeUpdate(sql);

                    JOptionPane.showMessageDialog(Projeto.this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    mostrarConteudo("Login");

                    stmt.close();
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar cadastro: " + ex.getMessage());
                    JOptionPane.showMessageDialog(Projeto.this, "Erro ao realizar cadastro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        cadastroPanel.add(nomeLabel);
        cadastroPanel.add(nomeField);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(emailLabel);
        cadastroPanel.add(emailField);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(senhaLabel);
        cadastroPanel.add(senhaField);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(cadastrarButton);

        painelConteudo.add(cadastroPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception var1) {
                var1.printStackTrace();
            }

            Projeto app = new Projeto();
            app.setVisible(true);
        });
    }
}
