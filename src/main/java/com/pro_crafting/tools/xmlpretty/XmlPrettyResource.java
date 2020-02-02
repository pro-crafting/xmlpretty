package com.pro_crafting.tools.xmlpretty;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * No Rest Api. This is just supposed as output medium for the prettifyed xml.
 */
@ApplicationScoped
@Path("xmlpretty")
public class XmlPrettyResource {

    @Inject
    Transformer transformer;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "prettifyCount", description = "How often a xml was prettified")
    @Timed(name = "ttfbTimer", description = "A measure of how long it takes to return the first byte.", unit = MetricUnits.MILLISECONDS)
    public StreamingOutput prettify(InputStream xmlStream) throws TransformerException {

        // The form is transmitted as text/plain enctype
        // in the form entryname=xmlstring
        // We have to filter out anything before the =,
        // so that we can read just the xml string

        // Read the first 4 bytes
        // containing xml=
        try {
            xmlStream.read(new byte[4], 0, 4);
        } catch (IOException ex) {
            return output -> output.write(asBytes("Unable to read xml"));
        }

        return output -> {
            try {
                transformer.transform(new StreamSource(new XMLMinifierFilterReader(new InputStreamReader(xmlStream))), new StreamResult(output));
            } catch (TransformerException e) {
                output.write(asBytes("Invalid XML, Reason: " + e.getMessage()));
            }
        };
    }

    private byte[] asBytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
