
package ds2application;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class GUI extends JPanel{
    
    private double[][] xCoordinates;
    private double[] set1, set2, set3, set4, set5;
    private double[] yCoordinates, xEuclidean, candidates,// = new double[xCoordinates.length],
            yEuclidean;// = new double[xCoordinates.length];
    private int mar=50;
    
    
    public void setCandidates(double [] cand){
        candidates = cand;
    }
    public void setg1(double[] coords){
        set1 = coords;
    }
    public void setg2(double[] coords){
        set2 = coords;
    }
    public void setg3(double[] coords){
        set3 = coords;
    }
    public void setg4(double[] coords){
        set4 = coords;
    }
    public void setg5(double[] coords){
        set5 = coords;
    }
    
    
    public void setXCoordinates(double[][] coords){
        xCoordinates = coords;
    }
    
    public void setYCoordinates(double[] coords){
        yCoordinates = coords;
    }
    
    protected void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D g1=(Graphics2D)g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int width=getWidth();
        int height=getHeight();
        g1.draw(new Line2D.Double(mar,mar,mar,height-mar));
        g1.draw(new Line2D.Double(mar,height-mar,width-mar,height-mar));
        double x=(double)(width-2*mar)/(xCoordinates.length);
        double scale= (double)(height-2*mar)/getMax();
      
        //we draw and plot all the points that are not zero(to actually have balanced mediods)
        //g1.setPaint(Color.RED);
        for(int i=0; i<5; ++i){
            if(i==0)
                g1.setPaint(Color.RED);
            if(i==1)
                g1.setPaint(Color.BLUE);
            if(i==2)
                g1.setPaint(Color.BLACK);
            if(i==3)
                g1.setPaint(Color.YELLOW);
            if(i==4)
                g1.setPaint(Color.GREEN);
            
            for(int j=0; j<xCoordinates.length; ++j){
            //if(xCoordinates[i][j] !=0){
            double x1 = mar+i*x;  
            double y1 = height-mar-scale*xCoordinates[i][j];  
            g1.fill(new Ellipse2D.Double(x1, y1, 100, 100)); 
            //}
            }
            }
        
    }
        //if(set1 != null || set2 != null || set3!= null){
//        for(int i=0; i<set1.length; i++){  
//            for(int j=0; j<set1.length; ++j){
//            if(set1[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set1[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//            }
//            }
//            }
//        g1.setPaint(Color.BLUE);
//        for(int i=0; i<set2.length; i++){  
//            for(int j=0; j<set2.length; ++j){
//            if(set2[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set2[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//            }
//            }
//            }
//        g1.setPaint(Color.BLACK);
//        for(int i=0; i<set3.length; i++){  
//            for(int j=0; j<set3.length; ++j){
//            if(set3[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set3[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//            }
//            }
//            }
//        g1.setPaint(Color.YELLOW);
//        for(int i=0; i<set4.length; i++){  
//            for(int j=0; j<set4.length; ++j){
//            if(set4[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set4[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//            }
//            }
//            }
//        g1.setPaint(Color.GREEN);
//        for(int i=0; i<set5.length; i++){  
//            for(int j=0; j<set5.length; ++j){
//            if(set5[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set5[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//            }
//            }
            
        //}
        //we now have arrays of every point on the plane, we will now randomly choose mediods and change them
        //as we need
//        mediods mediod = new mediods(8);
//        mediod.selectRandomMediods(g1, xEuclidean, yEuclidean, mar, height, width, getMax());
        
        
        //for each group lets paint the coordinates onto the plane
//        for(int i=0;i<set1.length;++i){
//            if(set1[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set1[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 10, 10)); 
//            }}
//            
//        g1.setPaint(Color.BLACK);
//        for(int i=0;i<set2.length;++i){
//            if(set2[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set2[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 10, 10)); 
//            }
//        }
//            
//       g1.setPaint(Color.GREEN);
//        for(int i=0;i<set3.length;++i){
//            if(set3[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set3[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 10, 10)); 
//            }}
//        g1.setPaint(Color.YELLOW);
//        for(int i=0;i<set4.length;++i){
//            if(set4[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set4[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 10, 10)); 
//            }}
//        g1.setPaint(Color.BLUE);
//        for(int i=0;i<set5.length;++i){
//            if(set5[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*set5[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 10, 10)); 
//            }}
//        
//            
//        for(int i=0; i<xCoordinates.length; i++){  
//            g1.setPaint(Color.RED);
//            if(xCoordinates[i] !=0){
//            double x1 = mar+i*x;  
//            double y1 = height-mar-scale*xCoordinates[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); }
//                else{
//                    double x1 = mar+i*x;  
//            double y1 = height-mar-scale*xCoordinates[i];  
//            g1.fill(new Ellipse2D.Double(x1, y1, 8, 8)); 
//                }
//            }
//          
        
        
        
    
    private double getMax(){
        
        
        double max=-Double.MAX_VALUE;
        for(int j=0;j<5;++j){
        for(int i=0;i<xCoordinates.length;i++){
            if(xCoordinates[j][i]>max)
                max=xCoordinates[j][i];
        }
           
        }return max;
    }       
}
