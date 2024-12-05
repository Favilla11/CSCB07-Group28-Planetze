import java.util.ArrayList;
import java.util.List;

public class work {

	/**
	 * Determines if the string, str, is within the bounds of teh array elements.
	 * Returns false if it is not in bounds
	 *
	 * @param arr       the String array to be checked
	 * @param str       the string to see if in array
	 * @param start     the start of the indexes to be checked
	 * @param end 	   the end of the indexes to be checked
	 * @return          <code>true</code> if the string is in the array
	 * 					between the start and end indexes;
	 *                  <code>false</code> otherwise.
	 * @since           1.0
	 */

	public boolean inIndexbounds(String[] arr, String str, int start, int end)
	{
		for(int i = start; i <= end; i++)
		{
			if(str.equals(arr[i]))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds and returns a list of all habits with the type which is the category
	 *
	 *
	 * @param list      the list of Habits
	 * @param type      the category to search for
	 * @return          list of habits of the type;
	 * @since           1.0
	 */

	public ArrayList<Habit> filterbytype(ArrayList<Habit> list, String type)
	{
		ArrayList<Habit> suggestions = new ArrayList<Habit>();

		String[] subcategories = {"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight", "Beef", "Pork", "Chicken", "Fish", "Plant-Based",
				"Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"};

		for(int i = 0; i < list.size(); i++)
		{
			if(type.equals("Transportation"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 0, 3))
				{
					suggestions.add(list.get(i));
				}
			}

			else if(type.equals("Diet"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 4, 8))
				{
					suggestions.add(list.get(i));
				}
			}

			else if(type.equals("Consumption"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 9, 11))
				{
					suggestions.add(list.get(i));
				}
			}
			else if(type.equals("Housing")&& list.get(i).getCategory().equals("Energy Bills"))
			{
				suggestions.add(list.get(i));
			}
		}

