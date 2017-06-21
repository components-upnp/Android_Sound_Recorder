package com.example.mkostiuk.android_sound_recorder.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by mkostiuk on 21/06/2017.
 */

public class GenerateurXml {
    public String getDocXml(String udn, String filePath) throws ParserConfigurationException, TransformerException {

        String namespace = "";
        Document doc;
        DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        builder = db.newDocumentBuilder();
        doc = builder.newDocument();
        Element root = doc.createElementNS(namespace, "SoundRecorder");
        doc.appendChild(root);

        Element nbQuestion = doc.createElementNS(namespace, "UDN");
        root.appendChild(nbQuestion);
        nbQuestion.appendChild(doc.createTextNode(udn));

        Element command = doc.createElementNS(namespace, "CHEMIN");
        root.appendChild(command);
        command.appendChild(doc.createTextNode(filePath));

        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);

        return writer.toString();
    }
}
