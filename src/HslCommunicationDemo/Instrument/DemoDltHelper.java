package HslCommunicationDemo.Instrument;

import HslCommunicationDemo.Demo.DeviceAddressExample;

public class DemoDltHelper {
    /// <summary>
    /// 获取DLT645-2007协议的地址示例
    /// </summary>
    /// <returns>地址示例信息</returns>
    public static DeviceAddressExample[] GetDlt645Address( )
    {
        return new DeviceAddressExample[]{
                new DeviceAddressExample( "00-00-00-00", "当前组合有功总电能", false, false, "读double, 00-00-01-00到00-00-3F-00分别是组合有功费率1~63电能", false, true,"kwh", "double" ),
                new DeviceAddressExample( "00-01-00-00", "当前正向有功总电能", false, false, "读double, 00-01-01-00到00-01-3F-00分别是正向有功费率1~63电能", false, true,"kwh", "double"),
                new DeviceAddressExample( "00-02-00-00", "当前反向有功总电能", false, false, "读double, 00-02-01-00到00-02-3F-00分别是反向有功费率1~63电能", false, true,"kwh", "double" ),
                new DeviceAddressExample( "00-03-00-00", "当前组合无功总电能", false, false, "读double, 00-03-01-00到00-03-3F-00分别是组合无功费率1~63电能", false, true,"kwh", "double" ),
                new DeviceAddressExample( "00-09-00-00", "当前正向视在总电能", false, false, "读double, 00-09-01-00到00-09-3F-00分别是正向视在费率1~63电能", false, true,"kwh", "double" ),
                new DeviceAddressExample( "00-0A-00-00", "当前反向视在总电能", false, false, "读double, 00-0A-01-00到00-0A-3F-00分别是反向视在费率1~63电能", false, true,"kwh", "double" ),
                new DeviceAddressExample( "02-01-01-00", "A相电压", false, false, "读double", false, true, "V", "double" ),
                new DeviceAddressExample( "02-01-02-00", "B相电压", false, false, "读double", false, true, "V", "double" ),
                new DeviceAddressExample( "02-01-03-00", "C相电压", false, false, "读double", false, true, "V", "double" ),
                new DeviceAddressExample( "02-02-01-00", "A相电流", false, false, "读double", false, true, "A", "double" ),
                new DeviceAddressExample( "02-02-02-00", "B相电流", false, false, "读double", false, true, "A", "double" ),
                new DeviceAddressExample( "02-02-03-00", "C相电流", false, false, "读double", false, true, "A", "double" ),
                new DeviceAddressExample( "02-03-00-00", "瞬时总有功功率", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false, true, "kw", "double" ),
                new DeviceAddressExample( "02-04-00-00", "瞬时总无功功率", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false, true, "kvar",  "double" ),
                new DeviceAddressExample( "02-05-00-00", "瞬时总视在功率", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false, true, "kva",  "double" ),
                new DeviceAddressExample( "02-06-00-00", "总功率因素", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false,  true, "",  "double" ),
                new DeviceAddressExample( "02-07-01-00", "A相相角", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false, true, "°", "double" ),
                new DeviceAddressExample( "02-08-01-00", "A相电压波形失真度", false, false, "读double，DI1=1时表示A相，2时表示B相，3时表示C相", false, true, "%", "double" ),
                new DeviceAddressExample( "02-80-00-01", "零线电流", false, false, "读double", false, true, "A",  "double" ),
                new DeviceAddressExample( "02-80-00-02", "电网频率", false, false, "读double", false, true, "HZ",  "double" ),
                new DeviceAddressExample( "02-80-00-03", "一分钟有功总平均功率", false, false, "读double", false, true, "kw",  "double" ),
                new DeviceAddressExample( "02-80-00-04", "当前有功需量", false, false, "读double", false, true, "kw",  "double" ),
                new DeviceAddressExample( "02-80-00-05", "当前无功需量", false, false, "读double", false, true, "kvar",  "double" ),
                new DeviceAddressExample( "02-80-00-06", "当前视在需量", false, false, "读double", false,  true, "kva",  "double" ),
                new DeviceAddressExample( "02-80-00-07", "表内温度", false, false, "读double", false, true, "℃",  "double" ),
                new DeviceAddressExample( "02-80-00-08", "时钟电池电压", false, false, "读double", false, true, "V",  "double" ),
                new DeviceAddressExample( "02-80-00-09", "停电抄表电池电压", false, false, "读double", false, true, "V",  "double" ),
                new DeviceAddressExample( "02-80-00-0A", "内部电池工作时间", false, false, "读double", false, true, "min",  "double" ),
                new DeviceAddressExample( "04-00-01-01", "日期及星期(0表示星期天)", false, false, "ReadString(\"04-00-01-01\", 4)", false, true, "",  "string" ),
                new DeviceAddressExample( "04-00-01-02", "时分秒", false, false, "ReadString(\"04-00-01-02\", 3)", false, true, "", "string" ),
                new DeviceAddressExample( "04-00-04-03", "资产管理编码", false, false, "ReadString(\"04-00-04-03\", 32)", false, true, "", "string" ),
                new DeviceAddressExample( "04-00-04-0B", "电表型号", false, false, "ReadString(\"04-00-04-0B\", 10)", false, true, "", "string" ),
                new DeviceAddressExample( "04-00-04-0C", "生产日期", false, false, "ReadString(\"04-00-04-0C\", 10)",false, true, "", "string" ),
			};
    }
}
