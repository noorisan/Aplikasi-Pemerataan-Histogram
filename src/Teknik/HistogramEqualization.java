package Teknik;

import java.awt.image.BufferedImage;
import java.io.File;

import ImageUtilities.*;

public class HistogramEqualization implements Technique{
    private ImageUnpacker imageUnpacker;
    private int imageX,imageY;

    private short[][] inputRed;	//Array dua dimensi mewakili peta intensitas setiap warna - [x] [y]
    private short[][] inputGreen;
    private short[][] inputBlue;

    private short[][] outputRed;//Array dua dimensi mewakili peta intensitas setiap warna - [x] [y]
    private short[][] outputGreen;
    private short[][] outputBlue;

    @Override
    public float operation(File inputImageFile,int unusedParameter0,int unusedParameter1){
        clear();
        imageUnpacker=new ImageUnpacker(inputImageFile);

        long time=System.currentTimeMillis();

        inputRed=imageUnpacker.getRed();
        inputGreen=imageUnpacker.getGreen();
        inputBlue=imageUnpacker.getBlue();

        imageX=imageUnpacker.getX();
        imageY=imageUnpacker.getY();

        outputRed=new short[imageY][imageX];
        outputGreen=new short[imageY][imageX];
        outputBlue=new short[imageY][imageX];

        int histogramRed[]=new int[256];
        int histogramGreen[]=new int[256];
        int histogramBlue[]=new int[256];

        int cumulativeHistogramRed[]=new int[256];
        int cumulativeHistogramGreen[]=new int[256];
        int cumulativeHistogramBlue[]=new int[256];

        float tempRed,tempGreen,tempBlue,temp;

        for(int i=0;i<256;i++){	//Defaulting histogram menjadi nol
            histogramRed[i]=0;
            histogramGreen[i]=0;
            histogramBlue[i]=0;
        }

        for(int currentY=0;currentY<imageY;currentY++){
            for(int currentX=0;currentX<imageX;currentX++){
                histogramRed[inputRed[currentY][currentX]]++;
                histogramGreen[inputGreen[currentY][currentX]]++;
                histogramBlue[inputBlue[currentY][currentX]]++;
            }
        }

        for(int i=1;i<256;i++){	//Accumulating histogram
            cumulativeHistogramRed[i]=cumulativeHistogramRed[i-1]+histogramRed[i];
            cumulativeHistogramGreen[i]=cumulativeHistogramGreen[i-1]+histogramGreen[i];
            cumulativeHistogramBlue[i]=cumulativeHistogramBlue[i-1]+histogramBlue[i];
        }
        temp=255/(float)(imageY*imageX); //Constant, sangat dihitung sebelumnya

        for(int currentY=0;currentY<imageY;currentY++){	//Equalizing
            for(int currentX=0;currentX<imageX;currentX++){
                tempRed=(float)cumulativeHistogramRed[inputRed[currentY][currentX]]*temp;
                tempGreen=(float)cumulativeHistogramGreen[inputGreen[currentY][currentX]]*temp;
                tempBlue=(float)cumulativeHistogramBlue[inputBlue[currentY][currentX]]*temp;

                if(tempRed<0) 		tempRed=0; //Memastikan intensitas tidak berada di luar jangkauan
                if(tempRed>255) 	tempRed=255;
                if(tempGreen<0) 	tempGreen=0;
                if(tempGreen>255) 	tempGreen=255;
                if(tempBlue<0) 		tempBlue=0;
                if(tempBlue>255) 	tempBlue=255;

                outputRed[currentY][currentX]=(short)tempRed;
                outputGreen[currentY][currentX]=(short)tempGreen;
                outputBlue[currentY][currentX]=(short)tempBlue;
            }
        }
        System.out.println("HE: complete - time taken in seconds="+(((float)(System.currentTimeMillis()-time))/1000));
        return (((float)(System.currentTimeMillis()-time))/1000);
    }

    @Override
    public void clear(){
        try{
            imageX=0;
            imageY=0;
            for(int i=0;i<inputRed.length;i++){
                for(int j=0;j<inputRed[i].length;j++){
                    inputRed[i][j]=0;		
                    inputGreen[i][j]=0;
                    inputBlue[i][j]=0;

                    outputRed[i][j]=0;	
                    outputGreen[i][j]=0;
                    outputBlue[i][j]=0;
                }
            }
        }catch(NullPointerException nPE){
            System.err.println("HE: previously uninitated");
        }
    }

    @Override
    public File getFile(String fileName){
        return ImagePacker.packFile(imageY,imageX,outputRed,outputGreen,outputBlue,fileName);
    }

    @Override
    public BufferedImage getBufferedImage(){
        return ImagePacker.packBufferedImage(imageY,imageX,outputRed,outputGreen,outputBlue);
    }	

    @Override
    public int getWidth(){
        return imageX;
    }
    @Override
    public int getHeight(){
        return imageY;
    }
}