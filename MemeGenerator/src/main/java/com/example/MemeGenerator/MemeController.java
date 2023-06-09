package com.example.MemeGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@RestController
public class MemeController {

	public static final String MEME_FONT = "Arial";
	public static final String TEMPLATE_DIR = "./images/base-images/";
	public static final String OUTPUT_DIR = "./images/generated-images/";
	public static final int BORDER_THICKNESS = 2;

	private byte[] handleTwoRight(BufferedImage bufferedImage, String topText, String bottomText, String fileName) throws IOException {
		// Create Graphics2D object from BufferedImage
		Graphics2D graphics2D = bufferedImage.createGraphics();
	
		float fontSize = bufferedImage.getHeight() * 0.04f;
		// Set font and color for top text
		Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
		Color topTextColor = Color.WHITE;
		Color topTextBorderColor = Color.BLACK;
	
		// Set font and color for bottom text
		Font bottomTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
		Color bottomTextColor = Color.WHITE;
		Color bottomTextBorderColor = Color.BLACK;
	
		// Calculate the position of the top text
		graphics2D.setFont(topTextFont);
		FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
		int topTextWidth = topTextFontMetrics.stringWidth(topText);
		int topTextX = bufferedImage.getWidth() / 2 + bufferedImage.getWidth() / 4 - topTextWidth / 2;
		int topTextY = bufferedImage.getHeight() / 4 + topTextFontMetrics.getAscent() + BORDER_THICKNESS;
	
		// Draw the top text border
		graphics2D.setColor(topTextBorderColor);
		for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
			for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
				graphics2D.drawString(topText, topTextX + i, topTextY + j);
			}
		}
	
		// Draw the top text
		graphics2D.setColor(topTextColor);
		graphics2D.drawString(topText, topTextX, topTextY);
	
		// Calculate the position of the bottom text
		graphics2D.setFont(bottomTextFont);
		FontMetrics bottomTextFontMetrics = graphics2D.getFontMetrics();
		int bottomTextWidth = bottomTextFontMetrics.stringWidth(bottomText);
		int bottomTextX = bufferedImage.getWidth() / 2 + bufferedImage.getWidth() / 4 - bottomTextWidth / 2;
		int bottomTextY = bufferedImage.getHeight() * 3 / 4 + bottomTextFontMetrics.getAscent() + BORDER_THICKNESS;
	
		// Draw the bottom text border
		graphics2D.setColor(bottomTextBorderColor);
		for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
			for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
				graphics2D.drawString(bottomText, bottomTextX + i, bottomTextY + j);
			}
		}
	
		// Draw the bottom text
		graphics2D.setColor(bottomTextColor);
		graphics2D.drawString(bottomText, bottomTextX, bottomTextY);
	
		// Dispose Graphics2D object
		graphics2D.dispose();
	
		// Convert BufferedImage to byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
		byte[] memeImageBytes = byteArrayOutputStream.toByteArray();
	
		ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
		BufferedImage bImage2 = ImageIO.read(bis);
		ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg"));
	
		return memeImageBytes;
	}
	
	
	
	@PostMapping("/generateMeme/top-bottom")
	@CrossOrigin
	public byte[] generateMeme(@RequestParam("baseImage") String baseImage, @RequestParam("topText") String topText, @RequestParam("bottomText") String bottomText, @RequestParam("fileName") String fileName) throws IOException {
	
		Path imagePath = Paths.get(TEMPLATE_DIR, baseImage);
		BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());
	
	
		// Create Graphics2D object from BufferedImage
		Graphics2D graphics2D = bufferedImage.createGraphics();
	
		float fontSize = bufferedImage.getHeight() * 0.08f;
		// Set font and color for top text
		Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
		Color topTextColor = Color.WHITE;
		Color topTextBorderColor = Color.BLACK;
	
		// Set font and color for bottom text
		Font bottomTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
		Color bottomTextColor = Color.WHITE;
		Color bottomTextBorderColor = Color.BLACK;

		// Draw top text centered and aligned to the top of the image
		graphics2D.setFont(topTextFont);
		FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
		int topTextWidth = topTextFontMetrics.stringWidth(topText);
		int topTextX = (bufferedImage.getWidth() - topTextWidth) / 2;
		int topTextY = topTextFontMetrics.getHeight();
	
		// Draw the top text border
		graphics2D.setColor(topTextBorderColor);
		for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
			for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
				graphics2D.drawString(topText, topTextX + i, topTextY + j);
			}
		}
	
		// Draw the top text
		graphics2D.setColor(topTextColor);
		graphics2D.drawString(topText, topTextX, topTextY);
	
		// Draw bottom text centered and aligned to the bottom middle of the image
		graphics2D.setFont(bottomTextFont);
		graphics2D.setColor(bottomTextColor);
		FontMetrics bottomTextFontMetrics = graphics2D.getFontMetrics();
		int bottomTextWidth = bottomTextFontMetrics.stringWidth(bottomText);
		int bottomTextX = (bufferedImage.getWidth() - bottomTextWidth) / 2;
		int bottomTextY = bufferedImage.getHeight() - bottomTextFontMetrics.getHeight();
	
		// Draw the bottom text border
		graphics2D.setColor(bottomTextBorderColor);
		for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
			for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
				graphics2D.drawString(bottomText, bottomTextX + i, bottomTextY + j);
			}
		}
	
		// Draw the bottom text
		graphics2D.setColor(bottomTextColor);
		graphics2D.drawString(bottomText, bottomTextX, bottomTextY);
	
		// Dispose Graphics2D object
		graphics2D.dispose();
	
		// Convert BufferedImage to byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
		byte[] memeImageBytes = byteArrayOutputStream.toByteArray();
	
		ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
		BufferedImage bImage2 = ImageIO.read(bis);
		ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg") );
		
		return memeImageBytes;
	        
	}

	@PostMapping("/generateMeme/top")
	@CrossOrigin
	public byte[] generateMemeTop(@RequestParam("baseImage") String baseImage, @RequestParam("topText") String topText, @RequestParam("fileName") String fileName) throws IOException {
	
		Path imagePath = Paths.get(TEMPLATE_DIR, baseImage);
		BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());
	
		// Create Graphics2D object from BufferedImage
		Graphics2D graphics2D = bufferedImage.createGraphics();
	
		// Set font and color for top text
		float fontSize = bufferedImage.getHeight() * 0.08f;
		Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
		Color topTextColor = Color.WHITE;
		Color topTextBorderColor = Color.BLACK;
	
		// Draw top text centered and aligned to the top of the image
		graphics2D.setFont(topTextFont);
		FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
		int topTextWidth = topTextFontMetrics.stringWidth(topText);
		int topTextX = (bufferedImage.getWidth() - topTextWidth) / 2;
		int topTextY = topTextFontMetrics.getHeight();
	
		// Draw the top text border
		graphics2D.setColor(topTextBorderColor);
		for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
			for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
				graphics2D.drawString(topText, topTextX + i, topTextY + j);
			}
		}
	
		// Draw the top text
		graphics2D.setColor(topTextColor);
		graphics2D.drawString(topText, topTextX, topTextY);
	
		// Dispose Graphics2D object
		graphics2D.dispose();
	
		// Convert BufferedImage to byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
		byte[] memeImageBytes = byteArrayOutputStream.toByteArray();
	
		ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
		BufferedImage bImage2 = ImageIO.read(bis);
		ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg"));
	
		return memeImageBytes;
	}

	@PostMapping("/generateMeme/two-right")
	@CrossOrigin
	public byte[] generateMemeTwoRight(@RequestParam("baseImage") String baseImage, @RequestParam("topText") String topText, @RequestParam("bottomText") String bottomText, @RequestParam("fileName") String fileName) throws IOException {

		Path imagePath = Paths.get(TEMPLATE_DIR, baseImage);
		BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());

		return handleTwoRight(bufferedImage, topText, bottomText, fileName);
	}

	@PostMapping("/generateMeme/user-upload")
	@CrossOrigin
	public byte[] generateMeme(@RequestParam("baseImage") MultipartFile baseImage, @RequestParam("topText") String topText, @RequestParam("bottomText") String bottomText, @RequestParam("fileName") String fileName, @RequestParam("selectedFormat") String selectedFormat) throws IOException {
	
		if ("two-right".equals(selectedFormat)) {
	
			BufferedImage bufferedImage = ImageIO.read(baseImage.getInputStream());
			// Create Graphics2D object from BufferedImage
			Graphics2D graphics2D = bufferedImage.createGraphics();
	
			float fontSize = bufferedImage.getHeight() * 0.04f;
			// Set font and color for top text
			Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
			Color topTextColor = Color.WHITE;
			Color topTextBorderColor = Color.BLACK;
	
			// Set font and color for bottom text
			Font bottomTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
			Color bottomTextColor = Color.WHITE;
			Color bottomTextBorderColor = Color.BLACK;
	
			// Draw top text centered and aligned to the top of the image
			graphics2D.setFont(topTextFont);
			graphics2D.setColor(topTextBorderColor);
			FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
			int topTextWidth = topTextFontMetrics.stringWidth(topText);
			int topTextX = (bufferedImage.getWidth() * 4 / 3 - topTextWidth) / 2;
			int topTextY = topTextFontMetrics.getHeight();
			
			// Draw the top text border
			for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
				for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
					graphics2D.drawString(topText, topTextX + i, topTextY + j);
				}
			}
	
			graphics2D.setColor(topTextColor);
			graphics2D.drawString(topText, topTextX, topTextY);
	
			// Draw bottom text centered and aligned to the bottom middle of the image
			graphics2D.setFont(bottomTextFont);
			graphics2D.setColor(bottomTextBorderColor);
			FontMetrics bottomTextFontMetrics = graphics2D.getFontMetrics();
			int bottomTextWidth = bottomTextFontMetrics.stringWidth(bottomText);
			int bottomTextX = (bufferedImage.getWidth() * 4 / 3 - bottomTextWidth) / 2;
			int bottomTextY = bufferedImage.getHeight() - bottomTextFontMetrics.getHeight();
			
			// Draw the bottom text border
			for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
				for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
					graphics2D.drawString(bottomText, bottomTextX + i, bottomTextY + j);
				}
			}
	
			graphics2D.setColor(bottomTextColor);
			graphics2D.drawString(bottomText, bottomTextX, bottomTextY);
	
			// Dispose Graphics2D object
			graphics2D.dispose();
	
			// Convert BufferedImage to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			byte[] memeImageBytes = byteArrayOutputStream.toByteArray();
	
			ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
			BufferedImage bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg") );
			
			return memeImageBytes;


		} else if ("top-bottom".equals(selectedFormat)) {
			// Convert MultipartFile to BufferedImage
			BufferedImage bufferedImage = ImageIO.read(baseImage.getInputStream());
		
			// Create Graphics2D object from BufferedImage
			Graphics2D graphics2D = bufferedImage.createGraphics();
		
			float fontSize = bufferedImage.getHeight() * 0.08f;
			// Set font and color for top text
			Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
			Color topTextColor = Color.WHITE;
			Color topTextBorderColor = Color.BLACK;
		
			// Set font and color for bottom text
			Font bottomTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
			Color bottomTextColor = Color.WHITE;
			Color bottomTextBorderColor = Color.BLACK;
		
			// Draw top text centered and aligned to the top of the image
			graphics2D.setFont(topTextFont);
			graphics2D.setColor(topTextBorderColor);
			FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
			int topTextWidth = topTextFontMetrics.stringWidth(topText);
			int topTextX = (bufferedImage.getWidth() - topTextWidth) / 2;
			int topTextY = topTextFontMetrics.getHeight();
		
			// Draw the top text border
			for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
				for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
					graphics2D.drawString(topText, topTextX + i, topTextY + j);
				}
			}
		
			graphics2D.setColor(topTextColor);
			graphics2D.drawString(topText, topTextX, topTextY);
		
			// Draw bottom text centered and aligned to the bottom middle of the image
			graphics2D.setFont(bottomTextFont);
			graphics2D.setColor(bottomTextBorderColor);
			FontMetrics bottomTextFontMetrics = graphics2D.getFontMetrics();
			int bottomTextWidth = bottomTextFontMetrics.stringWidth(bottomText);
			int bottomTextX = (bufferedImage.getWidth() - bottomTextWidth) / 2;
			int bottomTextY = bufferedImage.getHeight() - bottomTextFontMetrics.getHeight();
		
			// Draw the bottom text border
			for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
				for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
					graphics2D.drawString(bottomText, bottomTextX + i, bottomTextY + j);
				}
			}
		
			graphics2D.setColor(bottomTextColor);
			graphics2D.drawString(bottomText, bottomTextX, bottomTextY);
		
			// Dispose Graphics2D object
			graphics2D.dispose();
		
			// Convert BufferedImage to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			byte[] memeImageBytes = byteArrayOutputStream.toByteArray();
		
			ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
			BufferedImage bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg"));
		
			return memeImageBytes;
		} else if ("top".equals(selectedFormat)) {
			// Convert MultipartFile to BufferedImage
			BufferedImage bufferedImage = ImageIO.read(baseImage.getInputStream());

			// Create Graphics2D object from BufferedImage
			Graphics2D graphics2D = bufferedImage.createGraphics();

			float fontSize = bufferedImage.getHeight() * 0.08f;
   			 // Set font and color for top text
			Font topTextFont = new Font(MEME_FONT, Font.BOLD, Math.round(fontSize));
			Color topTextColor = Color.WHITE;
			Color topTextBorderColor = Color.BLACK;

			// Draw top text centered and aligned to the top of the image
			graphics2D.setFont(topTextFont);
			graphics2D.setColor(topTextBorderColor);
			FontMetrics topTextFontMetrics = graphics2D.getFontMetrics();
			int topTextWidth = topTextFontMetrics.stringWidth(topText);
			int topTextX = (bufferedImage.getWidth() - topTextWidth) / 2;
			int topTextY = topTextFontMetrics.getHeight();

			// Draw the top text border
			for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
				for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
					graphics2D.drawString(topText, topTextX + i, topTextY + j);
				}
			}

			graphics2D.setColor(topTextColor);
			graphics2D.drawString(topText, topTextX, topTextY);

			// Dispose Graphics2D object
			graphics2D.dispose();

			// Convert BufferedImage to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			byte[] memeImageBytes = byteArrayOutputStream.toByteArray();

			ByteArrayInputStream bis = new ByteArrayInputStream(memeImageBytes);
			BufferedImage bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File(OUTPUT_DIR + fileName + ".jpg"));

			return memeImageBytes;
		}

		return new byte[0];
		
	}

	
	@GetMapping("/getAllTemplates")
	public ResponseEntity<List<String>> getAllTemplates() {
		List<String> imageNames = getImageNames(TEMPLATE_DIR);
		return ResponseEntity.ok().body(imageNames);
	}

	@GetMapping("/templates/{imageName}")
	public ResponseEntity<Resource> getTemplate(@PathVariable String imageName) {
		Path imagePath = Paths.get(TEMPLATE_DIR, imageName);
		try {
			Resource resource = new UrlResource(imagePath.toUri());
			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/getAllGeneratedImages")
	public ResponseEntity<List<String>> getAllGeneratedImages() {
		List<String> imageNames = getImageNames(OUTPUT_DIR);
		return ResponseEntity.ok().body(imageNames);
	}

	@GetMapping("/generatedImages/{imageName}")
	public ResponseEntity<Resource> getGeneratedImage(@PathVariable String imageName) {
		Path imagePath = Paths.get(OUTPUT_DIR, imageName);
		try {
			Resource resource = new UrlResource(imagePath.toUri());
			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private List<String> getImageNames(String directory) {
		List<String> imageNames = new ArrayList<>();
		File folder = new File(directory);
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					imageNames.add(file.getName());
				}
			}
		}
		return imageNames;
	}


}
