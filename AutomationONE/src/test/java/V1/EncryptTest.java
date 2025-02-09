package V1;

import Utilities.Common.EncryptionUtils;

public class EncryptTest {

	public static void main(String[] args) {

		// Generate a new AES key and IV
		EncryptionUtils encryptionUtils = new EncryptionUtils();

		// Store these securely
		String secretKey = encryptionUtils.getSecretKey();
		String iv = encryptionUtils.getIV();

		System.out.println("Generated Key: " + secretKey);
		System.out.println("Generated IV: " + iv);

		// Encrypt message
		String secretMessage = "Hello, Secure World!";
		String encrypted = encryptionUtils.encrypt(secretMessage);
		System.out.println("Encrypted: " + encrypted);

		// Decrypt using the same key and IV
		EncryptionUtils decryptionUtils = new EncryptionUtils("3uNY3rGDTNF97DLRW1+TIy3TOo8w8rOrH6DGvqUOR1Y=","n/gM2Ho2dQMWuGBUwnaqkg==");
		String decrypted = decryptionUtils.decrypt("FLiBnqjefHJeg8z0Nr3BnNrZ+grIosHdR2KqSLxY2Ss=");
		System.out.println("Decrypted: " + decrypted);
	}

}
