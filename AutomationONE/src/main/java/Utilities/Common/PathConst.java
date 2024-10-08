package Utilities.Common;

/**
 * Contains all constant values
 * @author Saif
 *
 */
public interface PathConst {
	
	String CURRENTPATH=System.getProperty("user.dir");
	String EXCELPATH=CURRENTPATH+"/testdata/testdata.xlsx";
	String ENVEXCELPATH=CURRENTPATH+"/Environment/Environmentdata.xlsx";
	String PROPFILEPATH=CURRENTPATH+"/config.properties";
	public int IMPLICITWAIT=10;
	public int EXPLICITWAIT=15;

}
