package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IMessage {

    public void writeExternal(DataOutputStream dos) throws IOException;

    public void readExternal(DataInputStream dis) throws IOException;

}
