package databaseManager;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

public class DAOTest {

    private String url = "jdbc:h2:mem:Library;DB_CLOSE_DELAY=-1";
    private String user = "sa";

    @BeforeMethod
    public void prepareDB() throws Exception {
        RunScript.execute(url, user, "", "initTitles.sql", Charset.forName("UTF-8"), false);
        IDataSet set = new FlatXmlDataSetBuilder().build(new File(DAOTest.class.getResource("/titles.xml").getFile()));
        IDatabaseTester tester = new JdbcDatabaseTester("org.h2.Driver", url, user, "");
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setDataSet(set);
        tester.onSetup();
    }

    @Test(groups = "integration")
    public void extractFromDataSet() throws Exception {
        ResultSet results = dataSource().getConnection().createStatement().executeQuery("SELECT * FROM Titles");
        //FIXME: bug 2
        while (results.next())
            System.out.println(results.getString("title"));
    }

    //FIXME: I'm never used!
    public void DAOReadsProperlyFromDatabase() {
        DBRW.DAOInitializer.changeConfiguration("/hibernateTestConfiguration/hibernate.cfg.xml");
        assertThat(DBRW.getLibrariesFromDB().size()).isGreaterThan(0);
    }

    private JdbcDataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword("");
        return dataSource;
    }
}
