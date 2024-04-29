package com.wieex.utils;

import org.apache.commons.codec.digest.DigestUtils;


public class AizipStringUtils {


    public static String idxString(String src, int[] idxs) {

        System.out.println("instr: "+ src);

        src = DigestUtils.md5Hex(src);

        System.out.println("md5: "+ src);

        StringBuilder result = new StringBuilder();

        for (int index : idxs) {
            if (index >= 0 && index < src.length()) {
                result.append(src.charAt(index));
            } else {
                System.out.println("Index out of range: " + index);
            }
        }

        System.out.println("indx: "+ result);
        return result.toString();
    }


    public static void main(String[] args) {
        String deviceId = "36e7f4354008"; // 替换为你的 MD5 Key

        String libRandom = "1695906654943";


        String KEY_1 = "63a9f0ea7bb98050796b649e85481845";
        String KEY_2 = "9b23b3ef458bc13749b969ba78f7a5a4";

        int[] k1_idx = {30, 8, 11, 18, 12, 14, 26, 11, 16, 18, 22, 8, 16, 6, 20, 13};
        int[] k2_idx = {24, 22, 16, 21, 5, 12, 19, 15, 7, 10, 2, 3, 20, 9, 26, 18};
        int[] ran_idx = {26, 3, 9, 1, 7, 28, 13, 20, 16, 22, 18, 8, 11, 10, 5, 4};
        int[] mac_idx = {8, 28, 2, 23, 19, 11, 5, 17, 1, 13, 8, 11, 1, 9, 4, 14};


        //先校验，校验码
        String in_str = AizipStringUtils.idxString(deviceId,mac_idx) +AizipStringUtils.idxString(KEY_1,k1_idx)   + AizipStringUtils.idxString(libRandom,ran_idx)
                + AizipStringUtils.idxString(KEY_2,k2_idx)
                ;
        System.out.println("==============");
        System.out.println(in_str);
        String checkDigitServer = DigestUtils.md5Hex(in_str);

        System.out.println(checkDigitServer);




//        String generatedString = AizipStringUtils.idxString(in_str, ran_idx);
//
//        System.out.println("Generated string: " + generatedString);
    }

}
