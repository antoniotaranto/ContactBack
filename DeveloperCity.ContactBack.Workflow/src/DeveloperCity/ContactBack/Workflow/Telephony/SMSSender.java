/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow.Telephony;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer.Form;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class SMSSender {

    private static final Logger logger = Logger.getLogger(SMSSender.class);
    private String smsUrl;
    private String account;
    private String code;
    private String from;
    private Proxy proxy;

    public enum ReturnMessage implements Serializable {

        MessageSent("000"),
        EmptyMessageContent("010"),
        MessageBodyInvalid("011"),
        MessageContentOverflow("012"),
        DestinationInvalid("013"),
        DestinationEmpty("014"),
        SchedulingDateInvalid("015"),
        IDOverflow("016"),
        DuplicatedID("080"),
        AuthenticationError("900"),
        AccountLimitReached("990"),
        WrongOperationRequested("998"),
        UnknownError("999");
        private String message;

        ReturnMessage(String message) {
            this.message = message;
        }

        public String getReturnMessage() {
            return this.message;
        }

        public static ReturnMessage getFrom(String message) {
            if (message == null) {
                return null;
            }

            for (ReturnMessage m : ReturnMessage.values()) {
                if (message.equals(m.getReturnMessage())) {
                    return m;
                }
            }

            return null;
        }
    }

    public SMSSender(String SMSUrl, String account, String code, String from, Proxy proxy) {
        this.smsUrl = SMSUrl;
        this.account = account;
        this.code = code;
        this.from = from;
        this.proxy = proxy;
    }

    public ReturnMessage Send(String message, String number) throws MalformedURLException, IOException, Exception {
        logger.info(String.format("Send(String message = %s, String number = %s)", message, number));
        String data = URLEncoder.encode("dispatch", "UTF-8") + "=" + URLEncoder.encode("send", "UTF-8");
        data += "&" + URLEncoder.encode("account", "UTF-8") + "=" + URLEncoder.encode(account, "UTF-8");
        data += "&" + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");
        data += "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
        data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(number, "UTF-8");
        data += "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(RemoveAccents(message), "UTF8");

        try {
            URL url = new URL(smsUrl);
            logger.trace("Open connection");
            HttpURLConnection conn = null;
            if (proxy == null) {
                conn = (HttpURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection(proxy);
            }
            
            logger.trace("Set connection");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);

//          conn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            logger.trace("Write to destination");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            logger.trace("Read response");
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String responseText = br.readLine();

            logger.trace("Close all");
            wr.close();
            br.close();
            in.close();
            conn.disconnect();
            logger.trace("Return " + responseText);
            logger.info("Send(String, String) !");
            return (responseText != null && responseText.length() >= 3) ? ReturnMessage.getFrom(responseText.substring(0, 3)) : ReturnMessage.UnknownError;
        } catch(MalformedURLException e) {
            logger.error(e);
            logger.info("Send(String, String) !");
            throw e;
        } catch(IOException e) {
            logger.error(e);
            logger.info("Send(String, String) !");
            throw e;
        } catch (Exception e) {
            logger.error(e);
            logger.info("Send(String, String) !");
            throw e;
        }
    }

    public static String RemoveAccents(String input) {
        String normalized = java.text.Normalizer.normalize(input, Form.NFKD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }
}