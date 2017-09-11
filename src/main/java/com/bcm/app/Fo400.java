package com.bcm.app;

import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.MemberDescription;
import com.ibm.as400.access.MemberList;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.*;

/**
 * Class Fo400 is the command line class of fo400 project. It
 * takes input parameters and launch jtopen utililies.
 *
 */
public class Fo400 {

    private static String systemName="S657274B";
    private static String userName="ZCSERVICE";
    private static String password="ECIVRESCZ";

    private static AS400 system = new AS400(systemName, userName , password);

    public static void main( String[] args ){

        String library = "YMYLES1";
        String sourceFile = "QCLSRC";

        try{
            MemberDescription[] members = listMembers(library, sourceFile);
            String memberAsString = getMemberAsString(members[0]);
            echo(memberAsString);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static MemberDescription[] listMembers(String lib, String file) throws Exception{
        echo("Members:");
        MemberList memberList = new MemberList(system, lib, file);
        memberList.load();
        MemberDescription[] result = memberList.getMemberDescriptions();
        for (MemberDescription memberDescription : result) {
            echo(memberDescription.getPath());
        }
        return result;
    }

    private static String getMemberAsString(MemberDescription memberDescription) throws Exception {
        AS400File file = new SequentialFile(system, memberDescription.getPath());
        StringBuilder result = new StringBuilder();
        file.setRecordFormat();
        listFieldDescriptions(file);
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
        System.out.println("ECHO>>>" + msg);
    }

}
