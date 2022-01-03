package application.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileDuplicatorTest {
   FileDuplicator duplicator;
   
   //asserts the file id actually gets incremented
	@Test
	void incrementFileNameTestId() {
		
		duplicator = new FileDuplicator();
		String takenPathName ="parentPath/xyz/12(4)";
		
		String newFileName = duplicator.incrementFileNameId(takenPathName);
		
		assertEquals(newFileName,"parentPath/xyz/12(5)");

	}

}
