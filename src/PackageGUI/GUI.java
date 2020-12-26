package PackageGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import Teknik.*;

public class GUI extends JFrame implements ActionListener{
    JMenuBar menuBar;
    JMenu fileMenu,editMenu,helpMenu; //FM, EM & HM
    JMenuItem importFM,exportFM,hEEM,helpHM,creditHM; //Suffix untuk menu
    JFileChooser fileSelection;	//Memilih file/folder untuk membuka/menyimpan
    JLabel inputDisplay,outputDisplay,log; //Menampilkan sumber, hasil & detail operasi
    File inputFile,outputFile,outputHistogramFile; //IO
    String output; //Digunakan untuk memutuskan teknik mana yang akan digunakan algoritma pembuatan file
    ImageFilter imageFilter;

    HistogramEqualization histogramEqualisation;

    /*
     * Main method, simply calls a GUI into existence
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
        histogramEqualisation=new HistogramEqualization();

        imageFilter=new ImageFilter();

        new BorderLayout();
        fileSelection=new JFileChooser();
        fileSelection.setAcceptAllFileFilterUsed(false);

        importFM=new JMenuItem("Import image");
        exportFM=new JMenuItem("Export image");
        importFM.addActionListener(this);
        exportFM.addActionListener(this);
        fileMenu=new JMenu("File");
        fileMenu.add(importFM);
        fileMenu.add(exportFM);

        hEEM=new JMenuItem("Histogram equalisation [HE]");
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
        this.add(inputDisplay,BorderLayout.LINE_START); //input image disebelah kiri
        this.add(outputDisplay,BorderLayout.LINE_END); //output image disebelah kanan
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
        catch(NullPointerException nPE){ //Jika Ikon Gambar tidak dapat dibentuk dari file, kemungkinan besar jenis file salah
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
            if(aE.getSource()==importFM){ //Import file untuk diedit
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
            else if((aE.getSource()==exportFM)){ //Export file yang sudah diedit
                fileSelection.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileSelection.setFileFilter(imageFilter);
                int option=fileSelection.showSaveDialog(this);
                if(option==JFileChooser.APPROVE_OPTION){
                    outputFile=fileSelection.getSelectedFile();
                    if(output.equals("HE")){ //Membangun citra
                        outputFile=histogramEqualisation.getFile(outputFile.getPath());	
                    }
                    log.setText(" File exported: "+outputFile.getPath());
                }
            }

            else if(inputFile!=null){//Operasi
                if(aE.getSource()==hEEM){ //Histogram equalization
                    log.setText(" Please wait, perfoming histogram equalisation");
                    log.setText(" Histogram equalisation complete. Time taken: "+histogramEqualisation.operation(inputFile,0,0)+" seconds");
                    this.setOutputImage(histogramEqualisation.getBufferedImage(),histogramEqualisation.getWidth(),histogramEqualisation.getHeight());
                    output="HE";
                }
            }
            else if(inputFile==null){
                log.setText(" No file chosen");
                System.out.println("GUI: no file chosen");
            }
            if(aE.getSource()==helpHM){
                JOptionPane.showMessageDialog(null,"Langkah Dasar:\nImport Gambar [File>Import Gambar]\nPilih Operasi [Edit]\nSimpan Hasil Gambar [File>Export Gambar]","Help",JOptionPane.INFORMATION_MESSAGE);
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