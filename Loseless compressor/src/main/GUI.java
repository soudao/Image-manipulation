package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JFileChooser.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.PixelGrabber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.util.zip.Deflater; 

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * reference for JAI
 * http://download.java.net/media/jai/builds/release/1_1_3/INSTALL.html
 * https://docs.oracle.com/cd/E17802_01/products/products/java-media/jai/forDevelopers/jai-apidocs/com/sun/media/jai/codec/TIFFEncodeParam.html 
*/

/**
 * reference for GUI
 * https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
 */

/**
 * 
 * @author Jackie Ong 
 *
 */

public class GUI extends JFrame implements ActionListener
{
   public static BufferedImage image, image2, imagesave;
   private JTextArea log;
   static Image i;
   BufferedImage bi=null;
   BufferedImage si = null;
   static String fileName;
   
  // public JPanel jPanel = null;
   
   // instantiate jpanel and jlabel
   private JPanel jPanel = new JPanel();
   private static JLabel jLabel = new JLabel(); 
   
   public GUI() // constructor
   {
	      jPanel.add(jLabel); // add jlabel to jpanel
	      add(jPanel);    
	      JScrollPane scroller = new JScrollPane(jPanel);  // create the scrollbar
	      this.getContentPane().add(scroller, BorderLayout.CENTER); // add scrollbar
   }
  
   // throw exception if invalid file
   public static BufferedImage image(String fileName) throws IOException
   {
      File file = new File(fileName);
    // return ImageIO.read(file);
     return image= javax.imageio.ImageIO.read(file); // read image file from disk
     
   }
   
   /**
    * Actions performed for the GUI
    */
   
   public void actionPerformed(ActionEvent e)  
   {    
      System.out.println(e.getActionCommand()); // prints actions to console
      switch(e.getActionCommand())
      
      {
         case "Open": // if user selects open file
            JFileChooser chooser = new JFileChooser();
                     
            //Add a custom file filter and disable the default
            //(Accept All) file filter.
                chooser.addChoosableFileFilter(new ImageFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                //Add custom icons for file types.
                chooser.setFileView(new ImageFileView());
     
            //Add the preview pane.
                chooser.setAccessory(new ImagePreview(chooser));           
     
            //Show it.
            int returnVal = chooser.showDialog(GUI.this,
                                          "Open");
            
            // if user chooses yes
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               try
               {
            	   // get path of image file
                  image = image(chooser.getSelectedFile().getPath().toString());
               }
               catch(IOException ex) // catch exception error
               {
            	   System.out.println(ex.toString() + "not a valid image file");
               }
               
               // scale image to fit on the jpanel
               setI(image.getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH)); 
               jLabel.setIcon(new ImageIcon(getI())); // set image as icon     
            }
            break;
            
                
         // exit program
         case "Exit": 
            System.exit(0);
            break;
            
            // save image manipulation results
         case "Save":
        	 
        	 // display thresholded image to screen
        	 setI(image.getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH)); 
             jLabel.setIcon(new ImageIcon(getI())); // set image as icon
        	 
             // save the file
             try {
            		ImageIO.write((image), "tif",new File("C:\\nature2.tif"));
            			} catch (IOException e1) {
            				// TODO Auto-generated catch block
            				e1.printStackTrace();
            		}
             break;   
             
             /**
              * function to compress image
              */
            
         case "compress image": 
        		
