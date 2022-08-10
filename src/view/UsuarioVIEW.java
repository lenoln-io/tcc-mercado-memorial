package view;

import bll.OperacaoBLL;
import bll.PessoaFisicaBLL;
import bll.PessoaJuridicaBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.GrupoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.GrupoEntity;
import entity.OperacaoEntity;
import entity.PessoaFisicaEntity;
import entity.PessoaJuridicaEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.PessoaFisicaTableModel;
import model.PessoaJuridicaTableModel;
import model.UsuarioTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.EmailValidator;
import utility.ExportarExcel;
import utility.Login;


public class UsuarioVIEW extends javax.swing.JFrame implements IVIEW {
    
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
                        if(this.btNovo.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btNovo.setVisible(false);
                        } else
                        if(this.btEditar.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btEditar.setVisible(false);
                        } else
                        if(this.btExcluir.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btExcluir.setVisible(false);
                        }
                    }
                    
                    if(!btNovo.isVisible() && !btEditar.isVisible()){
                        btSalvar.setVisible(false);
                        btCancelar.setVisible(false);
                    }
                
            }else{
                System.exit(0);
            }
            
        }else{
            System.exit(0);
        }
    }

    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaUsuario.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaUsuario.getColumnName(i), modeloTabelaUsuario.getnomeTabela(i),
                modeloTabelaUsuario.getNomeColunaTabela(i),
                modeloTabelaUsuario.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposF() {

        cbxCam.removeAllItems();

        for (int i = 0; i < modelPf.getColumnCount(); i++) {
            cbxCam.addItem(new CampoFiltro(
                modelPf.getColumnName(i), modelPf.getnomeTabela(i),
                modelPf.getNomeColunaTabela(i),
                modelPf.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposJ() {

        cbxCam.removeAllItems();

        for (int i = 0; i < modelPj.getColumnCount(); i++) {
            cbxCam.addItem(new CampoFiltro(
                modelPj.getColumnName(i), modelPj.getnomeTabela(i),
                modelPj.getNomeColunaTabela(i),
                modelPj.getTipoColunaTabela(i))
            );
        }

    }
    
    private void insereIcones(){
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    }    
    
    private void carregarTabela() {
        modeloTabelaUsuario.Recarregar(controleUsuario.pesquisaPersonalizada("id > 0 and id <> "+Login.getLogin()));
        tbUsuarios.setModel(modeloTabelaUsuario);
    }

    private void carregarTabela(List<UsuarioEntity> filtrar) {
        for(int x = (filtrar.size()-1) ; x >= 0 ; x--){
            if(filtrar.get(x).getId() == Login.getLogin()){
                filtrar.remove(x);
            }
        }
        modeloTabelaUsuario.Recarregar(filtrar);
        tbUsuarios.setModel(modeloTabelaUsuario);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private situacaoPessoa sp;
    
    private enum situacaoPessoa {
        filtrar, escolher, escolhido, trancado
    }
    
    private void limparCampos() {
        modelPf.Recarregar(null);
        modelPj.Recarregar(null);
        txtEmail.setText("");
        txtSenha.setText("");
        buttonGroup1.clearSelection();
        tbPessoas.clearSelection();
        tbUsuarios.clearSelection();
        cbxGrupos.setSelectedIndex(-1);
        cbxTp.setSelectedIndex(-1);
    }
    
    private void controlarCampos(boolean condicao) {
        tbUsuarios.setEnabled(!condicao);
    }
    
    private void controlarBotoesP(){
        
        cbxTp.setEnabled(sp == situacaoPessoa.filtrar);
        btBus.setEnabled(sp == situacaoPessoa.filtrar);
        
        if(tbPessoas.getSelectedRow() != -1)
            btSel.setEnabled(sp == situacaoPessoa.escolher);
        else
            btSel.setEnabled(false);
        
        btDes.setEnabled(sp == situacaoPessoa.escolhido);
        btCan.setEnabled(sp == situacaoPessoa.escolher);
        
        
        cbxCam.setEnabled(sp == situacaoPessoa.escolher);
        txtCri.setEnabled(sp == situacaoPessoa.escolher);
        btPes.setEnabled(sp == situacaoPessoa.escolher);
        btLis.setEnabled(sp == situacaoPessoa.escolher);
        
        tbPessoas.setEnabled(sp == situacaoPessoa.escolher);
        txtEmail.setEnabled(sp == situacaoPessoa.escolhido);
        txtSenha.setEnabled(sp == situacaoPessoa.escolhido);
        rbAtivado.setEnabled(sp == situacaoPessoa.escolhido);
        rbDesativado.setEnabled(sp == situacaoPessoa.escolhido);
        cbxGrupos.setEnabled(sp == situacaoPessoa.escolhido);
    }
    
    private DefaultComboBoxModel<GrupoEntity> modelG = new DefaultComboBoxModel<GrupoEntity>();
    
    public final void carregarComboBoxGrupos(){
        UsuarioEntity ul = new UsuarioBLL().pesquisar(Login.getLogin());
        if(ul.getGrupo().getNome().equalsIgnoreCase("Controlador mestre") || ul.getGrupo().getNome().equalsIgnoreCase("Administradores")){
            modelG.removeAllElements();
            cbxGrupos.setModel(modelG);
            List<GrupoEntity> grupos = new GrupoBLL().listarTodos();
            for(int x = 0 ; x<grupos.size() ; x++){
                if(!grupos.get(x).getNome().equalsIgnoreCase("Controlador mestre"))
                    modelG.addElement(grupos.get(x));
            }
            cbxGrupos.setSelectedIndex(-1);
        }else{
            modelG.removeAllElements();
            cbxGrupos.setModel(modelG);
            List<GrupoEntity> grupos = new GrupoBLL().listarTodos();
            for(int x = 0 ; x<grupos.size() ; x++){
                if(!grupos.get(x).getNome().equalsIgnoreCase("Controlador mestre") && !grupos.get(x).getNome().equalsIgnoreCase("Administradores"))
                    modelG.addElement(grupos.get(x));
            }
            cbxGrupos.setSelectedIndex(-1);
        }
    }
    
    private void controlarBotoes() {
        btNovo.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btSalvar.setEnabled(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo);
        btEditar.setEnabled(situacao == situacaoCadastro.scVisualizando);
        btCancelar.setEnabled(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo);
        btExcluir.setEnabled(situacao == situacaoCadastro.scVisualizando);
        
        btExcel.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btFiltrar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        txtCriterio.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btListar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        cbxCampos.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
    }
    
    private void preencherCampos() {
        objetoUsuario = modeloTabelaUsuario.getObjeto(tbUsuarios.getSelectedRow()).getClone();
        txtEmail.setText(objetoUsuario.getEmail());
        txtSenha.setText(objetoUsuario.getSenha());
        if(objetoUsuario.getEstatus().equals("Ativado")){
            rbAtivado.setSelected(true);
        }else{
            rbDesativado.setSelected(true);
        }
        modelG.setSelectedItem(objetoUsuario.getGrupo());
        List<PessoaJuridicaEntity> res1 = new PessoaJuridicaBLL().pesquisaPersonalizada("pessoa = "+objetoUsuario.getPessoa().getId());
        if(res1 != null && !res1.isEmpty()){
            modelPj.Recarregar(res1);
            tbPessoas.setModel(modelPj);
            cbxTp.setSelectedItem("Jurídicas");
        }else{
            List<PessoaFisicaEntity> res2 = new PessoaFisicaBLL().pesquisaPersonalizada("pessoa = "+objetoUsuario.getPessoa().getId());
            modelPf.Recarregar(res2);
            tbPessoas.setModel(modelPf);
            cbxTp.setSelectedItem("Físicas");
        }
    }
    
    private situacaoCadastro situacao;
    private UsuarioEntity objetoUsuario;
    private final UsuarioBLL controleUsuario = new UsuarioBLL();
    private final UsuarioTableModel modeloTabelaUsuario = new UsuarioTableModel();
    private UsuarioTableModel tbmodel = new UsuarioTableModel();
    private PessoaFisicaTableModel modelPf = new PessoaFisicaTableModel();
    private PessoaJuridicaTableModel modelPj = new PessoaJuridicaTableModel();
    
    private void criaMascaras(){
        
    }
    
    private DefaultComboBoxModel<String> modelTpp = new DefaultComboBoxModel<>();
    
    public final void carregarCbxTpP(){
        cbxTp.setModel(modelTpp);
        modelTpp.addElement("Físicas");
        modelTpp.addElement("Jurídicas");
        cbxTp.setSelectedIndex(-1);
    }
    
    public UsuarioVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        carregarTabela();
        controlarBotoes();
        controlarCampos(false);
        insereIcones();
        preencherFiltroCampos();
        desabilitaBtFecharEAtalhos();
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        carregarCbxTpP();
        carregarComboBoxGrupos();
        controlaAcessos();
    }
    
    public final void desabilitaBtFecharEAtalhos(){
        this.addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent evt) {
                acaoFechar();
            }  
        });
    }
    
    public void acaoFechar(){
        if(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo){
                    
            int temp = JOptionPane.showConfirmDialog(null,"Você não salvou a operação atual, se sair perderá as informações desse registro.\nDeseja mesmo sair?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {
                dispose();
            }

        }else{
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btNovo = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPessoas = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btSel = new javax.swing.JButton();
        btDes = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        rbAtivado = new javax.swing.JRadioButton();
        txtSenha = new javax.swing.JPasswordField();
        cbxGrupos = new javax.swing.JComboBox();
        lbGrupos = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rbDesativado = new javax.swing.JRadioButton();
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btPes = new javax.swing.JButton();
        txtCri = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btLis = new javax.swing.JButton();
        cbxCam = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        btBus = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btCan = new javax.swing.JButton();
        cbxTp = new javax.swing.JComboBox();
        spTabela = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de usários");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar novo usuário<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoUsuario"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do usuário<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarUsuario"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados do usuário<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarUsuario"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír cidade<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirUsuario"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarUsuario"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de usuários<br />Atalho: alt + f</html>");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(btNovo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btSalvar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btEditar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btExcluir)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btCancelar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btFechar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btNovo)
                    .add(btSalvar)
                    .add(btEditar)
                    .add(btExcluir)
                    .add(btCancelar)
                    .add(btFechar))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jScrollPane1MousePressed(evt);
            }
        });

        tbPessoas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbPessoas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbPessoasMousePressed(evt);
            }
        });
        tbPessoas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPessoasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbPessoas);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/tick.png"))); // NOI18N
        btSel.setText("Selecionar pessoa para usuário");
        btSel.setToolTipText("Selecionar pessoa para o usuário");
        btSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelActionPerformed(evt);
            }
        });

        btDes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/decline.png"))); // NOI18N
        btDes.setText("Deselecionar pessoa para usuário");
        btDes.setToolTipText("Deselecionar pessoa do usuário");
        btDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDesActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Pessoa do usuário:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btSel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 226, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btDes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 226, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btSel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btDes)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Estatus:");

        buttonGroup1.add(rbAtivado);
        rbAtivado.setText("Ativado");

        lbGrupos.setText("Grupo:");

        jLabel3.setText("Senha:");

        jLabel2.setText("Email:");

        buttonGroup1.add(rbDesativado);
        rbDesativado.setText("Desativado");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Dados do usuário:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtEmail)
                    .add(txtSenha)
                    .add(cbxGrupos, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(lbGrupos)
                            .add(jLabel4)
                            .add(rbAtivado)
                            .add(rbDesativado)
                            .add(jLabel5))
                        .add(0, 67, Short.MAX_VALUE)))
                .add(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbGrupos)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxGrupos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbAtivado)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbDesativado)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btPes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btPes.setText("Pesquisar");
        btPes.setToolTipText("Filtrar tabela de pessoas por critério");
        btPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPesActionPerformed(evt);
            }
        });

        jLabel6.setText("Critério:");

        btLis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png"))); // NOI18N
        btLis.setText("Listar");
        btLis.setToolTipText("Listar todas as pessoas do tipo na tabela");
        btLis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLisActionPerformed(evt);
            }
        });

        cbxCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCamActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(cbxCam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCri, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btPes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btLis)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btLis)
                    .add(cbxCam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btPes)
                    .add(txtCri, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btBus.setText("Buscar pessoas por tipo");
        btBus.setToolTipText("Buscar pessoas do tipo");
        btBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBusActionPerformed(evt);
            }
        });

        jLabel7.setText("Tipo de pessoa:");

        btCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png"))); // NOI18N
        btCan.setText("Cancelar");
        btCan.setToolTipText("<html>Cancelar seleção de pessoa<html>");
        btCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCanActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxTp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 226, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btBus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btCan, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(cbxTp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btBus)
                    .add(btCan))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbUsuarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbUsuariosMousePressed(evt);
            }
        });
        tbUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbUsuariosKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbUsuarios);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setMnemonic('x');
        btExcel.setText("Excel");
        btExcel.setToolTipText("<html>Exportar dados da tabela para planilha excel<br />Atalho: alt + x</html>");
        btExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcelActionPerformed(evt);
            }
        });

        cbxCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCamposActionPerformed(evt);
            }
        });

        btFiltrar.setMnemonic('f');
        btFiltrar.setText("Filtrar");
        btFiltrar.setToolTipText("<html>Filtrar dados da tabela pelo critério<br />Atalho: alt + t</html>");
        btFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarActionPerformed(evt);
            }
        });

        btListar.setMnemonic('l');
        btListar.setText("Listar");
        btListar.setToolTipText("<html>Mostrar todos os usuários na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel1.setText("Critério:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 451, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btFiltrar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btListar)
                .addContainerGap())
        );
        pnFiltrosLayout.setVerticalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btExcel)
                    .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrar)
                    .add(btListar)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(tbAbas)
                    .add(pnFiltros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(spTabela))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 328, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed

        if(objetoUsuario != null){
            objetoUsuario.setPessoa(null);
        }
        limparCampos();
        situacao = situacaoCadastro.scInserindo;
        controlarBotoes();
        controlarCampos(true);
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
       
        if(objetoUsuario != null && objetoUsuario.getPessoa() != null){
            txtEmail.setText(txtEmail.getText().toLowerCase().trim());

            if(!txtEmail.getText().equals("") && txtSenha.getPassword().length > 0 && (rbAtivado.isSelected() || rbDesativado.isSelected()) && modelG.getSelectedItem() != null){

                EmailValidator em = new EmailValidator();
                if(em.validate(txtEmail.getText())){
                    
                    List<UsuarioEntity> resp;
                    if(situacao == situacaoCadastro.scInserindo){
                        resp = new UsuarioBLL().pesquisaPersonalizada("email = '"+txtEmail.getText()+"'");
                    }else{
                        resp = new UsuarioBLL().pesquisaPersonalizada("email = '"+txtEmail.getText()+"' and id <> "+objetoUsuario.getId());
                    }
                    if(resp == null || resp.isEmpty()){
                        if(txtSenha.getPassword().length <= 16){

                            objetoUsuario.setEmail(txtEmail.getText());
                            objetoUsuario.setGrupo((GrupoEntity) modelG.getSelectedItem());

                            if(rbAtivado.isSelected()){
                                objetoUsuario.setEstatus("Ativado");
                            }else{
                                objetoUsuario.setEstatus("Destivado");
                            }

                            String pass = "";
                            for(int x = 0 ; x < txtSenha.getPassword().length ; x++){
                                pass += txtSenha.getPassword()[x];
                            }
                            objetoUsuario.setSenha(pass);

                            controleUsuario.salvar(objetoUsuario);

                            if (situacao == situacaoCadastro.scInserindo) {
                                
                                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                                ul.setDescricao("Cadastrou o usuário de ID = "+objetoUsuario.getId()+" para a pessoa de nome = "+objetoUsuario.getPessoa().getNome());
                                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                ul.setHorario(DataUtility.getDateTime("EN"));
                                new UsuarioAcaoBLL().salvar(ul);
                                
                                JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                            }else{
                                
                                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                                ul.setDescricao("Alterou o usuário de ID = "+objetoUsuario.getId());
                                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                ul.setHorario(DataUtility.getDateTime("EN"));
                                new UsuarioAcaoBLL().salvar(ul);
                                
                                JOptionPane.showMessageDialog(null,"O registro foi atualizado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                            }

                            carregarTabela();
                            limparCampos();
                            situacao = situacaoCadastro.scEsperando;
                            controlarBotoes();
                            if(objetoUsuario != null){
                                objetoUsuario.setPessoa(null);
                            }
                            sp = situacaoPessoa.trancado;
                            controlarBotoesP();
                            controlarCampos(false);

                        }else{
                            JOptionPane.showMessageDialog(null,"Password inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Email inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Email inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                }

            }else{
                JOptionPane.showMessageDialog(null,"Preencha todos os campos.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Selecione uma pessoa.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        UsuarioEntity usuariol = new UsuarioBLL().pesquisar(Login.getLogin());
        if(usuariol.getGrupo().getNome().equalsIgnoreCase("Controlador mestre") || (usuariol.getGrupo().getNome().equalsIgnoreCase("Administradores") && (!objetoUsuario.getGrupo().getNome().equalsIgnoreCase("Administradores") && !objetoUsuario.getGrupo().getNome().equalsIgnoreCase("Controlador mestre")))){
            situacao = situacaoCadastro.scEditando;
            controlarBotoes();
            controlarCampos(true);
            sp = situacaoPessoa.escolhido;
            controlarBotoesP();
        }else{
            JOptionPane.showMessageDialog(null, "Você não têm permissão para editar esse usuário", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbUsuarios, new File(caminho));
                JOptionPane.showMessageDialog(null, "Arquivo: '" + caminho + "' exportado com sucesso.","Mensagem", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_btExcelActionPerformed

    private void btFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFiltrarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        
        txtCriterio.setText(txtCriterio.getText().trim());
        
        if(!txtCriterio.getText().equals("")){
            CriterioFiltro criterio = new CriterioFiltro();

            criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
            criterio.setCriterio(txtCriterio.getText());

            if("int".equals(criterio.getCampo().getTipoColuna())){
                try{
                    Integer.parseInt(criterio.getCriterio());
                    carregarTabela(new UsuarioBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new UsuarioBLL().filtrar(criterio));
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btFiltrarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        
        carregarTabela(new UsuarioBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void tbUsuariosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuariosMousePressed
        
        if(tbUsuarios.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
            sp = situacaoPessoa.trancado;
            controlarBotoesP();
        }
        
    }//GEN-LAST:event_tbUsuariosMousePressed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        UsuarioEntity usuariol = new UsuarioBLL().pesquisar(Login.getLogin());
        if(usuariol.getGrupo().getNome().equals("Controlador mestre") || (usuariol.getGrupo().getNome().equals("Administradores") && (!objetoUsuario.getGrupo().getNome().equals("Administradores") && !objetoUsuario.getGrupo().getNome().equals("Controlador mestre")))){
            boolean verificar = false;
            List<OperacaoEntity> resp = new OperacaoBLL().pesquisaPersonalizada("idusuario = "+objetoUsuario.getId());
            if(resp != null && !resp.isEmpty()){
                verificar = true;
            }

            int temp = JOptionPane.YES_OPTION;

            if(verificar == false){
                temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }else{
                temp = JOptionPane.showConfirmDialog(null,"Há históricos de vendas e/ou compras associadas a este usuário.\n"
                        + "Deseja mesmo exclui-lo??", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }

            if (temp == JOptionPane.YES_OPTION) {

                if(resp != null && !resp.isEmpty()){
                    for(int x = 0 ; x < resp.size() ; x++){
                        new OperacaoBLL().excluir(resp.get(x));
                    }
                }

                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                ul.setDescricao("Excluiu o usuário "+objetoUsuario.getEmail()+" de ID = "+objetoUsuario.getId()+" da pessoa de nome = "+objetoUsuario.getPessoa().getNome());
                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                ul.setHorario(DataUtility.getDateTime("EN"));
                new UsuarioAcaoBLL().salvar(ul);

                UsuarioAcaoBLL cuaBLL = new UsuarioAcaoBLL();
                cuaBLL.exclusaoPersonalizada("usuario = "+objetoUsuario.getId());
                controleUsuario.excluir(objetoUsuario);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
                sp = situacaoPessoa.trancado;
                controlarBotoesP();

            }
        }else{
            JOptionPane.showMessageDialog(null, "Você não têm permissão para excluír esse usuário", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBusActionPerformed
       
        if(modelTpp.getSelectedItem() != null){
            if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
                tbPessoas.setModel(modelPf);
                String sql = "SELECT * FROM pessoafisicas, pessoas WHERE pessoafisicas.pessoa = pessoas.id  AND pessoafisicas.pessoa not in (select pessoa from usuarios) AND pessoafisicas.pessoa > 0;";
                List<PessoaFisicaEntity> list = new PessoaFisicaBLL().sqlDeConsultaPersonalizada(sql);
                if(list != null && !list.isEmpty()){
                    modelPf.Recarregar(list);
                    preencherFiltroCamposF();
                    sp = situacaoPessoa.escolher;
                    controlarBotoesP();
                }else{
                    JOptionPane.showMessageDialog(null, "Todas as pessoas físicas já são usários.\n"
                            + "Ou não há pessoas físicas cadastradas no sistema.", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            }else{
                tbPessoas.setModel(modelPj);
                String sql = "SELECT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa AND pessoajuridicas.pessoa not in (select pessoa from usuarios) AND pessoajuridicas.pessoa > 0;";
                List<PessoaJuridicaEntity> list = new PessoaJuridicaBLL().sqlDeConsultaPersonalizada(sql);
                if(list != null && !list.isEmpty()){
                    modelPj.Recarregar(list);
                    preencherFiltroCamposJ();
                    sp = situacaoPessoa.escolher;
                    controlarBotoesP();
                }else{
                    JOptionPane.showMessageDialog(null, "Todas as pessoas jurídicas já são usários.\n"
                            + "Ou não há pessoas jurídicas cadastradas no sistema.", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Selecione um tipo de pesssoa.", "ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btBusActionPerformed

    private void btSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelActionPerformed
        
        if(tbPessoas.getSelectedRow() != -1){
            if(situacao == situacaoCadastro.scInserindo){
                objetoUsuario = new UsuarioEntity();
            }

            if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
                objetoUsuario.setPessoa(modelPf.getObjeto(tbPessoas.getSelectedRow()));
                List<PessoaFisicaEntity> one = new ArrayList<>();
                one.add(modelPf.getObjeto(tbPessoas.getSelectedRow()));
                modelPf.Recarregar(one);
            }else{
                objetoUsuario.setPessoa(modelPj.getObjeto(tbPessoas.getSelectedRow()));
                List<PessoaJuridicaEntity> one = new ArrayList<>();
                one.add(modelPj.getObjeto(tbPessoas.getSelectedRow()));
                modelPj.Recarregar(one);
            }

            sp = situacaoPessoa.escolhido;
            controlarBotoesP();
        }else{
            JOptionPane.showMessageDialog(null, "Selecione uma pesssoa para o usuário.", "ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSelActionPerformed

    private void btDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDesActionPerformed
       
        modelPj.Recarregar(null);
        modelPf.Recarregar(null);
        if(objetoUsuario.getPessoa() != null){
            objetoUsuario.setPessoa(null);
        }
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        
    }//GEN-LAST:event_btDesActionPerformed

    private void btCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCanActionPerformed
        
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        
    }//GEN-LAST:event_btCanActionPerformed

    private void cbxCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCamActionPerformed
        
    }//GEN-LAST:event_cbxCamActionPerformed

    private void btLisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLisActionPerformed
        
        if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
            String sql = "SELECT DISTINCT * FROM pessoafisicas, pessoas WHERE pessoafisicas.pessoa = pessoas.id AND pessoafisicas.pessoa not in (select pessoa from usuarios);";
            modelPf.Recarregar(new PessoaFisicaBLL().sqlDeConsultaPersonalizada(sql));
        }else{
            String sql = "SELECT DISTINCT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa AND pessoajuridicas.pessoa not in (select pessoa from usuarios);";
            List<PessoaJuridicaEntity> list = new PessoaJuridicaBLL().sqlDeConsultaPersonalizada(sql);
            modelPj.Recarregar(list);
            preencherFiltroCamposJ();
        }
        
        controlarBotoesP();
        
    }//GEN-LAST:event_btLisActionPerformed

    private void btPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesActionPerformed
        
        if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
            String sql = "SELECT DISTINCT * FROM pessoafisicas, pessoas WHERE pessoas.id = pessoafisicas.pessoa AND pessoafisicas.pessoa not in (select pessoa from usuarios)";
            txtCri.setText(txtCri.getText().trim());
            
            if(!txtCri.getText().equals("")){
                CriterioFiltro criterio = new CriterioFiltro();

                criterio.setCampo((CampoFiltro) cbxCam.getSelectedItem());
                criterio.setCriterio(txtCri.getText());

                if("int".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        Integer.parseInt(criterio.getCriterio());
                        modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if("string".equals(criterio.getCampo().getTipoColuna())){
                        modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
                    }else{
                        if("date".equals(criterio.getCampo().getTipoColuna())){
                            try{
                                criterio.setCriterio(criterio.getCriterio().replace("-", "/"));
                                criterio.setCriterio(criterio.getCriterio().replace("\\", "/"));
                                criterio.setCriterio(criterio.getCriterio().replace(" ", "/"));
                                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                                java.util.Date data = formatoData.parse(criterio.getCriterio());
                                java.sql.Date dataenvia = new java.sql.Date(data.getTime());
                                criterio.setCriterio(String.valueOf(dataenvia));
                                modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
                            }
                            catch(ParseException e){
                                JOptionPane.showMessageDialog(null,"Data inválida!!!","ERRO",JOptionPane.ERROR_MESSAGE);
                                txtCri.requestFocus();
                            }
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            String sql = "SELECT DISTINCT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa AND pessoajuridicas.pessoa not in (select pessoa from usuarios)";
            txtCri.setText(txtCri.getText().trim());

            if(!txtCri.getText().equals("")){
                CriterioFiltro criterio = new CriterioFiltro();

                criterio.setCampo((CampoFiltro) cbxCam.getSelectedItem());
                criterio.setCriterio(txtCri.getText());

                if("int".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        Integer.parseInt(criterio.getCriterio());
                        modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if("string".equals(criterio.getCampo().getTipoColuna())){
                        modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
                    }else{
                        if("date".equals(criterio.getCampo().getTipoColuna())){
                            try{
                                criterio.setCriterio(criterio.getCriterio().replace("-", "/"));
                                criterio.setCriterio(criterio.getCriterio().replace("\\", "/"));
                                criterio.setCriterio(criterio.getCriterio().replace(" ", "/"));
                                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                                java.util.Date data = formatoData.parse(criterio.getCriterio());
                                java.sql.Date dataenvia = new java.sql.Date(data.getTime());
                                criterio.setCriterio(String.valueOf(dataenvia));
                                modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
                            }
                            catch(ParseException e){
                                JOptionPane.showMessageDialog(null,"Data inválida!!!","ERRO",JOptionPane.ERROR_MESSAGE);
                                txtCriterio.requestFocus();
                            }
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
        
        controlarBotoesP();
        
    }//GEN-LAST:event_btPesActionPerformed

    private void cbxCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCamposActionPerformed
        
    }//GEN-LAST:event_cbxCamposActionPerformed

    private void jScrollPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MousePressed
       
    }//GEN-LAST:event_jScrollPane1MousePressed

    private void tbPessoasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPessoasMousePressed
        
        controlarBotoesP();
        
    }//GEN-LAST:event_tbPessoasMousePressed

    private void tbUsuariosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbUsuariosKeyReleased
        
        if(tbUsuarios.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
            sp = situacaoPessoa.trancado;
            controlarBotoesP();
        }
        
    }//GEN-LAST:event_tbUsuariosKeyReleased

    private void tbPessoasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPessoasKeyReleased
       
        controlarBotoesP();
        
    }//GEN-LAST:event_tbPessoasKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBus;
    private javax.swing.JButton btCan;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btDes;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btLis;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btPes;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbxCam;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxGrupos;
    private javax.swing.JComboBox cbxTp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbGrupos;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JRadioButton rbAtivado;
    private javax.swing.JRadioButton rbDesativado;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbPessoas;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtCri;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables

}
