package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.hexanet.eventhub.model.Evento; // Garanta que o import da sua classe Evento está correto

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EventoController implements Initializable {

    @FXML private TableView<Evento> tblEventos;
    @FXML private TableColumn<Evento, String>  colNome;
    @FXML private TableColumn<Evento, String>  colData;
    @FXML private TableColumn<Evento, String>  colHorario;
    @FXML private TableColumn<Evento, String>  colLocal;
    @FXML private TableColumn<Evento, Integer> colCapacidade;
    @FXML private TableColumn<Evento, Void>    colAcoes;
    @FXML private TableColumn<Evento, String>  colBanner;
    @FXML private Label                        lblNomeOrganizador;
    @FXML private ComboBox<String>             cbxBannerEvento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Se a tela atual for a de cadastro/formulário, o cbxBannerEvento existirá
        if (cbxBannerEvento != null) {
            carregarImagensDaPasta();
        }

        // Se a tela atual tiver a tabela, configura as colunas e carrega os dados
        if (tblEventos != null) {
            configurarColunas();
            carregarEventos();
        }
    }

    // RESOLVIDO: Criado o método que junta todas as configurações da tabela
    private void configurarColunas() {
        // Vincula as propriedades da classe Evento com as colunas da TableView
        // O texto entre aspas DEVE ser igual ao nome da variável na sua classe Evento
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));

        // Chama as configurações customizadas de imagem e botões
        configurarColunaBanner();
        configurarColunaAcoes();
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
                    URL url = getClass().getResource("/assets/" + nomeArquivo);
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
            private final Button btnEditar  = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox   box        = new HBox(6, btnEditar, btnExcluir);

            {
                // estilo botão Editar
                btnEditar.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 4 10;");

                // estilo botão Excluir
                btnExcluir.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 4 10;");

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
        // Seu código para buscar do banco e fazer:
        // tblEventos.setItems(FXCollections.observableArrayList(eventos));
    }

    private void editarEvento(Evento evento) {
        // Lógica de edição
    }

    private void excluirEvento(Evento evento) {
        // Lógica de exclusão
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
    public void cadastrarEvento() {
    }

    @FXML
    public void cancelar() {
    }

    private void carregarImagensDaPasta() {
        File pastaAssets = new File("src/main/resources/assets");

        if (pastaAssets.exists() && pastaAssets.isDirectory()) {
            File[] arquivos = pastaAssets.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") ||
                            name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".jpeg")
            );

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
}