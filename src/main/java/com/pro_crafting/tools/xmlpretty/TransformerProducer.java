package com.pro_crafting.tools.xmlpretty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

@ApplicationScoped
public class TransformerProducer {
    @Inject
    TransformerFactory transformerFactory;

    @Produces
    @RequestScoped
    public Transformer configureTransformer() {
        try {
            Transformer t = transformerFactory.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            return t;
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
