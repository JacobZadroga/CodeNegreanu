package project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;


public class Main{
	
	/*
	 * Please note: Hands are written to the pokerhands file as <unique ID>, ranking
	 */
	public static void main(String[] args)
	{
		ArrayList<Hand> hands = new ArrayList<Hand>();
		try
		{
			File pokerHands = new File("pokerhands.txt");
			Scanner readFile = new Scanner(pokerHands);
			while(readFile.hasNextLine())
			{
				Hand hand = new Hand(readFile.nextLine());
				hands.add(hand);
			}
			readFile.close();
		}
		catch(FileNotFoundException error)
		{
		}
		
		Collections.sort(hands, new HandComparator());
		
		//Set to 15 to be a simply suit-agnostic ranking system
		int rank = 15;
		boolean changed = false;
		HandComparator compare = new HandComparator();
		
		for(int i = 0; i < hands.size(); i++)
		{
			//Once again, account for suit-agnostic system
			if(hands.get(i).getHandType() == 2 && changed == false)
			{
				rank += 20;
				changed = true;
			}
			
			if(i != hands.size() - 1)
			{
				if(compare.compare(hands.get(i), hands.get(i + 1)) != 0)
				{
					hands.get(i).setRank(rank);
					rank++;
				}
				else
				{
					hands.get(i).setRank(rank);
				}
			}
			else
			{
				hands.get(i).setRank(rank);
			}
		}
		
		try
		{
			File writeFile = new File("HandsSorted.txt");
			
			if(writeFile.createNewFile())
			{
				System.out.println("File created!");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		try 
		{
			FileWriter write = new FileWriter("HandsSorted.txt");
			String line = System.getProperty("line.separator");
			
			for(int i = 0; i < hands.size(); i++)
			{
				hands.get(i);
				//Use this for actual format
				write.write(hands.get(i).getHandValue() + ", " + hands.get(i).getRank() + line);
			}
			
			write.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
