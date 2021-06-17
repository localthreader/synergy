package net.victorbetoni.tasker.utils;

import net.victorbetoni.tasker.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;

public class SQLUtils {
    public static void executeScript(String dir, Connection connection) {
        StringBuilder fileContent = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Database.class.getResourceAsStream("/" + dir))))){
            String line = "";
            while((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        final String[] queries = fileContent.toString().split(";");
        Arrays.stream(queries).forEach((query) -> {
            try{
                Statement st = connection.createStatement();
                st.execute(query);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        });
    }
}
