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

        String sysId = "12153524";
        String sysName = "";
        String sysStatus = "";
        String sysReturnCode = "";

        try{
            String programName="/QSYS.LIB/YMYLES1.LIB/DSNTEINQCL.PGM";
            /* Prepare parameter list */
            ProgramParameter[] parmList= new ProgramParameter[4];
            AS400Text idText = new AS400Text(8);
            parmList[0] = new ProgramParameter(idText.toBytes(sysId));
            parmList[1] = new ProgramParameter(80);
            parmList[2] = new ProgramParameter(10);
            parmList[3] = new ProgramParameter(1);
    
            ProgramCall program = new ProgramCall(system);
            program.setProgram(programName, parmList);

            if (program.run()!= true){
                AS400Message[] messagelist = program.getMessageList();
                for (int i = 0; i < messagelist.length; ++i){
                    System.out.println(messagelist[i]);
                }
            }else{
                // Get the chinese name with cp937 encoding
                sysName = new String(parmList[1].getOutputData(), "Cp937");
                // Get the english status with default encoding cp1047
                AS400Text statusText = new AS400Text(10);
                sysStatus = (String)statusText.toObject(
                parmList[2].getOutputData());
                // Get the return code with default encoding cp1047
                AS400Text returnCodeText = new AS400Text(1);
                sysReturnCode=(String)returnCodeText.toObject(
                parmList[3].getOutputData());
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }

        // Output result
        echo(sysName);
        echo(sysStatus);
        echo(sysReturnCode);

    }

    private static void echo(String msg){
        System.out.println("ECHO>>>" + msg);
    }

}
