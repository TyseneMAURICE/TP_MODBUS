package fr.btsciel;

import jssc.SerialPortEvent;
import jssc.SerialPortException;

import java.sql.SQLOutput;

public class ClasseModbus extends LiaisonSerie {
    public byte numeroEcslave;
    private Crc16 crc16 = new Crc16();
    private BigEndian bigEndian =  new BigEndian();


    public ClasseModbus(byte numeroEcslave) {
        this.numeroEcslave = numeroEcslave;
    }


    public void lectureCoils(int registre, int longueur){
      byte[] ad_registre = intDeuxBytes(registre);
      byte msb_ad_registre = ad_registre[0];     //Most Significatibe Byte MSB
      byte lsb_ad_registre = ad_registre[1];    //Low Significative Byte  LSB
      byte[] lalongueur  = intDeuxBytes(longueur);
      byte msb_lalongueur = lalongueur[0];
      byte lsb_lalongueur = lalongueur[1];
      byte[] TrameTemp = {numeroEcslave,(byte)03,msb_ad_registre,lsb_ad_registre,msb_lalongueur,lsb_lalongueur};
      byte[] crc16 = intDeuxBytes(Crc16.calculCrc16(TrameTemp,0xFFFF,0xA001));
      byte msb_crc16 = crc16[0];
      byte lsb_crc16 = crc16[1];
      byte[] TrameToSend = {numeroEcslave,(byte)03,msb_ad_registre,lsb_ad_registre,msb_lalongueur,lsb_lalongueur,lsb_crc16,msb_crc16};
      super.ecrire(TrameToSend);
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        int nbs = super.detecteSiReception();
        if(nbs == 0)return;
        byte[] trame = super.lireTrame(nbs);
        for(byte b : trame){
            System.out.print(b & 0xff); //dé-signé
            System.out.print(" ");
        }

        //vérifie si c'est le bonne esclave qui réponds à la bonne requete et après on vérifie le crc
        if(trame[0] == numeroEcslave && trame[1] == (byte)03){
            byte[] crcR = new byte[trame.length-2];
            for (int i = 0; i < trame.length-2; i++) {
                crcR[i] = trame[i];
            }
            byte [] crcCalcule = intDeuxBytes(Crc16.calculCrc16(crcR,0xFFFF,0xA001));
            byte msb_crcRecu = crcCalcule[0];
            byte lsb_crcRecu = crcCalcule[1];
            System.out.print("\n" +(lsb_crcRecu&0xff) +" "+ (msb_crcRecu&0xff));
        }

    }


    public void connecEsclave(String com,int vitesse,int donnees,int parity ,int stop) throws SerialPortException {
        super.initCom(com);
        configurerParametres(vitesse,donnees,parity,stop);
    }

    public byte[] intDeuxBytes(int value){
        byte[] data = new byte[2];
        data[1] = (byte) value;
        data[0] = (byte) (value >> 8);
        return data;
    }

    public void fermerLiaisonSerie(){
        fermerPort();
    }

}
