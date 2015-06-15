package xypipeline;


public interface Consts{
	public static final String DATAPATH = "/home/gtest/data/";
	public static final String PROGPATH = "/home/gtest/bin/";
	public static final String ANNOPATH = "/home/gtest/annos/";
	public static final String PIPELINE = "pipeline.xml";
	public static final String DATABASE = "database.xml";
	public static final String INDIVIDUAL = "individual.xml";
	
	public static final String DEFAULT_ENCODE = "ISO-8859-1";
	public static final String DATA_ROOT = "DataExchange";
	
	public static final String XML_TAG_PIPELINE = "pipeline";
	public static final String XML_TAG_COMMAND = "command";
	public static final String XML_TAG_PROGRAM = "program";
	public static final String XML_TAG_SCRIPT = "script";
	public static final String XML_TAG_PARAMETER = "parameter";
	public static final String XML_TAG_STDOUT = "stdout";
	public static final String XML_TAG_STDERR = "stderr";
	
	public static final String XML_TAG_INDIVIDUAL = "individual";
	public static final String XML_TAG_NAME = "name";
	public static final String XML_TAG_BIRTHDATE = "birthdate";
	public static final String XML_TAG_GENDER = "gender";
	public static final String XML_TAG_ETHNIC = "ethnic";
	
	public static final String XML_TAG_FAMILY = "family";
	public static final String XML_TAG_FATHER = "father";
	public static final String XML_TAG_MOTHER = "mother";
	public static final String XML_TAG_SPOUSE = "spouse";
	
	public static final String XML_TAG_ADDRESS = "address";
	public static final String XML_TAG_CONTACT = "contact";
	
	public static final String XML_TAG_PHENOTYPE = "phenotype";
	public static final String XML_TAG_HISTORY = "history";
	public static final String XML_TAG_DATA = "data";
	
	public static final String XML_TAG_FILE = "file";
	public static final String XML_TAG_PATH = "path";
	public static final String XML_TAG_DESCRIPTION = "description";
	
	public static final String XML_ATT_ID = "id";
	public static final String XML_ATT_TYPE = "type";
	public static final String XML_ATT_OPTION = "option";
	public static final String XML_ATT_KEY = "key";
	public static final String XML_ATT_FLAG = "flag";
	
	public static final String XML_FLAG_REMOVED = "removed";
	public static final String XML_FLAG_USING = "using";
}