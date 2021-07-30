package solmu.common;

import org.apache.log4j.Logger;
import solmu.core.Main;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Util {
    private static final Logger logger =  Logger.getLogger(Util.class);

    public static void writeToFile(String filename, String content){
        try (FileWriter fileWriter = new FileWriter(filename)){
            fileWriter.write(content);
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    public static String readFromFile(String filename){
        String content = "";
        try {
            content = new String(Files.readAllBytes(new File(filename).toPath()));
        }
        catch (Exception e) {
            System.out.println(e.toString());
            // TODO: handle exception
        }
        return content;
    }

    public static void InsertReplaceInFile(String file, String contents, String placeholder){
        String oldContents =Util.readFromFile(file);
        assert(oldContents.contains(placeholder));
        //System.out.println(oldContents);
        String[] parts = oldContents.split(Pattern.quote(placeholder));
        String newContents = "";
        if(parts.length == 2){
            newContents += parts[0] + placeholder + " \n "+ contents + " \n "+ placeholder + parts[1];

        }
        else if (parts.length == 3 ){
            newContents += parts[0] + placeholder + " \n "+ contents + " \n "+ placeholder+ parts[2];
        }
        else
        {
            assert(false);
        }
        //System.out.println(newContents);
        Util.writeToFile(file, newContents);
    }

    public static void InsertReplaceInFile(String file, ArrayList<String> contents, String placeholder){
        String oldContents =Util.readFromFile(file);
        assert(oldContents.contains(placeholder));
        //System.out.println(oldContents);
        String[] parts = oldContents.split(Pattern.quote(placeholder));
        String newContents = "";
        String content = "";
        for (String c: contents) {
            content +=  c + " \n ";
        }

        if(parts.length == 2){
            newContents += parts[0] + placeholder + " \n "+ content + " \n "+ placeholder + parts[1];

        }
        else if (parts.length == 3 ){
            newContents += parts[0] + placeholder + " \n "+ content + " \n "+ placeholder+ parts[2];
        }
        else
        {
            assert(false);
        }
        //System.out.println(newContents);
        Util.writeToFile(file, newContents);
    }

    public static Boolean isNullOrEmplty(String s){
        if(s == null || s.isEmpty()){
            return true;
        }
        return false;
    }



    public static void RunTruffleTest(){
        ProcessBuilder processBuilder = new ProcessBuilder();

        // -- Linux --

        // Run a shell command
        processBuilder.command("bash", "-c", "cd /home/admin1/truffletest && truffle test && node convert-truffle2.js && cd /home/admin1/truffletest/testcases-conflux && node metacoin && cd /home/admin1 && zip -r \""+ Main.ZipStartName+"-$(date +\"%Y-%m-%d %H-%M-%S\").zip\" truffletest");



        try {

            Process process = processBuilder.start();

            //StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                //output.append(line + "\n");
                System.out.println(line);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                //System.out.println("Success!");
                //System.out.println(output);
                //System.exit(0);
                return;
            } else {
                //abnormal...
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
