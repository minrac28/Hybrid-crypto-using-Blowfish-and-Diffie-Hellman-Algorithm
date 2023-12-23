//package com.java.blowfish;

import java.net.*;
import java.io.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class user2
 {
	
	static BigInteger ya,yb,key2;
	
	private static final String ALGORITHM = "Blowfish";
    private static String key= "key2";
    private static final String SAMPLE_FILE_PATH = "/C:\\Users\\Admin\\OneDrive\\Desktop\\\\Minaz.pdf";
    
    //filepath for encypted files
    private static final String ENCRYPTED_FILE_PATH ="/C:\\\\Users\\\\Admin\\\\OneDrive\\\\Desktop\\\\user_2_encry.pdf";
    private static final String demoencrypt= "/C:\\\\Users\\\\Admin\\\\OneDrive\\\\Desktop\\\\ccfile1.pdf";
    
    //filepath for decrypted file
    private static final String demodecrypt = "/C:\\\\Users\\\\Admin\\\\OneDrive\\\\Desktop\\\\user_2_pardecry.pdf";
    private static final String DECRYPTED_FILE_PATH = "/C:\\\\Users\\\\Admin\\\\OneDrive\\\\Desktop\\\\user_2_decryFile.pdf";
	

	public static void main(String[] args) {  
		try{    
			
			long startTime = System.nanoTime();
			
			File csampleFile = new File(SAMPLE_FILE_PATH);
	        File cencryptedFile = new File(ENCRYPTED_FILE_PATH);
	        File cencryptedFile1 = new  File (demodecrypt );
	        File cdecryptedFile = new File(DECRYPTED_FILE_PATH);
	        File cdecryptedFile1 = new  File (demodecrypt );
	        
		//Establish a connection with server
			Socket S1=new Socket("127.0.0.1",6974);
			System.out.println("Server connected");
			
			Scanner in=new Scanner(System.in);
			
			int  p=7919;
			int q=45;
			int  xb;
			
			 //private  key of user 2
			System.out.println("enter private key of user1 xb:");
			xb=in.nextInt();
			
			//public key of user 2
			yb =  BigInteger.valueOf(q).modPow(BigInteger.valueOf(xb), BigInteger.valueOf(p));
		    int  c = yb.intValue();
		    
		  //sending of public key of user 2 to user 1
			DataOutputStream Stream1=new DataOutputStream(S1.getOutputStream());
			Stream1.writeInt(c);
	
			//receiving of  public of user1
			DataInputStream f = new DataInputStream(S1.getInputStream()); 
			int a = f.readInt();
			ya =BigInteger.valueOf(a);
			
			//generation of shared key2
			 key2 =ya.modPow(BigInteger.valueOf(xb), BigInteger.valueOf(p));
			 System.out.println("shared key of user2:" +key2);
			 int e=key2.intValue();
			 
			System.out.println("Xb: " + xb);
			System.out.println("Ya: " + a);
			System.out.println("Yb: " + c);
			System.out.println("k2: " + key2);
			 
//-------------------------------------------receiving of encrypted file---------------------------------------------------------------------------------------
			
			    
			DataInputStream dis= new DataInputStream(S1.getInputStream());
            DataOutputStream dos= new DataOutputStream(S1.getOutputStream());  
            int bytes = 0;
	        FileOutputStream fileOutputStream = new FileOutputStream(cencryptedFile);
	        
	        long size = dis.readLong();     // read file size
	        byte[] buffer = new byte[4*1024];
	        while (size > 0 && (bytes = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
	            fileOutputStream.write(buffer,0,bytes);
	            size -= bytes;       // read upto file size
	        }
	        fileOutputStream.close();
		
//------------------------------------------------------------decryption using blowfish key -----------------------------------------------------------
            
             user2.decrypt(cencryptedFile, cdecryptedFile1);
            
//---------------------------------------------------------------decryption using shared key----------------------------------------------------------
             user2.decrypt1(cdecryptedFile1, cdecryptedFile);
		
				   
				S1.close();
				
				long endTime = System.nanoTime();
	            
	            long timeElapsed = endTime - startTime;
	            System.out.println("Execution time to complete process: " + timeElapsed / 1000000+"ms");
			 

}
		catch(Exception e){System.out.println(e);
		}  
}
	
//----------------------------------------------------------------blowfish-----------------------------------------------------------------------
	
//-------------------------------------------------------encryption using shared key-----------------------------------------------------------
	
public static void encrypt1(File csampleFile, File cencryptedFile1)
        throws Exception {
    doCrypto(Cipher.ENCRYPT_MODE, csampleFile, cencryptedFile1);
    

}

//---------------------------------------------------------------decryption using shared key---------------------------------------------------------------

public static void decrypt1(File cdecryptedFile1, File cdecryptedFile)
        throws Exception {
	doCrypto(Cipher.DECRYPT_MODE, cdecryptedFile1, cdecryptedFile);

}

//------------------------------------------------------------shared key encryption and decryption----------------------------------------------------

private static void doCrypto(int cipherMode, File cdecryptedFile1,File cdecryptedFile ) throws Exception {
  
    final String shkey =String.valueOf(key2);
    //System.out.println(key2);
  	final byte[] KeyData = shkey.getBytes();
  	//System.out.println("blowfish key from diffie hellman--" +KeyData);  		
  	SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
  	
	Cipher cipher = Cipher.getInstance("Blowfish");
	cipher.init(cipherMode, KS);
  
	InputStream inputStream = new FileInputStream(cdecryptedFile1);
	byte[] inputBytes = new byte[(int) cdecryptedFile1.length()];
	inputStream.read(inputBytes);

	byte[] outputBytes = cipher.doFinal(inputBytes);

	OutputStream outputStream = new FileOutputStream(cdecryptedFile);
	outputStream.write(outputBytes);

	inputStream.close();
	outputStream.close();
  
}


//----------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------encryption using blowfish key---------------------------------------------------------------

public static void encrypt(File cencryptedFile1, File cencryptedFile)
      throws Exception {
  doCrypto1(Cipher.ENCRYPT_MODE, cencryptedFile1, cencryptedFile);

}

//---------------------------------------------------------------decryption using  blowfish---------------------------------------------------------------

public static void decrypt(File cencryptedFile1, File cdecryptedFile1)
      throws Exception {
	doCrypto1(Cipher.DECRYPT_MODE, cencryptedFile1, cdecryptedFile1);

}

//------------------------------------------------------------blowfish encryption and decryption----------------------------------------------------------

private static void doCrypto1(int cipherMode, File cencryptedFile,File cdecryptedFile1) throws Exception {
    
	key="minazinsselfstudy";
    Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
    String z=secretKey.getEncoded().toString();
    //System.out.println("blowfishkey--" +z);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(cipherMode, secretKey);

    InputStream inputStream = new FileInputStream(cencryptedFile);
    byte[] inputBytes = new byte[(int) cencryptedFile.length()];
    inputStream.read(inputBytes);


    byte[] outputBytes = cipher.doFinal(inputBytes);

    OutputStream outputStream = new FileOutputStream(cdecryptedFile1);
	outputStream.write(outputBytes);

	inputStream.close();
	outputStream.close();
}
		
	}
	

