package javamu.core;

import com.github.javaparser.ast.Node;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import javamu.ASTparser.JParser;
import javamu.ASTparser.MyNode;
import javamu.common.BasicGrammar;
import javamu.common.MutationException;
import javamu.common.SystemInfo;
import javamu.common.Util;
import javamu.mutator.MutationEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Main {
    private static CommandLineValues commandLineValues;
    private static final Logger logger =  Logger.getLogger(Main.class);
    public static void main(String[] args) throws MutationException, IOException {

//        Node n = StaticJavaParser.parse("class A { int a =1; } //asdf");
//        System.out.println(n.getAllContainedComments());
//        System.out.println(n.toString());
//        System.out.println(n.getClass());
//        System.out.println(n.getChildNodes().get(0).getClass().getSimpleName());
//        System.out.println(n.getChildNodes().get(0).getChildNodes().get(0).getClass().getSimpleName());
//        System.out.println(n.getChildNodes().get(0).getChildNodes().get(1).getClass().getSimpleName());
//        //CompilationUnit compilationUnit = StaticJavaParser.parse("class A { }");


        for (BasicGrammar.NodeType nodeType : BasicGrammar.NodeType.values()) {
            SystemInfo.nodesMap.put(nodeType.toString(),Integer.valueOf(0));
        }
        /*System.out.println(compilationUnit.toString());


        System.out.println(compilationUnit.toString());

        compilationUnit.addClass("test");
        System.out.println(compilationUnit.toString());*/
        commandLineValues = new CommandLineValues(args);

        File path = new File(commandLineValues.getSource().toString());
        if(!path.exists() || !commandLineValues.getMutation().exists()){
            logger.error("Please check the input files exist : " + commandLineValues.getSource().toString());
            return;
        }

        if(path.isDirectory()){
            List<File> files = new ArrayList<>(Arrays.asList(path.listFiles()));
            new File(Paths.get(path.toString(),"output").toString()).mkdir();
            new File(Paths.get(path.toString(),"output", "mutated").toString()).mkdir();
            new File(Paths.get(path.toString(),"output", "original").toString()).mkdir();
            new File(Paths.get(path.toString(),"validation").toString()).mkdir();
            new File(Paths.get(path.toString(),"AST").toString()).mkdir();
            new File(Paths.get(path.toString(),"feature_table").toString()).mkdir();

            for (File file : files) {
                if(file.isFile() && file.getName().endsWith(".java")){
                    mutateJava(file);
                }
            }
            //NodeCount.calc(new File(Paths.get(commandLineValues.getSource().toString(), "feature_table").toString()));
        }else{
            new File(Paths.get(path.getParent(),"validation").toString()).mkdir();
            new File(Paths.get(path.getParent(),"output").toString()).mkdir();
            new File(Paths.get(path.getParent(),"output", "mutated").toString()).mkdir();
            new File(Paths.get(path.getParent(),"output", "original").toString()).mkdir();
            new File(Paths.get(path.getParent(),"AST").toString()).mkdir();
            new File(Paths.get(path.getParent(),"feature_table").toString()).mkdir();
            mutateJava(path);

        }


    }


    public static void mutateJava(File file) throws IOException, MutationException {

        Path outputJ = Paths.get(file.getParent(),"validation",file.getName());
        Path nodeTable = Paths.get(file.getParent(),"feature_table",file.getName()+ "_nodeMap.txt");
        String outputJson = outputJ.toString();
        logger.info(file.toString());



        //for(OldNode node : NodeFinder.findUnused(ast)) {
        //    logger.error("Unused Nodes: "+ node.getId());
        //}



        while(true) {
            JParser jParser = new JParser();
            //JsonParser jsonParser = new JsonParser();

            Node ast = jParser.parse(Util.readFile(file.getAbsolutePath()));

            StringBuilder nodeMap = new StringBuilder();
            for(Map.Entry<String,Integer> entry : SystemInfo.nodesMap.entrySet()){
                nodeMap.append(entry.getKey() + ":" + entry.getValue() +"\n");
            }
            Util.writeToFile(nodeTable.toString(),nodeMap.toString());



            FileUtils.copyFile(file,Paths.get(file.getParent(), "output", "original", file.getName()).toFile());

            MutationEngine mutationEngine = new MutationEngine(commandLineValues.getMutation().toString());

            MyNode n = new MyNode(ast);
            System.out.println("mynode " + n.toString() + n.getCode());
            mutationEngine.start(n);

            Path outputSolfile = Paths.get(file.getParent(), "output", "mutated", file.getName());
            System.out.println("outputMutfile " + outputSolfile.toFile().toString());
            System.out.println("writing " + ((Node)ast).toString() +"####"+ (new MyNode(ast)).getCode());
            Util.writeToFile(outputSolfile.toString(), (new MyNode(ast)).getCode());
            if(Util.checkCompileable(outputSolfile.toFile())){
                System.out.println("Successfully compiled!!!!!!!!!!!!!!!!");
                break;
            }
            System.out.println("Compilation failed!!!!!!!!!!!!!!!!");
        }




    }
}
