package V1;

import org.testng.annotations.Test;

import Utilities.Common.JavaUtilities;

public class CommandPromptTest {
	
	@Test
	public void runCMD(){
		
		JavaUtilities.runCommand("java -version", "1");
	}

}
