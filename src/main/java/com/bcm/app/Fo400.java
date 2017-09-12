package com.bcm.app;

import java.util.Properties;

import com.ibm.as400.access.*;

/**
 * Class Fo400 is the utility class that proxy JTOpen api power
 * into a friendly and simple way.
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

    public Fo400(Properties props) throws Exception{

        _systemName = props.getProperty(SYSTEM_NAME_PROPERTY);
        _userName = props.getProperty(USERNAME_PROPERTY);
        _password = props.getProperty(PASSWORD_PROPERTY);
        _system = new AS400(_systemName, _userName , _password);

    }

    public AS400 getSystem(){
        return _system;
    }

    public MemberDescription[] getMembers(String lib, String file) throws Exception{

        MemberList memberList = new MemberList(_system, lib, file);
        memberList.load();
        MemberDescription[] result = memberList.getMemberDescriptions();
        return result;

    }

    public MemberDescription getMember(String lib, String file, String mbr){
        return new MemberDescription(_system, lib, file, mbr);
    }

    public String getMemberContent(String lib, String file, String mbr) throws Exception {

        StringBuilder result = new StringBuilder();

        MemberDescription md = getMember(lib, file, mbr);
        AS400File asf = new SequentialFile(_system, md.getPath());
        asf.setRecordFormat();
        Record[] records = asf.readAll();

        for (Record record : records) {
            result.append(record.toString());
            result.append(System.lineSeparator());
        }

        return result.toString();

    }

    public String getMemberType(String lib, String file, String mbr)throws Exception{
        MemberDescription mb = getMember(lib, file, mbr);
        String type = (String)mb.getValue(MemberDescription.SOURCE_TYPE);
        return type;
    }

}
