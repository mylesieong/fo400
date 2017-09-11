package com.bcm.app;

import java.io.IOException;
import java.io.FileInputStream;

import java.util.Properties;

import com.ibm.as400.access.*;

/**
 * Class Fo400 is the command line class of fo400 project. It
 * takes input parameters and launch jtopen utililies.
 *
 */
public class Fo400 {

    private final static String SYSTEM_NAME_PROPERTY = "SYSTEM_NAME";
    private final static String USERNAME_PROPERTY = "USER";
    private final static String PASSWORD_PROPERTY = "PASSWORD";

    private static String _systemName;
    private static String _userName;
    private static String _password;

    private static AS400 _system;

    static{

        try{

            FileInputStream configFile = new FileInputStream("config.properties");
            Properties props = new Properties();
            props.load(configFile);
            _systemName = props.getProperty(SYSTEM_NAME_PROPERTY);
            _userName = props.getProperty(USERNAME_PROPERTY);
            _password = props.getProperty(PASSWORD_PROPERTY);
            _system = new AS400(_systemName, _userName , _password);

        }catch(Exception e){
            e.printStackTrace();
            echo("Initiate parameter incorrect! Please check the config file.");
        }

    }

    public static void main( String[] args ){

        String library = "YMYLES1";
        String sourceFile = "QCLSRC";
        String memberName = "DSFSTTCL";

        try{

            MemberDescription target = new MemberDescription(_system, library, sourceFile, memberName);

            if (target == null){
                throw new IOException("No such file member");
            }else{
                String memberAsString = getMemberAsString(target);
                echo(memberAsString);
            }

        }catch (Exception e){

            e.printStackTrace();
            echo("Cannot find this file member!");

        }

    }

    private static MemberDescription[] listMembers(String lib, String file) throws Exception{
        echo("Members:");
        MemberList memberList = new MemberList(_system, lib, file);
        memberList.load();
        MemberDescription[] result = memberList.getMemberDescriptions();
        for (MemberDescription memberDescription : result) {
            echo(memberDescription.getPath());
        }
        return result;
    }

    private static String getMemberAsString(MemberDescription memberDescription) throws Exception {
        AS400File file = new SequentialFile(_system, memberDescription.getPath());
        StringBuilder result = new StringBuilder();
        file.setRecordFormat();
        //listFieldDescriptions(file);
        Record[] records = file.readAll();
        for (Record record : records) {
            result.append(record.toString());
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    private static void listFieldDescriptions(AS400File file) {
        echo("Fields:");
        for (FieldDescription description : file.getRecordFormat().getFieldDescriptions()) {
            echo(description.getFieldName());
        }
    }
    
    private static void echo(String msg){
        System.out.println(msg);
    }

}
