package solmu.gen;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BaseGenerator {

    protected Random rand;
    public BaseGenerator(Random r){
        this.rand =r;
        reset();
    }
    public void reset(){
        varValues = new HashMap<>();
        varBits = new HashMap<>();
    }

    protected static Map<String,BigInteger> varValues;

    public Map<String,BigInteger> getVarValues(){
        return varValues;
    }

    protected static Map<String,Integer> varBits;

    public Map<String,Integer> getVarBits(){
        return varBits;
    }

//    public String randomElement(String type, Random rand){
//        switch (type){
//            //case "int": return ""+rand.nextInt();
//            case "string": return stringGen(rand);
//            case "boolean": return ""+rand.nextBoolean();
//            default: return intGen(rand);
//        }
//    }

    public String randomElement(String type) {
        return randomElement(null, type);
    }
    public String randomElement(String name, String type){
        if(type.equals("string")) {
            return "\"" + RandomStringUtils.randomAlphanumeric(rand.nextInt(3) + 1) + "\"";
        }
        else if(type.equals("bool")){
            return ""+rand.nextBoolean();
        }
        else if(type.startsWith("int")){
            String postfix = type.replace("int","").trim();
            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix); // =8
            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
            rand.nextBytes(bytes);
            BigInteger bi = new BigInteger(bytes);
            if(name != null){
                varValues.put(name,bi);
                varBits.put(name,bits);
            }
            String ret = (rand.nextBoolean()  ? bi.toString() : ("0x"+bi.toString(16)).replace("0x-","-0x"));
            //String ret = bi.toString();
            if(ret.length() >=41 && ret.length() <=45)
            {
                ret=ret.replace("0x","0x0000");
            }
            return ret;
        }
        else if(type.startsWith("uint")){
            String postfix = type.replace("uint","").trim();
            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix); //=8;
            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
            rand.nextBytes(bytes);
            BigInteger bi = new BigInteger(1,bytes);
            if(name != null){
                varValues.put(name,bi);
                varBits.put(name,bits);
            }
            String ret = (rand.nextBoolean() ? bi.toString() : "0x"+bi.toString(16));
            //String ret = bi.toString();
            if(ret.length() >=41 && ret.length() <=45)
            {
                ret=ret.replace("0x","0x0000");
            }
            return ret;
        }

//        else if(type.startsWith("int")){
//            String postfix = type.replace("int","").trim();
//            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix);
//            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
//            rand.nextBytes(bytes);
//            BigInteger bi = new BigInteger(bytes);
//            if(name != null){
//                varValues.put(name,bi);
//            }
//            String ret = (rand.nextInt(2) == 0 ? bi.toString() : ("0x"+bi.toString(16)).replace("0x-","-0x"));
//            if(ret.length() >=41 && ret.length() <=45)
//            {
//                ret=ret.replace("0x","0x0000");
//            }
//            return ret;
//        }
//        else if(type.startsWith("uint")){
//            String postfix = type.replace("uint","").trim();
//            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix);
//            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
//            rand.nextBytes(bytes);
//            BigInteger bi = new BigInteger(1,bytes);
//            if(name != null){
//                varValues.put(name,bi);
//            }
//            String ret = (rand.nextInt(2) == 0 ? bi.toString() : "0x"+bi.toString(16));
//            if(ret.length() >=41 && ret.length() <=45)
//            {
//                ret=ret.replace("0x","0x0000");
//            }
//            return ret;
//        }
        return "";
    }

    public String intUintTypeGen(String type){
        int typePostfix = (rand.nextInt(33)*8); //=0 TODO turn off on
        if(typePostfix==0){
            return type;
        }
        return type+typePostfix;
    }

    public String intUintTypeGen(){
        String[] types= {"int", "uint"};
        String type = types[rand.nextInt(types.length)];
        return intUintTypeGen(type);
    }

    public String intGen(Random r){
        switch(r.nextInt(4)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            //case 2: return "0x"+Integer.toHexString(r.nextInt());
            //case 3: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+r.nextInt();
        }
    }

    public String stringGen(Random r){
        switch(r.nextInt(2)){
            case 1:  byte[] array = new byte[10]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                return "\""+ StringEscapeUtils.escapeJava(generatedString)+"\"";
            default:  return "\""+ RandomStringUtils.randomAlphanumeric(r.nextInt(10)+1)+"\"";
        }
    }



    /*private String randomElement(String type,Random rand){
        if(type.equals("string")) {
            return "\"" + RandomStringUtils.randomAlphanumeric(rand.nextInt(3) + 1) + "\"";
        }
        else if(type.equals("bool")){
            return ""+rand.nextBoolean();
        }
        else if(type.startsWith("int")){
            String postfix = type.replace("int","").trim();
            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix);
            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
            rand.nextBytes(bytes);
            BigInteger bi = new BigInteger(bytes);
            String ret = (rand.nextInt(2) == 0 ? bi.toString() : ("0x"+bi.toString(16)).replace("0x-","-0x"));
            if(ret.length() >=41 && ret.length() <=44)
            {
                ret=ret.replace("0x","0x000");
            }
            return ret;
        }
        else if(type.startsWith("uint")){
            String postfix = type.replace("uint","").trim();
            int bits = postfix.isEmpty() ? 256 : Integer.parseInt(postfix);
            byte[] bytes = new byte[rand.nextInt(bits/8)+1];
            rand.nextBytes(bytes);
            BigInteger bi = new BigInteger(1,bytes);
            String ret = (rand.nextInt(2) == 0 ? bi.toString() : "0x"+bi.toString(16));
            if(ret.length() >=41 && ret.length() <=44)
            {
                ret=ret.replace("0x","0x000");
            }
            return ret;
        }
        return "";
    }*/

    public static  BigInteger trimBits(BigInteger bi, int bits, String type){
        int bytes = bits/8;
        if(bi.toByteArray().length <= bytes){
            if(type.equals("uint") && bi.toString().startsWith("-")){
                bi = parseBigIntegerPositive(bi.toString(),bits);
            }
            return bi;
        }
        byte data[] = bi.toByteArray();
        byte dataNew[] = new byte[bytes];
        for(int i = 0; i < bytes; i++){
            dataNew[i] = data[i+(data.length-bytes)];
        }

        System.out.println(bi.toString(16));


        BigInteger newBi = new BigInteger(dataNew);
        System.out.println(newBi.toString(16));
        if(type.equals("uint") && newBi.toString().startsWith("-")){
            newBi = parseBigIntegerPositive(newBi.toString(),bits);
        }
        System.out.println(newBi.toString(16));


        return newBi;
    }

    public static BigInteger parseBigIntegerPositive(String num, int bitlen) {
        if (bitlen < 1)
            throw new RuntimeException("Bad bit length:" + bitlen);
        BigInteger bref = BigInteger.ONE.shiftLeft(bitlen);
        BigInteger b = new BigInteger(num);
        if (b.compareTo(BigInteger.ZERO) < 0)
            b = b.add(bref);
        if (b.compareTo(bref) >= 0 || b.compareTo(BigInteger.ZERO) < 0 )
            throw new RuntimeException("Out of range: " + num);
        return b;
    }


}
