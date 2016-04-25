package com.eidos.simulatableAPI;

import com.kuka.roboticsAPI.geometricModel.Frame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Simulator {
    private static final int BUFFER_SIZE = 256;
    private Socket socket;
    private PrintWriter outWriter;
    private BufferedReader inReader;

    public void moveJoints(float[] jointsDegValues) {
        String values = "";
        for (float val : jointsDegValues) {
            values += "<value><float>" + val + "</float></value>";
        }
        // TODO: Pack ino kuka xml used in sim (or GCode, or smth)
        outWriter.write("<?xml version = '1.0'?><methodCall>" +
                "<methodName>Robots.ActiveRobot.MoveJoints</methodName>" +
                "<params><param>" +
                    "<value><array><data>" +
                        values +
                    "</data></array></value>" +
                "</param><param><value><float>0.2</float></value></param>" +
                "</params></methodCall>");
        outWriter.flush();
    }

    public boolean isCommandHandled() throws InterruptedException, IOException, SAXException,
            ParserConfigurationException, XmlRpcException {
        outWriter.write("<?xml version = '1.0'?><methodCall>" +
                "<methodName>Robots.ActiveRobot.IsCommandHandled</methodName><params></params></methodCall>");
        outWriter.flush();
        Thread.sleep(25L);
        CharBuffer responseCh = CharBuffer.allocate(BUFFER_SIZE);
        int charsRead = inReader.read(responseCh);
        //responseCh.reset();
        String response = responseCh.toString();
        return parseBoolResponse(response);
    }

    public Frame getFlangePosition() throws InterruptedException, SAXException, ParserConfigurationException,
            XmlRpcException, IOException {
        outWriter.write("<?xml version = '1.0'?><methodCall>" +
                "<methodName>Robots.ActiveRobot.Flange.GetPosition</methodName><params></params></methodCall>");
        outWriter.flush();
        Thread.sleep(25L);
        CharBuffer responseCh = CharBuffer.allocate(BUFFER_SIZE);
        int charsRead = inReader.read(responseCh);
        String response = responseCh.subSequence(0, charsRead).toString();
        return parseFrameResponse(response);
    }

    public double[] getJointsValues() throws InterruptedException, IOException, XmlRpcException, SAXException,
            ParserConfigurationException {
        outWriter.write("<?xml version = '1.0'?><methodCall>" +
                "<methodName>Robots.ActiveRobot.GetJointsValues</methodName><params></params></methodCall>");
        outWriter.flush();
        Thread.sleep(25L);
        CharBuffer responseCh = CharBuffer.allocate(BUFFER_SIZE);
        int charsRead = inReader.read(responseCh);
        String response = responseCh.subSequence(0, charsRead).toString();
        return parseFltArrayResponse(response);
    }

    public boolean isReady() {
        return socket.isConnected();
    }

    public void dispose() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Element loadDocumentRoot(String doc) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input =  new ByteArrayInputStream(doc.getBytes("UTF-8"));
        Document document = builder.parse(input);
        return document.getDocumentElement();
    }

    private boolean parseBoolResponse(String response) throws XmlRpcException, IOException, SAXException,
            ParserConfigurationException {
        Element root = loadDocumentRoot(response);

        boolean boolResponse = false;
        try {
            Node respParam = root.getElementsByTagName("param").item(0);
            Node boolVal = respParam.getFirstChild(/*value*/).getFirstChild(/*boolean*/);
            boolResponse = Boolean.valueOf(boolVal.getTextContent());
        } catch (Exception ex) {
            throw new XmlRpcException("Cannot parse Boolean response");
        }

        return boolResponse;
    }

    private double[] parseFltArrayResponse(String response) throws IOException, SAXException, ParserConfigurationException,
            XmlRpcException {
        double[] arr;
        Element root = loadDocumentRoot(response);
        try {
            NodeList floats = root.getElementsByTagName("float");
            int length = floats.getLength();
            arr = new double[length];
            for (int i = 0; i < length; i++) {
                arr[i] = Float.valueOf(floats.item(i).getTextContent());
            }
        } catch (Exception ex) {
            throw new XmlRpcException("Cannot parse float array response");
        }
        return arr;
    }

    private Frame parseFrameResponse(String response) throws XmlRpcException, IOException, SAXException,
            ParserConfigurationException {
        Map<String, Float> vect3 = new HashMap<String, Float>(3);
        Element root = loadDocumentRoot(response);
        try {
            NodeList members = root.getElementsByTagName("member");
            for (int i = 0, len = members.getLength(); i < len; i++) {
                Element member = (Element)members.item(i);
                String name = member.getElementsByTagName("name").item(0).getTextContent();
                Float val = Float.valueOf(member.getElementsByTagName("float").item(0).getTextContent());
                vect3.put(name, val);
            }
        } catch (Exception ex) {
            throw new XmlRpcException("Cannot parse Frame response");
        }
        return mapToFrame(vect3);
    }

    public static Frame mapToFrame(Map<String, Float> map) {
        if (!map.containsKey("x") || !map.containsKey("y") || !map.containsKey("z"))
            throw new IllegalArgumentException("Lack of x, y or z values in map for Vector3 instance");
        return new Frame(map.get("x"), map.get("y"), map.get("z"));
    }

    public Simulator(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            outWriter = new PrintWriter(socket.getOutputStream(), true);
            inReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


class XmlRpcException extends Exception {
    public XmlRpcException(String msg) {
        super(msg);
    }
}