package javamu.common;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Scanner;

public class Util {
    private static final Logger logger =  Logger.getLogger(Util.class);

    public static void writeToFile(String filename, String content){
        try (FileWriter fileWriter = new FileWriter(filename)){
            fileWriter.write(content);
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    public static String readFile(String filename){
        String content = null;
        try {
            content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        return content;
    }

    public static Boolean isNullOrEmplty(String s){
        if(s == null || s.isEmpty()){
            return true;
        }
        return false;
    }

    public  static  boolean checkCompileable(File file){

        StringBuilder stringBuilder = new StringBuilder();
        Runtime rt = Runtime.getRuntime();
        String javac = "javac";


        try {
            File errorFile = new File("error.txt");
            File outputFile = new File("out.txt");
            errorFile.delete();
            errorFile.createNewFile();
            outputFile.delete();
            // outputFile.createNewFile();
            System.out.println("before process builder for file" + file.toString());
            ProcessBuilder pb = new ProcessBuilder(javac , file.toString());
            pb.redirectError(errorFile);
            pb.redirectOutput(outputFile);
            Process p = pb.start();
            p.waitFor();
            p.destroy();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        try (BufferedReader stdInput = new BufferedReader(new FileReader("out.txt"));
             BufferedReader stdError = new BufferedReader(new FileReader("error.txt"))){

            String s;

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }


            while ((s = stdError.readLine()) != null) {
                if(s.contains("Error:") || s.contains("error:")){
                    logger.error(s);
                    return false;
                }
                if( s.contains("Unknown exception")){
                    logger.fatal(s);
                    return false;
                }
            }
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
        System.out.println("Compilation Success!");
        return true;
    }
}
