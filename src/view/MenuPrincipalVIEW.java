package view;

import bll.PessoaFisicaBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.PessoaFisicaEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import report.ExecutarRelatorio;
import utility.DataUtility;
import utility.Login;

public class MenuPrincipalVIEW extends javax.swing.JFrame implements IVIEW {

    public final void controlaAcessos(){
        if(Login.getLogin() != 0){
            
            UsuarioEntity user = new UsuarioBLL().pesquisar(Login.getLogin());
            if(user != null){
                
                    List<AcessoEntity> acessosG = new AcessoBLL().pesquisaPersonalizada("tiposistema = 'DESKTOP'");
                    List<AcessoEntity> acessosU = user.getGrupo().getAcessos();
                    for(int x = (acessosG.size()-1) ; x >= 0 ; x--){
                        for(int y = (acessosU.size()-1) ; y >= 0 ; y--){
                            if(acessosG.get(x).getId() == acessosU.get(y).getId()){
                                acessosG.remove(x);
                                break;
                            }
                        }
                    }

                    for(int x = (acessosG.size()-1) ; x >= 0 ; x--){
                        // menus de cadastros
                        if(this.miBairros.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miBairros.setVisible(false);
                        } else
                        if(this.miCategorias.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miCategorias.setVisible(false);
                        } else
                        if(this.miCidades.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miCidades.setVisible(false);
                        } else
                        if(this.miEstados.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miEstados.setVisible(false);
                        } else
                        if(this.miOfertas.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miOfertas.setVisible(false);
                        } else
                        if(this.miProdutos.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miProdutos.setVisible(false);
                        } else
                        if(this.miGrupos.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miGrupos.setVisible(false);
                        } else
                        if(this.miPessoasF.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miPessoasF.setVisible(false);
                        } else
                        if(this.miPessoasJ.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miPessoasJ.setVisible(false);
                        } else
                        if(this.miUsuarios.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miUsuarios.setVisible(false);
                        } else
                        // fim menu de cadastros
                            
                        // menu de operacoes
                        if(this.miCompras.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miCompras.setVisible(false);
                        } else
                        if(this.miVendas.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miVendas.setVisible(false);
                        } else
                        // fim dos menus de operacoes
                        
                        // menus de controles
                        if(this.miHistorico.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miHistorico.setVisible(false);
                        } else
                        if(this.miContatos.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miContatos.setVisible(false);
                        } else
                        //fim dos menus de controles    
                            
                        // menus de relatorios
                        if(this.miCategoriasR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miCategoriasR.setVisible(false);
                        } else
                        if(this.miBairrosR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miBairrosR.setVisible(false);
                        } else
                        if(this.miCidadesR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miCidadesR.setVisible(false);
                        } else
                        if(this.miEstadosR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miEstadosR.setVisible(false);
                        } else
                        if(this.miOfertasR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miOfertasR.setVisible(false);
                        } else
                        if(this.miProdutosR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miProdutosR.setVisible(false);
                        } else
                        if(this.miProdutosIR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miProdutosIR.setVisible(false);
                        } else
                        if(this.miPFR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miPFR.setVisible(false);
                        } else
                        if(this.miPJR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miPJR.setVisible(false);
                        } else
                        if(this.miComprasR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miComprasR.setVisible(false);
                        } else
                        if(this.miVendasR.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miVendasR.setVisible(false);
                        } else
                        //fim dos menus de relatorios
                        
                        //menus da conta
                        if(this.miMeusDados.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            miMeusDados.setVisible(false);
                        }
                        //fim dos menus de conta
                    }

                    if(!miBairros.isVisible() && !miCategorias.isVisible() && !miCidades.isVisible()
                            && !miEstados.isVisible() && !miProdutos.isVisible() && !miOfertas.isVisible()
                            && !miPessoasJ.isVisible() && !miPessoasF.isVisible() && !miGrupos.isVisible()
                            && !miUsuarios.isVisible()){
                        mnCadastros.setVisible(false);
                    }

                    if(!miContatos.isVisible() && !miHistorico.isVisible()){
                        mnControle.setVisible(false);
                    }

                    if(!miVendas.isVisible() && !miCompras.isVisible()){
                        mnOpercoes.setVisible(false);
                    }

                    if(!miBairrosR.isVisible() && !miCategoriasR.isVisible() && !miCidadesR.isVisible() && !miComprasR.isVisible()
                            && !miEstadosR.isVisible() && !miOfertasR.isVisible() && !miProdutosR.isVisible() && !miProdutosIR.isVisible()
                            && !miVendasR.isVisible() && !miPFR.isVisible() && !miPJR.isVisible()){
                        mnRelatorios.setVisible(false);
                    }
                    
                    if(!miMeusDados.isVisible()){
                        mnMinhaConta.setVisible(false);
                    }
                
            }else{
                System.exit(0);
            }
            
        }else{
            System.exit(0);
        }
    }
    
