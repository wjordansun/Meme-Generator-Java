package com.example.MemeGenerator;

public class MemeRequest {

	private byte[] image;
    private String topText;
    private String bottomText;
    
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getTopText() {
		return topText;
	}
	public void setTopText(String topText) {
		this.topText = topText;
	}
	public String getBottomText() {
		return bottomText;
	}
	public void setBottomText(String bottomText) {
		this.bottomText = bottomText;
	}
    
	
}
