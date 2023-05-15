package ui;


import components.HighlightableTextArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controller.App;
import tfidf.CrawledPage;
import utils.Prepravka;
import utils.Processor;

import java.io.*;
import java.util.*;
import java.util.concurrent.Flow;

/**
 * Hlavní třída aplikace.
 */
public class MainWindow extends Application {

    ArrayList<Prepravka> processQuery;

    Processor proc = new Processor();

    /** GUI prvky. */
    @FXML
    private ListView<String> listDocuments;

    @FXML
    private Button btnSearch;

    @FXML
    private Label scoreLBL;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private FlowPane titleLBL;

    @FXML
    private TextArea bodyTA;

    @FXML
    private FlowPane bodyLBL;
    //private HighlightableTextArea bodyTA;

    @FXML
    private Hyperlink link;

    /**
     * Main
     * @param args - Vstupní parametry.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start
     * @param stage - Hlavní okno aplikace
     * @throws Exception - Exception.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("MainWindow.fxml"));
        stage.setTitle("IR Semestral Work - Lehecka");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Inicializace dat pro GUI prvky a Indexer.
     */
    @FXML
    public void initialize() {
        try {
            proc.init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * Click event pro tlačítko "Vyhledat".
     * @param actionEvent - Event.
     */
    @FXML
    public void search(ActionEvent actionEvent) {
        listDocuments.getItems().clear();

        String query = txtFieldSearch.getText();
        System.out.println(query);

        processQuery = proc.processQuery(query);

        for(Prepravka prep: processQuery){
            System.out.println(prep.getKey() + " = " + prep.getVal() + " : " + prep.getEl().getTitle());
            listDocuments.getItems().add(String.valueOf(prep.getKey()));
        }
    }

    /**
     * Zobrazí dokument dle kliknutí v ListView
     * @param mouse - Event.
     */
    @FXML
    public void displayDocumentInfo(MouseEvent mouse) {
        String item = listDocuments.getSelectionModel().getSelectedItem();

        ArrayList<CrawledPage> list = proc.getEl();

        // Kontrola uživatelského kliku
        if (item == null) {
            // klikl mimo
            mouse.consume();
        }
        else {
            System.out.println(item);
            CrawledPage cl = list.get(Integer.valueOf(item) - 1);

            String query = txtFieldSearch.getText();

            titleLBL = createTextFlow(titleLBL, cl.getTitle(), query);

            String content = cl.getBody().replaceAll("\\s+"," ");
            bodyTA.setText(content);
            bodyTA.setWrapText(true);
            bodyTA.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick; -fx-font-size: 20px;");
            //bodyTA.highlight(13, 24);

            bodyLBL = createTextFlow(bodyLBL, cl.getBody(), query);


            Prepravka prep = processQuery.get(listDocuments.getSelectionModel().getSelectedIndex());
            scoreLBL.setText(prep.getVal());

            link.setTooltip(new Tooltip(cl.getUrl()));
            link.setText(cl.getUrl());
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    Hyperlink h = (Hyperlink) t.getTarget();
                    String s = h.getTooltip().getText();
                    getHostServices().showDocument(s);
                    t.consume();
                }
            });
        }
    }

    private FlowPane createTextFlow(FlowPane flow, String msg, String term) {
        boolean isColored = false;

        flow.getChildren().clear();

        String[] arrOfStr = msg.split(" ", 0);
        for (String s : arrOfStr) {
            System.out.print(s.equals(term));
            System.out.println(s);
            if (s.toLowerCase().equals(term.toLowerCase())) {
                isColored = true;
            }

            Text text = new Text(s);
            if (isColored) {
                text.setFill(Color.RED);
                text.setStyle("-fx-font-size: 25;");
                System.out.println("set " + s + " coloured");
                isColored = false;
            }
            else
            {
                text.setStyle("-fx-font-size: 25;");
                text.setFill(Color.BLACK);
            }

            flow.getChildren().add(text);
        }

        return flow;
    }
}
