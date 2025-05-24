package com.example.absensimahasiswa;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private final String boundary = "apiclient-" + UUID.randomUUID();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return Collections.emptyMap();
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Add text parameters
            Map<String, String> params = getParams();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                    bos.write((entry.getValue() + "\r\n").getBytes());
                }
            }

            // Add file data
            Map<String, DataPart> data = getByteData();
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, DataPart> entry : data.entrySet()) {
                    String key = entry.getKey();
                    DataPart file = entry.getValue();

                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getFileName() + "\"\r\n").getBytes());
                    bos.write(("Content-Type: " + file.getType() + "\r\n\r\n").getBytes());
                    bos.write(file.getContent());
                    bos.write("\r\n".getBytes());
                }
            }

            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error building multipart body", e);
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    protected abstract Map<String, String> getParams() throws AuthFailureError;

    protected abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
