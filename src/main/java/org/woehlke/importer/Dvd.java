package org.woehlke.importer;

import java.io.*;

/**
 * Created by Fert on 18.08.2014.
 */
public class Dvd {

    private final static String dateiName = "C:\\git\\beachbox\\src\\main\\resources\\DVD.csv";
    private final static String dateiNameOut = "C:\\git\\beachbox\\src\\main\\resources\\DVD.sql";
    private final static String sqlStart = "INSERT INTO vinyl (rubrik,tontraeger, interpret, song, name, jahr, genre) VALUES (\'";

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(dateiNameOut));
        BufferedReader br = new BufferedReader(new FileReader(dateiName));
        br.lines().forEach(line -> {
            try {
                bw.write(sqlStart + (line.replaceAll("'","´").replaceAll(";", "\',\'") + "\');\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bw.close();
        br.close();
    }
}
