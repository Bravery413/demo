package utils;

import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringUtils {
	private StringUtils(){
	}
	
	public static boolean isNullOrEmpty(String str) {
		return  (str == null || "".equals(str));
	}

	public static String emptyIfNull(String str) {
        return  (str == null ? "" : str);
    }
	
	public static boolean isEnglishStr(String str) {
		return str.matches("\\w*");
	}
	
	public static String md5(String source)
    {   
	    char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};   
        try {   
        	MessageDigest  md5 = MessageDigest.getInstance("MD5"); 
            byte[] bs = md5.digest(source.getBytes());   
            char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，   
            // 所以表示成 16 进制需要 32 个字符   
            int k = 0;                                // 表示转换结果中对应的字符位置   
            for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节   
                // 转换成 16 进制字符的转换   
                byte byte0 = bs[i];                 // 取第 i 个字节   
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,   
                // >>> 为逻辑右移，将符号位一起右移   
                str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换   
            }   
            return new String(str);   
        } catch (NoSuchAlgorithmException e) {   
            throw new RuntimeException("no such md5 algorithm!", e);   
        }
    }  
    
	public static int parseInt(String str, int defVal) {
        if (str == null) {
            return defVal;
        }
		try {
			int i = Integer.parseInt(str);
			return i;
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public static long parseLong(String str, long defVal) {
        if (str == null) {
            return defVal;
        }
		try {
			long i = Long.parseLong(str);
			return i;
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public static float parseFloat(String str, float defVal) {
		if (str == null) {
			return defVal;
		}
		try {
			float i = Float.parseFloat(str);
			return i;
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public static String urlEncode(String str) {
	    if (StringUtils.isNullOrEmpty(str)) {
	        return "";
        }
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String urlDecode(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            return "";
        }
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

    public static String base64Encode(byte[] data) {
	    if (data == null) {
	        return "";
        }
        try {
            return new String(Base64.encodeBase64(data), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

	public static String base64Encode(String data) {
        if (StringUtils.isNullOrEmpty(data)) {
            return "";
        }
		try {
			return new String(Base64.encodeBase64(data.getBytes("UTF-8")), "UTF-8");
		} catch (Exception e) {
			return "";
		}  
	}
	
	public static String base64Decode(String data) {
        if (StringUtils.isNullOrEmpty(data)) {
            return "";
        }
		try {
			byte[] bt = Base64.decodeBase64(data.getBytes("UTF-8"));
			return new String(bt, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}

    public static byte[] base64DecodeBytes(String data) {
        if (StringUtils.isNullOrEmpty(data)) {
            return null;
        }
        try {
            byte[] bt = Base64.decodeBase64(data.getBytes("UTF-8"));
            return bt;
        } catch (Exception e) {
            return null;
        }
    }

	public static String sha1(String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.getBytes("UTF-8"));
			return byteToStr(digest);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 将字节数组转换为十六进制字符串
	 */
	private static String byteToStr(byte[] byteArray) {
		int len = byteArray.length;
		StringBuilder strDigest = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			strDigest.append(byteToHexStr(byteArray[i]));
		}
		return strDigest.toString();
	}

	/**
	 * 将字节转换为十六进制字符串
	 */
	private static String byteToHexStr(byte mByte) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		char[] tempArr = new char[2];
		tempArr[0] = digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = digit[mByte & 0X0F];

		return new String(tempArr);
	}

    public static String genCheckCode(int size){
        if (size<=0||size>6) {
            size = 4;
        }
        StringBuilder chk_code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int num = random.nextInt(10);
            chk_code.append(num);
        }
        return chk_code.toString();
    }

    /**
     * 得到小的微信头像
     * @return
     */
    public static String getWeiXinSmallImg(String img){
        if (isNullOrEmpty(img)) {
            return "";
        }
	    if (img.endsWith("/0")){
		    return img.substring(0, img.length()-1) + "96";
	    }else if (img.endsWith("/46")) {
		    return img.substring(0, img.length()-2) + "96";
	    }else if (img.endsWith("/64")) {
		    return img.substring(0, img.length()-2) + "96";
	    }else if (img.endsWith("/96")) {
		    return img.substring(0, img.length()-2) + "96";
	    }else if (img.endsWith("/132")) {
		    return img.substring(0, img.length()-3) + "96";
	    }else {
		    return img;
	    }
    }

}