		return suggestions;
	}


	/**
	 * Finds and returns a list of all habits with the keyword
	 *
	 *
	 * @param list      the list of Habits
	 * @param keyword   the keyword to search for
	 * @return          list of habits of with the keyword;
	 * @since           1.0
	 */

	public ArrayList<Habit> filterbyKeyword(ArrayList<Habit> list, String keyword)
	{
		String[] subcategories = {"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight", "Beef", "Pork", "Chicken", "Fish", "Plant-Based",
				"Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"};

		ArrayList<Habit> suggestions = new ArrayList<Habit>();

		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).getCategory().contains(keyword)||list.get(i).getAct().contains(keyword))
			{
				suggestions.add(list.get(i));
			}

			else if(keyword.equals("Transportation"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 0, 3))
				{
					suggestions.add(list.get(i));
				}
			}

			else if(keyword.equals("Diet"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 4, 8))
				{
					suggestions.add(list.get(i));
				}
			}

			else if(keyword.equals("Consumption"))
			{
				if(inIndexbounds(subcategories, list.get(i).getCategory(), 9, 11))
				{
					suggestions.add(list.get(i));
				}
			}
			else if(keyword.equals("Housing")&& list.get(i).getCategory().equals("Energy Bills"))
			{
				suggestions.add(list.get(i));
			}
		}

		return suggestions;
	}

	/**
	 * Finds and returns a list of all habits with an impact between two numbers
	 *
	 *
	 * @param list      the list of Habits
	 * @param start     the start number for our range of impacts
	 * @param end       the end number for our range of impacts
	 * @return          list of habits of within the bounds;
	 * @since           1.0
	 */

	public ArrayList<Habit> filterbyImpact(ArrayList<Habit> list, double start, double end)
	{
		ArrayList<Habit> suggestions = new ArrayList<Habit>();

		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).getImpact() >= start||list.get(i).getImpact() <= end)
			{
				suggestions.add(list.get(i));
			}
		}

		return suggestions;
	}


	public static void main(String [] args) {

		ArrayList<Habit> premadesuggestions = new ArrayList<Habit>();

		premadesuggestions.add(new Habit("Cycling or Walking", 0, 0, "Bike or Walk to work", 0));

		premadesuggestions.add(new Habit("Public Transportation", 0, 0, "Take the bus", 0));

		premadesuggestions.add(new Habit("Flight", 0, 0, "Fly less", 0));

		premadesuggestions.add(new Habit("Plant-Based", 0, 0, "Make more of your diet vegetables", 0));

		premadesuggestions.add(new Habit("Beef", 0, 0, "Eat less beef", 0));

		premadesuggestions.add(new Habit("Buy New Clothes", 0, 0, "Buy less clothes", 0));

		premadesuggestions.add(new Habit("Buy Electronics", 0, 0, "Buy less electronics", 0));

		premadesuggestions.add(new Habit("Energy Bills", 0, 0, "Takes shorter showers", 0));

		premadesuggestions.add(new Habit("Energy Bills", 0, 0, "Use natural light", 0));

		//makes list of pre made habits to suggest


		String[] subcategories = {"Drive Personal Vehicle", "Public Transportation", "Cycling or Walking", "Flight", "Beef", "Pork", "Chicken", "Fish", "Plant-Based",
				"Buy New Clothes", "Buy Electronics", "Other Purchases", "Energy Bills"};

		double average = 14.249212;
		//average daily emission of canadians

		double factor = 0.1;



		Information info;

		if (info.getDailyEmission() > average)
		{
			factor = info.getDailyEmission();
		}

		//checks if emission is higher than average and if yes uses it to calculate factor to get to average

		ArrayList<Habit> suggestions = new ArrayList<Habit>();


		for(int i = 0; i < info.getActivityList().size(); i++)
		{
			String[] description = info.getActivityList().get(i).getDescription().split(":");
			double frequency = Double.parseDouble(description[description.length - 1].replaceAll("[^0-9\\\\.]", ""));
			//gets the numerical value of the actity from the description
			if(info.getActivityList().get(i).getSubCategory().equals("Drive Personal Vehicle"))
			{

				suggestions.add(new Habit("Drive Personal Vehicle", 0, info.getActivityList().get(i).getEmission()*factor, "Walk, bike or use more Public Transportation until Driving Distance: " + frequency*factor, frequency*factor));
			}

			else if(info.getActivityList().get(i).getSubCategory().equals("Public Transportation"))
			{

				suggestions.add(new Habit("Public Transportation", 0, info.getActivityList().get(i).getEmission()*factor, "Plan routes more efficently until Public Transportation: " + frequency*factor, frequency*factor));
			}

			else if(info.getActivityList().get(i).getSubCategory().equals("Flight"))
			{

				suggestions.add(new Habit("Flight", 0, info.getActivityList().get(i).getEmission()*factor, "Fly less." + description[0] + ":" + description[0] + ":" + (int)(frequency*factor), (int)(frequency*factor)));
			}

			else if(info.getActivityList().get(i).getCategory().equals("Diet")&&!(info.getActivityList().get(i).getSubCategory().equals("Plant-Based")))
			{

				suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0, info.getActivityList().get(i).getEmission()*factor, "Eat less meat and more plant based food until "+info.getActivityList().get(i).getSubCategory()+": "+ frequency*factor, frequency*factor));
			}

			else if(info.getActivityList().get(i).getCategory().equals("Consumption"))
			{

				suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0, info.getActivityList().get(i).getEmission()*factor, info.getActivityList().get(i).getSubCategory()+" less until : "+ frequency*factor, frequency*factor));
			}

			else if(info.getActivityList().get(i).getCategory().equals("Energy Bills"))
			{

				suggestions.add(new Habit(info.getActivityList().get(i).getSubCategory(), 0, info.getActivityList().get(i).getEmission()*factor, "Take shorter showers, use less heating and AC, use natural light, use energy efficent products, until Energy Bill: " + frequency*factor, frequency*factor));
			}
		}

		//goes through list and makes personalized habit suggestions based on the activity of the user, using the factor to calculate how to bring the emissions down to average



	}
}
