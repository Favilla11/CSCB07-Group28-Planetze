package com.example.cscb07_project;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver {
	public static void main(String [] args) {
		String file = "/Users/ronaldng/eclipse-workspace/review/bin/review/formulas.csv";
		BufferedReader reader = null;
		String line = "";
		ArrayList<String[]> graph = new ArrayList<String[]>();
		try {
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				graph.add(line.split(","));
			}
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		finally {
			try {
				reader.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		//gets all info from formula sheet to get carbon emissions
		HashMap<Integer, String> storedAnswer = new HashMap<>();
		double footprint = 0;
		
		if(storedAnswer.get(1).equals("Yes"))
		{
			double emmision = 0;
			if(storedAnswer.get(2).equals("Gasoline"))
			{
				emmision = 0.24;
			}
			else if(storedAnswer.get(2).equals("Diesel"))
			{
				emmision = 0.27;
			}
			else if(storedAnswer.get(2).equals("Hybrid"))
			{
				emmision = 0.16;
			}
			
			else if(storedAnswer.get(2).equals("Electric"))
			{
				emmision = 0.05;
			}
			
			double dist = 0;
			
			if (storedAnswer.get(3).equals("<= 5,000 km"))
			{
				dist = 5000;
			}
			
			else if (storedAnswer.get(3).equals("5,000–10,000 km"))
			{
				dist = 10000;
			}
			else if (storedAnswer.get(3).equals("10,000–15,000 km"))
			{
				dist = 15000;
			}
			else if (storedAnswer.get(3).equals("15,000–20,000 km"))
			{
				dist = 20000;
			}
			
			else if (storedAnswer.get(3).equals("20,000–25,000 km"))
			{
				dist = 25000;
			}
			else if (storedAnswer.get(3).equals(">=25,000 km"))
			{
				dist = 35000;
			}
			footprint = footprint + dist*emmision;
		}

		//gets carbon emissions from driving
		int column = 0;
		int row = 0;
		boolean transport = false;
		
		if (storedAnswer.get(4).equals("Occasionally (1-2 times/week)"))
		{
			column = 1;
			transport = true;
		}
		
		else if (storedAnswer.get(4).equals("Frequently (3-4 times/week)")|| storedAnswer.get(4).equals("Always (5+ times/week)"))
		{
			column = 2;
			transport = true;
		}
		
		if(storedAnswer.get(5).equals("Under 1 hour"))
		{
			row = 1;
		}
		else if(storedAnswer.get(5).equals("1-3 hours"))
		{
			row = 2;
		}
		else if(storedAnswer.get(5).equals("3-5 hours"))
		{
			row = 3;
		}
		else if(storedAnswer.get(5).equals("5-10 hours"))
		{
			row = 4;
		}
		else if(storedAnswer.get(5).equals("More than 10 hours"))
		{
			row = 5;
		}
		//assigns column based on frequency and row on time to find the right value from datasheet
		if(transport)
		{
			footprint = footprint + Double.parseDouble(graph.get(row)[column]);
		}

		if(storedAnswer.get(6).equals("1-2 flights"))
		{
			footprint = footprint + 225;
		}
		
		else if(storedAnswer.get(6).equals("3-5 flights"))
		{
			footprint = footprint + 600;
		}
		
		else if(storedAnswer.get(6).equals("6-10 flights"))
		{
			footprint = footprint + 1200;
		}
		else if(storedAnswer.get(6).equals("More than 10 flights"))
		{
			footprint = footprint + 1800;
		}
		
		if(storedAnswer.get(7).equals("1-2 flights"))
		{
			footprint = footprint + 825;
		}
		
		else if(storedAnswer.get(7).equals("3-5 flights"))
		{
			footprint = footprint + 2200;
		}
		
		else if(storedAnswer.get(7).equals("6-10 flights"))
		{
			footprint = footprint + 4400;
		}
		else if(storedAnswer.get(7).equals("More than 10 flights"))
		{
			footprint = footprint + 6600;
		}
		//gets carbon footprint from flights

		if(storedAnswer.get(8).equals("Vegetarian"))
		{
			footprint = footprint + 1000;
		}
		
		else if(storedAnswer.get(8).equals("Vegan"))
		{
			footprint = footprint + 500;
		}
		
		else if(storedAnswer.get(8).equals("Pescatarian (fish/seafood)"))
		{
			footprint = footprint + 1500;
		}
		else {
			String[] meat = storedAnswer.get(9).split(":");
			boolean eat = false;
			for(int i = 1; i <= 4; i++)
			{
				//gets the carbon value each of meat
				if(meat[i].substring(1,2).equals("D"))
				{
					column = 4;
					eat = true;
				}
				
				else if(meat[i].substring(1,2).equals("F"))
				{
					column = 5;
					eat = true;
				}
				else if(meat[i].substring(1,2).equals("O"))
				{
					column = 6;
					eat = true;
				}
				//gets appropriate carbon emission value based on frequency
				if(eat)
				{
					footprint = footprint + Double.parseDouble(graph.get(i)[column]);
				}
			}
		}
		
		if(storedAnswer.get(10).equals("Rarely"))
		{
			footprint = footprint + 23.4;
		}
		
		else if(storedAnswer.get(10).equals("Occasionally"))
		{
			footprint = footprint + 70.2;
		}
		
		else if(storedAnswer.get(10).equals("Frequently"))
		{
			footprint = footprint + 140.4;
		}



		boolean energy = true;
		
		
		
		if(storedAnswer.get(11).equals("Detached house"))
		{
			row = 7;
		}
		
		else if(storedAnswer.get(11).equals("Semi-detached house"))
		{
			row = 25;
		}
		
		else if(storedAnswer.get(11).equals("Townhouse"))
		{
			row = 43;
		}
		
		else if(storedAnswer.get(11).equals("Condo/Apartment"))
		{
			row = 61;
		}
		
		if(storedAnswer.get(12).equals("2"))
		{
			row = row + 1;
		}
		
		else if(storedAnswer.get(12).equals("3-4"))
		{
			row = row + 2;
		}
		
		else if(storedAnswer.get(12).equals("5 or more"))
		{
			row = row + 3;
		}
		
		if(storedAnswer.get(13).equals("1000-2000 sq. ft."))
		{
			row = row + 6;
		}
		
		else if(storedAnswer.get(13).equals("Over 2000 sq. ft."))
		{
			row = row + 12;
		}
		
		if(storedAnswer.get(14).equals("Natural Gas"))
		{
			column = 2;
		}
		
		else if(storedAnswer.get(14).equals("Electricity"))
		{
			column = 3;
		}
		
		else if(storedAnswer.get(14).equals("Oil"))
		{
			column = 4;
		}
		
		else if(storedAnswer.get(14).equals("Propane"))
		{
			column = 5;
		}
		
		else if(storedAnswer.get(14).equals("Wood"))
		{
			column = 6;
		}
		
		if(storedAnswer.get(14).equals("Other"))
		{
			energy = false;
		}
		
		
		if(storedAnswer.get(15).equals("$50-$100"))
		{
			column = column + 5;
		}
		else if(storedAnswer.get(15).equals("$100-$150"))
		{
			column = column + 10;
		}
		else if(storedAnswer.get(15).equals("$150-$200"))
		{
			column = column + 15;
		}
		else if(storedAnswer.get(15).equals("Over $200"))
		{
			column = column + 20;
		}
		
		if(energy)
		{
			footprint = footprint + Double.parseDouble(graph.get(row)[column]);
		}

		//uses all data to find footprint of housing, using all factors for row and column
		
		if(!storedAnswer.get(14).equals(storedAnswer.get(16)))
		{
			footprint = footprint + 233;
		}
		
		if(storedAnswer.get(17).equals("Yes, primarily (more than 50% of energy use)"))
		{
			footprint = footprint - 6000;
		}
		
		else if(storedAnswer.get(17).equals("Yes, partially (less than 50% of energy use)"))
		{
			footprint = footprint - 4000;
		}
		double product = 0;
		boolean reduct = true;
		if(storedAnswer.get(18).equals("Monthly"))
		{
			product = 360;
			row = 79;
		}
		
		else if(storedAnswer.get(18).equals("Quarterly"))
		{
			product = 120;
			reduct = false;
		}
		
		else if(storedAnswer.get(18).equals("Annually"))
		{
			product = 100;
			row = 80;
		}
		
		else if(storedAnswer.get(18).equals("Rarely"))
		{
			product = 5;
			row = 81;
		}
		
		if(storedAnswer.get(19).equals("Yes, regularly"))
		{
			product = product*0.5;
		}
		
		else if(storedAnswer.get(19).equals("Yes, occasionally"))
		{
			product = product*0.3;
		}
		//gets footprint of purchases
		footprint = footprint + product;
		
		if(storedAnswer.get(20).equals("1"))
		{
			footprint = footprint + 300;
		}
		
		else if(storedAnswer.get(20).equals("2"))
		{
			footprint = footprint + 600;
		}
		
		else if(storedAnswer.get(20).equals("3 or more"))
		{
			footprint = footprint + 1200;
		}
		
		if(storedAnswer.get(21).equals("Occasionally"))
		{
			
			column = 1;
		}
		
		else if(storedAnswer.get(21).equals("Frequently"))
		{
			column = 2;
		}
		
		else if(storedAnswer.get(21).equals("Always"))
		{
			column = 3;
		}
		
		if(reduct)
		{
			footprint = footprint - Double.parseDouble(graph.get(row)[column]);
		}
		//gets carbon reduction due to recycling
		
		
	}
}
