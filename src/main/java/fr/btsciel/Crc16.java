package fr.btsciel;

public class Crc16 {
    static int calculCrc16 (byte[] octets, int valeurInitiale, int polynomme){
        int stdPoly = 0xA001;
        int initialValue = 0xffff;
        int crc = valeurInitiale;
        for(int p = 0; p < octets.length; p++){
            crc ^= (octets[p] & 0xFF);
            for(int i = 0; i < 8; i++){
              if((crc & 1) != 0){
                  crc = (crc >>1) ^ polynomme;
                }else {
                  crc = crc >>1;
              }
            }
        }
        return crc;
    }

    static byte[] hexStringEnByteArray(String message){
        int len = message.length();
        byte[] data = new byte[len/2];
        for (int i = 0; i < len; i += 2) {
            data[i/2] = (byte) ((Character.digit(message.charAt(i), 16)<<4)
                    +  Character.digit(message.charAt(i+1), 16));
        }
        return data;
    }



}