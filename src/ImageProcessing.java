import java.awt.BorderLayout; 
import java.awt.Container; 
import java.awt.GridLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JPanel; 
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI.BasicHorizontalLayoutManager;

import marvin.gui.MarvinImagePanel; 
import marvin.image.MarvinImage; 
import marvin.color.MarvinColorModelConverter;
import marvin.io.MarvinImageIO;
import marvin.image.MarvinImageMask; 
import marvin.plugin.MarvinImagePlugin; 
import marvin.thread.MarvinThread;
import marvin.thread.MarvinThreadEvent;
import marvin.thread.MarvinThreadListener;
import marvin.util.MarvinPluginLoader;

public class ImageProcessing extends JFrame implements ActionListener, MarvinThreadListener
{ 
    private MarvinImagePanel	imagePanel,
                                imagePanel2,
                                imagePanel3,
                                imagePanel4;
    
    private MarvinImage		image,  
				backupImage,
                                image2,  
				backupImage2;
    
    private JPanel		panelBottom;
    
    private JLabel              labelPerformance;
     
     
    private JButton             buttonSingle,  
                                buttonDouble,  
                                buttonQuadra,
                                buttonExample,
                                buttonReset; 
    
    private int                 threadsFinished, totalThreads;
    private long                processStartTime;
     
    public ImageProcessing() 
    { 
        super("ImageProcessing");
         
        // Create Graphical Interface 
        buttonExample = new JButton("Example");
        buttonExample.addActionListener(this);
        buttonSingle = new JButton("SingleThread");
        buttonSingle.addActionListener(this); 
        buttonDouble = new JButton("DoubleThread");
        buttonDouble.addActionListener(this); 
        buttonQuadra = new JButton("QuadraThread");
        buttonQuadra.addActionListener(this); 
        buttonReset = new JButton("Reset");
        buttonReset.addActionListener(this); 
         
        panelBottom = new JPanel(); 
        panelBottom.add(buttonExample);
        panelBottom.add(buttonSingle); 
        panelBottom.add(buttonDouble); 
        panelBottom.add(buttonQuadra);
        panelBottom.add(buttonReset);
        
        // Label 
        labelPerformance = new JLabel("Performance:");
         
        // ImagePanel 
        imagePanel = new MarvinImagePanel();
        imagePanel2 = new MarvinImagePanel();
        imagePanel3 = new MarvinImagePanel();
        imagePanel4 = new MarvinImagePanel();
         
        Container l_c = getContentPane(); 
        l_c.setLayout(new GridLayout(3,2));
        l_c.add(imagePanel);
        l_c.add(imagePanel2);
        l_c.add(imagePanel3);
        l_c.add(imagePanel4);
        l_c.add(panelBottom);
        l_c.add(labelPerformance);
        
        // Load image
		
	image = MarvinImageIO.loadImage("src\\res\\manfe.jpg");
        backupImage = image.clone();         
        imagePanel.setImage(image);
        imagePanel2.setImage(image);
        imagePanel3.setImage(image);
        imagePanel4.setImage(image);
        
        image2 = MarvinImageIO.loadImage("src\\res\\bigSizeImage.jpg");
        backupImage2 = image2.clone();
        
        setSize(800,600);
        setVisible(true);     
    }
    
    private void singleThread(){
        processStartTime = System.currentTimeMillis(); 
        MarvinImagePlugin plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
        plgImage.process(image2, image2);
        labelPerformance.setText("Performance: "+ (System.currentTimeMillis()-processStartTime)+ " milliseconds (Single Thread)");  
    } 
     
    private void doubleThread(){
         
        totalThreads = 2;
        // Load Plug-ins 
        MarvinImagePlugin plgImage1 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
        MarvinImagePlugin plgImage2 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
         
        // Create masks 
        MarvinImageMask mask1 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            0,                    // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/2            // region´s height 
        ); 
         
