package com.pro_crafting.tools.xmlpretty;

import com.pro_crafting.tools.xmlpretty.model.SimpleInputPage;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Path("xmltools")
public class XMLMinifierResource {

    @Inject
    Transformer transformer;

    @Inject
    Template simpleinputpage;

    @Path("minify/page")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Counted(name = "pageviews", description = "How often the index page of the minifier was viewed")
    @Timed(name = "pagettfbtimer", description = "A measure of how long it takes to return the first byte.", unit = MetricUnits.MILLISECONDS)
    public TemplateInstance page() throws TransformerException {
        SimpleInputPage data = new SimpleInputPage();
        data.setButtonLabel("Minify!");
        data.setDescription("Insert your XML Text here, and click on \"Minify!\". Output will be minified xml.");
        data.setResource("/xmltools/minify");
        data.setTitle("XML Minify");

        return simpleinputpage.data("page", data);
    }

    @Path("minify")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "minifyCount", description = "How often a xml was minified")
    @Timed(name = "MinifyttfbTimer", description = "A measure of how long it takes to return the first byte.", unit = MetricUnits.MILLISECONDS)
    public StreamingOutput minify(InputStream xmlStream) throws TransformerException {
        try {
            xmlStream.read(new byte[4], 0, 4);
        } catch (IOException ex) {
            return output -> output.write(asBytes("Unable to read xml"));
        }

        return output -> {
            XMLMinifierFilterReader reader = new XMLMinifierFilterReader(new InputStreamReader(xmlStream));

            char[] cbuf = new char[8096];
            int read = reader.read(cbuf, 0, 8096);
            while (read > 0) {
                for (int i = 0; i < read; i++) {
                    output.write(cbuf[i]);
                }

                read = reader.read(cbuf, 0, 8096);
            }
        };
    }

    private byte[] asBytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
