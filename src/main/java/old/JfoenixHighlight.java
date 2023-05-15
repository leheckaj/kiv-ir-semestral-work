package old;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class JfoenixHighlight extends Application {
    private static final String[] fruits = {"apple"};
    private static final String[] fruitImageLocs = {
            "http://weknowyourdreamz.com/images/apple/apple-02.jpg",
            "http://pic.1fotonin.com/data/wallpapers/165/WDF_2048871.png",
            "http://vignette1.wikia.nocookie.net/pikmin/images/c/cc/Pear-01.jpg"
    };
    private Map<String, Image> fruitImages = new HashMap<>();

    public static void main(String[] args) {
        Application.launch(JfoenixHighlight.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Fruit Tales");

        for (int i = 0; i < fruits.length; i++) {
            Image image = new Image(fruitImageLocs[i], 0, 30, true, true);
            fruitImages.put(fruits[i], image);
        }

        ListView<String> list = new ListView<>(FXCollections.observableArrayList(fruits));
        list.setCellFactory(listView -> new FruitFlowCell());
        list.setPrefSize(440, 180);

        Scene scene = new Scene(list);
        //scene.getStylesheets().add("/fruit.css");
        stage.setScene(scene);
        stage.show();
    }

    private String getResource(String resourceName) {
        return getClass().getResource(resourceName).toExternalForm();
    }

    private class FruitFlowCell extends ListCell<String> {
        static final String FRUIT_PLACEHOLDER = "%f";

        {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(String s, boolean empty) {
            super.updateItem(s, empty);
            if (s != null && !"".equals(s) && !isEmpty()) {

                //Node node = createTextFlow("Eat ", FRUIT_PLACEHOLDER, s, " a day.");
                Node node = createTextFlow("Eat apple a day.", "apple");
                setGraphic(node);
            } else {
                setGraphic(null);
            }
        }

        private Node createTextFlow(String msg, String term) {
            FlowPane flow = new FlowPane();
            boolean isColored = false;

            String[] arrOfStr = msg.split(" ", 0);
            for (String s : arrOfStr) {
                System.out.print(s.equals(term));
                System.out.println(s);
                if (s.equals(term)) {
                    isColored = true;
                }

                Text text = new Text(s);
                if (isColored) {
                    text.setFill(Color.RED);
                    text.setStyle("-fx-font-size: 45;");
                    System.out.println("set " + s + " coloured");
                    isColored = false;
                }

                flow.getChildren().add(text);
            }

            return flow;
        }

        private Node createSpacer(int width) {
            HBox spacer = new HBox();
            spacer.setMinWidth(HBox.USE_PREF_SIZE);
            spacer.setPrefWidth(width);
            spacer.setMaxWidth(HBox.USE_PREF_SIZE);

            return spacer;
        }
    }
}
