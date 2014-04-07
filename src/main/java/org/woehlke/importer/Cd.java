package org.woehlke.importer;

import java.io.*;

/**
 * Created by Fert on 04.04.2014.
 */
public class Cd {

    /* Rubrik;Tontraeger;Interpret;Name;Genre;Jahr;Label;Bemerkung; */

    private final static String dateiName = "C:\\git\\beachbox\\src\\main\\resources\\CD.csv";
    private final static String dateiNameOut = "C:\\git\\beachbox\\src\\main\\resources\\CD.sql";
    private final static String sqlStart = "INSERT INTO vinyl (rubrik,tontraeger, interpret, name, genre, jahr, label, bemerkung) VALUES (\'";

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
