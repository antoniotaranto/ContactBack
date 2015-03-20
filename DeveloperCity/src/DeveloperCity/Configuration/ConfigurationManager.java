/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.Configuration;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;

/**
 *
 * @author lbarbosa
 */
public class ConfigurationManager {
	Document doc;

	public ConfigurationManager (String configFileName)
		throws javax.xml.parsers.ParserConfigurationException, SAXException, java.io.IOException
	{
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse (new File(configFileName));

        // normalize text representation
        doc.getDocumentElement().normalize();

        if (! doc.getDocumentElement().getNodeName().equalsIgnoreCase("configuration"))
        	throw new javax.xml.parsers.ParserConfigurationException("The configuration document must have a root node called configuration");
	}

	public String Get(String settingName)
		throws javax.xml.parsers.ParserConfigurationException
	{
        NodeList appSettingsCollection = doc.getElementsByTagName("appSettings");
        if (appSettingsCollection.getLength() != 1)
        	throw new javax.xml.parsers.ParserConfigurationException("The configuration document must have only one appSettings node under the root");

        Node appSettings = appSettingsCollection.item(0);

        if(appSettings.getNodeType() == Node.ELEMENT_NODE){
        	NodeList allSettings = appSettings.getChildNodes();
        	for (int i = 0; i < allSettings.getLength(); i++)
        	{
        		if (allSettings.item(i).getNodeType() == Node.ELEMENT_NODE)
        		{
        			Element cfg = (Element)allSettings.item(i);
            		if (cfg.getNodeName().equalsIgnoreCase(settingName))
            			return cfg.getFirstChild().getNodeValue();
        		}
        	}
        }
		return "";
	}
}