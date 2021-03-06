import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*; 
import javax.swing.*; 

public class DrawTour extends Frame{

	public static int[] bestPathIndex;
	public static int noCities;
	public static double tourCost;
	public static String filename;
	public static double[][] cityCoordinates;
	
	public DrawTour(int[] pathindex, int cities, double cost, String file){
		this.bestPathIndex = pathindex;		
		this.noCities = cities;
		this.tourCost = cost;
		this.filename = file;
	}
	
	public int[] getPathIndex(){
		return bestPathIndex;		
	}
	
	public int getNoOfCities(){
		return noCities;		
	}
	
	public void printPath(){
		
		System.out.print("Path index of best route : ");
		for(int i = 0; i < bestPathIndex.length; i++){
			System.out.print(bestPathIndex[i] + " ");
		}
		System.out.print("\n");
		System.out.println("FileName of Coordinates " + filename);
		System.out.println("No of cities " + noCities);
		System.out.println("The tourcost is " + tourCost);
		
	}
	
	public void setCityCoordinates() throws IOException{
		cityCoordinates = new double[noCities][2];
		
		FileInputStream fstream = new FileInputStream(filename);
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		String strLine;
		int index = 0;
  
		  while ((strLine = br.readLine()) != null)   {
			  strLine = strLine.trim();
			  
			  Scanner line = new Scanner(strLine);
			  
			  double xcoor = line.nextDouble();
			  double ycoor = line.nextDouble();

			  cityCoordinates[index][0] = xcoor;
			  cityCoordinates[index][1] = ycoor;
			  
			  line.close();
			  //System.out.print(xcoor + ", " + ycoor + "; ");
			  index++;
          }
	}
	
	public void printCityCoordinates(){
		 for(int i = 0; i < noCities; i++){
			 System.out.print(cityCoordinates[i][0] + ", " + cityCoordinates[i][1] + "; ");
		  }
	}
	
	public void main() throws IOException{
		//printPath();
		setCityCoordinates();
		//printCityCoordinates();
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		  	});
		
		String str = "Best ACO route for " + noCities + " cities (distance = " + tourCost + ")";
		
		Label textLabel = new Label(str,SwingConstants.NORTH); 
		textLabel.setFont(new Font("Serif", Font.PLAIN, 23));
	    textLabel.setPreferredSize(new Dimension(20, 20)); 
	    this.add(textLabel, BorderLayout.NORTH); 
		
	
	    this.setSize(1000, 1000);
	    this.setVisible(true);
		
	}

	 public void paint(Graphics g) {
		  Graphics2D graph = (Graphics2D)g;
		  
		  //put city text
		  graph.setColor(Color.blue);
		  graph.setFont(new Font(null, Font.PLAIN, 20));
		  //graph.scale(50.0, 50.0);
		  //graph.drawString("Test", 25, 35);
		  
		  /////////Draw Cities///////////////
		  for(int i = 0; i < noCities; i++){
			//50 for 25 cities
			 // 
			  float xcoor = (float)(cityCoordinates[i][0] * (70) + 30);
			  float ycoor = (float)(cityCoordinates[i][1] * (70) + 70);
			  
			  graph.setColor(Color.blue);
			  graph.setStroke(new BasicStroke(5));
			  graph.draw(new Line2D.Double(xcoor, ycoor, xcoor, ycoor));

			  //graph.drawLine(xcoor, ycoor, xcoor, ycoor);
			  graph.setStroke(new BasicStroke(1));
			  graph.setColor(Color.black);
			  String cityName = "" + (i + 1);
			  graph.drawString(cityName, xcoor, ycoor);
		  }
		  /////////////////Cities drawn//////////////////////////
		  
		  graph.setColor(Color.blue);
		  ////Draw Tour////////////////
		  for(int i = 0; i < bestPathIndex.length - 1; i++){
			  int city1 = bestPathIndex[i];
			  int city2 = bestPathIndex[i+1];
			  
			  float xcity1 = (float)(cityCoordinates[city1][0]*70 + 30);
			  float ycity1 = (float)(cityCoordinates[city1][1]*70 + 70);
			  
			  float xcity2 = (float)(cityCoordinates[city2][0]*70 + 30);
			  float ycity2 = (float)(cityCoordinates[city2][1]*70 + 70);
			  
			  graph.draw(new Line2D.Double(xcity1, ycity1, xcity2, ycity2));
		  }
		  //Tour drawn
		  
		  
		  
		  
	 }//end of paint method
	
	
	
}
	
	
	


