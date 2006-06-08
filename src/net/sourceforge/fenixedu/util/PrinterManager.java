package net.sourceforge.fenixedu.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class PrinterManager {
	
	private static final Map<String, PrintService> printers = new HashMap<String, PrintService>();
	private static final PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
	private static final Map<String, String[]> printerNames = new HashMap<String, String[]>();
	
	static {
		loadPrinters();
		loadPrinterNames();
	}
	
	public static PrintService getPrintServiceByName(String name) {
		if(name == null) {
			return defaultPrinter;
		} else {
			return printers.get(name);
		}
	}
	
	public static String[] getFunctionPrinterNames(String key) {
		return printerNames.get(key);
	}
	
	private static void loadPrinterNames() {
		try {
			Properties properties = PropertiesManager.loadProperties("/printers.properties");
			for (Object key : properties.keySet()) {
				String function = (String) key;
				String string = properties.getProperty(function);
				String[] names = string.split(",");
				printerNames.put(function, names);
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
	}

	
	private static void loadPrinters() {		
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService service : services) {
			printers.put(service.getName(), service);
		}
	}


}
