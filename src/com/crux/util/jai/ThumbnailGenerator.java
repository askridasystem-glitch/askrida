/***********************************************************************
 * Module:  com.crux.util.jai.ThumbnailGenerator
 * Author:  Denny Mahendra
 * Created: Jul 30, 2006 2:08:07 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.jai;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.media.jai.codec.SeekableStream;


import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class ThumbnailGenerator {

	public static void main(String[] args) {
           try{
              FileInputStream fis = new FileInputStream(args[0]);
              FileOutputStream fos = new FileOutputStream(args[1]);
              ThumbnailGenerator.createThumbnail(fis, fos, Integer.parseInt(args[2]), args[0]);
              fis.close();
              fos.close();
            }
            catch (Exception e){
               System.out.println("error at main "+e);
            }
			System.exit(0);
	}

	/**
	* Create Thumbnail from stream.
	* @param inStream source image stream
	* @param outStream target image stream
	* @param maxDim maximum height/width of thumbnail will be created
	* @param fileName target file name
	*/
	public static void createThumbnail(InputStream inStream, OutputStream outStream, int maxDim, String fileName) {
		String imgType = getImageType(fileName);
		if(imgType.equals("GIF"))
			createGIFThumbnail(inStream, outStream, maxDim);
		else
			createXXXThumbnail(inStream, outStream, maxDim, imgType);
	}

   public static void createThumbnail(InputStream inStream, OutputStream outStream, int maxDim) {
      createXXXThumbnail(inStream, outStream, maxDim, "JPEG");
	}

	/**
	* Create GIF Thumbnail from stream.
	* @param inStream source image stream
	* @param outStream target image stream
	* @param maxDim The width and height of the thumbnail must be maxDim pixels or less.
	*/
	// NOTE: this class is used for gif thumbnailing
	public static void createGIFThumbnail(InputStream inStream, OutputStream outStream, int maxDim) {
		try {

			byte[] temporaryData = new byte[1024*120];
			int count = inStream.read(temporaryData);
			byte[] imageData = new byte[count];
			System.arraycopy(temporaryData,0,imageData,0,count);

			// Get the image from a file.
			Image inImage = new ImageIcon(imageData).getImage();

			// Determine the scale.
			int scaledW = (int)(inImage.getWidth(null));
			int scaledH = (int)(inImage.getHeight(null));
			double scale = (double)maxDim/(double)inImage.getHeight(null);
			if (inImage.getWidth(null) > inImage.getHeight(null)) {
				scale = (double)maxDim/(double)inImage.getWidth(null);
			}

			// Set the scale.
			AffineTransform tx = new AffineTransform();

			// If the image is smaller than the desired image size,
			// don't bother scaling.
			if (scale < 1.0d) {
				// Determine size of new  image
				// One of them should equal maxDim.
				scaledW = (int)(scale*inImage.getWidth(null));
				scaledH = (int)(scale*inImage.getHeight(null));
				tx.scale(scale, scale);
			}

			// Create an image buffer in which to paint on.
			BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);

			// Paint image.
			Graphics2D g2d = outImage.createGraphics();
			g2d.drawImage(inImage, tx, null);
			g2d.dispose();

			// JPEG-encode the image and write to file.

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outStream);
			encoder.encode(outImage);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Create Thumbnail from stream.
	* @param inStream source image stream
	* @param outStream target image stream
	* @param maxDim maximum height/width of thumbnail will be created
	* @param imgType image type
	*/
	public static void createXXXThumbnail(InputStream inStream, OutputStream outStream, int maxDim, String imgType) {
		try {
			// Wrap the InputStream in a SeekableStream
			SeekableStream s = SeekableStream.wrapInputStream(inStream, false);

			// Create the ParameterBlock and add the SeekableStream to it.
			ParameterBlock pbi = new ParameterBlock();
			pbi.add(s);

			// Perform the BMP operation
			RenderedOp inImage = JAI.create(imgType, pbi);

			// Determine the scale.
			double scale = (double)maxDim/(double)inImage.getHeight();
			if (inImage.getWidth() > inImage.getHeight()) {
				scale = (double)maxDim/(double)inImage.getWidth();
			}

			// Determine size of new image.
			// One of them should equal maxDim.
			// If the image is smaller than the desired image size,
			// don't bother scaling.
			if (scale < 1.0d) {
				// Set image scale
				ParameterBlock pbs = new ParameterBlock();
				pbs.addSource(inImage); // source image
				pbs.add((float)scale); // x Scale
				pbs.add((float)scale); // y Scale
				pbs.add(0.0F); // x Translation
				pbs.add(0.0F); // y Translation
				pbs.add(new InterpolationNearest());

				// Scale operation
				inImage = JAI.create("scale", pbs, null);
			}

			// Encode the image n store to the stream
			JAI.create("encode", inImage, outStream, imgType, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Identify recognized image type from file name.
	 * @param fileName file name
	 */
	private static String getImageType(String fileName) {
		String fileExtension = "";
		int initialCapacity = 7;
		float loadFactor = new Float(1.00).floatValue();
		HashMap supportedImageType = new HashMap(initialCapacity, loadFactor);

		supportedImageType.put(".BMP", "BMP");
		supportedImageType.put(".GIF", "GIF");
		supportedImageType.put(".JPEG", "JPEG");
		supportedImageType.put(".JPG", "JPEG");
		supportedImageType.put(".PNG", "PNG");
		supportedImageType.put(".PNM", "PNM");
		supportedImageType.put(".TIFF", "TIFF");

		if(fileName.lastIndexOf('.') > 1)
			fileExtension = fileName.substring(fileName.lastIndexOf('.')).toUpperCase().trim();

		if(supportedImageType.containsKey(fileExtension))
			return (String) supportedImageType.get(fileExtension);

		return "";
	}



}
