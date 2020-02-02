package com.pro_crafting.tools.xmlpretty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.xml.transform.TransformerFactory;

@ApplicationScoped
public class TransformerFactoryProducer {
    @Produces
    @ApplicationScoped
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
}
