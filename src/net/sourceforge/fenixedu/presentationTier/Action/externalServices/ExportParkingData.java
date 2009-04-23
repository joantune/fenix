package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.Vehicle;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.FileUtils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;

public class ExportParkingData extends FenixAction {

    private static final int _3 = 3;
    private static final int _1 = 1;
    private static final int _0 = 0;
    private static final Date DATE = new LocalDate(2000, 1, 1).toDateTimeAtStartOfDay().toDate();
    private static final String EMPTY_STRING = "";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String password = request.getParameter("password");
	final String username = request.getParameter("username");
	checkPermissions(username, password);

	final String path = getServlet().getServletContext().getRealPath("/").replace('\\', '/');
	InputStream inputStream = new FileInputStream(path + "/templates/Cartoes_XML.mdb");
	if (inputStream != null) {
	    File temp = FileUtils.copyToTemporaryFile(inputStream);
	    Database db = Database.open(temp, Boolean.FALSE, Boolean.TRUE);

	    Table xml = db.getTable("XML");
	    List<ParkingParty> parkingParties = getValidParkingParties();
	    final Date now = new DateTime().toDate();
	    final Map<String, Integer> parkingGroupCodes = getParkingGroupCodes();
	    for (ParkingParty parkingParty : parkingParties) {
		Object[] newRow = new Object[28];
		fillInRow(parkingParty, newRow, parkingGroupCodes, now);
		xml.addRow(newRow);
	    }
	    response.setContentType("application/vnd.ms-access");
	    response.setHeader("Content-disposition", "attachment; filename=Cart�es_XML.mdb");
	    final ServletOutputStream writer = response.getOutputStream();
	    writer.write(pt.utl.ist.fenix.tools.file.utils.FileUtils.readByteArray(temp));
	    writer.flush();
	    writer.close();

	    response.flushBuffer();
	}
	return null;
    }

    private Map<String, Integer> getParkingGroupCodes() {
	Map<String, Integer> codes = new HashMap<String, Integer>();
	codes.put("Docentes", 1);
	codes.put("N�o Docentes", 2);
	codes.put("Especiais", 3);
	codes.put("Bolseiros", 4);
	codes.put("Investigadores", 5);
	codes.put("3� ciclo", 6);
	codes.put("2� ciclo", 7);
	codes.put("IPSFL", 8);
	codes.put("Jubilados", 9);
	codes.put("Limitados", 10);
	codes.put("Limitado1", 11);
	codes.put("Limitado2", 12);
	codes.put("Limitado3", 13);
	codes.put("Limitado4", 14);
	codes.put("Limitado5", 15);
	return codes;
    }

    private void checkPermissions(String username, String password) throws NotAuthorizedException {
	final String allowedUser = PropertiesManager.getProperty("exportParkingData.username");
	final String allowedPass = PropertiesManager.getProperty("exportParkingData.password");
	if ((!allowedUser.equals(username)) || (!allowedPass.equals(password))) {
	    throw new NotAuthorizedException();
	}
    }

    private void fillInRow(ParkingParty parkingParty, Object[] newRow, Map<String, Integer> parkingGroupCodes, Date now)
	    throws Exception {

	Person person = null;
	if (parkingParty.getParty().isPerson()) {
	    person = (Person) parkingParty.getParty();
	}

	newRow[0] = _0;
	newRow[1] = parkingParty.getCardNumber().toString();
	newRow[2] = _3;
	newRow[3] = Boolean.TRUE;
	newRow[4] = parkingGroupCodes.get(parkingParty.getParkingGroup().getGroupName()).intValue();
	newRow[5] = _1;
	newRow[6] = _0;
	newRow[7] = DATE;
	newRow[8] = now;
	newRow[9] = now;
	newRow[10] = person != null ? getName(person.getNickname()) : getName(parkingParty.getParty().getName());
	newRow[11] = EMPTY_STRING;
	String vehicle1PlateNumber = EMPTY_STRING;
	String vehicle2PlateNumber = EMPTY_STRING;
	int counter = 1;
	for (Vehicle vehicle : parkingParty.getVehicles()) {
	    if (counter == 1) {
		vehicle1PlateNumber = vehicle.getPlateNumber();
	    } else if (counter == 2) {
		vehicle2PlateNumber = vehicle.getPlateNumber();
	    } else {
		break;
	    }
	    counter++;
	}
	newRow[12] = vehicle1PlateNumber;
	newRow[13] = vehicle2PlateNumber;
	newRow[14] = person != null && person.getPersonWorkPhone() != null ? getString(person.getPersonWorkPhone().getNumber(),
		19) : EMPTY_STRING;
	newRow[15] = person != null && person.getDefaultMobilePhone() != null ? getString(person.getDefaultMobilePhone()
		.getNumber(), 19) : EMPTY_STRING;
	newRow[16] = EMPTY_STRING;
	newRow[17] = EMPTY_STRING;
	newRow[18] = _0;
	newRow[19] = parkingParty.getCardEndDate() == null ? null : parkingParty.getCardEndDate().toDate();
	newRow[20] = DATE;
	newRow[21] = Boolean.FALSE;
	newRow[22] = parkingParty.getCardStartDate() != null ? Boolean.FALSE : Boolean.TRUE;
	newRow[23] = Boolean.FALSE;
	newRow[24] = Boolean.FALSE;
	newRow[25] = Boolean.FALSE;
	newRow[26] = Boolean.TRUE;
	newRow[27] = parkingParty.getCardStartDate() == null ? null : parkingParty.getCardStartDate().toDate();
    }

    private String getName(String name) {
	if (name.length() > 59) { // max size of the other parking application
	    // DB
	    StringBuilder resultName = new StringBuilder();
	    resultName = new StringBuilder();
	    String[] names = name.split("\\p{Space}+");
	    for (int iter = 1; iter < names.length - 1; iter++) {
		if (names[iter].length() > 5) {
		    names[iter] = names[iter].substring(0, 5) + ".";
		}
	    }
	    for (int iter = 0; iter < names.length; iter++) {
		resultName.append(names[iter]).append(" ");
	    }
	    if (resultName.length() > 59) {
		resultName = new StringBuilder(names[0]).append(" ").append(names[names.length - 1]);
	    }
	    return resultName.toString().trim();
	} else {
	    return name;
	}

    }

    private List<ParkingParty> getValidParkingParties() {
	List<ParkingParty> parkingParties = new ArrayList<ParkingParty>();
	for (ParkingParty parkingParty : ParkingParty.getAll()) {
	    if (parkingParty.getCardNumber() != null) {
		parkingParties.add(parkingParty);
	    }
	}
	return parkingParties;
    }

    private String getString(String string, int maxSize) {
	if (string == null) {
	    return EMPTY_STRING;
	}
	if (string.length() > maxSize) {
	    return string.substring(0, maxSize - 1);
	} else {
	    return string;
	}
    }

}