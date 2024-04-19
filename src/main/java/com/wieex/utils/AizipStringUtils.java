package com.wieex.utils;

public class AizipStringUtils {


    public static String idxString(String src, int[] idxs) {
        StringBuilder result = new StringBuilder();

        for (int index : idxs) {
            if (index >= 0 && index < src.length()) {
                result.append(src.charAt(index));
            } else {
                System.out.println("Index out of range: " + index);
            }
        }

        return result.toString();
    }


    public static void main(String[] args) {
        String md_KEY1 = "63a9f0ea7bb98050796b649e85481845"; // 替换为你的 MD5 Key

        int[] k1_idx = {30, 8, 11, 18, 12, 14, 26, 11, 16, 18, 22, 8, 16, 6, 20, 13};

        String generatedString = AizipStringUtils.idxString(md_KEY1, k1_idx);

        System.out.println("Generated string: " + generatedString);
    }

}
