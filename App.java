import java.io.BufferedWriter;
import java.io.File;
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
          app.remove();
          break;
        case "find":
          app.find();
          break;
        default:
          System.out.println("\nInvalid command. Type 'help' for a list of commands.\n");
          break;
      }
      next = sc.nextLine().toLowerCase().trim();
    }
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
    System.out.println("  find - prints the information of a data point if the data point is found.");
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
    if (!data.equals("") || !contains(data)) {
      fileWrite("data.txt", data);
      System.out.println("Person added to file.\n");
    } else {
      System.out.println("  Person already exists in the file. Use 'edit' to change their data.");
    }

    return "";
  }

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
      int[] date = new int[]{
          Integer.parseInt(parts[0]), 
          Integer.parseInt(parts[1]), 
          Integer.parseInt(parts[2])};
      // Making sure that the day, month and year are valid
      if (date[0] > 0 && date[0] < 32 && date[1] > 0 && date[1] < 13 && date[2] > 0) {
        // Making sure that the day and month are 2 digits long
        if (date[0] < 10) {
          parts[0] = "0" + parts[0];
        }
        // Making sure that no invalid days for certain months are entered
        if (date[1] < 10) {
          if (date[1] == 2) {
            if (date[0] > 29) {
              return "";
            }
          } else if (date[1] == 4 || date[1] == 6 || date[1] == 9 || date[1] == 11) {
            if (date[0] > 30) {
              return "";
            }
          }
          parts[1] = "0" + parts[1];
        }
        // Making sure that the year is 4 digits long
        if (date[2] < 10) {
          parts[2] = "000" + parts[2];
        } else if (date[2] < 100) {
          parts[2] = "00" + parts[2];
        } else if (date[2] < 1000) {
          parts[2] = "0" + parts[2];
        }
        return parts[0] + "/" + parts[1] + "/" + parts[2];
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

  public void remove() {
  }

  public void find() {
  }

  public void edit() {
  }

  private boolean contains(String data) {
    return false;
  }
}