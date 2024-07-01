package ann.main;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ann.neuralnetwork.Backup;
import ann.neuralnetwork.Network;

class NetworkPanel extends JPanel implements ActionListener
{
    private static final int IMAGE_WIDTH = 448;
    private static final int IMAGE_HEIGHT = 375;

    private BufferedImage image;
    private final JLabel imageLabel;
    private final JLabel responseLabel;
    private final JButton button;

    private final Random random;
    private Network network;

    public NetworkPanel()
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imageLabel = new JLabel("No image was loaded yet...");
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(imageLabel);

        responseLabel = new JLabel("(Here I will tell you whether the image is a cat or a dog.)");
        responseLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(responseLabel);

        button = new JButton("Next");
        button.setActionCommand("next");
        button.addActionListener(this);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setEnabled(false);
        add(button);

        random = new Random();
    }

    public void loadNetwork()
    {
        responseLabel.setText("The network is being loaded...");

        Backup backup = new Backup();
        if (backup.readFromFile("dogs-and-cats-net.txt"))
        {
            try
            {
                network = new Network(backup);
            }
            catch (Exception e)
            {
                responseLabel.setText(e.getMessage());
            }
        }

        responseLabel.setText("The network was loaded!");
        button.setEnabled(true);
    }

    public void loadImage()
    {
        String animal = random.nextBoolean() ? "Cats" : "Dogs";
        int number = random.nextInt(0, 999);
        String path = "C:/Users/Użytkownik/Pictures/Cats & Dogs/%s/%d.jpg"
                .formatted(animal, number);
        try
        {
            InputStream stream = Files.newInputStream(Paths.get(path));
            image = ImageIO.read(stream);
            if (image != null)
            {
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
            }
            else
            {
                imageLabel.setText("No image!");
            }
        }
        catch (IOException io)
        {

        }
    }

    private List<Double> getPixels()
    {
        List<Double> pixels = new ArrayList<>(IMAGE_WIDTH * IMAGE_HEIGHT);
        for (int i = 0; i < IMAGE_WIDTH; ++i)
        {
            for (int j = 0; j < IMAGE_HEIGHT; ++j)
            {
                int color = image.getRGB(i, j) & 0x00FFFFFF;
                double value = (double) color / (double) 0x00FFFFFF;
                pixels.add(value);
            }
        }
        return pixels;
    }

    public void decide()
    {
        try
        {
            List<Double> pixels = getPixels();
            List<Double> result = network.computeFor(pixels);

            assert (result.size() == 2);

            double voteForCat = result.get(0);
            double voteForDog = result.get(1);

            String response = "I think this is a %s (cat: %.3f, dog: %.3f)".formatted(
                    (voteForCat > voteForDog) ? "CAT" : "DOG",
                    voteForCat,
                    voteForDog
            );
            responseLabel.setText(response);
        }
        catch (Exception e)
        {
            responseLabel.setText(e.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("next"))
        {
            loadImage();
            decide();
        }
    }
}

/**
 *
 * @author Kay Jay O'Nail
 */
public class TestDogsAndCats
{
    public static void main(String... args)
    {
        try
        {
            JFrame frame = new JFrame("BIAI Project");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            NetworkPanel panel = new NetworkPanel();

            frame.setContentPane(panel);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.loadNetwork();
            panel.loadImage();
            panel.decide();
        }
        catch (HeadlessException e)
        {
            System.err.println(e);
        }
    }
}
