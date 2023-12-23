
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


public class user1 {
	
	static BigInteger ya,yb,key1;
	static int a,b;
	
//-----------------------------------------------------------------blowfish file path-----------------------------------------------------------------------
	
		private static final String ALGORITHM = "Blowfish";
	    private static String key= "key1";
	    private static final String SAMPLE_FILE_PATH = "/C:\\Users\\Admin\\OneDrive\\Desktop\\\\Minaz.pdf";
	    
	    //filepath for encypted files
	    private static final String ENCRYPTED_FILE_PATH ="/C:\\Users\\Admin\\AppData\\Local\\Temp\\\\user1_encryfile.pdf";
	    private static final String demoencrypt= "/C:\\Users\\Admin\\AppData\\Local\\Temp\\\\user1_file1.pdf";
	    
	    //filepath for decrypted file
	    private static final String demodecrypt = "/C:\\Users\\Admin\\AppData\\Local\\Temp\\\\user1_partialdecryfile1.pdf";
	    private static final String DECRYPTED_FILE_PATH = "/C:\\Users\\Admin\\AppData\\Local\\Temp\\\\Desktop\\\\user1_decryptedfile.pdf";

//----------------------------------------------------------------main-------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	   
	public static void main(String[] args) {
		try {
			
//-----------------------------------------------------------------files-----------------------------------------------------------------------------------
			
			 	File sampleFile = new File(SAMPLE_FILE_PATH);
		        File encryptedFile = new File(ENCRYPTED_FILE_PATH);
		        File encryptedFile1 = new  File (demodecrypt );
		        File decryptedFile = new File(DECRYPTED_FILE_PATH);
		        File  decryptedFile1 = new  File (demodecrypt );
		        
		        
		        	int  p=7919;//q
		        	int q=453;//alpha
		        	int  xa;
			
		        		Scanner in =new Scanner(System.in);
			
			
//-------------------------------------------------------------------------diffie hellman--------------------------------------------------------------------
			
//-------------------------------------------------------private  key of user 1--------------------------------------------------------------
			
				System.out.println("enter private key of user1 xa:---");
				xa=in.nextInt();
			
//--------------------------------------------------------------------connection establishment-----------------------------------------------------------
				
			   ServerSocket s= new ServerSocket(6974);
			   System.out.println("server stRTrs..");
			   Socket sk1= s.accept();
			   
 //----------------------------------------------------------------------generation of public key of user1----------------------------------------------- 
			   
				ya =  BigInteger.valueOf(q).modPow(BigInteger.valueOf(xa), BigInteger.valueOf(p));
		        int a =ya.intValue();
			   
//----------------------------------------------------------------------------sending public key------------------------------------------------------
		        
			    DataOutputStream Stream1=new DataOutputStream(sk1.getOutputStream());
				Stream1.writeInt(a);
				
//----------------------------------------------------------------------recieving public key of user2---------------------------------------------
				
			   DataInputStream E = new DataInputStream(sk1.getInputStream()); 
				int c = E.readInt();
				yb =BigInteger.valueOf(c);
				
 //-------------------------------------------------------------------------secret key1-------------------------------------------------------------
				
				key1 =yb.modPow(BigInteger.valueOf(xa), BigInteger.valueOf(p));
				int b =key1.intValue();
			    
//----------------------------------------------------------------printing of keys-----------------------------------------------------------------------
				System.out.println("Xa: " + xa);
				System.out.println("Ya: " + a);
				System.out.println("Yb: " + c);
				System.out.println("k1: " + key1);
				
//------------------------------------------------------------encrypt using sharedkey1------------------------------------------------------------------
				
				    user1.encrypt1(sampleFile, encryptedFile1);
				    
//-------------------------------------------------------------encryption using blowfish key------------------------------------------------------------
				    
		            user1.encrypt(encryptedFile1, encryptedFile);
		            
 //------------------------------------------------------------decryption using blowfish key -----------------------------------------------------------
		            
		           //user1.decrypt(encryptedFile, decryptedFile1);
		            
 //---------------------------------------------------------------decryption using shared key----------------------------------------------------------
		           //user1.decrypt1(decryptedFile1, decryptedFile);
		            
//--------------------------------------------------------------sendind of encrypted file to user2-----------------------------------------------------
		           
		            FileInputStream fi =new FileInputStream(encryptedFile);
		            DataOutputStream os = new DataOutputStream(sk1.getOutputStream());
		            os.writeLong(encryptedFile.length());
		            // break file into chunks
		            int bytes =0;
		            byte[] buffer = new byte[4*1024];
		            while ((bytes=fi.read(buffer))!=-1){
		                os.write(buffer,0,bytes);
		                os.flush();
		            }
		            fi.close();
		            
		            
		            
		}
		
//---------------------------------------------------------------------catch--------------------------------------------------------------------------------------------
		        catch (Exception e)
				{
		            e.printStackTrace();
		        }			
		
	}
	
