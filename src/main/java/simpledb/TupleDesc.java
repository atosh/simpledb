package simpledb;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
        Type[] newTypes = new Type[td1.numFields + td2.numFields];
        String[] newNames = new String[newTypes.length];
        System.arraycopy(td1.types, 0, newTypes, 0, td1.numFields);
        System.arraycopy(td2.types, 0, newTypes, td1.numFields, td2.numFields);
        System.arraycopy(td1.names, 0, newNames, 0, td1.numFields);
        System.arraycopy(td2.names, 0, newNames, td1.numFields, td2.numFields);
        return new TupleDesc(newTypes, newNames);
    }

    private Type[] types;
    private String[] names;
    private int numFields;
    private int size;
    private boolean hasName;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        this(typeAr);
        names = fieldAr;
        hasName = true;
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        types = typeAr;
        names = new String[types.length];
        numFields = types.length;
        for (Type type : types) {
            size += type.getLen();
        }
        hasName = false;
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return numFields;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        if (numFields <= i) {
            throw new NoSuchElementException();
        }
        return names[i];
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int nameToId(String name) throws NoSuchElementException {
        if (!hasName) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < numFields; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
        if (numFields <= i) {
            throw new NoSuchElementException();
        }
        return types[i];
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        int size = 0;
        for (Type type : types) {
            size += type.getLen();
        }
        return size;
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numFields; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i].getClass().getSimpleName()).append('(')
                    .append(names[i]).append(')');
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(names);
        result = prime * result + numFields;
        result = prime * result + size;
        result = prime * result + Arrays.hashCode(types);
        return result;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TupleDesc other = (TupleDesc) obj;
        if (numFields != other.numFields) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (!Arrays.equals(types, other.types)) {
            return false;
        }
        return true;
    }
}
