package de.sb.messenger.persistence;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;

import de.sb.toolbox.bind.JsonProtectedPropertyStrategy;


@Entity
@Table(name="Document", schema="messenger")
@PrimaryKeyJoinColumn(name="documentIdentity")
@JsonbVisibility(value = JsonProtectedPropertyStrategy.class)
@XmlRootElement
@XmlType
public class Document extends BaseEntity{
	
	static private final byte[] DEFAULT_CONTENT = new byte[0];
	static private final byte[] DEFAULT_CONTENT_HASH = HashTools.sha256HashCode(DEFAULT_CONTENT);
	
	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 32, max = 32)
	private byte[] contentHash;
	
	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 1, max = 63)
	private String contentType;

	@Column(nullable = false, updatable = false, insertable = true)
	@NotNull
	@Size(min = 1, max = 16777215)
	private static byte[] content;
		

	
	public Document() {
		super();
		this.content = DEFAULT_CONTENT;
		this.contentHash = DEFAULT_CONTENT_HASH;
		this.contentType = "application/octet-stream";
	}
	

	
	@JsonbProperty
	@XmlAttribute 
	public byte[] getContentHash() {
		return contentHash;
	}

	@JsonbProperty
	@XmlAttribute 
	public String getContentType() {
		return contentType;
	}

	@JsonbTransient
	@XmlTransient
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
	//TODO get image size and resize
	@JsonbProperty 
	@XmlAttribute 
	static public byte[] scaledImageContent(byte[] content, int width, int height) {
		  ByteArrayInputStream in = new ByteArrayInputStream(content);
			try {
	            BufferedImage img = ImageIO.read(in);
	            if(height == 0) {
	                height = (width * img.getHeight())/ img.getWidth(); 
	            }
	            if(width == 0) {
	                width = (height * img.getWidth())/ img.getHeight();
	            }
	            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

	            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	            ImageIO.write(imageBuff, "jpg", buffer);

	            return buffer.toByteArray();
	        } catch (IOException e) {
	            
	        }
	 
			return content;}
}