	//-------------------------------------------------------------end of main----------------------------------------------------------------------
	
	//----------------------------------------------------------------blowfish-----------------------------------------------------------------------
	
	//-------------------------------------------------------encryption using shared key-----------------------------------------------------------
	
public static void encrypt1(File sampleFile, File encryptedFile1)
        throws Exception {
    doCrypto(Cipher.ENCRYPT_MODE, sampleFile, encryptedFile1);

}

//---------------------------------------------------------------decryption using shared key---------------------------------------------------------------

    public static void decrypt1(File decryptedFile1, File decryptedFile)
        throws Exception {
	doCrypto(Cipher.DECRYPT_MODE, decryptedFile1, decryptedFile);

}

//------------------------------------------------------------shared key encryption and decryption----------------------------------------------------

private static void doCrypto(int cipherMode, File sampleFile,File encryptedFile1 ) throws Exception {
  
    final String shkey =String.valueOf(key1);
  	final byte[] KeyData = shkey.getBytes();
  	//System.out.println("blowfish key from diffie hellman--" +KeyData);  		
  	SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
  	
	Cipher cipher = Cipher.getInstance("Blowfish");
	cipher.init(cipherMode, KS);
  
	InputStream inputStream = new FileInputStream(sampleFile);
	byte[] inputBytes = new byte[(int) sampleFile.length()];
	inputStream.read(inputBytes);

	byte[] outputBytes = cipher.doFinal(inputBytes);

	OutputStream outputStream = new FileOutputStream(encryptedFile1);
	outputStream.write(outputBytes);

	inputStream.close();
	outputStream.close();
  
}


//----------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------encryption using blowfish key---------------------------------------------------------------

public static void encrypt(File encryptedFile1, File encryptedFile)
      throws Exception {
  doCrypto1(Cipher.ENCRYPT_MODE, encryptedFile1, encryptedFile);

}

//---------------------------------------------------------------decryption using  blowfish---------------------------------------------------------------

public static void decrypt(File decryptedFile1, File decryptedFile)
      throws Exception {
	doCrypto1(Cipher.DECRYPT_MODE, decryptedFile1, decryptedFile);

}

//------------------------------------------------------------blowfish encryption and decryption----------------------------------------------------------

private static void doCrypto1(int cipherMode, File encryptedFile1,File encryptedFile) throws Exception {
    
	key="minazinsselfstudy";
    Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
    String z=secretKey.getEncoded().toString();
    //System.out.println("blowfishkey--" +z);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(cipherMode, secretKey);

    InputStream inputStream = new FileInputStream(encryptedFile1);
    byte[] inputBytes = new byte[(int) encryptedFile1.length()];
    inputStream.read(inputBytes);


    byte[] outputBytes = cipher.doFinal(inputBytes);

    OutputStream outputStream = new FileOutputStream(encryptedFile);
	outputStream.write(outputBytes);

	inputStream.close();
	outputStream.close();
}
	}