package Util.IgniteModels;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

public class Address implements Binarylizable {

    private String street;
    private int zip;

    public Address() {}

    public Address(String street, int zip) {
        this.street = street;
        this.zip = zip;
    }


    @Override
    public void writeBinary(BinaryWriter binaryWriter) throws BinaryObjectException {
       binaryWriter.writeString("street", street);
       binaryWriter.writeInt("zip", zip);
    }

    @Override
    public void readBinary(BinaryReader binaryReader) throws BinaryObjectException {
        street = binaryReader.readString("street");
        zip = binaryReader.readInt("zip");
    }

    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' +
                ", zip=" + zip +
                '}';
    }
}
