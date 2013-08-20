package packagepath

class Constants {

	public static List<String> FEDEX_REGEX_LIST = new ArrayList<String>();
	public static List UPS_REGEX_LIST = new ArrayList<String>();
	public static List<String> USPS_REGEX_LIST = new ArrayList<String>();
	
	public static final String UPS = "ups";
	public static final String USPS = "usps";
	public static final String FEDEX = "fedex";
	
	static{
		
		//fedex
		FEDEX_REGEX_LIST.add(/(\b96\d{20}\b)|(\b\d{15}\b)|(\b\d{12}\b)/);
		FEDEX_REGEX_LIST.add(/\b((98\d\d\d\d\d?\d\d\d\d|98\d\d) ?\d\d\d\d ?\d\d\d\d( ?\d\d\d)?)\b/);
		FEDEX_REGEX_LIST.add(/^[0-9]{15}$/);
		
		//ups
		UPS_REGEX_LIST.add(/\b(1Z ?[0-9A-Z]{3} ?[0-9A-Z]{3} ?[0-9A-Z]{2} ?[0-9A-Z]{4} ?[0-9A-Z]{3} ?[0-9A-Z]|[\dT]\d\d\d ?\d\d\d\d ?\d\d\d|\d{22})\b/)
		
		//usps
		USPS_REGEX_LIST.add(/(\b\d{30}\b)|(\b91\d+\b)|(\b\d{20}\b)/);
		USPS_REGEX_LIST.add(/^E\D{1}\d{9}\D{2}$|^9\d{15,21}$/);
		USPS_REGEX_LIST.add(/^91[0-9]+$/);
		USPS_REGEX_LIST.add(/^[A-Za-z]{2}[0-9]+US$/);
	}
}
