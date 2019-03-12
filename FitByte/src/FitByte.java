/*
 * Name: Walter Geanacopoulos
 * Date: November 30, 2015
 * Description: Coverts exercise info from file
 */
import java.util.*;  // for Scanner
import java.io.*;    // for file in
import java.io.FileNotFoundException; //for the exception


public class FitByte
{
  // static variable
  static String fitDataFile;

  public static void main(String[] args)
  {
    // used for input and output
    Scanner console = new Scanner(System.in);

    // data types and variables
    String input;
    String exerciseStartTime = " ";
    String exerciseEndTime = " ";
    double beatsPerMin;
    double stepsPerMin;
    int beats = 0;
    int steps = 0;
    int startTime = 0;
    int endTime = 0;
    int exerciseTime;

    // askes the user what file they would like to load and the user inputs it
    System.out.println("What file will you like to load? fitdata1.txt or fitdata2.txt: ");
    fitDataFile = console.nextLine();

    // to open the file that the person chooses
    File fitdata = new File(fitDataFile);

    // to see if there is a exception and if so it must have a catch at the end to catch the exception
    try
    {
      // used to input the users file 
      Scanner inFile = new Scanner(fitdata);

      for (int i = 0; inFile.hasNextLine(); i++)
      {
        input = inFile.nextLine();

        if (i==0)
        {
          // calls the method for start time
          startTime = returnTimeInt(input);
          exerciseStartTime = input;
        }

        else if (i==1)
        {
          // calls the method for end time
          endTime = returnTimeInt(input);
          exerciseEndTime = input;
        }
        // pulls all the strings beginning with b and adds them together
        else if (input.charAt(2) == 'b')
          beats += Character.getNumericValue(input.charAt(0));
          // // pulls all the strings beginning with s and adds them together
        else if  (input.charAt(2) == 's')
          steps += Character.getNumericValue(input.charAt(0));


      }
      // calculation for the total time
      exerciseTime = endTime - startTime;

      // calculates the beats and steps per min
      beatsPerMin = beats / (exerciseTime / 60.0);
      stepsPerMin = steps / (exerciseTime / 60.0);

      //outputs the start time, end time, total time working out, beats & steps per min
      System.out.println("Start Time: " + exerciseStartTime);
      System.out.println("End Time: " + exerciseEndTime);
      System.out.printf("Total Time: %.2f mins%n", (exerciseTime /60.0));
      System.out.printf("Beats: %.1f/min%n", beatsPerMin);
      System.out.printf("Steps: %.1f/min%n", stepsPerMin);

      // catches any file exception
    }catch(FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  //converts the time so that it can be subtracted easily
  public static int returnTimeInt(String input)
  {
    int totalTime = 0;
    // time conversion for the first file. 
    if (fitDataFile.equals("fitdata1.txt"))
    {
      totalTime += 36000 * Character.getNumericValue(input.charAt(0));
      totalTime += 3600 * Character.getNumericValue(input.charAt(1));
      totalTime += 600 * Character.getNumericValue(input.charAt(3));
      totalTime += 60 * Character.getNumericValue(input.charAt(4));
      totalTime += 10 * Character.getNumericValue(input.charAt(6));
      totalTime += Character.getNumericValue(input.charAt(7));
    }
    // time coversion for the second file
    else if(fitDataFile.equals("fitdata2.txt"))
    {
      totalTime += 3600 * Character.getNumericValue(input.charAt(0));
      totalTime += 600 * Character.getNumericValue(input.charAt(2));
      totalTime += 60 * Character.getNumericValue(input.charAt(3));
      totalTime += 10 * Character.getNumericValue(input.charAt(5));
      totalTime += Character.getNumericValue(input.charAt(6));
    }

    return totalTime;
  }
}