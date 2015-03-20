package DeveloperCity.Security;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto {

    public static final String EncryptForever(String plainText) {
        if (plainText == null || plainText.length() == 0) {
            return "";
        }
        return encryptPassword(plainText);
    }

    private static final String encryptPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(password.getBytes("UTF8"));
            byte[] hash = messageDigest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hexSequence = Integer.toHexString(0xFF & hash[i]).toUpperCase();
                if (hexSequence.length() == 1) {
                    hexSequence = "0".concat(hexSequence);
                }
                hexString.append(hexSequence);
            }
            password = hexString.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.io.UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return password;
    }
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static final String Encrypt(String plainText) {
        if (plainText == null || plainText.equals("")) {
            return "";
        }
        return Encrypt(plainText, getKey(), getInitVector());
    }

    private static final String Encrypt(final String plainText,
                                        final byte[] key,
                                        final String initVector) {

        try {
            SecretKey secret = new SecretKeySpec(key, KEY_ALGORITHM);
            sun.misc.BASE64Encoder b64e = new sun.misc.BASE64Encoder();

            byte[] exchange = null;
            byte[] text = plainText.getBytes("UnicodeLittle");
            if ((text[0] == -1 && text[1] == -2) || (text[0] == -2 && text[1] == -1)) {
                exchange = new byte[text.length - 2];
                System.arraycopy(text, 2, exchange, 0, exchange.length);
            }
            else {
                exchange = new byte[text.length];
                System.arraycopy(text, 0, exchange, 0, exchange.length);
            }

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes("ASCII"));
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
            byte[] ciphertext = cipher.doFinal(exchange);
            return b64e.encode(ciphertext);
        } catch (Exception e) {
            return "";
        }
    }

    public static final String Decrypt(String cipherText) {

        if (cipherText == null || cipherText.equals("")) {
            return "";
        }
        return Decrypt(cipherText, getKey(), getInitVector());
    }

    private static final String Decrypt(final String cipherText,
                                        final byte[] key,
                                        final String initVector) {

        try {
            SecretKey secret = new SecretKeySpec(key, KEY_ALGORITHM);
            sun.misc.BASE64Decoder b64d = new sun.misc.BASE64Decoder();
            byte[] hash = b64d.decodeBuffer(cipherText);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector.getBytes("ASCII"));
            cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
            byte[] plainTextBytes = cipher.doFinal(hash);

            String plainText = new String(plainTextBytes, "UnicodeLittle");
            
            return plainText;
        } catch (Exception e) {
            return "";
        }
    }

    private static final byte[] getKey() {
		 throw new Exception("Not implemented");
    }

    private static final String getInitVector() {
        return "AÖùãGR¼\nã¯hvï";
    }
}
