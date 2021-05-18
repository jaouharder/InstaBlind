package com.example.lib;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Application {

    public static void main(String[] args)
    {
    	
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        DnnProcessor processor = new DnnProcessor();
        List<DnnObject> detectObject = new ArrayList<DnnObject>();
      //  VideoCapture capturre = new VideoCapture("res/Funny Cats and Kittens Meowing Compilation.MP4");
       // VideoCapture capturre = new VideoCapture(0);
       // while (true)
        //{
        Mat myimg=Imgcodecs.imread("C:/Users/hpcode/Desktop/OpencvJavaWorkSpace/opencv-object-detection/data/sample images/aile.jpg");
           Mat frame = new Mat();
          // capturre.read(myimg);
           detectObject = processor.getObjectsInFrame(myimg, false);
           for (DnnObject obj: detectObject)
           {
               Imgproc.rectangle(myimg,obj.getLeftBottom(),obj.getRightTop(),new Scalar(255,0,0),1);
               Imgproc.putText(myimg, obj.getObjectName(), obj.getLeftBottom(), 1, 2, new Scalar(0,0,255));
           }
          // Imgcodecs.imwrite("DetectedObject.jpg",frame);
           PushImage(ConvertMat2Image(myimg));
        //}
    	

    }
    
    
    static /* part added by me*/

    JFrame frame;
	static JLabel lbl;
	static ImageIcon icon;
 
private static BufferedImage ConvertMat2Image(Mat kameraVerisi) {
	
		
		MatOfByte byteMatVerisi = new MatOfByte();
	
		Imgcodecs.imencode(".jpg", kameraVerisi, byteMatVerisi);
		
		byte[] byteArray = byteMatVerisi.toArray();
		BufferedImage goruntu = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			goruntu = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return goruntu;
	}
	
	
    public static void PencereHazirla() {
	    frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(700, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void PushImage(Image img2) {
	
		if (frame == null)
			PencereHazirla();
	
		if (lbl != null)
			frame.remove(lbl);
		icon = new ImageIcon(img2);
		lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.revalidate();
	}
}