        MarvinImageMask mask2 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            image2.getHeight()/2,                // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/2            // region´s height 
        ); 
         
         
        MarvinThread mvThread1 = new MarvinThread(plgImage1, image2, image2, mask1);
        MarvinThread mvThread2 = new MarvinThread(plgImage2, image2, image2, mask2);
         
        mvThread1.addThreadListener(this); 
        mvThread2.addThreadListener(this); 
        
        processStartTime = System.currentTimeMillis();
        mvThread1.start(); 
        mvThread2.start(); 
         
        threadsFinished = 0; 
    }
    
    private void quadraThread(){
        
        totalThreads = 4;
         
        // Load Plug-ins 
        MarvinImagePlugin plgImage1 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
        MarvinImagePlugin plgImage2 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
        MarvinImagePlugin plgImage3 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
        MarvinImagePlugin plgImage4 = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.statistical.maximum");
         
        // Create masks 
        MarvinImageMask mask1 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            0,                    // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/4            // region´s height 
        ); 
         
        MarvinImageMask mask2 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            image2.getHeight()/4,                // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/4            // region´s height 
        ); 
        
        MarvinImageMask mask3 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            image2.getHeight()/2,                // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/4            // region´s height 
        ); 
        
        MarvinImageMask mask4 = new MarvinImageMask 
        ( 
            image2.getWidth(),            // width 
            image2.getHeight(),            // height 
            0,                    // x-start 
            image2.getHeight()/4*3,                // y-start 
            image2.getWidth(),            // region´s width 
            image2.getHeight()/4            // region´s height 
        ); 
         
         
        MarvinThread mvThread1 = new MarvinThread(plgImage1, image2, image2, mask1);
        MarvinThread mvThread2 = new MarvinThread(plgImage2, image2, image2, mask2);
        MarvinThread mvThread3 = new MarvinThread(plgImage3, image2, image2, mask3);
        MarvinThread mvThread4 = new MarvinThread(plgImage4, image2, image2, mask4);
         
        mvThread1.addThreadListener(this); 
        mvThread2.addThreadListener(this); 
        mvThread3.addThreadListener(this); 
        mvThread4.addThreadListener(this); 
        
        processStartTime = System.currentTimeMillis(); 
        mvThread1.start(); 
        mvThread2.start();
        mvThread3.start(); 
        mvThread4.start();
         
        threadsFinished = 0; 
    }
    
    private void example(){
        //Mosaic
        image = backupImage.clone();
        MarvinImagePlugin plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.artistic.mosaic");
        plgImage.process(image, image);
        image.update();
        imagePanel.setImage(image);
        
        //Invert
        image = backupImage.clone();
        plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert");
        plgImage.process(image, image);
        image.update();
        imagePanel2.setImage(image);
        
        //GrayScale
        image = backupImage.clone();
        plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");
        plgImage.process(image, image);
        image.update();
        imagePanel3.setImage(image);
        
        //EdgeDetector
        image = backupImage.clone();
        plgImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.edge.edgeDetector");
        plgImage.process(image, image);
        image.update();
        imagePanel4.setImage(image);
        
    } 
    
    public void threadFinished(MarvinThreadEvent e){
        threadsFinished++; 

        if(threadsFinished == totalThreads){
            labelPerformance.setText("Performance: "+ (System.currentTimeMillis()-processStartTime)+ " milliseconds (Multiple Thread)"); 
        }
    }   
     
    public static void main(String args[]){
        ImageProcessing t = new ImageProcessing(); 
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    } 
     
    public void actionPerformed(ActionEvent e){
        image = backupImage.clone();
        if(e.getSource() == buttonSingle){ 
            singleThread();
        } 
        else if(e.getSource() == buttonDouble){
            doubleThread();
        } 
        else if(e.getSource() == buttonExample) {
            example();
        }
        else if(e.getSource() == buttonQuadra){
            quadraThread();
        }
        else if(e.getSource() == buttonReset){
            image.update();
            imagePanel.setImage(image);
            imagePanel2.setImage(image);
            imagePanel3.setImage(image);
            imagePanel4.setImage(image);
        }

    } 
    
}