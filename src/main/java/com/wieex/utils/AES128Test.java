package com.wieex.utils;

import java.io.IOException;

public class AES128Test {

    public static void main(String[] args) {


        String hexKey = "6e892e57c9fa098afb0850998245273f" +
                "ef273e2989acb87d466d534bda5890c0" +
                "8786e7f97a9097da8b6a9898a0ab90b8" +
                "7df7d8e7e09a0d909abc099a09d90aa8" +
                "e69aadb8ad6a89e6897c89a09c634193" +
                "152c2167c98c0d8f48e7043896c7d4f7" +
                "09903647175348908ddf4aae3490dc9d" +
                "645aed0db7a4afe3db08b0a5784bb6f2" +
                "a6648788b567db4d68eb8658986688c6" +
                "68a5e87ab075a64b5dacef45ebea7ea4" +
                "5afa8cea75e4578a0998e53a232e4e78";



        String PAIICKEY="3352416f55744f777732314e6839625a77526242544f42484d527a6365783949474164516b59374b4f6345385865675176624547525859426d424d4e433434674e484c464c4d34656f4d6b4c6b4c7574677575586348436e596b5a474f427954484c37336c3065337456764d36476548756f7044766e6643533262366c6446304b3746584d5066766e69686b466e4c6c437a6750753742716774726366567858466d5876494c634c386c66644f646c45";

        String Cocheer = "6D5774586941566B694D6B7675703265\n" +
                "506B615635587151564559786E685A78\n" +
                "61676D4B664E44466C45364268504152\n" +
                "636C554775385A724657723046707976\n" +
                "69443671337179626F65576156467359\n" +
                "6C38575846346F673035363447693331\n" +
                "554E3447476869775373475568674857\n" +
                "46315741366E6862686E723167453056\n" +
                "686165596C6B774B546E567930474735\n" +
                "31476264795178706B57714E50445352\n" +
                "4F63584B4F5263386953574264314E55";
        /**
         INSERT INTO `chip_key` (`id`, `chip_factory`, `chip`, `channel`, `burn_factory`, `key_info`, `model_version`, `available_sn`, `status`, `create_time`)
         VALUES
         (2, 'PAIIC', '192', 'PAIIC', 'PAIIC', '3352416f55744f777732314e6839625a77526242544f42484d527a6365783949474164516b59374b4f6345385865675176624547525859426d424d4e433434674e484c464c4d34656f4d6b4c6b4c7574677575586348436e596b5a474f427954484c37336c3065337456764d36476548756f7044766e6643533262366c6446304b3746584d5066766e69686b466e4c6c437a6750753742716774726366567858466d5876494c634c386c66644f646c45', 'KWS-1.0.0P', 10, 1, '2022-09-27 19:19:33'),
         (3, 'JIELI', 'AC7911B8', 'COCHEER', 'COCHEER', '6d5774586941566b694d6b7675703265506b615635587151564559786e685a7861676d4b664e44466c45364268504152636c554775385a72465772304670797669443671337179626f655761564673596c38575846346f673035363447693331554e344747686977537347556867485746315741366e6862686e723167453056686165596c6b774b546e56793047473531476264795178706b57714e504453524f63584b4f5263386953574264314e55', 'VWW-1.0.0', 10, 1, '2022-09-27 19:19:33');
         */


        String uid0 = "0x21553098";
        String uid1 = "0xFFE62688";
        String uid2 = "0xFFFFFFFF";
        String uid3 = "0xFFFFFFFF";

        String uids1 = "00000000983055218826E6FFFFFFFFFF";
        String res1 = "5FAEB25A3E8626C6D6E2E01C4C96E79D";

        String uids2 = "00000000BCDE6E400084CBB11429FCFF";
        String res2 = "05C58ED0F9CAFBFE7345385B5778699D";

        //byte RoundKey[] = new byte[176];
        byte RoundKey[] = new byte[176];

        try {

            System.out.println(hexKey);

            RoundKey = AesEcbCodec.decodeHex(hexKey);

            byte[] cpuIds = AesEcbCodec.decodeHex(uids1);
            System.out.println("in cpuIds1 : " + AesEcbCodec.encodeHexString(cpuIds));
            AES128.AES_ECB_encrypt(RoundKey, cpuIds);
            System.out.println("out cpuIds1 : " + AesEcbCodec.encodeHexString(cpuIds));
            System.out.println("res1 : " + AesEcbCodec.encodeHexString(cpuIds).equals(res1));


            cpuIds = AesEcbCodec.decodeHex(uids2);
            System.out.println("in cpuIds1 : " + AesEcbCodec.encodeHexString(cpuIds));
            AES128.AES_ECB_encrypt(RoundKey, cpuIds);
            System.out.println("out cpuIds1 : " + AesEcbCodec.encodeHexString(cpuIds));
            System.out.println("res1 : " + AesEcbCodec.encodeHexString(cpuIds).equals(res2));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
