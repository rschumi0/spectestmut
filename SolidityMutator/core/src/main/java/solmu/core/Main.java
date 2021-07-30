package solmu.core;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import solmu.ASTparser.JsonParser;
import solmu.common.BasicGrammar;
import solmu.common.SolidityMutationException;
import solmu.common.SystemInfo;
import solmu.common.Util;
import solmu.gen.MathExprGenerator;
import solmu.grammer.Node;
import solmu.mutator.MutationEngine;
import solmu.mutator.NodeFinder;
import solmu.mutator.SolidityCompiler;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    private static CommandLineValues commandLineValues;
    private static final Logger logger =  Logger.getLogger(Main.class);

    public static ArrayList<String> TestCalls = new ArrayList<>();
    public  static ArrayList<String> TestFuctions = new ArrayList<>();
    public static  String TestCallInsertPath = "/home/admin1/truffletest/test/metacoin.js";
    public static String TestFunctionInsertPath = "/home/admin1/truffletest/contracts/MetaCoin.sol";
    public static String InsertionPlaceholder = "/* Placeholder */";
    public static String ZipStartName = "compIncDecHex3";

    public static void mainasdfgdf(String[] args) throws SolidityMutationException, IOException {
        if(SolidityCompiler.checkCompileable(new File("/home/admin1/contractsSolSem/output/mutated/while_nested_loop_break.sol"))){
            System.out.println("Successfully compiled!!!!!!!!!!!!!!!!");
        }
        else
        {
            System.out.println("Failed compiled!!!!!!!!!!!!!!!!");
        }
    }


    public static void mainasdf(String[] args) throws SolidityMutationException, IOException {
        MathExprGenerator meg = new MathExprGenerator(new Random());
//        uint   vG0dlh = 143;
//        uint   v9eQoE = 0;
//        vG0dlh += uint(178) - uint(115792089237316195423570985008687907853269984665640564039457584007913129639934) * uint(115792089237316195423570985008687907853269984665640564039457584007913129639916);
//        return vG0dlh; // 301

        BigInteger a = new BigInteger("143");
        BigInteger b = new BigInteger("178");
        BigInteger c = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639934");
        BigInteger d  = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639916");

        //BigInteger d = new BigInteger("-603713986634562864421");

        ArrayList< BigInteger > vals = new ArrayList<>();
        vals.add(a);
        vals.add(b); vals.add(c); vals.add(d);

        ArrayList<String> ops = new ArrayList<>();
        ops.add("+");
        ops.add("-"); ops.add("*");

        ArrayList<Integer> bits = new ArrayList<>();
        bits.add(256);bits.add(256); bits.add(256); bits.add(256);

        meg.calcCompRes(vals, bits, ops,"uint");
        System.out.println(" "+ meg.getCompRes());
    }
    public static void main(String[] args) throws SolidityMutationException, IOException {
        for (int i=0; i< 5;i++) {
                for (BasicGrammar.NodeType nodeType : BasicGrammar.NodeType.values()) {
                    SystemInfo.nodesMap.put(nodeType.toString(),Integer.valueOf(0));
                }
                
            commandLineValues = new CommandLineValues(args);

            File path = new File(commandLineValues.getSource().toString());
            if (!path.exists() || !commandLineValues.getMutation().exists()) {
                logger.error("Plese check the input files exist : " + commandLineValues.getSource().toString());
                return;
            }

            if (path.isDirectory()) {
                List<File> files = new ArrayList<>(Arrays.asList(path.listFiles()));
                new File(Paths.get(path.toString(), "output").toString()).mkdir();
                new File(Paths.get(path.toString(), "output", "mutated").toString()).mkdir();
                new File(Paths.get(path.toString(), "output", "original").toString()).mkdir();
                new File(Paths.get(path.toString(), "validation").toString()).mkdir();
                new File(Paths.get(path.toString(), "AST").toString()).mkdir();
                new File(Paths.get(path.toString(), "feature_table").toString()).mkdir();

                for (File file : files) {
                    if (file.isFile()) {
                        while(true) {
                            System.out.println("Mutating " +file.getAbsolutePath());
                            mutate(file);
                            Path outputSolfile = Paths.get(file.getParent(), "output", "mutated", file.getName());
                            System.out.println("Checking " +outputSolfile.toFile().getAbsolutePath());
                            if(SolidityCompiler.checkCompileable(outputSolfile.toFile())){
                                System.out.println("Successfully compiled!!!!!!!!!!!!!!!!");
                                break;
                            }
                            else
                            {
                                TestCalls.remove(TestCalls.size()-1);
                                TestFuctions.remove(TestFuctions.size()-1);
                            }
                        }
                    }
                }
                NodeCount.calc(new File(Paths.get(commandLineValues.getSource().toString(), "feature_table").toString()));
            } else {
                new File(Paths.get(path.getParent(), "validation").toString()).mkdir();
                new File(Paths.get(path.getParent(), "output").toString()).mkdir();
                new File(Paths.get(path.getParent(), "output", "mutated").toString()).mkdir();
                new File(Paths.get(path.getParent(), "output", "original").toString()).mkdir();
                new File(Paths.get(path.getParent(), "AST").toString()).mkdir();
                new File(Paths.get(path.getParent(), "feature_table").toString()).mkdir();
                mutate(path);
            }

            if (!TestCalls.isEmpty()) {
                Util.InsertReplaceInFile(TestCallInsertPath, TestCalls, InsertionPlaceholder);
                Util.InsertReplaceInFile(TestFunctionInsertPath, TestFuctions, InsertionPlaceholder);
                System.out.println("Test calls/functions written!");
                Util.RunTruffleTest();
                TestCalls = new ArrayList<>();
                TestFuctions = new ArrayList<>();
            }
        }
    }


    public static void mutate(File file) throws SolidityMutationException, IOException {

        Path outputJ = Paths.get(file.getParent(),"validation",file.getName());
        Path nodeTable = Paths.get(file.getParent(),"feature_table",file.getName()+ "_nodeMap.txt");
        String outputJson = outputJ.toString();
        logger.info(file.toString());

        JsonParser jsonParser = new JsonParser();
        String jsonAst =SolidityCompiler.createASTjson(file,"AST");
        if (jsonAst == null){
            logger.error("Compilation failure in input file");
            logger.error("Skipping mutating " + file.toString());
            return;
        }
        Node ast = jsonParser.parse(jsonAst);
        StringBuilder nodeMap = new StringBuilder();
        for(Map.Entry<String,Integer> entry : SystemInfo.nodesMap.entrySet()){
            nodeMap.append(entry.getKey() + ":" + entry.getValue() +"\n");
        }
        Util.writeToFile(nodeTable.toString(),nodeMap.toString());
        Node astValidation = jsonParser.parse(jsonAst);

        String debug = (ast.getCode());
        for(Node node : NodeFinder.findUnused(ast)) {
            logger.error("Unused Nodes: "+ node.getId());
        }
        Path validatoinFile = Paths.get(file.getParent(),"validation", file.getName());
        Util.writeToFile(validatoinFile.toString(), debug);

        String newJsonAst = SolidityCompiler.createASTjson(new File(validatoinFile.toString()),"");
        if(Util.isNullOrEmplty(newJsonAst)){
            logger.fatal("Error in AST Validation");
            return;
        }
        Node newAst = jsonParser.parse(newJsonAst);

        if(!astValidation.equals(newAst)){
            throw new SolidityMutationException("Error while building AST. Validation failed");
        } else
            logger.info("AST validated");

        FileUtils.copyFile(file,Paths.get(file.getParent(), "output", "original", file.getName()).toFile());

        MutationEngine mutationEngine = new MutationEngine(commandLineValues.getMutation().toString());
        mutationEngine.start(ast);

        Path outputSolfile = Paths.get(file.getParent(), "output", "mutated", file.getName());
        Util.writeToFile(outputSolfile.toString(), ast.getCode());

        String finaljsonAst = SolidityCompiler.createASTjson(new File(outputSolfile.toString()),
                "mutation_validation");
        if(Util.isNullOrEmplty(finaljsonAst)){
            logger.fatal("Mutation is not compiled");
        } else {
            logger.info("Mutation Successful");
        }
    }
}
