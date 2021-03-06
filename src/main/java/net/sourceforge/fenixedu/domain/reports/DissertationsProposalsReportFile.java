package net.sourceforge.fenixedu.domain.reports;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class DissertationsProposalsReportFile extends DissertationsProposalsReportFile_Base {

    public DissertationsProposalsReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de propostas de dissertações com afiliações externas";
    }

    @Override
    protected String getPrefix() {
        return "propostas de dissertações com afiliações externas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
        listProposals(spreadsheet, getExecutionYear());
    }

    private void listProposals(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
        spreadsheet.setName("Propostas " + executionYear.getNextYearsYearString().replace("/", ""));
        spreadsheet.setHeader("Cursos");
        spreadsheet.setHeader("Siglas dos Cursos");
        spreadsheet.setHeader("Proposta Tese");
        spreadsheet.setHeader("Aluno atribuido");
        spreadsheet.setHeader("Distribuicao Creditos Orientador");
        spreadsheet.setHeader("Distribuicao Creditos Corientador");
        spreadsheet.setHeader("Empresa do Acompanhante");
        spreadsheet.setHeader("Local de Realização");

        for (final Scheduleing scheduleing : getRootDomainObject().getScheduleingsSet()) {
            final StringBuilder degreeNamesSB = new StringBuilder();
            final StringBuilder degreeCodesSB = new StringBuilder();
            for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() != executionYear) {
                    continue;
                }
                if (degreeNamesSB.length() > 0) {
                    degreeNamesSB.append(", ");
                    degreeCodesSB.append(", ");
                }
                final Degree degree = executionDegree.getDegree();
                degreeNamesSB.append(degree.getPresentationName());
                degreeCodesSB.append(degree.getSigla());
            }
            final String degreeNames = degreeNamesSB.toString();
            final String degreeCodes = degreeCodesSB.toString();
            if (degreeCodes.length() == 0) {
                continue;
            }

            for (final Proposal proposal : scheduleing.getProposalsSet()) {
                final Row row = spreadsheet.addRow();
                row.setCell(degreeNames);
                row.setCell(degreeCodes);
                row.setCell(proposal.getTitle());
                row.setCell(getProposalAttributee(proposal));
                row.setCell(proposal.getOrientatorsCreditsPercentage().toString());
                row.setCell(proposal.getCoorientatorsCreditsPercentage().toString());
                row.setCell(proposal.getCompanyName());
                row.setCell(proposal.getLocation());
            }
        }

        spreadsheet.exportToXLSSheet(new File("propostas" + executionYear.getNextYearsYearString().replace("/", "") + ".xls"));
    }

    private String getProposalAttributee(final Proposal proposal) {
        final FinalDegreeWorkGroup attributed = proposal.getGroupAttributed();
        if (attributed != null) {
            return getGroupAttributedString(attributed);
        }
        final FinalDegreeWorkGroup attributedByTeacher = proposal.getGroupAttributedByTeacher();
        if (attributedByTeacher != null) {
            return getGroupAttributedString(attributedByTeacher);
        }
        return "--";
    }

    private String getGroupAttributedString(FinalDegreeWorkGroup finalDegreeWorkGroup) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final GroupStudent groupStudent : finalDegreeWorkGroup.getGroupStudentsSet()) {
            final Student student = groupStudent.getRegistration().getStudent();
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(student.getNumber());
        }
        return stringBuilder.toString();
    }

}
