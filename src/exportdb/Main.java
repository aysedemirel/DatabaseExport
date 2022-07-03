package exportdb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {

  /**
   * Connect to the test.db database
   * 
   * @return the Connection object
   */
  private Connection connect() {
    // SQLite connection string
    String url = "jdbc:sqlite:D:/GIT/Java/exportDatabase/db/chinook.db";
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  /**
   * select all rows in the warehouses table
   */
  public void selectAll() {
    String sql =
        "SELECT EmployeeId, Title, LastName, FirstName FROM employees where title != 'Sales Support Agent'";

    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      writeToExcel(rs);
      // loop through the result set
      // while (rs.next()) {
      // System.out.println(rs.getInt("EmployeeId") + "\t" + rs.getString("Title") + "\t"
      // + rs.getString("FirstName") + "\t" + rs.getString("LastName"));
      // }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void writeToExcel(ResultSet rs) {
    File csvOutputFile = new File(".\\a.csv");
    List<String> resultSetArray = new ArrayList<>();
    try {
      int numCols = rs.getMetaData().getColumnCount();
      // Add header
      resultSetArray.add("EmployeeId\tTitle\tLastName\tFirstName");
      // Add results
      while (rs.next()) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numCols; i++) {
          sb.append(String.format(String.valueOf(rs.getString(i))) + "\t");
        }
        resultSetArray.add(sb.toString());

      }
      FileWriter fileWriter = new FileWriter(csvOutputFile, false);
      for (String mapping : resultSetArray) {
        fileWriter.write(mapping + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    Main main = new Main();
    main.selectAll();
    System.out.println("DONE");
  }

}
