package components;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HighlightableTextAreaDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        Scene sc = new Scene(root, 600, 600);
        stage.setScene(sc);
        stage.show();


        final HighlightableTextArea highlightableTextArea = new HighlightableTextArea();
        highlightableTextArea.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        highlightableTextArea.getTextArea().setWrapText(true);
        highlightableTextArea.getTextArea().setStyle("-fx-font-size: 20px;");
        VBox.setVgrow(highlightableTextArea, Priority.ALWAYS);

        Button highlight = new Button("Highlight");
        TextField stF = new TextField("40");
        TextField enF = new TextField("50");
        HBox hb = new HBox(highlight, stF, enF);
        hb.setSpacing(10);
        highlight.setOnAction(e -> {
            highlightableTextArea.highlight(Integer.parseInt(stF.getText()), Integer.parseInt(enF.getText()));
        });

        Button remove = new Button("Remove Highlight");
        remove.setOnAction(e -> highlightableTextArea.removeHighlight());

        Label lbl = new Label("Resize the window to see if the highlight is moving with text");
        lbl.setStyle("-fx-font-size: 17px;-fx-font-style:italic;");
        HBox rb = new HBox(remove, lbl);
        rb.setSpacing(10);

        root.getChildren().addAll(hb, rb, highlightableTextArea);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}