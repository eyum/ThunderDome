package virassan.gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

import virassan.utils.Utils;

/**
 * Loads Images to be read and done something with
 * @author Virassan
 *
 */
public class ImageLoader {
	
	/**
	 * Loads the BufferedImage and reads it
	 * @param filepath file of the BufferedImage
	 * @return the BufferedImage
	 */
	public static BufferedImage loadImage(String filepath){
		try {
			if((new File(filepath)).isFile()){
				System.out.println("Message: ImageLoader_loadImage is file");
			}
			BufferedImage original = ImageIO.read(ImageLoader.class.getResource(filepath));
			return original; //returns the buffered image
		}catch(FileNotFoundException f){
			String mesg = "Error Message: ImageLoader_loadImage FILE NOT FOUND: " + filepath;
			System.out.println(mesg);
			Utils.addErrorToLog(mesg);
			f.printStackTrace();
		} catch (IOException e) {
			String mesg = "Error Message: ImageLoader_loadImage IOException filepath: " + filepath;
			System.out.println(mesg);
			Utils.addErrorToLog(mesg);
			e.printStackTrace();
			System.exit(1);
		}catch(Exception k){
			String mesg = "Error Message: ImageLoader_loadImage UNKNOWN EXCEPTION: " + filepath + System.getProperty("line.separator") +
					k.getMessage();
			System.out.println(mesg);
			Utils.addErrorToLog(mesg);
			k.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	
}
