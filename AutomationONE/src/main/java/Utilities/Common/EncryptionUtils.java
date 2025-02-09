package Utilities.Common;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	private SecretKey secretKey;
	private IvParameterSpec ivSpec;

	// Constructor to generate a new key and IV
	public EncryptionUtils() {
		generateKey();
	}

	// Constructor to load an existing key and IV (for decryption)
	public EncryptionUtils(String base64Key, String base64IV) {
		this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM);
		this.ivSpec = new IvParameterSpec(Base64.getDecoder().decode(base64IV));
	}

	private void generateKey() {
		try {
			// Generate a secure AES key
			KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(256); // Use AES-256
			this.secretKey = keyGenerator.generateKey();

			// Generate a secure random IV (Initialization Vector)
			byte[] iv = new byte[16]; // AES block size is 16 bytes
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(iv);
			this.ivSpec = new IvParameterSpec(iv);
		} catch (Exception e) {
			throw new RuntimeException("Error generating key", e);
		}
	}

	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Encryption failed", e);
		}
	}

	public String decrypt(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Decryption failed", e);
		}
	}

	public String getSecretKey() {
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	public String getIV() {
		return Base64.getEncoder().encodeToString(ivSpec.getIV());
	}
}