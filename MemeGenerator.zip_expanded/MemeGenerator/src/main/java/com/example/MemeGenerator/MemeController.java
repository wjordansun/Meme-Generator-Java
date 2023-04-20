package com.example.MemeGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.PageAttributes.MediaType;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemeController {
	
	 @PostMapping("/generateMeme")
	 @CrossOrigin
	    //public byte[] generateMeme(@RequestParam("image") MultipartFile image, @RequestParam("topText") String topText, @RequestParam("bottomText") String bottomText, @RequestParam("fileName") String fileName) throws IOException {
		public byte[] generateMeme(@RequestParam("templateName") String templateName, @RequestParam("topText") String topText, @RequestParam("bottomText") String bottomText, @RequestParam("fileName") String fileName) throws IOException {
		 	System.out.print("here");
		 	
		 	
		 	 // Convert MultipartFile to BufferedImage
	        BufferedImage bufferedImage = ImageIO.read(new File(templateName));

	        System.out.println(templateName);
	        System.out.println(bufferedImage);
	        System.out.println(topText);
	        System.out.println(bottomText);
	        
	        // Create Graphics2D object from BufferedImage
	        Graphics2D graphics2D = bufferedImage.createGraphics();

	        // Set font and color for top text
	        Font topTextFont = new Font("Arial", Font.BOLD, 36);
	        Color topTextColor = Color.BLACK;

	        // Set font and color for bottom text
	        Font bottomTextFont = new Font("Arial", Font.BOLD, 36);
	        Color bottomTextColor = Color.BLACK;

	        // Draw top text centered and aligned to the top of the image
	        graphics2D.setFont(topTextFont);
	        graphics2D.setColor(topTextColor);
	        FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
	        int topTextWidth = topTextFontMetrics.stringWidth(topText);
	        int topTextX = (bufferedImage.getWidth() - topTextWidth) / 2;
	        int topTextY = topTextFontMetrics.getHeight();
	        graphics2D.drawString(topText, topTextX, topTextY);

	        // Draw bottom text centered and aligned to the bottom middle of the image
	        graphics2D.setFont(bottomTextFont);
	        graphics2D.setColor(bottomTextColor);
	        FontMetrics bottomTextFontMetrics = graphics2D.getFontMetrics();
	        int bottomTextWidth = bottomTextFontMetrics.stringWidth(bottomText);
	        int bottomTextX = (bufferedImage.getWidth() - bottomTextWidth) / 2;
	        int bottomTextY = bufferedImage.getHeight() - bottomTextFontMetrics.getHeight();
	        graphics2D.drawString(bottomText, bottomTextX, bottomTextY);

	        // Dispose Graphics2D object
	        graphics2D.dispose();

	        // Convert BufferedImage to byte array
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
	        byte[] memeImageBytes = byteArrayOutputStream.toByteArray();

	        ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
	        BufferedImage bImage2 = ImageIO.read(bis);
	        ImageIO.write(bImage2, "jpg", new File(fileName + ".jpg") );
	        System.out.println("meme created");
	        
	        return memeImageBytes;
	        
	    }
		
	
}
