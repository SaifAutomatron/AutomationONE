package V1;

import org.testng.annotations.Test;

import Utilities.Common.JavaUtilities;

public class CommandPromptTest {
	
	@Test
	public void runCMD(){
		
		JavaUtilities.runWindowsCommand("java -version", "1");
	}

}
