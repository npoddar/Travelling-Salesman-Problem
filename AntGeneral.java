import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.io.*;

import javax.imageio.ImageIO;

import sun.awt.image.codec.JPEGImageEncoderImpl;


public class AntGeneral {
	
	public static int cities;
	public static double[][] h1;
	public static double[][] tao;
	public static double[] tourlength;
	public static double lbest = 900000000.0;		//best path cost
	public static int[] path;	//best ant path'
	public static int iteration = 0;
	public static int alpha = 0;
	public static int beta = 50;     
	public static int time = 1000;
	//public static int startCity = 2;
	public static double forgettingfactor = 0.5; //was 0.3 before
	public static int Q = 100;
	public static int factor = 1;
	public static String filename = "";
	public static double[][] cityCoordinates;
	public static int b = 5;
	public static int renewRate = 10; //was 30 before //20 gave good results

	/*
	 * @param args
	 */
    
        
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		cities = 20;
		time = 1000;
		
		System.out.print("Enter the number of cities you want to visit :");
		Scanner in = new Scanner(System.in);
		cities = in.nextInt();
		//in.close();
		//System.out.println("No of cities you want to visit are " + cities);
		
				
		//System.out.println();
		System.out.print("Enter the number of iterations you want the ACO algorithm to run in milliseconds : ");
		//Scanner in2 = new Scanner(System.in);
		time = in.nextInt();
		
		in.nextLine();
		
		System.out.print("Enter the file that contains the city coordinates :");
		filename = in.nextLine();
		filename = filename.trim();
		
		in.close();
		
		cityCoordinates = new double[cities][2];
		
		//System.out.println("Name of file : " + filename);	
		
		String fitnessFile = "Fitness" + cities + ".txt";
		FileWriter outputStream = new FileWriter(fitnessFile);
		BufferedWriter out = new BufferedWriter(outputStream);
		
		FileWriter taoOutput = new FileWriter("Tao.out");
		BufferedWriter taoOut = new BufferedWriter(taoOutput);
		
		  FileInputStream fstream = new FileInputStream(filename);
		  // Get the object of DataInputStream
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
		  
		  fstream.close();
		  data.close();
		  br.close();
		  
		  for(int i = 0; i < cities; i++){
			 // System.out.print(cityCoordinates[i][0] + ", " + cityCoordinates[i][1] + "; ");
		  }
		  //System.out.println("Index = " + index);
		  //System.out.print("\n");
			  
		  
		//in.close();
		//System.out.println("You want the algorithm to run for " + time + " milliseconds.");
		//System.out.println();
		
		
		
		h1 = new double[cities][cities];
		tourlength = new double[cities];
		path = new int[cities + 1];
		
	/*	
		 Random generator1 = new Random();
		    for(int i = 0 ; i < cities; i++){
		    	for(int j = 0; j < cities; j++){
		    		h1[i][j] = (int) (generator1.nextDouble() * 100.0 + 50);
		    		}	
		    }
		
	for (int i=0; i < cities ; i++){
		for (int j=0; j < cities; j++)
			h1[i][i]= 0;
	}
	
	for (int i=0; i < cities; i++){
		for (int j=0; j < cities; j++){
			h1[i][j]= h1[j][i];}
	}    
		*/
	
		/////////////Calculating Distance///////////////
		for(int i = 0 ; i < cities; i++){
	    	for(int j = 0; j < cities; j++){
	    		
	    		double xLength = cityCoordinates[i][0] - cityCoordinates[j][0];
	    		xLength = xLength*xLength;
	    			    		
	    		double yLength = cityCoordinates[i][1] - cityCoordinates[j][1];
	    		yLength = yLength*yLength;

	    		double sum = xLength + yLength;
	    		
	    		h1[i][j] = Math.sqrt(sum);

	    		}	
	    }
		
		for(int i = 0 ; i < cities; i++){
	    	for(int j = 0; j < cities; j++){
	    		//System.out.println("Distance : City(" + i + ", " + j + ") = " + h1[i][j]);
	    		
	    	}
		}
		
