package com.tju.myproject.utils;

import java.io.*;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        try {
            body = readBytes(request.getInputStream());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    public static byte[] readBytes(InputStream is) throws Exception {
        byte[] buffer=new byte[1024];
        int len=0;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        while((len=is.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        bos.flush();
        return bos.toByteArray();
    }
}