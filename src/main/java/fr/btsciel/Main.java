package fr.btsciel;

import jssc.SerialPortException;

public class Main {
    public static void main(String[] args) {
        byte numeroEsclave = 1;
        ClasseModbus classeModbus = new ClasseModbus(numeroEsclave);

        try {
            System.out.println("Quel comx ?");
            String com = In.readString();
            classeModbus.connecEsclave(com,9600,8,0,1);
            System.out.println("N° de l'esclave à interroger ?");
            int  numEscalve = In.readInteger();
            if(numEscalve<=0){
                System.out.println("ça ne peut pas etre en dessous ou égal à 0");
            }
            System.out.println("registre interroger ?");
            int reg = In.readInteger();
            classeModbus.lectureCoils(reg,2);


        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}