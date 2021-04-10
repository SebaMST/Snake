package Game.GUI;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class ImageLoader {
    private static final String PATH_RESOURCES;
    private static final HashMap<String,Image> backgroundImages;

    static {
        PATH_RESOURCES = "src/main/resources/";
        backgroundImages = new HashMap<>();
        load();
    }

    private static void load() {
        File iconsFolder = new File(PATH_RESOURCES);
        File[] images = iconsFolder.listFiles();

        if (images != null) {
            for(File image: images)
            {
                String fullImageName = image.getName();
                String imageName = fullImageName.substring(0,fullImageName.length()-4); //without .png extension
                Image img = Toolkit.getDefaultToolkit().getImage(PATH_RESOURCES+fullImageName);
                backgroundImages.put(imageName,img);
            }
        }
    }

    public static Image getImageByName(String name) {
        Image img = backgroundImages.get(name);
        if(img==null) {
            throw new IllegalArgumentException("Image does not exist: "+name+".png");
        }
        return img;
    }
}