	//////////////////////////////////////////////////
	//initialize tao//
	/////////////////////////////////////////////////
	
	tao = new double[cities][cities];
	Random generator = new Random();
	
	for(int i = 0; i < cities; i++){
		for (int j=0; j < cities; j++){
			tao[i][j]= 500.0 * generator.nextDouble() + 100;
		}
    	}
    
    for (int i=0; i < cities; i++){
		for (int j=0; j < cities; j++)
			tao[i][i]= 0;
	}
	
	for (int i=0; i < cities; i++){
		for (int j=0; j < cities; j++){
			//tao[i][j]= tao[j][i];
			}
	}	
		
	
	
	//Run NN algorithm//
	
	//long startNN = System.currentTimeMillis();
	//double cost = nearestNeighbour();
	//long elapsedTimeMillisNN = System.currentTimeMillis()-startNN;
	//System.out.println("\nRunning time of Nearest Neighbour algorithm : " + elapsedTimeMillisNN + "ms");

	System.out.println("");
	///////////////////////////////
	
	// The number of ants are equal to no of cities
	int[] ant = new int[cities];
	int t = 0;

	long start = System.currentTimeMillis();
	long elapsedTimeMillis = 0;
	
	while ((iteration < time))
	{
		if(iteration == 10){
				alpha = 1;
				beta = 5;
		}
		/*
		if(iteration == 40){
			alpha = 1;
			beta = 4;
		}
		if(iteration == 50){
			alpha = 2;
			beta = 3;			
		}
		if( (iteration % 100)== 0) {
			if(alpha <15 || beta < 15){
				alpha++;
				beta++;
				System.out.println("Alpha beta increase case");
			}
		}
		*/
	    	//iteration++;
	    	double[][] taocp = new double[cities][cities];
	    	for (int cp= 0; cp < cities; cp++){
	    		for (int cp1 = 0; cp1 < cities; cp1++){
	    			taocp[cp][cp1] = 0.0;
	    		}
	    	}
	    	
			Random cityRandom = new Random(19580427);

			
		for(int i= 0; i < cities; i++)		//one for each ant
		{
			int[] citylist = new int[cities + 1];
			int nextcity =  cities + 200; 
			
			int startCity;
		
			startCity = cityRandom.nextInt(cities);
			
			//System.out.println("StartCity "+ startCity);
			
			citylist[0] = startCity;
			
			int noCities = 1;
			
			for(int j = 1; j < cities ; j++){	
				
				nextcity = plexProbability(citylist, noCities);
				noCities++;
							
				citylist[j] = nextcity;	//add nextcity to list of cities visited
				//return the next city to be visited after doing probability calculation
	
				
			}//a tour created for one ant

			citylist[cities] = startCity;
			
			double length = eval(citylist);
			tourlength[i] = length;
			//System.out.println("Length of current tour : " + length);

			if (length < lbest){
				lbest = length;
				//System.out.println("The best length so far " + lbest);
				//adding the path//
				for(int p=0; p < cities + 1; p++){
					int cityindex = citylist[p];
					path[p] = cityindex;
				}
				
			}
			
			tourlength[i] = length;
				
			//One ant deposits pheromone on each edge it travels through//
			for(int phr = 0; phr < cities - 1; phr++){
				int citycurr = citylist[phr];
				int citynext = citylist[phr+1];
					taocp[citycurr][citynext] = taocp[citycurr][citynext] + Q/tourlength[i];
			}
			
			//
			
			} //ant loop ends
			
			/////////////////////////Update pheromone//////////////
			////////////////////////////////////////////////////
			
			//tao updated//						
			for(int c = 0; c < cities; c++){
				for(int c1 = 0; c1 < cities; c1++){
					double taoOld = tao[c][c1];
					//System.out.println("Tao old " + taoOld);
					tao[c][c1] = (1 - forgettingfactor)*tao[c][c1] + taocp[c][c1];
			
					if((iteration % renewRate) == 0)
					{
						if(tao[c][c1]  < 80){
							tao[c][c1] = tao[c][c1] * 100;
						}
						else{
							//tao[c][c1] = tao[c][c1] + 300;
						}
					}
						
					//System.out.println("Tao new " + tao[c][c1]);
					//System.out.println("Tao diff " + (taoOld - tao[c][c1]));
					
					
				}				
			}
			
			taoOut.write("IterationTest = " + iteration + "\n");
			for(int i = 0; i < cities ; i++)
			{	
				int city1 = path[i];
				int city2 = path[i+1];
			tao[city1][city2] = tao[city1][city2] + (Q / lbest)*b;
			//taoOut.write(city1 + " to " + city2 + " = " + tao[city1][city2] + "\n");
			
			}
			/*
			for(int i = 0; i < cities; i++){
				for(int j = 0; j < cities; j++){
					taoOut.write("" + i + " to " + j + " = " + tao[i][j] + "\n");
				}
			}
			*/
			taoOut.write("\n\n");
		
			t = t + 1;
		
		elapsedTimeMillis = System.currentTimeMillis()-start;
		
		///////////Print out lbest and iteration number to output file/////
		
		
		out.write("Fitness: " + lbest + " iteration: " + iteration + " \n");
		iteration++;
	
	}//while loop
	
