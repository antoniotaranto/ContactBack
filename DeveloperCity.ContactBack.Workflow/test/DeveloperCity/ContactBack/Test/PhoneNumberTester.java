/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Test;

import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber.PhoneNumberType;
import DeveloperCity.ContactBack.Exception.InvalidNumberOfDigitsException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author luizguitar
 */
public class PhoneNumberTester {
    List<ItemToTest> testCases;

    @Before
    public void setUp() {
        testCases = new ArrayList<ItemToTest>();
        
        // NEXTEL
        testCases.add(new ItemToTest(   "7099-0000",  PhoneNumberType.MobilePhone,  "11",   "70990000"   ));
        testCases.add(new ItemToTest(   "7799-0000",  PhoneNumberType.MobilePhone,  "11",   "77990000"   ));
        testCases.add(new ItemToTest(   "7899-0000",  PhoneNumberType.MobilePhone,  "11",   "78990000"   ));
        testCases.add(new ItemToTest(   "7999-0000",  PhoneNumberType.MobilePhone,  "11",   "79990000"   ));
        testCases.add(new ItemToTest( "117099-0000",  PhoneNumberType.MobilePhone,  "11",   "70990000"   ));
        testCases.add(new ItemToTest( "117799-0000",  PhoneNumberType.MobilePhone,  "11",   "77990000"   ));
        testCases.add(new ItemToTest( "117899-0000",  PhoneNumberType.MobilePhone,  "11",   "78990000"   ));
        testCases.add(new ItemToTest( "117999-0000",  PhoneNumberType.MobilePhone,  "11",   "79990000"   ));
        testCases.add(new ItemToTest("0117099-0000",  PhoneNumberType.MobilePhone,  "11",   "70990000"   ));
        testCases.add(new ItemToTest("0117799-0000",  PhoneNumberType.MobilePhone,  "11",   "77990000"   ));
        testCases.add(new ItemToTest("0117899-0000",  PhoneNumberType.MobilePhone,  "11",   "78990000"   ));
        testCases.add(new ItemToTest("0117999-0000",  PhoneNumberType.MobilePhone,  "11",   "79990000"   ));
        
        testCases.add(new ItemToTest(   "a123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(        "0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(    "123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(   "0000-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(   "0900-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(   "1111-1111",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(   "2123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest(   "3123-4567",  PhoneNumberType.Landline,     "11",   "31234567"   ));
        testCases.add(new ItemToTest(   "4123-4567",  PhoneNumberType.Landline,     "11",   "41234567"   ));
        testCases.add(new ItemToTest(   "5123-4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest(   "6123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest(   "7123-4567",  PhoneNumberType.MobilePhone,  "11",   "971234567"  ));
        testCases.add(new ItemToTest(   "8123-4567",  PhoneNumberType.MobilePhone,  "11",   "981234567"  ));
        testCases.add(new ItemToTest(   "9123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));
        testCases.add(new ItemToTest(  "5123-a4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest(   "2222.2222",  PhoneNumberType.Landline,     "11",   "22222222"   ));
        testCases.add(new ItemToTest(  "00123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "01123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "02123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest(  "03123-4567",  PhoneNumberType.Landline,     "11",   "31234567"   ));
        testCases.add(new ItemToTest(  "05123-4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest(  "06123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest(  "06123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest(  "07123-4567",  PhoneNumberType.MobilePhone,  "11",   "971234567"  ));
        testCases.add(new ItemToTest(  "09123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));  
        testCases.add(new ItemToTest(  "10123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "11123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "12123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "13123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "15123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "16123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "17123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "19123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "29123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "39123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "49123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "59123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "69123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "79123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "89123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest(  "90123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest(  "91123-4567",  PhoneNumberType.MobilePhone,  "11",   "911234567"  ));
        testCases.add(new ItemToTest(  "92123-4567",  PhoneNumberType.MobilePhone,  "11",   "921234567"  ));
        testCases.add(new ItemToTest(  "93123-4567",  PhoneNumberType.MobilePhone,  "11",   "931234567"  ));
        testCases.add(new ItemToTest(  "95123-4567",  PhoneNumberType.MobilePhone,  "11",   "951234567"  ));
        testCases.add(new ItemToTest(  "96123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest(  "99123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest( "000123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "001123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "002123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest( "003123-4567",  PhoneNumberType.Landline,     "11",   "31234567"   ));
        testCases.add(new ItemToTest( "005123-4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest( "006123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest( "006123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest( "007123-4567",  PhoneNumberType.MobilePhone,  "11",   "971234567"  ));
        testCases.add(new ItemToTest( "009123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));  
        testCases.add(new ItemToTest( "010123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "011123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "012123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "013123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "015123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "016123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "017123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "019123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "029123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "089123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "090123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest( "091123-4567",  PhoneNumberType.MobilePhone,  "11",   "911234567"  ));
        testCases.add(new ItemToTest( "092123-4567",  PhoneNumberType.MobilePhone,  "11",   "921234567"  ));
        testCases.add(new ItemToTest( "093123-4567",  PhoneNumberType.MobilePhone,  "11",   "931234567"  ));
        testCases.add(new ItemToTest( "095123-4567",  PhoneNumberType.MobilePhone,  "11",   "951234567"  ));
        testCases.add(new ItemToTest( "096123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest( "099123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest( "109123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "209123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "309123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "409123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "509123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "609123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "709123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "809123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "909123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "310000-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "310900-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "311111-1111",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest( "312123-4567",  PhoneNumberType.Landline,     "31",   "21234567"   ));
        testCases.add(new ItemToTest( "313123-4567",  PhoneNumberType.Landline,     "31",   "31234567"   ));
        testCases.add(new ItemToTest( "314123-4567",  PhoneNumberType.Landline,     "31",   "41234567"   ));
        testCases.add(new ItemToTest( "315123-4567",  PhoneNumberType.Landline,     "31",   "51234567"   ));
        testCases.add(new ItemToTest( "316123-4567",  PhoneNumberType.MobilePhone,  "31",   "61234567"   ));
        testCases.add(new ItemToTest( "317123-4567",  PhoneNumberType.MobilePhone,  "31",   "71234567"   ));
        testCases.add(new ItemToTest( "318123-4567",  PhoneNumberType.MobilePhone,  "31",   "81234567"   ));
        testCases.add(new ItemToTest( "319123-4567",  PhoneNumberType.MobilePhone,  "31",   "91234567"   ));
        testCases.add(new ItemToTest("315123-a4567",  PhoneNumberType.Landline,     "31",   "51234567"   ));
        testCases.add(new ItemToTest( "312222.2222",  PhoneNumberType.Landline,     "31",   "22222222"   ));
        testCases.add(new ItemToTest("1100123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1101123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1102123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1103123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1105123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1106123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1106123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1107123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1109123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1110123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1111123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1112123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1113123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1115123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1116123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1117123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1119123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1129123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1139123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1149123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1159123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1169123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1179123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1189123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("1190123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest("1191123-4567",  PhoneNumberType.MobilePhone,  "11",   "911234567"  ));
        testCases.add(new ItemToTest("1192123-4567",  PhoneNumberType.MobilePhone,  "11",   "921234567"  ));
        testCases.add(new ItemToTest("1193123-4567",  PhoneNumberType.MobilePhone,  "11",   "931234567"  ));
        testCases.add(new ItemToTest("1195123-4567",  PhoneNumberType.MobilePhone,  "11",   "951234567"  ));
        testCases.add(new ItemToTest("1196123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest("1199123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest("3100123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3101123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3102123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3103123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3105123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3106123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3106123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3107123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3109123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3110123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3111123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3112123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3113123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3115123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3116123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3117123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3119123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3129123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3139123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3149123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3159123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3169123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3179123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3189123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3190123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3191123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3192123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3193123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3195123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3196123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("3199123-4567",  PhoneNumberType.Unknown,      null,   null         ));

    
    
        testCases.add(new ItemToTest("0000000a123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("0000000000000000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000000123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000000000-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000000900-0000",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000001111-1111",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000002123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest("00000006123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest("00000009123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));
        testCases.add(new ItemToTest("0000005123-a4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest("00000002222.2222",  PhoneNumberType.Landline,     "11",   "22222222"   ));
        testCases.add(new ItemToTest("00000000123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000001123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000002123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest("00000003123-4567",  PhoneNumberType.Landline,     "11",   "31234567"   ));
        testCases.add(new ItemToTest("00000005123-4567",  PhoneNumberType.Landline,     "11",   "51234567"   ));
        testCases.add(new ItemToTest("00000006123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest("00000010123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000011123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000012123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000090123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest("00000091123-4567",  PhoneNumberType.MobilePhone,  "11",   "911234567"  ));
        testCases.add(new ItemToTest("00000099123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest("00000000123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000002123-4567",  PhoneNumberType.Landline,     "11",   "21234567"   ));
        testCases.add(new ItemToTest("00000006123-4567",  PhoneNumberType.MobilePhone,  "11",   "961234567"  ));
        testCases.add(new ItemToTest("00000009123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));  
        testCases.add(new ItemToTest("00000010123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000011123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000012123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000019123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000090123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest("00000099123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest("00000109123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000209123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000909123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000311111-1111",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00000312123-4567",  PhoneNumberType.Landline,     "31",   "21234567"   ));
        testCases.add(new ItemToTest("00000313123-4567",  PhoneNumberType.Landline,     "31",   "31234567"   ));
        testCases.add(new ItemToTest("00000316123-4567",  PhoneNumberType.MobilePhone,  "31",   "61234567"   ));
        testCases.add(new ItemToTest("00000317123-4567",  PhoneNumberType.MobilePhone,  "31",   "71234567"   ));
        testCases.add(new ItemToTest("00000319123-4567",  PhoneNumberType.MobilePhone,  "31",   "91234567"   ));
        testCases.add(new ItemToTest("0000315123-a4567",  PhoneNumberType.Landline,     "31",   "51234567"   ));
        testCases.add(new ItemToTest("00000312222.2222",  PhoneNumberType.Landline,     "31",   "22222222"   ));
        testCases.add(new ItemToTest("00001100123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001101123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001102123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001103123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001113123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001115123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001159123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001169123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00001190123-4567",  PhoneNumberType.MobilePhone,  "11",   "901234567"  ));
        testCases.add(new ItemToTest("00001191123-4567",  PhoneNumberType.MobilePhone,  "11",   "911234567"  ));
        testCases.add(new ItemToTest("00001192123-4567",  PhoneNumberType.MobilePhone,  "11",   "921234567"  ));
        testCases.add(new ItemToTest("00001199123-4567",  PhoneNumberType.MobilePhone,  "11",   "991234567"  ));        
        testCases.add(new ItemToTest("00003100123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003101123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003102123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003113123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003115123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003139123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003149123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003190123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003191123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003192123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003193123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003195123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003196123-4567",  PhoneNumberType.Unknown,      null,   null         ));
        testCases.add(new ItemToTest("00003199123-4567",  PhoneNumberType.Unknown,      null,   null         ));
    

        testCases.add(new ItemToTest("+000551152119450",  PhoneNumberType.Landline,     "11",   "52119450"   ));
        testCases.add(new ItemToTest("+005501152119450",  PhoneNumberType.Landline,     "11",   "52119450"   ));
        
        testCases.add(new ItemToTest("+000551162119450",  PhoneNumberType.MobilePhone,  "11",   "962119450"  ));
        testCases.add(new ItemToTest("+005501162119450",  PhoneNumberType.MobilePhone,  "11",   "962119450"  ));
        testCases.add(new ItemToTest("+055011962119450",  PhoneNumberType.MobilePhone,  "11",   "962119450"  ));
        testCases.add(new ItemToTest("+005511962119450",  PhoneNumberType.MobilePhone,  "11",   "962119450"  ));
        
        testCases.add(new ItemToTest("+000551170119450",  PhoneNumberType.MobilePhone,  "11",   "70119450"   ));
        testCases.add(new ItemToTest("+005501170119450",  PhoneNumberType.MobilePhone,  "11",   "70119450"   ));

        testCases.add(new ItemToTest("+000551171119450",  PhoneNumberType.MobilePhone,  "11",   "971119450"  ));
        testCases.add(new ItemToTest("+005501171119450",  PhoneNumberType.MobilePhone,  "11",   "971119450"  ));
        testCases.add(new ItemToTest("+055011971119450",  PhoneNumberType.MobilePhone,  "11",   "971119450"  ));
        testCases.add(new ItemToTest("+005511971119450",  PhoneNumberType.MobilePhone,  "11",   "971119450"  ));
        
        testCases.add(new ItemToTest("+000551182119450",  PhoneNumberType.MobilePhone,  "11",   "982119450"  ));
        testCases.add(new ItemToTest("+005501182119450",  PhoneNumberType.MobilePhone,  "11",   "982119450"  ));
        testCases.add(new ItemToTest("+055011982119450",  PhoneNumberType.MobilePhone,  "11",   "982119450"  ));
        testCases.add(new ItemToTest("+005511982119450",  PhoneNumberType.MobilePhone,  "11",   "982119450"  ));

        testCases.add(new ItemToTest("+000551192119450",  PhoneNumberType.MobilePhone,  "11",   "992119450"  ));
        testCases.add(new ItemToTest("+005501192119450",  PhoneNumberType.MobilePhone,  "11",   "992119450"  ));
        testCases.add(new ItemToTest("+055011992119450",  PhoneNumberType.MobilePhone,  "11",   "992119450"  ));
        testCases.add(new ItemToTest("+005511992119450",  PhoneNumberType.MobilePhone,  "11",   "992119450"  ));
    }
    
    @Test
    public void TestFormatNumber() throws InvalidNumberOfDigitsException {
        for ( ItemToTest item : testCases ) {
            PhoneNumber tested = PhoneNumber.fromBina(item.bina);
            System.out.append("Bina:" + item.bina);
            System.out.append(" Area:" + tested.getLocalArea());
            System.out.append(" Number:" + tested.getPhone());
            System.out.append("\r\n");
            Assert.assertEquals(tested.getPhoneNumberType(), item.typeExpected);
            Assert.assertEquals(tested.getLocalArea(), item.localAreaCode);
            Assert.assertEquals(tested.getPhone(), item.number);
        }
    }    

    private class ItemToTest {
        private String bina;
        private PhoneNumberType typeExpected;
        private String localAreaCode;
        private String number;
                
        public ItemToTest(String bina, PhoneNumberType typeExpected, String localAreaCode, String number) {
            this.bina = bina;
            this.typeExpected = typeExpected;
            this.localAreaCode = localAreaCode;
            this.number = number;
        }
    }    
}
