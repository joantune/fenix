/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IRevenueReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.RevenueReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentRevenueReport extends PersistentReport {

    public List<IRevenueReportLine> getCompleteReport(ReportType reportType, String projectCode, final BackendInstance instance)
            throws ExcepcaoPersistencia {
        List<IRevenueReportLine> result = new ArrayList<IRevenueReportLine>();

        try {
            PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance(instance);
            p.startTransaction();
            String tableOrView = getTableOrViewName(p, reportType);

            String query =
                    "select \"idMov\", \"Ent. Financ.\", \"Rubrica\", \"Data\", \"Descrição\", \"Valor\" from " + tableOrView
                            + " where PROJECTCODE='" + projectCode + "' order by \"Data\", \"idMov\"";
            PreparedStatement stmt = p.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                IRevenueReportLine report = new RevenueReportLine();
                report.setProjectCode(projectCode);
                report.setMovementId(rs.getString("idMov"));
                report.setFinancialEntity(rs.getString("Ent. Financ."));
                report.setRubric(new Integer(rs.getInt("Rubrica")));
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = rs.getDate("Data");
                report.setDate(formatter.format(date));
                report.setDescription(rs.getString("Descrição"));
                report.setValue(new Double(rs.getDouble("Valor")));
                result.add(report);
            }

            rs.close();
            p.commitTransaction();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }

        return result;
    }

}
