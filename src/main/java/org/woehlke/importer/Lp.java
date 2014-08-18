package org.woehlke.importer;

import java.io.*;

/**
 * Created by Fert on 03.04.2014.
 */
public class Lp {

    /* Rubrik;Tontraeger;Interpret;Name;Jahr;Genre;Label */

    private final static String dateiName = "C:\\git\\beachbox\\src\\main\\resources\\LP.csv";
    private final static String dateiNameOut = "C:\\git\\beachbox\\src\\main\\resources\\LP.sql";
    private final static String sqlStart = "INSERT INTO vinyl (rubrik,tontraeger, interpret, name, jahr, genre, label,bemerkung) VALUES (\'";

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
