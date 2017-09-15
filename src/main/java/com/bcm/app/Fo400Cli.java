package com.bcm.app;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

import java.util.Properties;

/**
 * Class Fo400Cli is the command line class of fo400 project. It
 * takes input parameters and launch/select class Fo400 power.
 *
 */
public class Fo400Cli {

    public static void main( String[] args ){

        FileWriter fw = null;
        BufferedWriter bw = null;

        try{

            // Parse input parameter
            Fo400CliArgument ar = new Fo400CliArgument(args);
            String library = ar.getLibrary();
            String sourceFile = ar.getFile();
            String memberName = ar.getMember();
             
            // Load property file 
            String configName = System.getenv("FO400") + File.separator + "config.properties";
            FileInputStream configFile = new FileInputStream(configName);
            Properties props = new Properties();
            props.load(configFile);
            
            // Create Fo400 object and call for its power
            Fo400 fo400 = new Fo400(props);
            String memberContent = fo400.getMemberContent(library, sourceFile, memberName);

            if (!ar.hasSaveLoc()){
                echo(memberContent);
            }else{

                String filename = memberName;
                String filetype = fo400.getMemberType(library, sourceFile, memberName);
                fw = new FileWriter(ar.getSaveLoc() + File.separator + 
                        filename + "." + filetype);
                bw = new BufferedWriter(fw);
                bw.write(memberContent);
                echo("Save complete.");

            }

        }catch(Exception e){

            e.printStackTrace();
            echo("Error. Reason may be:" + System.lineSeparator() + 
                    "* Initiate parameter incorrect" + System.lineSeparator() +
                    "* Config file corrupted" + System.lineSeparator() +
                    "* Internet connection malfunction" + System.lineSeparator() +
                    "* Cannot find this file member");

        }finally {

            try{
                if (bw != null){bw.close();}
                if (fw != null){fw.close();}
            }catch(Exception ee){
                ee.printStackTrace();
            }

        }

    }

    private static void echo(String msg){
        System.out.println(msg);
    }

    /**
     * Inner Class Fo400CliArgument is argument object abstraction in fo400
     * context. Arguments should be like below example, for other cases,
     * feature is not garranteed:
     * $ fo400 ZXXXLIB/QXXXSRC/XXXPGM
     * $ fo400 -f ZXXXLIB/QXXXSRC/XXXPGM
     *
     */
    private static class Fo400CliArgument {

        private String _library;
        private String _file;
        private String _member;
        private boolean _hasSaveLoc;

        Fo400CliArgument(String[] args)throws Exception{

            if (args.length > 2){

                throw new IllegalArgumentException();

            }else{

                if (args.length == 1){
                    String[] s = args[0].split("/");
                    _library = s[0];
                    _file = s[1];
                    _member = s[2];
                    _hasSaveLoc = false;
                }

                if (args.length == 2 ){
                    if (args[0].compareTo("-f") != 0){
                        throw new IllegalArgumentException();
                    }else{
                        String[] s = args[1].split("/");
                        _library = s[0];
                        _file = s[1];
                        _member = s[2];
                        _hasSaveLoc = true;
                    }
                }

            }

        }

        String getLibrary(){
            return _library;
        }

        String getFile(){
            return _file;
        }

        String getMember(){
            return _member;
        }

        boolean hasSaveLoc(){
            return _hasSaveLoc;
        }

        String getSaveLoc(){
            // TODO Default as current folder
            return ".";
        }

    }

}