	taoOut.close();
	taoOutput.close();
	out.close();
	outputStream.close();
	

	elapsedTimeMillis = System.currentTimeMillis()-start;
		
		System.out.println("The best tour cost of ACO algorithm is :" + lbest);
		System.out.print("The ACO algorithm's tour :");
		for(int test1 = 0; test1 < cities+1; test1++)
		{
			System.out.print(" " + path[test1]);
		}
		System.out.println("\nRunning time of ACO algorithm in milliseconds : " + elapsedTimeMillis);

		System.out.println("\n");
				
		Frame path1 = new DrawCities(path, cities, lbest, filename);
		//path1.printPath();
		((DrawCities) path1).main();
		
		//////////////////printing frame to image ///////////
		
		String imagefile1 = "Image" + cities + "cities.png";
		BufferedImage awtImage = new BufferedImage(path1.getWidth(),path1.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics g = awtImage.getGraphics();
		path1.printAll(g);
		File outputfile = new File(imagefile1);
	    ImageIO.write(awtImage, "png", outputfile);

	    /////printing frame to image file/////////
		Frame path2 = new DrawTour(path, cities, lbest, filename);
		//path1.printPath();
		((DrawTour) path2).main();
		String imagefile2 = "Tour" + cities + "cities.png";
		BufferedImage awtImage1 = new BufferedImage(path2.getWidth(),path2.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics g1 = awtImage1.getGraphics();
		path2.printAll(g1);
		File outputfile1 = new File(imagefile2);
	    ImageIO.write(awtImage1, "png", outputfile1);
		
		//path1.setCityCoordinates();
		//path1.printCityCoordinates();
		
		/*path1.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent we){
					System.exit(0);
				}
			  	});
		path1.setSize(300, 300);
			  //frame.pack();
		path1.setVisible(true);
		*/
		
	
	}//end of main
	

public static double eval(int[] citylist){
	
		
		double distance = 0.0;
		for(int i = 0; i < cities ; i++){
			
			int cityPrev = citylist[i];
			int cityNext = citylist[i+1];
			
			distance = distance + h1[cityPrev][cityNext];
			
		}
		
		
		return distance;
	}//end of eval
	
	
	public static int plexProbability(int[] citylist, int noCities )
	{
		int lastcity = citylist[noCities - 1];
		
		
		boolean[] visited = new boolean[cities];
		for(int i = 0; i < cities; i++){
			visited[i] = false;
		}
		
		for(int i = 0; i < noCities; i++){
			int city = citylist[i];
			visited[city] = true;			
		}
		
		//lastcity is lastcity;
		//Now we calculate the denominator
		//noCities is no of cities visited
		
		//denominator calculation//
		//sumprod will be calculated iteratively
		
		double sumprod = 0.0;
		double taoval = 0.0;
		double distval = 0.0;

		
		for(int i = 0; i < cities; i++){
			//if city i is unvisited the do :
			if(visited[i] == false){
				taoval = tao[lastcity][i];
		///////////////////////////////////////////////////////////
				distval = h1[lastcity][i];
				distval = 800 / distval;
				taoval = Math.pow(taoval, alpha) * 1.0;
				if(taoval == 0.0){
					//System.out.println("Taoval in plexProbability is 0.0");
					taoval = 1.0;	
				}
				distval = Math.pow(distval, beta) * 1.0;
				
				
				sumprod = sumprod +  (taoval*distval);
				//System.out.println("Sumprod calculating : " + sumprod);
			}
		}
		
		
		
		if(sumprod == 0.0){
			
			for(int i = 0; i < cities; i++){
				for(int j = 0; j<cities; j++){
				}
			}
		}
		
		
		//find the highest probable city//
		//maxProb is the maximum probability so far
		//maxCity is the index of max probability city so far
		//currProb is current city's probability of being picked
		
		double maxProb = 0.0;
		int maxCity =  100;
		double currProb = -1.0 ;
		double sumCurrProb = 0;
		
		
		//implementing random probability walks//
	    Random generator = new Random();
		boolean found = false;
		while(found == false)
		{
		
				for(int i = 0; i < cities; i++){
					
					if(found == false){

					//if city is unvisited//
					if(visited[i] == false){
						taoval = tao[lastcity][i];
						distval = h1[lastcity][i];
						distval = 800 / distval;
						if(taoval == 0.0){
							taoval = 1.0;	
						}
						taoval = Math.pow(taoval, alpha);
						distval = Math.pow(distval, beta);
						currProb = ((taoval)*(distval)) / sumprod;
				
						double randomProb = generator.nextDouble();
						if(randomProb < currProb){
							found = true;
							maxCity = i;
			    	
						}
			    
			    
						sumCurrProb = sumCurrProb + currProb;
				
				
			}
		} 

					
	}
				
  } 
		
		//finished implementing random prob walks
		
		
		if(maxCity == cities + 100){

			for(int i = 0; i < cities; i++){
				//System.out.println("Visited city " + i + "- " + visited[i]);
			}
			
			
			for(int i = 0; i < cities; i++){
				if(visited[i] == false){
					System.out.println("Default returning city " + i);
					return i;	
				}
			}
			
			
		}
		
		
		return maxCity;
		
	}
	
	/*
	public static double nearestNeighbour(){

		int currCity = startCity;
		
		//decides whether the city has been visited or not//
		boolean[] visited = new boolean[cities];
		for(int i = 0; i<cities; i++)
		{
			visited[i] = false;
		}
		visited[currCity] = true;
		
		//the path as a list//
		List<Integer> path = new ArrayList<Integer>();
		path.add(currCity);
		
		for(int i = 0; i < cities - 1; i++){
			double closestDist = 9000000000.0;
			for(int j = 0; j < cities; j++){
				if(visited[j] == false){
					if (h1[currCity][j] < closestDist){
						closestDist = h1[currCity][j];
						currCity = j;
					}
				}
			}// all unvisited cities have been compared against current city
			visited[currCity] = true;
			path.add(currCity);
		} // all cities should be visited by now
		
		Iterator it = path.iterator();
		
		//System.out.println("\nNearest neighbour output \n");
		
		int[] cityList = new int[cities];
		int i = 0;
		while(it.hasNext()){
			Integer next = (Integer)it.next();
			cityList[i] = next.intValue();
			i++;
		}
		
		double cost = eval(cityList);
		
		System.out.println("\nNearest Neighbour Tour Cost : " + cost);
		
		System.out.print("Nearest Neighbour Tour :");
		Iterator it1 = path.iterator();
		while(it1.hasNext()){
			System.out.print(" " + it1.next());			
		}
		
		return cost;
		
	}*/	
} //end of class//
