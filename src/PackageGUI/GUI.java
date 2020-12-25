package PackageGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class GUI extends JFrame implements ActionListener{
    JMenuBar menuBar;
    JMenu fileMenu,editMenu,helpMenu;									//	FM, EM & HM
    JMenuItem importFM,exportFM,hEEM,helpHM,creditHM;	//	Suffix denotes which menu it belongs to
    JFileChooser fileSelection;											//	Selecting file/folder to open/save from
    JLabel inputDisplay,outputDisplay,log;								//	Displaying the source, result & details about the operations
    File inputFile,outputFile,outputHistogramFile;						//	IO
    String output;														//	Used for deciding which technique's file building algorithm to use
    ImageFilter imageFilter;

    /*
     * 	Main method, simply calls a GUI into existence
     */
    public static void main(String args[]){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new GUI();
        } 
        catch (Exception e){
            System.err.println("Something went wrong with L&F");
        }		
    }

    public GUI(){	

        imageFilter=new ImageFilter();

        new BorderLayout();
        fileSelection=new JFileChooser();
        fileSelection.setAcceptAllFileFilterUsed(false);

        importFM=new JMenuItem("Import Gambar");
        exportFM=new JMenuItem("Export image");
        importFM.addActionListener(this);
        exportFM.addActionListener(this);
        fileMenu=new JMenu("File");
        fileMenu.add(importFM);
        fileMenu.add(exportFM);

        hEEM=new JMenuItem("Histogram equalization [HE]");
        hEEM.addActionListener(this);
        editMenu=new JMenu("Edit");
        editMenu.add(hEEM);

        helpHM=new JMenuItem("Help");
        creditHM=new JMenuItem("Tentang");
        helpHM.addActionListener(this);
        creditHM.addActionListener(this);
        helpMenu=new JMenu("Help");
        helpMenu.add(helpHM);
        helpMenu.add(creditHM);

        menuBar=new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        inputDisplay=new JLabel();
        inputDisplay.setPreferredSize(new Dimension(600,600));
        outputDisplay=new JLabel();
        outputDisplay.setPreferredSize(new Dimension(600,600));

        log=new JLabel(" No file chosen");
        log.setBorder(BorderFactory.createLineBorder(Color.black));

        this.add(menuBar,BorderLayout.PAGE_START);		
        this.add(inputDisplay,BorderLayout.LINE_START);	//Left for input image
        this.add(outputDisplay,BorderLayout.LINE_END);	//Right for output image
        this.add(log,BorderLayout.PAGE_END);

        this.setVisible(true);
        this.setSize(1200,700);
        this.setTitle("Aplikasi Pemerataan Histogram Oleh Isan DBC118018");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setInputImage(BufferedImage inputImageDisplay,int width,int height){
        try{
            float resultWidth=600,resultHeight=650;
            float ratio=(float)width/(float)height;

            System.out.println("GUI: "+ratio);
            if(height>650){
                    resultWidth=(600*ratio);		
            }
            else if(width>600){
                    resultHeight=(650*ratio);
            }
            inputDisplay.setIcon(new ImageIcon(inputImageDisplay.getScaledInstance((int)resultWidth,(int)resultHeight,Image.SCALE_SMOOTH)));
        }
        catch(NullPointerException nPE){	//	In case an ImageIcon cannot be formed from the file - likely wrong filetype
            log.setText("Invalid file: "+inputFile.getName());
            inputFile=null;
        }
    }

    public void setOutputImage(BufferedImage outputImageDisplay,int width,int height){
        float resultWidth=600,resultHeight=650;
        float ratio=(float)width/(float)height;

        if(height>650){
            resultWidth=(600*ratio);		
        }
        else if(width>600){
            resultHeight=(650*ratio);
        }
        outputDisplay.setIcon(new ImageIcon(outputImageDisplay.getScaledInstance((int)resultWidth,(int)resultHeight,Image.SCALE_SMOOTH)));
    }

    @Override
    public void actionPerformed(ActionEvent aE){
        try{
            if(aE.getSource()==importFM){		//	Import file to edit
                fileSelection.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileSelection.setAccessory(new ImagePreview(fileSelection));
                fileSelection.setFileFilter(imageFilter);
                int option=fileSelection.showOpenDialog(this);
                if(option==JFileChooser.APPROVE_OPTION){
                    inputFile=fileSelection.getSelectedFile();
                    BufferedImage inputBufferedImage=ImageIO.read(inputFile);
                    this.setInputImage(inputBufferedImage,inputBufferedImage.getWidth(),inputBufferedImage.getHeight());					
                    log.setText(" File chosen: "+inputFile.getName());
                    try{
                        this.setOutputImage(null,0,0);
                    }
                    catch(NullPointerException nPE){
                        System.err.println("GUI: null setting of output image");
                    }
                }
            }
            if((aE.getSource()==exportFM)&&(outputFile!=null)){	//	Export edited file
                fileSelection.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileSelection.setFileFilter(imageFilter);
                int option=fileSelection.showSaveDialog(this);

            }
            else if(inputFile!=null){			//	Operations

            }
            else if(inputFile==null){
                log.setText(" No file chosen");
                System.out.println("GUI: no file chosen");
            }
            if(aE.getSource()==helpHM){
                JOptionPane.showMessageDialog(null,"Langkah Dasar:\nImport Gambar [File>Import Gambar]\nPilih Operasi [Edit]","Help",JOptionPane.INFORMATION_MESSAGE);
            }
            else if(aE.getSource()==creditHM){
                JOptionPane.showMessageDialog(null,"Program ini dibuat oleh M. Noor Ihsan DBC 118 018","Tentang",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(IOException iOE){
                System.err.println("GUI: IOException in listening method");
                iOE.printStackTrace();
        }
    }	
}