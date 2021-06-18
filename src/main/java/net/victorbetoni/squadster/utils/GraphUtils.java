package net.victorbetoni.squadster.utils;

//import javafx.embed.swing.SwingFXUtils;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
/*import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;*/

public class GraphUtils {

    public static ImageView invertColor(Image img) {
        PixelReader reader = img.getPixelReader();
        int w = (int)img.getWidth();
        int h = (int)img.getHeight();
        WritableImage wImage = new WritableImage(w, h);
        PixelWriter writer = wImage.getPixelWriter();
        for(int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color color = reader.getColor(x, y);
                writer.setColor(x, y, color.invert());
            }
        }
        ImageView imageView = new ImageView();
        imageView.setImage(wImage);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static FillTransition createFillTransition(GridPane gridPane, Color from, Color to) {
        FillTransition transition = new FillTransition();
        Rectangle rectangle = new Rectangle();
        transition.setShape(rectangle);
        transition.setDuration(Duration.millis(500));
        rectangle.setFill(from);
        transition.setFromValue(from);
        transition.setToValue(to);

        transition.setInterpolator(new Interpolator() {
            protected double curve(double t) {
                gridPane.setBackground(new Background(new BackgroundFill(rectangle.getFill(), CornerRadii.EMPTY, Insets.EMPTY)));
                return t;
            }
        });
        return transition;
    }

    /*public static ImageView loadSVGImage(String dir) {
        ImageView imageView = new ImageView();
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        try (InputStream file = GraphUtils.class.getResourceAsStream("/" + dir)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                imageView.setImage(img);
                return imageView;
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }


    public static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage img = null;
        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        @Override
        public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
            this.img = img;
        }
        public BufferedImage getBufferedImage() {
            return img;
        }
    }*/

}
