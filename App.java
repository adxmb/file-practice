import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class App {
  public static void main(String[] args) throws IOException {
    App app = new App();
    app.printHelp();
    Scanner sc = new Scanner(System.in);
    String next = sc.nextLine().toLowerCase().trim();

    // While the user types commands, the program will execute them until signalled to stop
    while(!next.equals("exit")) {
      switch(next) {
        case "help":
          app.printHelp();
          break;
        case "add":
          String a = app.add(sc);
          if (a.equals("exit")) {
            return;
          }
          break;
        case "remove":
          app.remove(sc);
          break;
        case "find":
          app.find(sc);
          break;
        case "edit":
          app.edit(sc);
          break;
        default:
          System.out.println("\nInvalid command. Type 'help' for a list of commands.\n");
          break;
      }
      next = sc.nextLine().toLowerCase().trim();
    }
    sc.close();
  }

  /**
   * Method for reading the file, making each line into a string and adding them to a list.
   * 
   * @param fileName The name of the file to read.
   * @return A list of strings, each string being a line from the file.
   */
  public List<String> fileRead(String fileName) {
    List<String> lines = Collections.emptyList();
    try {
      lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

  /** 
   * Method to write a line of text to a file.
   * 
   * @param fileName The name of the file to write to.
   * @param text The text to write to the file.
   */
  public void fileWrite(String fileName, String text) {
    try {
      FileWriter fw = new FileWriter(fileName, true);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter pw = new PrintWriter(bw);

      pw.println(text);
      pw.close();
      bw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to print the list of commands.
   */
  public void printHelp() {
    System.out.println("\nCommands:");
    System.out.println("  help - Prints this list of commands.");
    System.out.println("  add - Adds new data into the file.");
    System.out.println("  remove - Removes specific data into the file.");
    System.out.println("  find - prints the information of a datapoint if the data point is found.");
    System.out.println("  edit - Edits a datapoint if the data point is found.");
    System.out.println("  exit - Exits the program.\n");
  }

  /**
   * Method to add data to the file.
   */
  public String add(Scanner sc) {
    String data = askForData(sc).trim();
    if (data.equals("exit")) {
      return "exit"; 
    }
    // If the person is already in the file, then cannot alter their data
    if (!data.equals("") && contains(data.split(" ")[0], data.split(" ")[1]).equals("")) {
      fileWrite("data.txt", data);
      System.out.println("  Datapoint added to file.\n");
    } else {
      System.out.println("  Datapoint already exists in the file. Use 'edit' to change the data.\n");
    }

    return "";
  }

  /**
   * Method to ask the user for the data to add to the file.
   * Asks for all the key points from the user, and then checks if the date of birth is valid.
   * 
   * @param sc The scanner to read the user's input.
   * @return The data to add to the file.
   */
  private String askForData(Scanner sc) {
    String data = "";
    System.out.println("  Given Name: ");
    String givenName = sc.nextLine().trim();
    if (givenName.toLowerCase().equals("exit")) {
      return "exit";
    }
    data += makeGoodGrammar(givenName);

    System.out.println("  Surname: ");
    String surname = sc.nextLine().trim();
    if (surname.toLowerCase().equals("exit")) {
      return "exit";
    }
    data += " " + makeGoodGrammar(surname);

    while (true) {
      System.out.println("  Date of Birth (DD/MM/YYYY): ");
      String dob = sc.nextLine().trim();
      try {
        if (dob.toLowerCase().equals("exit")) {
          return "exit";
        } else if (isValidDate(dob).length() == 10) {
          data += " " + isValidDate(dob);
          break;
        } else {
          System.out.println("  Invalid date of birth. (Please use / to separate the day, month and year.)");
        }
      } catch (NumberFormatException e) {
        System.out.println("  Invalid date of birth. (Please use numbers to represent the date.)");
      }
    }
    return data;
  }

  /**
   * Checks if the date is a valid date. If it is a valid date, making sure it is in the format DD/MM/YYYY
   * with the correct number of characters.
   * 
   * @param dob The date of birth to check.
   * @return The date of birth if it is valid, otherwise an empty string.
   */
  private String isValidDate(String dob) throws NumberFormatException {
    String[] parts = dob.split("/");
    if (parts.length == 3) {
      // Check if the inputted date is a valid date
      int day = Integer.parseInt(parts[0]);
      int month = Integer.parseInt(parts[1]);
      int year = Integer.parseInt(parts[2]);
      if (day > 0 && day < 32 && month > 0 && month < 13 && year > 0) {
        // Put the date into the DD/MM/YYYY format
        String date = "";
        if (day < 10) {
          date += "0" + day + "/";
        } else {
          date += day + "/";
        }
        if (month < 10) {
          // If the month does not have the right amount of days in it, then the date is invalid
          if (!checkMonthDay(month, day)) {
            return "";
          }
          date += "0" + month + "/";
        } else {
          date += month + "/";
        }
        date += year;
        return date;
      }
    }
    return "";
  }

  /**
   * Capitalizes the first letter of a string and makes the rest lowercase.
   * 
   * @param name The string to be gramatified.
   * @return The gramatified string.
   */
  private String makeGoodGrammar(String name) {
    return name.substring(0, 1).toUpperCase() 
      + name.substring(1).toLowerCase();
  }

  /**
   * Method to remove a person from the file.
   * Finds the line that the data is on, and then removes it from the file.
   * If the data does not exit in the file, then does not do anything.
   * 
   * @param sc The scanner to read the user's input.
   * @throws IOException If the file cannot be read.
   */
  public void remove(Scanner sc) throws IOException {
    System.out.println("  Given Name: ");
    String givenName = sc.nextLine().trim();
    if (givenName.toLowerCase().equals("exit")) {
      return;
    }
    givenName = makeGoodGrammar(givenName);

    System.out.println("  Surname: ");
    String surname = sc.nextLine().trim();
    if (surname.toLowerCase().equals("exit")) {
      return;
    }
    surname = makeGoodGrammar(surname);

    // If the person is in the file, then remove them
    String line = contains(givenName, surname);
    if (!line.equals("")) {
      System.out.println("  Datapoint found!");
      // Logic to remove the person from the file
      File inputFile = new File("data.txt");
      // Uses the temp file to temporarily store the data without the person to be removed
      File tempFile = new File("temp.txt");

      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String currentLine;

      while((currentLine = reader.readLine()) != null) {
        String trimmedLine = currentLine.trim();
        if(trimmedLine.equals(line)) continue;
        writer.write(currentLine + System.getProperty("line.separator"));
      }
      writer.close();
      reader.close();
      boolean successful = tempFile.renameTo(inputFile);
      if (successful) {
        System.out.println("  Datapoint removed from file.\n");
      } else {
        System.out.println("  Datapoint not removed from file.\n");
      }
    } else {
      System.out.println("  Datapoint not found\n");
    }
  }

  /**
   * Method to find a person in the file. Uses the person's given name and surname to find them.
   * 
   * @param sc The scanner to read the user's input.
   * @return An empty string.
   */
  public String find(Scanner sc) {
    System.out.println("  Given Name: ");
    String givenName = sc.nextLine().trim();
    if (givenName.toLowerCase().equals("exit")) {
      return "exit";
    }
    givenName = makeGoodGrammar(givenName);

    System.out.println("  Surname: ");
    String surname = sc.nextLine().trim();
    if (surname.toLowerCase().equals("exit")) {
      return "exit";
    }
    surname = makeGoodGrammar(surname);
    // If the person is in the file, then print their data
    String line = contains(givenName, surname);
    if (!line.equals("")) {
      System.out.println("  Datapoint found!");
      System.out.println("  " + line + "\n");
    } else {
      System.out.println("  Datapoint not found\n");
    }
    return "";
  }

  /**
   * Method to edit a person in the file. Uses the person's given name and surname to find them.
   * 
   * @param sc The scanner to read the user's input.
   * @throws IOException If the file cannot be read.
   */
  public void edit(Scanner sc) throws IOException {
    System.out.println("  Given Name: ");
    String givenName = sc.nextLine().trim();
    if (givenName.toLowerCase().equals("exit")) {
      return;
    }
    givenName = makeGoodGrammar(givenName);

    System.out.println("  Surname: ");
    String surname = sc.nextLine().trim();
    if (surname.toLowerCase().equals("exit")) {
      return;
    }
    surname = makeGoodGrammar(surname);

    // If the person is in the file, then remove them
    String line = contains(givenName, surname);
    if (!line.equals("")) {
      System.out.println("  Datapoint found!");
      System.out.println("  Original data:" + line + "\n");
      System.out.println("  Please enter new data:");
      String data = askForData(sc).trim();
      // Logic to remove the person from the file
      File inputFile = new File("data.txt");
      // Uses the temp file to temporarily store the data without the person to be removed
      File tempFile = new File("temp.txt");

      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String currentLine;

      while((currentLine = reader.readLine()) != null) {
        String trimmedLine = currentLine.trim();
        if(trimmedLine.equals(line)) {
          writer.write(data + System.getProperty("line.separator"));
        } else {
          writer.write(currentLine + System.getProperty("line.separator"));
        }
      }
      writer.close();
      reader.close();
      boolean successful = tempFile.renameTo(inputFile);
      if (successful) {
        System.out.println("  Datapoint edited.\n");
      } else {
        System.out.println("  Datapoint not edited.\n");
      }
    } else {
      System.out.println("  Datapoint not found\n");
    }
  }

  /**
   * Method to check if the file contains a person with the given name and surname.
   * 
   * @param givenName The given name of the person to check.
   * @param surname The surname of the person to check.
   * @return True if the person is in the file, otherwise false.
   */
  private String contains(String givenName, String surname) {
    List<String> lines = Collections.emptyList();
    try {
      lines = Files.readAllLines(Paths.get("data.txt"), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (String line : lines) {
      String[] lineParts = line.split(" ");
      if (lineParts[0].equals(givenName) && lineParts[1].equals(surname)) {
        return line;
      }
    }
    return "";
  }

  /**
   * Method to check if the day and month are valid for the given month.
   * 
   * @param month The month to check.
   * @param day The day to check.
   * @return True if the day and month are valid, otherwise false.
   */
  private boolean checkMonthDay(int month, int day) {
    if (month == 2) {
      if (day > 29) {
        return false;
      }
    } else if (month == 4 || month == 6 || month == 9 || month == 11) {
      if (day > 30) {
        return false;
      }
    }
    return true;
  }
}