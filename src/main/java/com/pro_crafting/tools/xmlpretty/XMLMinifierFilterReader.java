package com.pro_crafting.tools.xmlpretty;

import java.io.*;

public class XMLMinifierFilterReader extends FilterReader {

    protected XMLMinifierFilterReader(Reader in) {
        super(in);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        int read = super.read(cbuf, off, len);

        if (read < 0) {
            return -1;
        }

        String buf = new String(cbuf, 0, read);
        buf = buf.replaceAll("\\s+(<[^\\/])", "$1");
        buf = buf.replaceAll("(<\\/\\w.*>)\\s+", "$1");

        int min = Math.min(buf.length(), read);
        buf.getChars(0, min, cbuf, off);

        return min;
    }
}
