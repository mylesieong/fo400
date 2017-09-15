package com.bcm.app;

import java.util.Properties;

import com.ibm.as400.access.*;

/**
 * Class Fo400 is a proxy class that proxy JTOpen's API power
 * into a simple and stupid way.
 *
 * Usage is as simple as create an object and call the method:
 * <code>Fo400 foo = new Fo400(props);</code>
 * <code>String lib = "ZCHQLIB";</code>
 * <code>String file = "QCQRSRC";</code>
 * <code>String mbr = "CQRIAPP";</code>
 * <code>String code = foo.getMemberContent(lib, file, mbr); </code>
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

    /**
     * Constructor Fo400(Properties) is the only constructor of this proxy
     * class.
     *
     * @param Properties the property file that contains system name and user
     * token
     */
    public Fo400(Properties props) throws Exception{

        _systemName = props.getProperty(SYSTEM_NAME_PROPERTY);
        _userName = props.getProperty(USERNAME_PROPERTY);
        _password = props.getProperty(PASSWORD_PROPERTY);
        _system = new AS400(_systemName, _userName , _password);

    }

    /**
     * Method getSystem() return the AS400 object built base on the init
     * property. 
     *
     * @return AS400
     */
    public AS400 getSystem(){
        return _system;
    }

    /**
     * Method getMember(...) accept library name, file name and member 
     * name. It then returns the MemberDescription object that contains
     * meta data of file member.
     *
     * @param String library name w/o suffix. e.g. ZCHQLIB
     * @param String file name w/o suffix. e.g. QCQRSRC
     * @param String member name w/o suffix. e.g. CQRIAPP
     * @return MemberDescription metadata of a file member
     */
    public MemberDescription getMember(String lib, String file, String mbr){
        return new MemberDescription(_system, lib, file, mbr);
    }

    /** Method getMembers(...) accept lib name and file name. It then 
     * returns a list of MemberDescription objects which represent every
     * member in file respectively.
     *
     * @param String lib name
     * @param String file name
     * @return MemberDescription[]
     */ 
    public MemberDescription[] getMembers(String lib, String file) throws Exception{

        MemberList memberList = new MemberList(_system, lib, file);
        memberList.load();
        MemberDescription[] result = memberList.getMemberDescriptions();
        return result;

    }

    /**
     * Method getMemberContent(...) accepts lib name, file name and member 
     * name. It then returns members' content as a string to client. Please
     * note that it works for both pf-src member and pf-dta member.
     *
     * @param String library name
     * @param String file name
     * @param String member name
     * @return String member content
     */
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

    /**
     * Method getMemberType(...) accepts lib name, file name and member
     * name. It then returns type of the member. 
     *
     * @param String library name
     * @param String file name
     * @param String member name
     * @return String member type. e.g. RPGLE, CLP
     */
    public String getMemberType(String lib, String file, String mbr)throws Exception{
        MemberDescription mb = getMember(lib, file, mbr);
        String type = (String)mb.getValue(MemberDescription.SOURCE_TYPE);
        return type;
    }

}
