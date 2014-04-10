package org.woehlke.beachbox.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.*;

/**
 * Created by tw on 10.04.2014.
 */
@Repository
public class InstallDaoImpl implements InstallDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void installInitialData() {
        System.out.println("run SQL initial data import.");
        try {
            ClassLoader cl = InstallDaoImpl.class.getClassLoader();
            InputStream is = cl.getResourceAsStream("alle.sql");
            InputStreamReader isr =
                    new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            jdbcTemplate.execute("delete from vinyl");
            br.lines().forEach(sql -> jdbcTemplate.execute(sql));
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
