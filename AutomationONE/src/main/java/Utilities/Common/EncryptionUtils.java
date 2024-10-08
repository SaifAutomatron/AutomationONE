package Utilities.Common;

import java.security.spec.KeySpec;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import lombok.SneakyThrows;

public class EncryptionUtils {

	private static final String UNICODE_FORMAT="UTF8";
	public static final String ENCRYPTION_SCHEME="DESede";
	private KeySpec keySpec;
	private SecretKeyFactory secretKeyFactory;
	private Cipher cipher;
	byte[] arrayBytes;
	private String myKey;
	private String myScheme;
	SecretKey key;

	@SneakyThrows
	public
	EncryptionUtils(){
		myKey="AutomationAutomatron1234";
		myScheme=ENCRYPTION_SCHEME;
		arrayBytes=myKey.getBytes(UNICODE_FORMAT);
		keySpec=new DESedeKeySpec(arrayBytes);
		secretKeyFactory=SecretKeyFactory.getInstance(myScheme);
		cipher=Cipher.getInstance(myScheme);
		key=secretKeyFactory.generateSecret(keySpec);
	}

	public String encrypt(String unEncryptedString)
	{
		String encryptedString=null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE,key);
			byte[] plainText=unEncryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText=cipher.doFinal(plainText);
			encryptedString=new String(Base64.encodeBase64(encryptedText));

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Encryption Failed!!");
		}

		return encryptedString;
	}

	public String decrypt(String encryptedString)
	{
		String decrypytedString=null;
		try {
			cipher.init(Cipher.DECRYPT_MODE,key);
			byte[] encryptedText=Base64.decodeBase64(encryptedString);
			byte[] plainText=cipher.doFinal(encryptedText);
			decrypytedString=new String(plainText);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Decryption Failed!!");
		}

		return decrypytedString;
	}
}
