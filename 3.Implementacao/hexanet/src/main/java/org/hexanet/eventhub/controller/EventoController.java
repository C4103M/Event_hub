package org.hexanet.eventhub.controller;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.TipoIngresso;
import org.hexanet.eventhub.model.enums.StatusEvento;
import org.hexanet.eventhub.service.EventoService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class EventoController implements Initializable {

    @FXML
    private TableView<Evento> tblEventos;
    @FXML
    private TableColumn<Evento, String> colNome;
    @FXML
    private TableColumn<Evento, String> colData;
    @FXML
    private TableColumn<Evento, String> colHorario;
    @FXML
    private TableColumn<Evento, String> colLocal;
    @FXML
    private TableColumn<Evento, Integer> colCapacidade;
    @FXML
    private TableColumn<Evento, Void> colAcoes;
    @FXML
    private TableColumn<Evento, String> colBanner;
    @FXML
    private TableColumn<Evento, Double> colPreco;
    @FXML
    private TableColumn<Evento, StatusEvento> colStatus;
    @FXML
    private Label lblNomeOrganizador;
    @FXML
    private ComboBox<String> cbxBannerEvento;
    @FXML
    private Label lblTituloForm;

    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfLocal;
    @FXML
    private TextField tfCapacidade;
    @FXML
    private DatePicker dpData;
    @FXML
    private TextField tfHorario;
    @FXML
    private ComboBox<StatusEvento> cbStatus;

    private Evento eventoEmEdicao = null;
    @FXML
    private Label lblImagemSelecionada;

    @FXML
    private VBox vboxTiposIngresso;

    private File imagemSelecionada;
    private EventoService eventoService = new EventoService();

    private class LinhaIngresso {
        TextField tfNomeTipo;
        TextField tfPreco;
        TextField tfQtd;
        HBox container;
        Button btnRemover;

        public LinhaIngresso(TextField tfNomeTipo, TextField tfPreco, TextField tfQtd, HBox container, Button btnRemover) {
            this.tfNomeTipo = tfNomeTipo;
            this.tfPreco = tfPreco;
            this.tfQtd = tfQtd;
            this.container = container;
            this.btnRemover = btnRemover;
        }
    }
    private List<LinhaIngresso> listaCamposIngresso = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (cbxBannerEvento != null) {
            carregarImagensDaPasta();
        }

        if (tblEventos != null) {
            configurarColunas();
            carregarEventos();
        }

        if (lblNomeOrganizador != null && org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().isLogado()) {
            lblNomeOrganizador.setText(org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().getUsuarioLogado().getNome());
        }

        // AGORA SEGURO: Tudo que é do formulário envelopado aqui
        if (tfCapacidade != null) {
            tfCapacidade.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    tfCapacidade.setText(newVal.replaceAll("[^\\d]", ""));
                }
            });

            if (dpData != null) {
                dpData.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(java.time.LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        java.time.LocalDate today = java.time.LocalDate.now();
                        setDisable(empty || date.compareTo(today) < 0);
                    }
                });
            }


            cbStatus.getItems().clear();
            cbStatus.getItems().addAll(StatusEvento.ABERTO, StatusEvento.RASCUNHO);
            
            if(vboxTiposIngresso != null){
                vboxTiposIngresso.getChildren().clear();
                listaCamposIngresso.clear();
                adicionarLinhaIngresso();
            }
        }
    }


    @FXML
    public void adicionarLinhaIngresso() {
        HBox hbox = new HBox(12);
        hbox.setAlignment(Pos.BOTTOM_LEFT);
        hbox.setStyle("-fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-padding: 12; -fx-background-color: #F9FAFB;");
        
        VBox boxNome = new VBox(5);
        Label lblNome = new Label("Tipo:");
        lblNome.setStyle("-fx-text-fill: #4B5563; -fx-font-size: 13px; -fx-font-weight: bold;");
        TextField tfNomeTipo = new TextField();
        tfNomeTipo.setPromptText("Ex: Vip");
        tfNomeTipo.setPrefWidth(160);
        tfNomeTipo.setMinWidth(120);
        boxNome.getChildren().addAll(lblNome, tfNomeTipo);
        HBox.setHgrow(boxNome, Priority.ALWAYS); // Permite que o campo de nome cresça
        
        VBox boxPreco = new VBox(5);
        Label lblPreco = new Label("Valor:");
        lblPreco.setStyle("-fx-text-fill: #4B5563; -fx-font-size: 13px; -fx-font-weight: bold;");
        TextField tfPreco = new TextField();
        tfPreco.setPromptText("0,00");
        tfPreco.setPrefWidth(90);
        tfPreco.setMinWidth(70);
        
        // Permite só números e ponto/vírgula no preço
        tfPreco.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*([\\.,]\\d*)?")) tfPreco.setText(oldVal);
        });
        boxPreco.getChildren().addAll(lblPreco, tfPreco);

        VBox boxQtd = new VBox(5);
        Label lblQtd = new Label("Qtd:");
        lblQtd.setStyle("-fx-text-fill: #4B5563; -fx-font-size: 13px; -fx-font-weight: bold;");
        TextField tfQtd = new TextField();
        tfQtd.setPromptText("0");
        tfQtd.setPrefWidth(70);
        tfQtd.setMinWidth(50);
        tfQtd.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) tfQtd.setText(newVal.replaceAll("[^\\d]", ""));
        });
        boxQtd.getChildren().addAll(lblQtd, tfQtd);

        Button btnRemover = new Button("X");
        btnRemover.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6;");
        
        hbox.getChildren().addAll(boxNome, boxPreco, boxQtd, btnRemover);
        LinhaIngresso linha = new LinhaIngresso(tfNomeTipo, tfPreco, tfQtd, hbox, btnRemover);
        listaCamposIngresso.add(linha);
        btnRemover.setOnAction(e -> {
            vboxTiposIngresso.getChildren().remove(hbox);
            listaCamposIngresso.remove(linha);
            atualizarBotoesRemover();
        });
        vboxTiposIngresso.getChildren().add(hbox);
        atualizarBotoesRemover();
    }

    private void atualizarBotoesRemover() {
        boolean isUnico = listaCamposIngresso.size() <= 1;
        for (LinhaIngresso l : listaCamposIngresso) {
            l.btnRemover.setDisable(isUnico);
        }
    }


    private void configurarColunas() {
        if (colNome != null) colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        if (colData != null) colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        if (colHorario != null) colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        if (colLocal != null) colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        if (colCapacidade != null) colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadeTotal"));
        if (colPreco != null) {
            colPreco.setCellValueFactory(cellData -> {
                double minVal = 0.0;
                if (cellData.getValue() != null) {
                    List<TipoIngresso> t = cellData.getValue().getTiposIngresso();
                    if (t != null && !t.isEmpty()) {
                        minVal = t.stream()
                                  .filter(ti -> ti != null)
                                  .mapToDouble(TipoIngresso::getPreco)
                                  .min()
                                  .orElse(0.0);
                    }
                }
                return new javafx.beans.property.SimpleDoubleProperty(minVal).asObject();
            });

            colPreco.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double minPreco, boolean empty) {
                    super.updateItem(minPreco, empty);
                    if (empty || minPreco == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Evento ev = getTableView().getItems().get(getIndex());
                        List<TipoIngresso> t = ev.getTiposIngresso();
                        
                        String textoPreco;
                        if (t == null || t.isEmpty()) {
                            textoPreco = "Grátis";
                        } else if (t.size() == 1) {
                            textoPreco = String.format("%.2f", t.get(0).getPreco());
                        } else {
                            double maxPreco = t.stream().mapToDouble(TipoIngresso::getPreco).max().orElse(0.0);
                            textoPreco = String.format("%.2f - %.2f", minPreco, maxPreco);
                        }
                        
                        Label label = new Label(textoPreco);
                        label.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold; -fx-cursor: hand;");
                        
                        label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: #059669; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"));
                        label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold; -fx-underline: false;"));
                        label.setOnMouseClicked(e -> mostrarPopUpIngressos(ev));
                        
                        setGraphic(label);
                    }
                }
            });
        }
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("statusEvento"));

        if (colBanner != null) configurarColunaBanner();
        if (colAcoes != null) configurarColunaAcoes();
    }

    private void configurarColunaBanner() {
        colBanner.setCellFactory(col -> new TableCell<>() {
            private final ImageView imgView = new ImageView();
            {
                imgView.setFitWidth(70);
                imgView.setFitHeight(45);
                imgView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String nomeArquivo, boolean empty) {
                super.updateItem(nomeArquivo, empty);
                if (empty || nomeArquivo == null) {
                    setGraphic(null);
                } else {
                    String cleanPath = nomeArquivo;
                    if (cleanPath.startsWith("assets/images/")) {
                        cleanPath = cleanPath.substring("assets/images/".length());
                    } else if (cleanPath.startsWith("assets/")) {
                        cleanPath = cleanPath.substring("assets/".length());
                    }

                    URL url = getClass().getResource("/org/hexanet/eventhub/assets/" + cleanPath);
                    if (url == null) {
                        url = getClass().getResource("/assets/" + cleanPath);
                    }
                    if (url == null) {
                        url = getClass().getResource(nomeArquivo.startsWith("/") ? nomeArquivo : "/" + nomeArquivo);
                    }

                    if (url != null) {
                        imgView.setImage(new Image(url.toExternalForm()));
                        setGraphic(imgView);
                    } else {
                        setGraphic(null); // Se não achar a imagem, deixa vazio
                    }
                }
            }
        });
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox box = new HBox(6, btnEditar, btnExcluir);

            {
                // estilo botão Editar
                btnEditar.setStyle(
                        "-fx-background-color: #1a1a2e; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 4 10;");

                // estilo botão Excluir
                btnExcluir.setStyle(
                        "-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 4 10;");

                btnEditar.setOnAction(e -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    editarEvento(evento);
                });

                btnExcluir.setOnAction(e -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    excluirEvento(evento);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void carregarEventos() {
        if (tblEventos != null) {
            List<Evento> eventos = eventoService.listarTodos();
            tblEventos.setItems(FXCollections.observableArrayList(eventos));
        }
    }

    private void mostrarPopUpIngressos(Evento evento) {
        if (evento == null || evento.getTiposIngresso() == null || evento.getTiposIngresso().isEmpty()) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setTitle("Ingressos - " + evento.getNome());
        dialog.setResizable(false);
        
        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 20; -fx-alignment: center;");
        
        Label title = new Label("Ingressos Cadastrados");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");
        
        VBox listContainer = new VBox(10);
        listContainer.setStyle("-fx-alignment: top-left; -fx-fill-width: true; -fx-background-color: #FFFFFF;");
        
        for (TipoIngresso ti : evento.getTiposIngresso()) {
            HBox item = new HBox(12);
            item.setStyle("-fx-background-color: #F3F4F6; -fx-padding: 10 15; -fx-background-radius: 8; -fx-alignment: center-left;");
            
            VBox details = new VBox(3);
            Label name = new Label(ti.getNome());
            name.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151; -fx-font-size: 13px;");
            Label qtd = new Label("Disponível: " + ti.getQtdDisponiveis() + " un.");
            qtd.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");
            details.getChildren().addAll(name, qtd);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Label price = new Label(String.format("R$ %.2f", ti.getPreco()));
            price.setStyle("-fx-font-weight: bold; -fx-text-fill: #10B981; -fx-font-size: 14px;");
            
            item.getChildren().addAll(details, spacer, price);
            listContainer.getChildren().add(item);
        }
        
        // ScrollPane elegante caso haja muitos tipos de ingresso
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(240);
        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-background-color: #FFFFFF; -fx-border-color: transparent;");
        
        Button btnFechar = new Button("Fechar");
        btnFechar.setStyle("-fx-background-color: #F97316; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 24; -fx-background-radius: 6; -fx-font-size: 13px;");
        btnFechar.setOnAction(e -> dialog.close());
        
        vbox.getChildren().addAll(title, scrollPane, btnFechar);
        
        // Define o tamanho da Scene explicitamente para 480x380 px para evitar truncamento no Linux
        Scene scene = new Scene(vbox, 480, 380);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void editarEvento(Evento evento) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/hexanet/eventhub/eventos/FormularioEvento.fxml"));
            javafx.scene.Parent root = fxmlLoader.load();
            
            EventoController controller = fxmlLoader.getController();
            controller.preencherFormulario(evento);

            Stage stage = (Stage) tblEventos.getScene().getWindow();
            stage.setTitle("Editar Evento");
            stage.getScene().setRoot(root);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void excluirEvento(Evento evento) {
        // Lógica de exclusão
    }

    @FXML
    public void excluirEvento() {
        if (tblEventos == null) return;
        Evento selecionado = tblEventos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                eventoService.excluirEvento(selecionado.getId());
                carregarEventos();
            } catch (Exception e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao excluir evento");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum evento selecionado");
            alert.setContentText("Por favor, selecione um evento na tabela.");
            alert.showAndWait();
        }
    }

    @FXML
    public void editarEvento() {
        if (tblEventos == null) return;
        Evento selecionado = tblEventos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            editarEvento(selecionado);
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum evento selecionado");
            alert.setContentText("Por favor, selecione um evento na tabela.");
            alert.showAndWait();
        }
    }

    @FXML
    public void novoEvento() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/hexanet/eventhub/eventos/FormularioEvento.fxml"));
            javafx.scene.Parent root = fxmlLoader.load();
            Stage stage = (Stage) tblEventos.getScene().getWindow();
            stage.setTitle("Cadastrar Evento");
            stage.getScene().setRoot(root);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sair() {
        if (tblEventos != null && tblEventos.getScene() != null) {
            Stage stage = (Stage) tblEventos.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void abrirFormulario() {
    }

    @FXML
    public void selecionarImagem() {
        if (tfNome == null || tfNome.getScene() == null) return;
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione a Imagem do Evento");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        
        
        Stage stage = (Stage) tfNome.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            this.imagemSelecionada = file;
            lblImagemSelecionada.setText(file.getName());
        }
    }
    
    @FXML
    public void cadastrarEvento() {
        try{
            java.time.LocalDate data = dpData.getValue();
            if (data == null) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Data Vazia");
                alert.setContentText("Por favor, selecione uma data para o evento.");
                alert.showAndWait();
                return;
            }

            String horaStr = tfHorario.getText().trim();
            if (horaStr.isEmpty()) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Horário Vazio");
                alert.setContentText("Por favor, digite um horário para o evento (ex: 20:30).");
                alert.showAndWait();
                return;
            }

            java.time.LocalTime hora;
            try {
                hora = java.time.LocalTime.parse(horaStr);
            } catch (Exception e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Horário Inválido");
                alert.setContentText("Por favor, use o formato de horário HH:mm (ex: 20:30).");
                alert.showAndWait();
                return;
            }

            java.time.LocalDateTime dataHora = java.time.LocalDateTime.of(data, hora);
            if (dataHora.isBefore(java.time.LocalDateTime.now())) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Data e Hora no Passado");
                alert.setContentText("A data e hora do evento não podem ser no passado.");
                alert.showAndWait();
                return;
            }
            
            int capacidade = Integer.parseInt(tfCapacidade.getText());
            StatusEvento status = cbStatus.getValue();

            Evento evento = new Evento(
                tfNome.getText(),
                tfLocal.getText(),
                capacidade,
                dataHora,
                status
            );
            
            List<TipoIngresso> tipos = new ArrayList<>();
            int qtdTotalIngressos = 0;
            
            for (LinhaIngresso linha : listaCamposIngresso){
                String nomeTipo = linha.tfNomeTipo.getText();
                String precoStr = linha.tfPreco.getText().replace(",",".");
                String qtdStr = linha.tfQtd.getText();

                if (nomeTipo.trim().isEmpty() || precoStr.trim().isEmpty() || qtdStr.trim().isEmpty()) {
                    continue; 
                }
                
                double preco = Double.parseDouble(precoStr);
                int qtd = Integer.parseInt(qtdStr);
                qtdTotalIngressos += qtd;

                TipoIngresso tipo = new TipoIngresso(nomeTipo, preco, qtd, evento);
                tipos.add(tipo);
            }
            
            if (qtdTotalIngressos > capacidade) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Capacidade Excedida");
                alert.setContentText("A soma dos ingressos (" + qtdTotalIngressos + ") excede a capacidade do evento (" + capacidade + ").");
                alert.showAndWait();
                return;
            }
            
            evento.setTiposIngresso(tipos);
            

            if (eventoEmEdicao == null) {
                eventoService.cadastrarEvento(evento, imagemSelecionada);
                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sucesso");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Evento cadastrado com sucesso!");
                successAlert.showAndWait();
            } else {
                evento.setId(eventoEmEdicao.getId());
                eventoService.atualizarEvento(evento, imagemSelecionada);
                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sucesso");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Evento atualizado com sucesso!");
                successAlert.showAndWait();
            }

            if (tfNome != null && tfNome.getScene() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/hexanet/eventhub/eventos/GerenciarEventos.fxml"));
                javafx.scene.Parent root = fxmlLoader.load();
                Stage stage = (Stage) tfNome.getScene().getWindow();
                stage.setTitle("EventHub - Gerenciar Eventos");
                stage.getScene().setRoot(root);
            }
        }catch (Exception e) {
            System.err.println("Erro ao cadastrar evento: " + e.getMessage());
            e.printStackTrace();
            javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            errorAlert.setTitle("Erro");
            errorAlert.setHeaderText("Erro ao salvar o evento");
            errorAlert.setContentText("Ocorreu um erro ao salvar o evento: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void cancelar() {
        try {
            if (tfNome != null && tfNome.getScene() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/hexanet/eventhub/eventos/GerenciarEventos.fxml"));
                javafx.scene.Parent root = fxmlLoader.load();
                Stage stage = (Stage) tfNome.getScene().getWindow();
                stage.setTitle("EventHub - Gerenciar Eventos");
                stage.getScene().setRoot(root);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarImagensDaPasta() {
        File pastaAssets = new File("src/main/resources/assets");

        if (pastaAssets.exists() && pastaAssets.isDirectory()) {
            File[] arquivos = pastaAssets.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") ||
                    name.toLowerCase().endsWith(".jpg") ||
                    name.toLowerCase().endsWith(".jpeg"));

            if (arquivos != null && cbxBannerEvento != null) {
                cbxBannerEvento.getItems().clear(); // Limpa antes de carregar
                for (File arquivo : arquivos) {
                    cbxBannerEvento.getItems().add(arquivo.getName());
                }
            }
        } else {
            System.out.println("Aviso: Pasta assets não encontrada.");
        }
    }

    public void preencherFormulario(Evento evento) {
        this.eventoEmEdicao = evento;
        
        if (lblTituloForm != null) {
            lblTituloForm.setText("Editar Evento");
        }
        
        tfNome.setText(evento.getNome());
        tfLocal.setText(evento.getLocal());
        tfCapacidade.setText(String.valueOf(evento.getCapacidadeTotal()));
        
        if (evento.getDataHora() != null) {
            dpData.setValue(evento.getDataHora().toLocalDate());
            tfHorario.setText(evento.getDataHora().toLocalTime().toString());
        }
        
        cbStatus.setValue(evento.getStatusEvento());
        
        if (evento.getEventoImg() != null) {
            lblImagemSelecionada.setText("Imagem carregada: " + evento.getEventoImg());
        }

        vboxTiposIngresso.getChildren().clear();
        listaCamposIngresso.clear();
        
        if (evento.getTiposIngresso() != null && !evento.getTiposIngresso().isEmpty()) {
            for (TipoIngresso ti : evento.getTiposIngresso()) {
                adicionarLinhaIngresso();
                LinhaIngresso linha = listaCamposIngresso.get(listaCamposIngresso.size() - 1);
                linha.tfNomeTipo.setText(ti.getNome());
                linha.tfPreco.setText(String.valueOf(ti.getPreco()).replace(".", ","));
                linha.tfQtd.setText(String.valueOf(ti.getQtdDisponiveis()));
            }
        } else {
            adicionarLinhaIngresso();
        }
    }
}