package HslCommunicationDemo.PLC.OpenProtocol;

public class OpenMessage {
    public OpenMessage( int mID, int revision, int stationID, int spindleID, String[] dataField  )
    {
        this.MID = mID;
        this.Revision = revision;
        this.StationID = stationID;
        this.SpindleID = spindleID;
        this.DataField = dataField;
    }

    public String Key = "";

    public int MID = 0;

    public int Revision = 0;

    public int StationID = 0;

    public int SpindleID = 0;

    public String[] DataField = new String[0];

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return Key;
    }
}
