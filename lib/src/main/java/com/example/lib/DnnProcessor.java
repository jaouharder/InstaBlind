package com.example.lib;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Mesut Piskin
 * 17/08/2018 11:08
 **/


public class DnnProcessor {
   // private final static Logger LOGGER = LoggerFactory.getLogger(DnnProcessor.class);
    private Net net;
    private final String proto = "data/dnnmodel/MobileNetSSD_deploy.prototxt";
    private final String model = "data/dnnmodel/MobileNetSSD_deploy.caffemodel";
    private final File file=new File("data/dnnmodel/Label.txt");

    private  String[] classNames={"background",
            "aeroplane", "bicycle", "bird", "boat",
            "bottle", "bus", "car", "cat", "chair",
            "cow", "diningtable", "dog", "horse",
            "motorbike", "person", "pottedplant",
            "sheep", "sofa", "train", "tvmonitor"};
    

    public DnnProcessor() {
      this.net = Dnn.readNetFromCaffe(proto, model);
      /*classNames=ListofLabels(file);
      for (String string : classNames) {
			System.out.println(string);
		}*/
      
    }
    
   /* private String[] ListofLabels(File labelsfile) {
    	
    	File myfile=labelsfile;
    	String[] labels=new String[93];
    	int counter=0;
    	try {
			Scanner sc=new Scanner(new FileInputStream(myfile));
			while (sc.hasNext()) {
				labels[counter]=sc.next();
				counter++;
				
			}
			
			return labels;
		} 
    	catch (FileNotFoundException e) {e.printStackTrace(); return labels;}
    	
    }*/


    public int getObjectCount(Mat frame, boolean isGrayFrame, String objectName) {

        int inWidth = 320;
        int inHeight = 240;
        double inScaleFactor = 0.007843;
        double thresholdDnn =  0.5;
        double meanVal = 127.5;

        int personObjectCount = 0;
        Mat blob = null;
        Mat detections = null;


        try {
            if (isGrayFrame)
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2RGB);

            blob = Dnn.blobFromImage(frame, inScaleFactor,
                    new Size(inWidth, inHeight),
                    new Scalar(meanVal, meanVal, meanVal),
                    false, false);
            net.setInput(blob);
            detections = net.forward();
            detections = detections.reshape(1, (int) detections.total() / 7);
            for (int i = 0; i < detections.rows(); ++i) {
                double confidence = detections.get(i, 2)[0];

                if (confidence < thresholdDnn)
                    continue;

                int classId = (int) detections.get(i, 1)[0];
                if (classNames[classId].toString() != objectName.toLowerCase()) {
                    continue;
                }
                personObjectCount++;
            }
        } catch (Exception ex) {
            //LOGGER.error("An error occurred DNN: ", ex);
        }
        return personObjectCount;
    }

    public List<DnnObject> getObjectsInFrame(Mat frame, boolean isGrayFrame) {

        int inWidth = 320;
        int inHeight = 240;
        double inScaleFactor = 0.007843;
        double thresholdDnn =  0.5;
        double meanVal = 127.5;
        
        Mat blob = null;
        Mat detections = null;
        List<DnnObject> objectList = new ArrayList<DnnObject>();

        int cols = frame.cols();
        int rows = frame.rows();

        try {
            if (isGrayFrame)
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2RGB);

            blob = Dnn.blobFromImage(frame, inScaleFactor,
                    new Size(inWidth, inHeight),
                    new Scalar(meanVal, meanVal, meanVal),
                    false, false);

            net.setInput(blob);
            detections = net.forward();
            detections = detections.reshape(1, (int) detections.total() / 7);

            //all detected objects
            for (int i = 0; i < detections.rows(); ++i) {
                double confidence = detections.get(i, 2)[0];

                if (confidence < thresholdDnn)
                    continue;

                int classId = (int) detections.get(i, 1)[0];

                //calculate position
                int xLeftBottom = (int) (detections.get(i, 3)[0] * cols);
                int yLeftBottom = (int) (detections.get(i, 4)[0] * rows);
                Point leftPosition = new Point(xLeftBottom, yLeftBottom);

                int xRightTop = (int) (detections.get(i, 5)[0] * cols);
                int yRightTop = (int) (detections.get(i, 6)[0] * rows);
                Point rightPosition = new Point(xRightTop, yRightTop);

                float centerX = (xLeftBottom + xRightTop) / 2;
                float centerY = (yLeftBottom - yRightTop) / 2;
                Point centerPoint = new Point(centerX, centerY);


                DnnObject dnnObject = new DnnObject(classId, classNames[classId].toString(), leftPosition, rightPosition, centerPoint);
                objectList.add(dnnObject);
            }

        } catch (Exception ex) {
            //LOGGER.error("An error occurred DNN: ", ex);
        }
        System.out.println(objectList);
        return objectList;
    }


}
