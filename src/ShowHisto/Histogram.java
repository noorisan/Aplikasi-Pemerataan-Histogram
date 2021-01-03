package ShowHisto;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
/**
 *
 * @author ASUS
 */
public class Histogram {
    BufferedImage image;
    int width,height;
    public Histogram(BufferedImage img){
        try{
            this.image=img;
            this.width=img.getWidth();
            this.height=img.getHeight();
        }catch(Exception e){
                    System.out.println("Error\n"+e);
        }
    }
                    
    public int [] setHistogram(){
        int[]Histogram=new int [256];
        for(int x=0;x<height;x++){
            for(int y=0;y<width;y++){
                Color c=new Color(image.getRGB(y,x));
                int grey =(int)((c.getRed()+c.getGreen()+c.getBlue())/3);
                Histogram[grey]+=1;
            }
        }
        return Histogram;
    }
    
    public void openHist(int []data,String title){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int x=0;x<data.length;x++){
            dataset.addValue(data[x],""+x,"");
        }
        JFreeChart chart = ChartFactory.createBarChart(title, "", "Nilai", dataset, PlotOrientation.VERTICAL,false,true,false);
        ChartFrame frame = new  ChartFrame("Histogram", chart);
        frame.setSize(600,400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
            
            
        
    
            
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        Histogram hs = new Histogram ("isan.jpeg");
//        int[]data=hs.setHistogram();
//        hs.openHist(data,"Histogram");
//        // TODO code application logic here
//    }
    
    
}
