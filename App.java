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

public class App {
  public static void main(String[] args) throws IOException {
    // Initialising the writers
    FileWriter fw = new FileWriter("information.txt");
    BufferedWriter bw = new BufferedWriter(fw);
    PrintWriter pw = new PrintWriter(bw);
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}