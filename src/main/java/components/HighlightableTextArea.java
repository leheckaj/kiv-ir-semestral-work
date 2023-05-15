package components;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * Custom TextArea Component.
 */
public class HighlightableTextArea extends StackPane {
    final TextArea textArea = new TextArea();
    int highlightStartPos = -1;
    int highlightEndPos = -1;
    boolean highlightInProgress = false;

    final Rectangle highlight = new Rectangle();

    private StringProperty text = new SimpleStringProperty();

    private Group selectionGroup;

    public final String getText() {
        return text.get();
    }

    public final void setText(String value) {
        text.set(value);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public HighlightableTextArea() {
        highlight.setFill(Color.RED);
        highlight.setMouseTransparent(true);
        highlight.setBlendMode(BlendMode.DARKEN);

        textArea.textProperty().bindBidirectional(text);
        getChildren().add(textArea);
        setAlignment(Pos.TOP_LEFT);
        textArea.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (highlightStartPos > -1 && highlightEndPos > -1 && selectionGroup != null) {
                highlightInProgress = true;
                textArea.selectRange(highlightStartPos, highlightEndPos);
                Bounds bounds = selectionGroup.getBoundsInLocal();
                updateHightlightBounds(bounds);
            }
        });
        textArea.setWrapText(true);
        textArea.setEditable(false);
    }

    private void updateHightlightBounds(Bounds bounds) {
        if (bounds.getWidth() > 0) {
            if (!getChildren().contains(highlight)) {
                getChildren().add(highlight);
            }
            highlight.setTranslateX(bounds.getMinX() + 1);
            highlight.setTranslateY(bounds.getMinY() + 1);
            highlight.setWidth(bounds.getWidth());
            highlight.setHeight(bounds.getHeight());
            Platform.runLater(() -> {
                textArea.deselect();
                highlightInProgress = false;
            });
        }
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (selectionGroup == null) {
            final Region content = (Region) lookup(".content");
            // Looking for the Group node that is responsible for selection
            content.getChildrenUnmodifiable().stream().filter(node -> node instanceof Group).map(node -> (Group) node).filter(grp -> {
                boolean notSelectionGroup = grp.getChildren().stream().anyMatch(node -> !(node instanceof Path));
                return !notSelectionGroup;
            }).findFirst().ifPresent(n -> {
                n.boundsInLocalProperty().addListener((obs, old, bil) -> {
                    if (highlightInProgress) {
                        updateHightlightBounds(bil);
                    }
                });
                selectionGroup = n;
            });
        }
    }

    public void highlight(int startPos, int endPos) {
        highlightInProgress = true;
        highlightStartPos = startPos;
        highlightEndPos = endPos;
        textArea.selectRange(startPos, endPos);
    }

    public void removeHighlight() {
        textArea.deselect();
        getChildren().remove(highlight);
        highlightStartPos = -1;
        highlightEndPos = -1;
    }

}