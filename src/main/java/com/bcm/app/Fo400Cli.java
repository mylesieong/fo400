package com.bcm.app;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Class Fo400Cli is the command line class of fo400 project. It
 * takes input parameters and launch/select class Fo400 power.
 *
 */
public class Fo400Cli {

    public static void main( String[] args ){

        try{

            // Parse input parameter
            Fo400CliArgument ar = new Fo400CliArgument(args[0]);
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
            echo(memberContent);

        }catch(Exception e){

            e.printStackTrace();
            echo("Error. Reason may be:\n* Initiate parameter incorrect\n* Config file corrupted\n* Internet connection malfunction\n* Cannot find this file member");

        }

    }

    private static void echo(String msg){
        System.out.println(msg);
    }

    private static class Fo400CliArgument {

        private String _library;
        private String _file;
        private String _member;

        Fo400CliArgument(String s){
            String[] ss = s.split("/");
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

    }

}
