package HslCommunicationDemo.Demo;

/**
 * 地址示例类
 */
public class DeviceAddressExample {

    // region Constructor

    /**
     * 实例化一个默认的对象
     */
    public DeviceAddressExample( ) {

    }

    /**
     * 指定相关的参数信息来实例化地址示例的对象
     * @param address 地址的名称
     * @param type 地址的类型描述
     * @param bit 是否支持位操作
     * @param word 是否支持字操作
     * @param mark 备注
     */
    public DeviceAddressExample( String address, String type, boolean bit, boolean word, String mark ) {
        this(address, type, bit, word, mark, false, false, "", "");
    }

    /**
     * 指定相关的参数信息来实例化地址示例的对象
     * @param address 地址的名称
     * @param type 地址的类型描述
     * @param bit 是否支持位操作
     * @param word 是否支持字操作
     * @param mark 备注
     * @param header 是否是标题栏操作
     */
    public DeviceAddressExample( String address, String type, boolean bit, boolean word, String mark, boolean header ) {
        this(address, type, bit, word, mark, header, false, "", "");
    }

    /**
     * 指定相关的参数信息来实例化地址示例的对象
     * @param address 地址的名称
     * @param type 地址的类型描述
     * @param bit 是否支持位操作
     * @param word 是否支持字操作
     * @param mark 备注
     * @param header 是否是标题栏操作
     * @param fill 是否使用名称信息填充数据标签名
     * @param unit 数据的单位信息
     * @param dataType 默认使用的数据类型，有些数据标签使用固定的读写类型信息时，可以指定
     */
    public DeviceAddressExample( String address, String type, boolean bit, boolean word, String mark, boolean header, boolean fill, String unit, String dataType ) {
        this.AddressExample = address;
        this.AddressType = type;
        this.BitEnable = bit;
        this.WordEnable = word;
        this.Mark = mark;
        this.IsHeader = header;
        this.FillTagNameWithAddressType = fill;
        this.Unit = unit;
        this.DataType = dataType;
    }

    // endregion

    /**
     * 示例的地址
     */
    public String AddressExample = "";

    /**
     * 地址类型说明
     */
    public String AddressType = "";

    /**
     * 是否支持位操作
     */
    public boolean BitEnable = false;

    /**
     * 是否支持字操作
     */
    public boolean WordEnable = false;

    /**
     * 标记信息
     */
    public String Mark = "";

    /**
     * 当前的地址信息是否是标题栏信息
     */
    public boolean IsHeader = false;

    /**
     * 当前数据的单位，有些特殊的设备可以指定单位信息
     */
    public String Unit = "";

    /**
     * 是否使用地址名称信息填入节点名称信息
     */
    public boolean FillTagNameWithAddressType = false;

    /**
     * 默认使用的数据类型，有些数据标签使用固定的读写类型信息时，可以指定
     */
    public String DataType = "";

    public String toString( ) {
        return "DeviceAddressExample[" + AddressExample + "]";
    }


    /**
     * 获取标题信息
     * @return 标题信息
     */
    public static String GetTitle( ) {
        return "Address Example";
    }
}
