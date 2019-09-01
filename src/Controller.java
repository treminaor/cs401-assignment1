// The Stock program is following the MVC design template and this is our controller object.
// The main functionality for buying and selling the stocks are in this controller object.
// This is the ONLY file you may edit

import java.util.LinkedList;
import java.util.Scanner;

public class Controller {
	
	static LinkedList<Stock> allStock = new LinkedList<Stock>();
	
	public Controller() {
		Scanner input = new Scanner(System.in);
		
		// Sample Stock Purchases
		Controller.buyStock("Google", 10, 100);
		Controller.buyStock("Amazon", 20, 200);
		Controller.buyStock("eBay", 30, 300);
		Controller.buyStock("Google", 10, 100);
		Controller.buyStock("Google", 20, 200);
		Controller.buyStock("Apple", 30, 300);
		
		Controller.printStock();
		
		do {
			System.out.print("\nEnter name of stock to begin, or \"print\" to print current portfolio, or \"quit\" to quit: ");
			
			String stockSelect = input.next();
			
			if(stockSelect.equals("quit"))
				break;
			
			if(stockSelect.equals("print"))
			{
				Controller.printStock();
				continue;
			}
			
			System.out.print("Input 1 to buy " + stockSelect + " stock, 2 to sell " + stockSelect + " stock: ");
			int controlNum = input.nextInt();
			System.out.print("How many stocks: ");
			int quantity = input.nextInt();
			
			if(controlNum == 1) {
				System.out.print("At what price: ");
				double price = input.nextDouble();
				Controller.buyStock(stockSelect, quantity, price);
			}
			else {
				if(indexOfStock(stockSelect) == -1)
				{
					System.out.println("Error: You do not own any " + stockSelect + " stock!");
					continue;
				}
				System.out.print("Enter per-stock sales price: ");
				int salesPrice = input.nextInt();
				System.out.print("Press 1 for LIFO accounting, 2 for FIFO accounting: ");
				controlNum = input.nextInt();
				if(controlNum == 1) {
					Controller.sellLIFO(stockSelect, salesPrice, quantity);
				}
				else {
					Controller.sellFIFO(stockSelect, salesPrice, quantity);
				}
			}
			
		} while(true); 
		input.close();
	}
			
	public static void buyStock(String name, int quantity, double price) {
		Stock temp = new Stock(name,quantity,price);
		allStock.push(temp);
		System.out.printf("You bought %d shares of %s stock at $%.2f per share %n", quantity, name, price);

	}
	
	/* Sell numToSell shares starting from the most recent purchase backward */
	public static void sellLIFO(String name, int salesPrice, int numToSell) {
	    // You need to write the code to sell the stock using the LIFO method (Stack)
	    // You also need to calculate the profit/loss on the sale
		double cost = 0; // this variable will store the total after the sale
		int stockCount = 0; // counts how many stocks we've processed so far
		
		if(getTotalStock(name) < numToSell) {
			System.out.printf("Error: You are trying to sell more stocks than you own! %d vs %d %n", numToSell, getTotalStock(name));
			return;
		}
		
		for(int i = 0; i < allStock.size(); i++) { //LIFO
			Stock s = allStock.get(i); //each stock purchase on record
			int shares = s.getQuantity();
			for(int j = 0; j < shares; j++)
			{
				if(!s.getName().equals(name) || s.getQuantity() <= 0)
					continue;
				if(stockCount >= numToSell)
					break;
				stockCount++;
				s.setQuantity(s.getQuantity() - 1);
				cost += s.getPrice();
			}
		}

		double total = (salesPrice * numToSell); //$$ made from sale
		double profit = total - (cost); // the price paid minus the sale price, negative # means a loss
		
		System.out.printf("You sold %d shares of %s stock at $%.2f per share for a total of $%.2f %n", numToSell, name, (total / numToSell), total);
	    System.out.printf("You made $%.2f on the sale. (Original cost: $%.2f) %n", profit, cost);
	}

	/* Sell numToSell shares starting from the oldest purchase forwards */
	public static void sellFIFO(String name, int salesPrice, int numToSell) {
	    // You need to write the code to sell the stock using the FIFO method (Queue)
	    // You also need to calculate the profit/loss on the sale
		double cost = 0; // this variable will store the total after the sale
		int stockCount = 0; // counts how many stocks we've processed so far
		
		if(getTotalStock(name) < numToSell) {
			System.out.printf("Error: You are trying to sell more stocks than you own! %d vs %d %n", numToSell, getTotalStock(name));
			return;
		}
		
		for(int i = allStock.size() - 1; i >= 0; i--) { //FIFO
			Stock s = allStock.get(i); //each stock purchase on record
			int shares = s.getQuantity();
			for(int j = 0; j < shares; j++)
			{
				if(!s.getName().equals(name) || s.getQuantity() <= 0)
					continue;
				if(stockCount >= numToSell)
					break;
				stockCount++;
				s.setQuantity(s.getQuantity() - 1);
				cost += s.getPrice();
			}
		}

		double total = (salesPrice * numToSell); //$$ made from sale
		double profit = total - (cost); // the price paid minus the sale price, negative # means a loss
		
		System.out.printf("You sold %d shares of %s stock at $%.2f per share for a total of $%.2f %n", numToSell, name, (total / numToSell), total);
	    System.out.printf("You made $%.2f on the sale. (Original cost: $%.2f) %n", profit, cost);
	}
	
	public static int getTotalStock(String name)
	{
		int total = 0;
		for(int i = allStock.size() - 1; i >= 0; i--) //FIFO
		{
			Stock s = allStock.get(i); //each stock purchase on record
			if(!s.getName().equals(name) || s.getQuantity() <= 0)
				continue;
			total += s.getQuantity();
		}
		
		return total;
	}
	
	public static int indexOfStock(String name) {
		return indexOfStock(name, true);
	}
	public static int indexOfStock(String name, boolean fifo) {
		int index = -1;
		
		if(fifo) {
			for(int i = allStock.size() - 1; i >= 0; i--) { //FIFO
				if(allStock.get(i).getName().equals(name) && allStock.get(i).getQuantity() > 0)
					return i;
			}
		}
		else {
			for(int i = 0; i < allStock.size(); i++) { //LIFO
				if(allStock.get(i).getName().equals(name) && allStock.get(i).getQuantity() > 0)
					return i;
			}
		}
		return index;
	}
	
	public static void printStock() {
		printStock(allStock);
	}
	public static void printStock(LinkedList<Stock> list) {
		System.out.println("\nYour Stock Portfolio (DESC. purchase order)");
		System.out.println("----------------");
		
		for(int i=0; i < list.size(); i++) {
			System.out.println(i +": "+ list.get(i));
		} 
		
		System.out.println();
	}
}