    private javax.swing.Timer timer;
    private ActionListener rel;
    
    public MenuPrincipalVIEW() {
        initComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        rel = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Date hora = new Date();
                SimpleDateFormat hora_formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                lbHorario.setText(hora_formato.format(hora));
            }
        };
        disparaRelogio();
        controlaAcessos();
        desabilitaBtFecharEAtalhos();
        pnIMGBG.setImagem(System.getProperty("user.dir")+"\\src\\icones\\back.jpg");
    }
    
    public final void disparaRelogio() {
        if (timer == null) {
            timer = new javax.swing.Timer(1000, rel);
            timer.setInitialDelay(0);
            timer.start();
        } else if (!timer.isRunning()) {
            timer.restart();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnIMGBG = new utility.PanelVisualizaImagem();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbHorario = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnCadastros = new javax.swing.JMenu();
        miBairros = new javax.swing.JMenuItem();
        miCategorias = new javax.swing.JMenuItem();
        miCidades = new javax.swing.JMenuItem();
        miEstados = new javax.swing.JMenuItem();
        miGrupos = new javax.swing.JMenuItem();
        miOfertas = new javax.swing.JMenuItem();
        miPessoasF = new javax.swing.JMenuItem();
        miPessoasJ = new javax.swing.JMenuItem();
        miProdutos = new javax.swing.JMenuItem();
        miUsuarios = new javax.swing.JMenuItem();
        mnControle = new javax.swing.JMenu();
        miContatos = new javax.swing.JMenuItem();
        miHistorico = new javax.swing.JMenuItem();
        mnOpercoes = new javax.swing.JMenu();
        miCompras = new javax.swing.JMenuItem();
        miVendas = new javax.swing.JMenuItem();
        mnRelatorios = new javax.swing.JMenu();
        miBairrosR = new javax.swing.JMenuItem();
        miCategoriasR = new javax.swing.JMenuItem();
        miCidadesR = new javax.swing.JMenuItem();
        miComprasR = new javax.swing.JMenuItem();
        miEstadosR = new javax.swing.JMenuItem();
        miOfertasR = new javax.swing.JMenuItem();
        miPFR = new javax.swing.JMenuItem();
        miPJR = new javax.swing.JMenuItem();
        miProdutosR = new javax.swing.JMenuItem();
        miProdutosIR = new javax.swing.JMenuItem();
        miVendasR = new javax.swing.JMenuItem();
        mnMinhaConta = new javax.swing.JMenu();
        miMeusDados = new javax.swing.JMenuItem();
        mnSair = new javax.swing.JMenu();
        miLogout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Principal");
        setName("mnPrincipal"); // NOI18N
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TCC - Igor Otoni Ripardo de Assis - Supermercado DESKTOP - CEFET Campus Timóteo MG 2014");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/time.png"))); // NOI18N
        jLabel1.setText("Data & Horário:");

        lbHorario.setBackground(new java.awt.Color(255, 255, 255));
        lbHorario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHorario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lbHorario.setOpaque(true);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbHorario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 191, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lbHorario, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout pnIMGBGLayout = new org.jdesktop.layout.GroupLayout(pnIMGBG);
        pnIMGBG.setLayout(pnIMGBGLayout);
        pnIMGBGLayout.setHorizontalGroup(
            pnIMGBGLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnIMGBGLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnIMGBGLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, pnIMGBGLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnIMGBGLayout.setVerticalGroup(
            pnIMGBGLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnIMGBGLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 486, Short.MAX_VALUE)
                .add(jLabel2)
                .addContainerGap())
        );

        mnCadastros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/database_yellow.png"))); // NOI18N
        mnCadastros.setMnemonic('c');
        mnCadastros.setText("Cadastros");
        mnCadastros.setToolTipText("<html>Menu para acesso a todos os cadastros do sistema<br />Atalho: alt + c<html>");
        mnCadastros.setName("menuCadastros"); // NOI18N

        miBairros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/map.png"))); // NOI18N
        miBairros.setMnemonic('b');
        miBairros.setText("Bairros");
        miBairros.setToolTipText("<html>Menu para cadastro de bairros<br />Atalho: alt + b</html>");
        miBairros.setName("menuBairros"); // NOI18N
        miBairros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miBairrosActionPerformed(evt);
            }
        });
        mnCadastros.add(miBairros);

        miCategorias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/paste_plain.png"))); // NOI18N
        miCategorias.setMnemonic('a');
        miCategorias.setText("Categorias");
        miCategorias.setToolTipText("<html>Menu para cadastro de categorias<br />Atalho: alt + a</html>");
        miCategorias.setName("menuCategorias"); // NOI18N
        miCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCategoriasActionPerformed(evt);
            }
        });
        mnCadastros.add(miCategorias);

        miCidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/map.png"))); // NOI18N
        miCidades.setMnemonic('i');
        miCidades.setText("Cidades");
        miCidades.setToolTipText("<html>Menu para cadastros de cidades<br />Atalho: alt + i</html>");
        miCidades.setName("menuCidades"); // NOI18N
        miCidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCidadesActionPerformed(evt);
            }
        });
        mnCadastros.add(miCidades);

        miEstados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/map.png"))); // NOI18N
        miEstados.setMnemonic('e');
        miEstados.setText("Estados");
        miEstados.setToolTipText("<html>Menu para cadastro de estados<br />Atalho: alt + e</html>");
        miEstados.setName("menuEstados"); // NOI18N
        miEstados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEstadosActionPerformed(evt);
            }
        });
        mnCadastros.add(miEstados);

        miGrupos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/group.png"))); // NOI18N
        miGrupos.setMnemonic('g');
        miGrupos.setText("Grupos");
        miGrupos.setToolTipText("<html>Menu para cadastro de grupos<br />Atalho: alt + a</html>");
        miGrupos.setName("menuGrupos"); // NOI18N
        miGrupos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGruposActionPerformed(evt);
            }
        });
        mnCadastros.add(miGrupos);

        miOfertas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/note.png"))); // NOI18N
        miOfertas.setMnemonic('f');
        miOfertas.setText("Ofertas");
        miOfertas.setToolTipText("<html>Menu para cadastro de ofertas<br />Atalho: alt + f</html>");
        miOfertas.setName("menuOfertas"); // NOI18N
        miOfertas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOfertasActionPerformed(evt);
            }
        });
        mnCadastros.add(miOfertas);

        miPessoasF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user.png"))); // NOI18N
        miPessoasF.setMnemonic('c');
        miPessoasF.setText("Pessoas Físicas");
        miPessoasF.setToolTipText("<html>Menu para cadastro de pessoas físicas<br />Atalho: alt + c</html>");
        miPessoasF.setName("menuPessoasF"); // NOI18N
        miPessoasF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPessoasFActionPerformed(evt);
            }
        });
        mnCadastros.add(miPessoasF);

        miPessoasJ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_brown.png"))); // NOI18N
        miPessoasJ.setMnemonic('j');
        miPessoasJ.setText("Pessoas Jurídicas");
        miPessoasJ.setToolTipText("<html>Menu para cadastro de pessoas jurídicas<br />Atalho: alt + a</html>");
        miPessoasJ.setName("menuPessoasJ"); // NOI18N
        miPessoasJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPessoasJActionPerformed(evt);
            }
        });
        mnCadastros.add(miPessoasJ);

        miProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package.png"))); // NOI18N
        miProdutos.setMnemonic('p');
        miProdutos.setText("Produtos");
        miProdutos.setToolTipText("<html>Menu para cadastro de produtos<br />Atalho: alt + p</html>");
        miProdutos.setName("menuProdutos"); // NOI18N
        miProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miProdutosActionPerformed(evt);
            }
        });
        mnCadastros.add(miProdutos);

        miUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_gray.png"))); // NOI18N
        miUsuarios.setMnemonic('u');
        miUsuarios.setText("Usuários");
        miUsuarios.setToolTipText("<html>Menu para cadastro de usuários do sistema<br />Atalho: alt + u</html>");
        miUsuarios.setName("menuUsuarios"); // NOI18N
        miUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miUsuariosActionPerformed(evt);
            }
        });
        mnCadastros.add(miUsuarios);

        jMenuBar1.add(mnCadastros);

        mnControle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cog.png"))); // NOI18N
        mnControle.setMnemonic('t');
        mnControle.setText("Controle");
        mnControle.setToolTipText("<html>Menu para acesso a todos os controles do sistema<br />Atalho: alt + t<html>");
        mnControle.setName("menuControle"); // NOI18N

        miContatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_red.png"))); // NOI18N
        miContatos.setMnemonic('o');
        miContatos.setText("Contatos");
        miContatos.setToolTipText("<html>Menu para cadastro de contatos<br />Atalho: alt + o<html>");
        miContatos.setName("menuContatos"); // NOI18N
        miContatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miContatosActionPerformed(evt);
            }
        });
        mnControle.add(miContatos);

        miHistorico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/database_table.png"))); // NOI18N
        miHistorico.setText("Histórico de usuários");
        miHistorico.setName("menuHistorico"); // NOI18N
        miHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miHistoricoActionPerformed(evt);
            }
        });
        mnControle.add(miHistorico);

        jMenuBar1.add(mnControle);

        mnOpercoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/money_dollar.png"))); // NOI18N
        mnOpercoes.setMnemonic('o');
        mnOpercoes.setText("Operações");
        mnOpercoes.setToolTipText("<html>Menu para acesso a todas as operações do sistema<br />Atalho: alt + o<html>");
        mnOpercoes.setName("menuOperacao"); // NOI18N

        miCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package.png"))); // NOI18N
        miCompras.setText("Realizar Compra");
        miCompras.setName("menuComprasO"); // NOI18N
        miCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miComprasActionPerformed(evt);
            }
        });
        mnOpercoes.add(miCompras);

        miVendas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/money.png"))); // NOI18N
        miVendas.setText("Realizar Venda");
        miVendas.setName("menuVendasO"); // NOI18N
        miVendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVendasActionPerformed(evt);
            }
        });
        mnOpercoes.add(miVendas);

        jMenuBar1.add(mnOpercoes);

        mnRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/chart_bar.png"))); // NOI18N
        mnRelatorios.setMnemonic('r');
        mnRelatorios.setText("Relatórios");
        mnRelatorios.setToolTipText("<html>Menu para acesso a todos os relatórios do sistema<br />Atalho: alt + r<html>");
        mnRelatorios.setName("menuRelatorio"); // NOI18N

        miBairrosR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miBairrosR.setText("Bairros");
        miBairrosR.setName("relatorioBairros"); // NOI18N
        miBairrosR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miBairrosRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miBairrosR);

        miCategoriasR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miCategoriasR.setText("Categorias");
        miCategoriasR.setName("relatorioCategorias"); // NOI18N
        miCategoriasR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCategoriasRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miCategoriasR);

        miCidadesR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miCidadesR.setText("Cidades");
        miCidadesR.setName("relatorioCidades"); // NOI18N
        miCidadesR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCidadesRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miCidadesR);

        miComprasR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miComprasR.setText("Compras");
        miComprasR.setName("relatorioCompras"); // NOI18N
        miComprasR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miComprasRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miComprasR);

        miEstadosR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miEstadosR.setText("Estados");
        miEstadosR.setName("relatorioEstados"); // NOI18N
        miEstadosR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEstadosRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miEstadosR);

        miOfertasR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miOfertasR.setText("Ofertas");
        miOfertasR.setName("relatorioOfertas"); // NOI18N
        miOfertasR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOfertasRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miOfertasR);

        miPFR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miPFR.setText("Pessoas Físicas");
        miPFR.setName("relatorioPessoasF"); // NOI18N
        miPFR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPFRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miPFR);

        miPJR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miPJR.setText("Pessoas Jurídicas");
        miPJR.setName("relatorioPessoasJ"); // NOI18N
        miPJR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPJRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miPJR);

        miProdutosR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miProdutosR.setText("Produtos");
        miProdutosR.setName("relatorioProdutos"); // NOI18N
        miProdutosR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miProdutosRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miProdutosR);

        miProdutosIR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miProdutosIR.setText("Produtos com ícone");
        miProdutosIR.setName("relatorioProdutosIMG"); // NOI18N
        miProdutosIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miProdutosIRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miProdutosIR);

        miVendasR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/book_open.png"))); // NOI18N
        miVendasR.setText("Vendas");
        miVendasR.setName("relatorioVendas"); // NOI18N
        miVendasR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVendasRActionPerformed(evt);
            }
        });
        mnRelatorios.add(miVendasR);

        jMenuBar1.add(mnRelatorios);

        mnMinhaConta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/vcard.png"))); // NOI18N
        mnMinhaConta.setText("Minha conta");
        mnMinhaConta.setName("menuMinhaConta"); // NOI18N

        miMeusDados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/vcard_key.png"))); // NOI18N
        miMeusDados.setText("Meus dados");
        miMeusDados.setName("menuMeusDados"); // NOI18N
        miMeusDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMeusDadosActionPerformed(evt);
            }
        });
        mnMinhaConta.add(miMeusDados);

        jMenuBar1.add(mnMinhaConta);

        mnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/control_power.png"))); // NOI18N
        mnSair.setMnemonic('s');
        mnSair.setText("Sair");
        mnSair.setToolTipText("<html>Menu para sair do sistema<br />Atalho: alt + s<html>");
        mnSair.setName("menuSair"); // NOI18N
        mnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnSairMouseClicked(evt);
            }
        });
        mnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSairActionPerformed(evt);
            }
        });

        miLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/bullet_cross.png"))); // NOI18N
        miLogout.setMnemonic('u');
        miLogout.setText("Logout");
        miLogout.setToolTipText("<html>Sair da conta e do sisemta<br />Atalho: alt + u</html>");
        miLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogoutActionPerformed(evt);
            }
        });
        mnSair.add(miLogout);

        jMenuBar1.add(mnSair);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnIMGBG, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnIMGBG, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnSairMouseClicked
        
    }//GEN-LAST:event_mnSairMouseClicked

    private void miCidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCidadesActionPerformed
        
        new CidadeVIEW().setVisible(true);
        
    }//GEN-LAST:event_miCidadesActionPerformed

    private void miCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCategoriasActionPerformed
        
        new CategoriaVIEW().setVisible(true);
        
    }//GEN-LAST:event_miCategoriasActionPerformed

    private void miEstadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadosActionPerformed
        
        new EstadoVIEW().setVisible(true);
        
    }//GEN-LAST:event_miEstadosActionPerformed

    private void miBairrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miBairrosActionPerformed
        
        new BairroVIEW().setVisible(true);
        
    }//GEN-LAST:event_miBairrosActionPerformed

    private void miPessoasFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPessoasFActionPerformed
       
        new PessoaFisicaVIEW().setVisible(true);
        
    }//GEN-LAST:event_miPessoasFActionPerformed

    private void miContatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miContatosActionPerformed
        
        new ContatoVIEW().setVisible(true);
        
    }//GEN-LAST:event_miContatosActionPerformed

    private void miProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miProdutosActionPerformed
        
        new ProdutoVIEW().setVisible(true);
        
    }//GEN-LAST:event_miProdutosActionPerformed

    private void miOfertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOfertasActionPerformed
        
        new OfertaVIEW().setVisible(true);
        
    }//GEN-LAST:event_miOfertasActionPerformed

    private void mnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSairActionPerformed
        
        
        
    }//GEN-LAST:event_mnSairActionPerformed

    public final void desabilitaBtFecharEAtalhos(){
        this.addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent evt) {
                acaoFechar();
            }  
        });
    }
    
    public void acaoFechar(){
                    
        int temp = JOptionPane.showConfirmDialog(null,"Quer mesmo sair?", "CONFIRMAR",JOptionPane.YES_NO_OPTION);

        if(temp == JOptionPane.YES_OPTION){
            
            UsuarioEntity u = new UsuarioBLL().pesquisar(Login.getLogin());
            u.setUltimoLogout(DataUtility.getDateTime("EN"));
            new UsuarioBLL().salvar(u);
            
            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Logout'").get(0));
            ul.setDescricao("Realizou logout.");
            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
            ul.setHorario(DataUtility.getDateTime("EN"));
            new UsuarioAcaoBLL().salvar(ul);
            
            this.dispose();
            Login.logout();
            new LoginVIEW().setVisible(true);
            
        }
        
    }
    
    private void miLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogoutActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_miLogoutActionPerformed

    private void miGruposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGruposActionPerformed
        
        new GrupoVIEW().setVisible(true);
        
    }//GEN-LAST:event_miGruposActionPerformed

    private void miPessoasJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPessoasJActionPerformed
        
        new PessoaJuridicaVIEW().setVisible(true);
        
    }//GEN-LAST:event_miPessoasJActionPerformed

    private void miUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsuariosActionPerformed
        
        new UsuarioVIEW().setVisible(true);
        
    }//GEN-LAST:event_miUsuariosActionPerformed

    private void miEstadosRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadosRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Estados", "RelatorioEstado.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miEstadosRActionPerformed

    private void miCidadesRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCidadesRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Cidades", "RelatorioCidade.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miCidadesRActionPerformed

    private void miBairrosRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miBairrosRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Bairros", "RelatorioBairro.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miBairrosRActionPerformed

    private void miCategoriasRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCategoriasRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Categorias", "RelatorioCategoria.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miCategoriasRActionPerformed

    private void miProdutosRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miProdutosRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Produtos", "RelatorioProduto.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miProdutosRActionPerformed

    private void miOfertasRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOfertasRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Ofertas", "RelatorioOferta.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miOfertasRActionPerformed

    private void miHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miHistoricoActionPerformed
        
        new HistoricoVIEW().setVisible(true);
        
    }//GEN-LAST:event_miHistoricoActionPerformed

    private void miComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miComprasActionPerformed
       
        new CompraVIEW().setVisible(true);
        
    }//GEN-LAST:event_miComprasActionPerformed

    private void miPJRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPJRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Ofertas", "RelatorioPessoasJuridicas.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miPJRActionPerformed

    private void miPFRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPFRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Pessoas Físicas", "RelatorioPessoaFisica.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miPFRActionPerformed

    private void miVendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVendasActionPerformed
        
        new VendaVIEW().setVisible(true);
        
    }//GEN-LAST:event_miVendasActionPerformed

    private void miProdutosIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miProdutosIRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Produtos com ícones", "RelatorioProdutoImg.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miProdutosIRActionPerformed

    private void miComprasRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miComprasRActionPerformed
        
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Compras", "RelatorioCompras.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miComprasRActionPerformed

    private void miMeusDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMeusDadosActionPerformed
        
        UsuarioEntity usuario = new UsuarioBLL().pesquisar(Login.getLogin());
        PessoaFisicaEntity pessof = new PessoaFisicaBLL().pesquisar(usuario.getPessoa().getId());
        if(pessof != null){
            new MinhaContaPFVIEW().setVisible(true);
        }else{
            new MinhaContaPJVIEW().setVisible(true);
        }
        this.dispose();
        
    }//GEN-LAST:event_miMeusDadosActionPerformed

    private void miVendasRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVendasRActionPerformed
       
        ExecutarRelatorio rel = new ExecutarRelatorio();
        try{
            rel.abrirRelatorio( "Relatório de Vendas", "RelatorioVendas.jasper");
        }
        catch(JRException e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_miVendasRActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbHorario;
    private javax.swing.JMenuItem miBairros;
    private javax.swing.JMenuItem miBairrosR;
    private javax.swing.JMenuItem miCategorias;
    private javax.swing.JMenuItem miCategoriasR;
    private javax.swing.JMenuItem miCidades;
    private javax.swing.JMenuItem miCidadesR;
    private javax.swing.JMenuItem miCompras;
    private javax.swing.JMenuItem miComprasR;
    private javax.swing.JMenuItem miContatos;
    private javax.swing.JMenuItem miEstados;
    private javax.swing.JMenuItem miEstadosR;
    private javax.swing.JMenuItem miGrupos;
    private javax.swing.JMenuItem miHistorico;
    private javax.swing.JMenuItem miLogout;
    private javax.swing.JMenuItem miMeusDados;
    private javax.swing.JMenuItem miOfertas;
    private javax.swing.JMenuItem miOfertasR;
    private javax.swing.JMenuItem miPFR;
    private javax.swing.JMenuItem miPJR;
    private javax.swing.JMenuItem miPessoasF;
    private javax.swing.JMenuItem miPessoasJ;
    private javax.swing.JMenuItem miProdutos;
    private javax.swing.JMenuItem miProdutosIR;
    private javax.swing.JMenuItem miProdutosR;
    private javax.swing.JMenuItem miUsuarios;
    private javax.swing.JMenuItem miVendas;
    private javax.swing.JMenuItem miVendasR;
    private javax.swing.JMenu mnCadastros;
    private javax.swing.JMenu mnControle;
    private javax.swing.JMenu mnMinhaConta;
    private javax.swing.JMenu mnOpercoes;
    private javax.swing.JMenu mnRelatorios;
    private javax.swing.JMenu mnSair;
    private utility.PanelVisualizaImagem pnIMGBG;
    // End of variables declaration//GEN-END:variables

}
