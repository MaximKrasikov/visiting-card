package core.responses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import core.Response;

public class HandshakeResponse extends Response {

    public static final String HANDSHAKE_RESPONSE_STRING = "handshake response";

    private String handshake;

    @Override
    public void readExternal(DataInputStream dis) throws IOException {
        handshake = dis.readUTF();
    }

    @Override
    public void writeExternal(DataOutputStream dos) throws IOException {
        dos.writeUTF(HANDSHAKE_RESPONSE_STRING);
    }

    public boolean match() {
        return HANDSHAKE_RESPONSE_STRING.equals(handshake);
    }

}

