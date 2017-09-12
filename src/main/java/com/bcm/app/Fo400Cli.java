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
            FileInputStream configFile = new FileInputStream("config.properties");
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
                if (fw != null){fw.close();}
                if (bw != null){bw.close();}
            }catch(Exception ee){
                ee.printStackTrace();
            }

        }

    }

    private static void echo(String msg){
        System.out.println(msg);
    }

    private static class Fo400CliArgument {

        private String _library;
        private String _file;
        private String _member;

        Fo400CliArgument(String[] args){
            // TODO
            
            String[] ss = args[0].split("/");
            _library = ss[0];
            _file = ss[1];
            _member = ss[2];
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
            // TODO
            return false;
        }

        String getSaveLoc(){
            // TODO
            return null;
        }

    }

}