package javamu.gen;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class BaseGenerator {

    protected Random rand;
    public BaseGenerator(Random r){
        this.rand =r;
    }

    public static String randomElement(String type, Random rand){
        switch (type){
            //case "int": return ""+rand.nextInt();
            case "String": return stringGen(rand);
            case "Object": return objectGen(rand);
            case "boolean": return ""+rand.nextBoolean();
            //case "float": return floatGen(rand);
            case "double": return doubleGen(rand);
            case "long": return longGen(rand);
            case "short": return shortGen(rand);
            case "byte": return byteGen(rand);
            case "char": return charGen(rand);
            default: return intGen(rand);
        }
    }

    public static  String charGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            //case 1: return "0x"+Integer.toHexString(r.nextInt());
            //case 2: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+((char)r.nextInt(26) + 'a');
        }
    }

    public static String byteGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            //case 1: return "0x"+Integer.toHexString(r.nextInt());
            //case 2: return "0"+Integer.toOctalString(r.nextInt());
            default:  byte[] nbyte = new byte[1];
                r.nextBytes(nbyte);
                return ""+nbyte[0];
        }
    }


    public static String intGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            //case 1: return "0x"+Integer.toHexString(r.nextInt());
            //case 2: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+r.nextInt(100);
        }
    }
    public static String longGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Long.toBinaryString(r.nextLong()) + "L";
            //case 1: return "0x"+Long.toHexString(r.nextLong())+ "L";
            //case 2: return "0"+Long.toOctalString(r.nextLong())+ "L";
            default: return ""+r.nextInt(100)+"L";//r.nextLong(()+ "L";
        }
    }
    public static String shortGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt(Short.MAX_VALUE));
            //case 1: return "0x"+Integer.toHexString(r.nextInt(Short.MAX_VALUE));
            //case 2: return "0"+Integer.toOctalString(r.nextInt(Short.MAX_VALUE));
            default: return ""+r.nextInt(Short.MAX_VALUE);
        }
    }
    public static String doubleGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
//            case 1: while(true) {
//                String f = Double.toHexString(r.nextInt());
//                if(!(f.contains("0x1.a") || f.contains("0x1.b") || f.contains("0x1.c") || f.contains("0x1.d") || f.contains("0x1.e") || f.contains("0x1.f"))){
//                    return f;
//                }
//            }
            //case 2: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+(Math.round((-100.0 + (100.0 - -100.0) * r.nextDouble())*100d)/100d);///(Math.round(r.nextDouble()*100d)/100d);
        }
    }
    public static String floatGen(Random r){
        switch(r.nextInt(3)){
            //case 1: return "0b"+Integer.toBinaryString(r.nextInt());
//            case 1: while(true){
//                String f = Float.toHexString(r.nextFloat())+"f";
//                if(!(f.contains("0x1.a") || f.contains("0x1.b") || f.contains("0x1.c") || f.contains("0x1.d") || f.contains("0x1.e") || f.contains("0x1.f"))){
//                    return f;
//                }
//            }
//            case 2: return "0"+Integer.toOctalString(r.nextInt())+"f";
            default: return ""+((float) Math.round(r.nextFloat()*100f)/100f)+"f";
        }
    }

    public static String stringGen(Random r){
        switch(r.nextInt(2)){
            /*case 1:  byte[] array = new byte[10]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                return "\""+ StringEscapeUtils.escapeJava(generatedString)+"\"";*/
            default:  return "\""+ RandomStringUtils.randomAlphanumeric(r.nextInt(10)+1)+"\"";
        }
    }

    public static String objectGen(Random r){
        switch(r.nextInt(2)){
            case 1:  return "(Object) \""+ RandomStringUtils.randomAlphanumeric(r.nextInt(5)+1)+"\"";
            default:  return "new Object()";
        }
    }
}
