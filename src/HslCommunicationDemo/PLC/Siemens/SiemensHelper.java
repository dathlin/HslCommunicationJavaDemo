package HslCommunicationDemo.PLC.Siemens;

import HslCommunicationDemo.Demo.DeviceAddressExample;

public class SiemensHelper {

    public static DeviceAddressExample[] GetSiemensS7Address( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "I0",    "输入寄存器", true, true, "位地址示例：I1.6" ),
                        new DeviceAddressExample( "Q0",    "输出寄存器", true, true, "位地址示例：Q1.6" ),
                        new DeviceAddressExample( "M0",    "内部寄存器", true, true, "位地址示例：M1.6 , 也可以输入 MW100,MD100" ),
                        new DeviceAddressExample( "DB1.0", "数据寄存器", true, true, "位地址示例：DB1.0.1, 也可以输入 DB1.DBD100" ),
                        new DeviceAddressExample( "V0",    "数据寄存器", true, true, "等同于DB1.0" ),

                        new DeviceAddressExample( "T0",    "定时器寄存器", true, true, "smart200测试通过" ),
                        new DeviceAddressExample( "C0",    "计数器寄存器", true, true, "smart200测试通过" ),
                        new DeviceAddressExample( "AI0",   "智能输入寄存器", false, true, "仅支持字单位" ),
                        new DeviceAddressExample( "AQ0",   "智能输出寄存器", false, true, "仅支持字单位" ),
                };
    }

    public static DeviceAddressExample[] GetSiemensPPIAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "I0",    "输入寄存器",     true, true, "位地址示例：I1.6" ),
                        new DeviceAddressExample( "Q0",    "输出寄存器",     true, true, "位地址示例：Q1.6" ),
                        new DeviceAddressExample( "M0",    "内部寄存器",     true, true, "位地址示例：M1.6" ),
                        new DeviceAddressExample( "DB1.0", "数据寄存器",     true, true, "位地址示例：DB1.0.1" ),
                        new DeviceAddressExample( "V0",    "数据寄存器",     true, true, "等同于DB1.0" ),

                        new DeviceAddressExample( "T0",    "定时器寄存器",   true, true, "smart200测试通过" ),
                        new DeviceAddressExample( "C0",    "计数器寄存器",   true, true, "smart200测试通过" ),
                        new DeviceAddressExample( "AI0",   "智能输入寄存器", false, true, "仅支持字单位" ),
                        new DeviceAddressExample( "AQ0",   "智能输出寄存器", false, true, "仅支持字单位" ),
                        new DeviceAddressExample( "SYS0",  "系统内部地址",   true, true, "位地址示例：SYS1.6" ),
                        new DeviceAddressExample( "S0",    "内部寄存器",     true, true, "位地址示例：S1.6" ),
                        new DeviceAddressExample( "SM0",   "特殊内部寄存器", true, true, "位地址示例：SM1.6" ),
                        new DeviceAddressExample( "s=1;M0",    "内部寄存器",     true, true, "支持额外指定站号信息" ),
                };
    }

    public static DeviceAddressExample[] GetSiemensFWAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "I0",    "输入寄存器", true, true, "仅支持字单位" ),
                        new DeviceAddressExample( "Q0",    "输出寄存器", true, true, "仅支持字单位" ),
                        new DeviceAddressExample( "M0",    "内部寄存器", true, true, "仅支持字单位" ),
                        new DeviceAddressExample( "DB1.0", "数据寄存器", true, true, "仅支持字单位" ),

                        new DeviceAddressExample( "T0",    "定时器寄存器", true, true, "仅支持字单位" ),
                        new DeviceAddressExample( "C0",    "计数器寄存器", true, true, "仅支持字单位" ),
                };
    }
}
