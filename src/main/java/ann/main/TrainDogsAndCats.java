package ann.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import ann.neuralnetwork.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class TrainDogsAndCats
{
    public static final int WIDTH = 448;
    public static final int HEIGHT = 375;
    
    private static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
    
    public static List<Double> getPixels()
    {
        List<Double> pixels = new ArrayList<>(WIDTH * HEIGHT);
        for (int i = 0; i < WIDTH; ++i)
        {
            for (int j = 0; j < HEIGHT; ++j)
            {
                int color = image.getRGB(i, j) & 0x00FFFFFF;
                double value = (double) color / (double) 0x00FFFFFF;
                pixels.add(value);
            }
        }
        return pixels;
    }
    
    public static boolean loadImage(String path)
    {
        boolean success = true;
        try
        {
            InputStream stream = Files.newInputStream(Paths.get(path));
            image = ImageIO.read(stream);
        }
        catch (IOException e)
        {
            image = null;
            success = false;
        }
        return success;
    }
    
    public static void main(String... args)
    {
        System.out.println("Train Dogs & Cats");
        
        int inputSize = WIDTH * HEIGHT;
        int outputSize = 2;
        int hiddenSize = 100;
        
        Backup source = new Backup();
        source.readFromFile("dogs-and-cats-net.txt");
        //Network network = new Network(Arrays.asList(inputSize, hiddenSize, outputSize), 0.01);
        
        try
        {
            Network network = new Network(source);
            for (int epoch = 40; epoch < 50; ++epoch)
            {
                System.out.println("-------------------- EPOCH %d --------------------".formatted(epoch));
                
                /* Training */
                
                for (int i = 0; i < 1000; ++i)
                {
                    System.out.print(String.format("%3d ", i));
                    if (i % 50 == 49)
                    {
                        System.out.println();
                    }
                    
                    /* Cat */
                    if (loadImage("C:/Users/Użytkownik/Pictures/Cats & Dogs/Cats/%d.jpg".formatted(i)))
                    {
                        IORecord record = new IORecord(getPixels(), Arrays.asList(1.0, 0.0));
                        network.trainRecord(record);
                    }
                    else
                    {
                        System.err.println("\nImage Cats/%d.jpg was not loaded!".formatted(i));
                    }

                    /* Dog */
                    if (loadImage("C:/Users/Użytkownik/Pictures/Cats & Dogs/Dogs/%d.jpg".formatted(i)))
                    {
                        IORecord record = new IORecord(getPixels(), Arrays.asList(0.0, 1.0));
                        network.trainRecord(record);
                    }
                    else
                    {
                        System.err.println("\nImage Dogs/%d.jpg was not loaded!".formatted(i));
                    }
                }

                Backup backup = network.serialize();
                backup.saveToFile("network (%02d).txt".formatted(epoch));
                System.out.println("\nbackuping");
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
