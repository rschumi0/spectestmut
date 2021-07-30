package solmu.mutator;

import org.apache.log4j.Logger;
import solmu.common.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SolidityCompiler {
    private static final Logger logger =  Logger.getLogger(SolidityCompiler.class);

    public static String createASTjson(File file, String outputDir) {
        File dumpDir = new File(Paths.get(file.getParent(),outputDir).toString());
        if(!dumpDir.exists()){
            dumpDir.mkdir();
        }

        Path outputJ = Paths.get(file.getParent(),outputDir,file.getName());
        String outputJson = outputJ.toString() + ".json";
        StringBuilder stringBuilder = new StringBuilder();
        Runtime rt = Runtime.getRuntime();
        String solc;

        if (System.getProperty("SOLIDITY") != null){
            solc = System.getProperty("SOLIDITY");
        } else{
            if (System.getProperty("os.name").contains("windows")){
                solc = "C:\\solc\\solc.exe";
            }else if(System.getProperty("os.name").contains("Linux")){
                //solc = "/home/admin1/.local/bin/solc";
                solc = "/usr/bin/solc";
            }
            else{
                solc = "/usr/local/bin/solc";
            }
        }

        try {
            File errorFile = new File("error.txt");
            File outputFile = new File("out.txt");
            errorFile.delete();
            errorFile.createNewFile();
            outputFile.delete();
           // outputFile.createNewFile();
            ProcessBuilder pb = new ProcessBuilder(solc , "--ast-json" , file.toString());
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
                if(!s.startsWith("===") && !s.startsWith("JSON AST"))
                    stringBuilder.append(s+"\n");
            }
            Util.writeToFile(outputJson, stringBuilder.toString());



            while ((s = stdError.readLine()) != null) {
                if(s.contains("Error:") || s.contains("error:")){
                    logger.error(s);
                    return null;
                }
                if( s.contains("Unknown exception")){ 
                    logger.fatal(s);
                    return null;
                }
            }
        } catch (IOException e) {
            logger.error(e);
            return null;
        }

        return stringBuilder.toString();
    }

    public static boolean checkCompileable(File file){

        StringBuilder stringBuilder = new StringBuilder();
        Runtime rt = Runtime.getRuntime();
        String solc;

        if (System.getProperty("SOLIDITY") != null){
            solc = System.getProperty("SOLIDITY");
        } else{
            if (System.getProperty("os.name").contains("windows")){
                solc = "C:\\solc\\solc.exe";
            }else{
                //solc = "/home/admin1/.local/bin/solc";
                solc = "/usr/bin/solc";
            }
        }

        try {
            File errorFile = new File("error.txt");
            File outputFile = new File("out.txt");
            errorFile.delete();
            errorFile.createNewFile();
            outputFile.delete();
            // outputFile.createNewFile();
            System.out.println("before process builder for file" + file.toString());
            ProcessBuilder pb = new ProcessBuilder(solc , file.toString());
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
