package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class PersistentSuportMGPIT extends PersistentSuportOracle {

    private static PersistentSuportMGPIT instance = null;

    private String databaseUrl = null;

    public PersistentSuportMGPIT(String userNamePropertyName, String userPassPropertyName, String urlPropertyName) {
        super(userNamePropertyName, userPassPropertyName, urlPropertyName);
    }

    public static synchronized PersistentSuportOracle getProjectDBInstance() {
        if (instance == null) {
            instance =
                    new PersistentSuportMGPIT("db.itProjectManagement.user", "db.itProjectManagement.pass",
                            "db.itProjectManagement.alias");
        }
        return instance;
    }

    @Override
    public String getDatabaseUrl() throws ExcepcaoPersistencia {
        if (databaseUrl == null) {
            setDatabaseUrl();
        }
        return databaseUrl;
    }

    @Override
    public void setDatabaseUrl() throws ExcepcaoPersistencia {
        if (databaseUrl == null) {
            String DBUserName = PropertiesManager.getProperty(connectionProperties.userNamePropertyName);
            String DBUserPass = PropertiesManager.getProperty(connectionProperties.userPassPropertyName);
            String DBUrl = PropertiesManager.getProperty(connectionProperties.urlPropertyName);
            if (DBUserName == null || DBUserPass == null || DBUrl == null) {
                throw new ExcepcaoPersistencia();
            }
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("jdbc:oracle:thin:");
            stringBuffer.append(DBUserName);
            stringBuffer.append("/");
            stringBuffer.append(DBUserPass);
            stringBuffer.append("@");
            stringBuffer.append(DBUrl);
            databaseUrl = stringBuffer.toString();
        }
    }

}
