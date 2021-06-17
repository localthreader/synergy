package net.victorbetoni.tasker.utils;

import net.victorbetoni.tasker.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    public static Optional<ResultSet> query(String sql, boolean dml, Connection conn) {
        try {
            if(dml) {
                conn.createStatement().executeUpdate(sql);
            } else {
                return Optional.of(conn.createStatement().executeQuery(sql));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<ResultSet> query(String sql, Connection conn, boolean dml, Pair<Class<?>, Object>... args) {
        AtomicReference<ResultSet> resultSetAtomicReference = new AtomicReference<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            AtomicInteger index = new AtomicInteger(0);
            Arrays.stream(args).forEach(pair -> {
                try {
                    st.setObject(index.incrementAndGet(), pair.getFirst().cast(pair.getSecond()));
                    if(!dml) {
                        resultSetAtomicReference.set(st.executeQuery());
                    } else {
                        conn.createStatement().executeUpdate(sql);
                    }
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(dml) {
            return Optional.of(resultSetAtomicReference.get());
        }
        return Optional.empty();
    }
}