        	 TIFFEncodeParam param = new TIFFEncodeParam();			
    			// deflate is a lossless compression
    			param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE); //set compression
    			
    			System.out.print("Enter compression value 0-9 "); // get user input
    			Scanner c = new Scanner(System.in); // scan user input
  
    			int quality = c.nextInt(); 
    			while (quality < 0 || quality > 9) { // if quality less than 0 or 9 catch exceptions
    				  try {
    		                quality = Integer.parseInt(c.nextLine());
    		            } catch (NumberFormatException nfe) { // catch number format exception
    		            	 System.out.print("Invalid input ");
    		            	 c.next();
    		            } catch(IllegalArgumentException e2) { // catch illegal argument exception
    		                System.out.print("Invalid input "); 
    		         //       c.next();
    		            }  
    				  catch (InputMismatchException e3) {
  					    System.out.print(e3.getMessage()); //try to find out specific reason
  					}
    			}
    			
    			//set image quality to user settings
    			param.setDeflateLevel(quality);
    			
    			/*
    			 * user manually type a new name for new image file
    			 */
    			System.out.print("Name of new image file?");
    			Scanner d = new Scanner(System.in); 
    			String choice = d.nextLine();
    			
    			// display in new window
    			// GUI
    			JPanel content = new JPanel();
    			content.setLayout(new FlowLayout());
    			// label to load image
    			content.add(new JLabel(new ImageIcon(image)));
    			JFrame f = new JFrame("compressed Image ");
    			
    			f.pack(); // pack the frame
    			f.setSize(800,800); // set size of new window  		  
    			JScrollPane pane = new JScrollPane(content); // create a scrollbar
    			f.add(pane); // add scrollbar to window
    			f.setVisible(true); // set the GUI frame visible  
      			
    	/**
    	 * save compressed image file		
    	 */
    				
    	File compressedImageFile = new File(choice +".tif"); // save new compressed image file
		OutputStream out;
		try {
			out = new FileOutputStream(compressedImageFile);
			ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", out, param);
			try {
				//encode the output stream
				encoder.encode(image);
			} catch (IOException e1) { // catch IO and file not found exceptions
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		
		// print out compression level to console

		param.getDeflateLevel();
		System.out.println("Deflate level compression is " +param.getDeflateLevel()*10 + "%");
    			
        	 break;    
        	        	
        	 /**
        	  * function to decompress image
        	  */
        	 
         case "decompress image": 
        	 TIFFEncodeParam param2 = new TIFFEncodeParam();			
 			// deflate is a lossless compression
 			param2.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE); // set compression
 			
 			// set compression quality to 0 to decompression  
 			param2.setDeflateLevel(0);
 			
 			/*
 			 * user manually type a name for new image file
 			 */
 			System.out.print("Name of new image file?");
			Scanner de = new Scanner(System.in); 
			String choice2 = de.nextLine();
  			
			System.out.println("Image saved as " +choice2 +".tif");
 			System.out.println("Image has been decompressed");
 			
 		// display in new window
			// GUI
			JPanel content2 = new JPanel();
			content2.setLayout(new FlowLayout());
			// label to load image
			content2.add(new JLabel(new ImageIcon(image)));
			JFrame f2 = new JFrame("decompressed Image ");
			
			f2.pack(); // pack the frame
			f2.setSize(800,800); // set size of new window  		  
			JScrollPane pane2 = new JScrollPane(content2); //create a scrollbar
			f2.add(pane2); //add scrollbar to window
			f2.setVisible(true); //set window visible
 			
 			/**
 			 * save decompressed image file
 			 */
 			
 	    	File compressedImageFile2 = new File(choice2 +".tif"); // save new decompressed image file
 			OutputStream out2;
 			try {
 				out2 = new FileOutputStream(compressedImageFile2);
 				// encode the output stream
 				ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", out2, param2);
 				try {
 					encoder.encode(image);
 				} catch (IOException e1) { // catch IO and file not found exceptions
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} 
 			} catch (FileNotFoundException e2) {
 				// TODO Auto-generated catch block
 				e2.printStackTrace();
 			}

       	 break;       
     }
     
  }
  

/**
* creates the menu bar and the drop down options
**/

  public JMenuBar createMenuBar() // create top menu bar
  {
     JMenuBar menuBar;
     JMenu menu,menu2;
     JMenuItem menuItem,menuItem2;
  
       //Create the menu bar
     menuBar = new JMenuBar();
  
       //Build the first menu
     menu = new JMenu("File");
     menuBar.add(menu);
     
     menu2 = new JMenu("Image");
     menuBar.add(menu2);
  
       //group of JMenuItems
     menuItem = new JMenuItem("Open");
  	// Add an action handler to this menu item
     menuItem.addActionListener(this);
     menu.add(menuItem);
  
     menuItem = new JMenuItem("Save");
  	// Add an action handler to this menu item
     menuItem.addActionListener(this);
     menu.add(menuItem);
     
     menuItem = new JMenuItem("Exit");
  	// Add an action handler to this menu item
     menuItem.addActionListener(this);
     menu.add(menuItem);
     
     // 2nd menu bar
     menuItem = new JMenuItem("compress image");
  	// Add an action handler to this menu item
     menuItem.addActionListener(this);
     menu2.add(menuItem);
     
     menuItem = new JMenuItem("decompress image");
  	// Add an action handler to this menu item
     menuItem.addActionListener(this);
     menu2.add(menuItem);
     
     return menuBar;
  }
  
  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event dispatch thread.
   */

  private static void createAndShowGUI() {
	   GUI window = new GUI();
	      window.setJMenuBar(window.createMenuBar());
	      window.setBounds(1, 1, 900, 900); // set maximum fixed dimensions
	      window.setDefaultCloseOperation(EXIT_ON_CLOSE); // exit on click x
	      JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	      JScrollPane thePane = new JScrollPane();
	     // window.add(thePane);
	    //  window.setContentPane(pane);
	      window.setVisible(true);
	      	  
  }
  
  /**
   * main function
   * @param args
   */
  public static void main(String[] args) 
  {
	   //Schedule a job for the event dispatch thread:
      //creating and showing this application's GUI.
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              //Turn off metal's use of bold fonts
              UIManager.put("swing.boldMetal", Boolean.FALSE);
              createAndShowGUI();
          }
      });
  }

/**
* getters and setters
* @return/set i
*/
public static Image getI() {
	return i;
}

public static void setI(Image i) {
	GUI.i = i;
}
}


