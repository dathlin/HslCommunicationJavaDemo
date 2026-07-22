package HslCommunicationDemo.PLC.Melsec;

import HslCommunication.Core.Types.List;
import HslCommunication.Utilities;
import HslCommunicationDemo.Demo.DeviceAddressExample;

public class DemoMelsecHelper {

    public static DeviceAddressExample[] GetMcAddress( ){
        return  GetMcAddress(false);
    }
    /**
     * 获取MC协议的地址示例
     * @return 地址示例信息
     */
    public static DeviceAddressExample[] GetMcAddress( boolean advance )
    {
        List<DeviceAddressExample> array = new List<>( );

                array.add( new DeviceAddressExample( "M0",  "内部继电器",       true, true, "" ) );
                array.add( new DeviceAddressExample( "X0",  "输入继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 X011" ));
                array.add( new DeviceAddressExample( "Y0",  "输出继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 Y011" ));
                array.add( new DeviceAddressExample( "SM0", "SM特殊继电器",     true, true, "" ));
                array.add( new DeviceAddressExample( "S0", "步进继电器",        true, true, "" ));
                array.add( new DeviceAddressExample( "L0", "锁存继电器",        true, true, "" ));
                array.add( new DeviceAddressExample( "F0", "报警器",            true, true, "" ));
                array.add( new DeviceAddressExample( "V0", "边沿继电器",        true, true, "" ));
                array.add( new DeviceAddressExample( "B0", "链接继电器",        true, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "SB0", "特殊链接继电器",   true, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "DX0", "直接输入",         true, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "DY0", "直接输出",         true, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "TS0", "定时器触点",       true, false, "" ));
                array.add( new DeviceAddressExample( "TC0", "定时器线圈",       true, false, "" ));
                array.add( new DeviceAddressExample( "SS0", "累计定时器触点",   true, false, "" ));
                array.add( new DeviceAddressExample( "SC0", "累计定时器线圈",   true, false, "" ));
                array.add( new DeviceAddressExample( "CS0", "计数器触点",       true, false, "" ));
                array.add( new DeviceAddressExample( "CC0", "计数器线圈",       true, false, "" ));
                array.add( new DeviceAddressExample( "D0", "数据寄存器",        false, true, "" ));
                array.add( new DeviceAddressExample( "SD0", "特殊数据寄存器",   false, true, "" ));
                array.add( new DeviceAddressExample( "W0", "链接寄存器",        false, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "SW0", "特殊链接寄存器",   false, true, "16进制地址" ));
                array.add( new DeviceAddressExample( "R0", "文件寄存器",        false, true, "" ));
                array.add( new DeviceAddressExample( "Z0", "变址寄存器",        false, true, "" ));
                array.add( new DeviceAddressExample( "ZR0", "ZR文件寄存器",     false, true, "" ));
                array.add( new DeviceAddressExample( "TN0", "定时器当前值",     false, true, "" ));
                array.add( new DeviceAddressExample( "SN0", "累计定时器当前值", false, true, "" ));
                array.add( new DeviceAddressExample( "CN0", "计数器当前值",     false, true, "" ));
                if (advance)
                {
                    array.add( new DeviceAddressExample( "ext=1;W100", "扩展的数据地址", false, true, "[商业授权] 访问扩展区域为1的W100的地址信息" ) );
                    array.add( new DeviceAddressExample( "mem=32", "缓冲存储器地址", false, true, "[商业授权] 访问地址为32的本站缓冲存储器地址" ) );
                    array.add( new DeviceAddressExample( "module=3;4106", "智能模块地址", false, true, "[商业授权] 访问模块号3，偏移地址是4106的数据，偏移地址需要根据模块的详细信息来确认。" ) );
                    array.add( new DeviceAddressExample( "s=AAA", "基于标签的地址", false, true, "[商业授权] 仅支持GX Works3的全局标签并且配置对外公开" ) );
                }


                return Utilities.ArrayListToArray(DeviceAddressExample.class, array);
    }

    public static DeviceAddressExample[] GetMcServerAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "M0",  "内部继电器",       true, true, "" ),
                        new DeviceAddressExample( "X0",  "输入继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 X011" ),
                        new DeviceAddressExample( "Y0",  "输出继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 Y011" ),
                        new DeviceAddressExample( "L0",  "锁存继电器",       true, true, "" ),
                        new DeviceAddressExample( "B0",  "链接继电器",       true, true, "16进制地址" ),
                        new DeviceAddressExample( "S0",  "步进继电器",       true, true, "" ),
                        new DeviceAddressExample( "F0",  "报警器",           true, true, "" ),
                        new DeviceAddressExample( "D0",  "数据寄存器",       false, true, "" ),
                        new DeviceAddressExample( "W0",  "链接寄存器",       false, true, "16进制地址" ),
                        new DeviceAddressExample( "R0",  "文件寄存器",       false, true, "" ),
                        new DeviceAddressExample( "Z0",  "变址寄存器",       false, true, "" ),
                        new DeviceAddressExample( "ZR0", "ZR文件寄存器",     false, true, "" ),
                        new DeviceAddressExample( "D100.1",  "数据寄存器",       true, false, "可以访问字寄存器的位" )
                };
    }

    public static DeviceAddressExample[] GetMc1EAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "M0",  "内部继电器",       true, true, "" ),
                        new DeviceAddressExample( "X0",  "输入继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 X011" ),
                        new DeviceAddressExample( "Y0",  "输出继电器",       true, true, "默认16进制，如果需要8进制，使用0开头 y011" ),
                        //new DeviceAddressExample( "SM0", "SM特殊继电器",     true, true, "" ),
                        new DeviceAddressExample( "S0", "步进继电器",        true, true, "" ),
                        //new DeviceAddressExample( "L0", "锁存继电器",        true, true, "" ),
                        new DeviceAddressExample( "F0", "报警器",            true, true, "" ),
                        //new DeviceAddressExample( "V0", "边沿继电器",        true, true, "" ),
                        new DeviceAddressExample( "B0", "链接继电器",        true, true, "16进制地址" ),
                        //new DeviceAddressExample( "SB0", "特殊链接继电器",   true, true, "16进制地址" ),
                        //new DeviceAddressExample( "DX0", "直接输入",         true, true, "16进制地址" ),
                        //new DeviceAddressExample( "DY0", "直接输出",         true, true, "16进制地址" ),
                        new DeviceAddressExample( "TS0", "定时器触点",       true, false, "" ),
                        new DeviceAddressExample( "TC0", "定时器线圈",       true, false, "" ),
                        //new DeviceAddressExample( "SS0", "累计定时器触点",   true, false, "" ),
                        //new DeviceAddressExample( "SC0", "累计定时器线圈",   true, false, "" ),
                        new DeviceAddressExample( "CS0", "计数器触点",       true, false, "" ),
                        new DeviceAddressExample( "CC0", "计数器线圈",       true, false, "" ),
                        new DeviceAddressExample( "D0", "数据寄存器",        false, true, "" ),
                        //new DeviceAddressExample( "SD0", "特殊数据寄存器",   false, true, "" ),
                        new DeviceAddressExample( "W0", "链接寄存器",        false, true, "16进制地址" ),
                        //new DeviceAddressExample( "SW0", "特殊链接寄存器",   false, true, "16进制地址" ),
                        new DeviceAddressExample( "R0", "文件寄存器",        false, true, "" ),
                        //new DeviceAddressExample( "Z0", "变址寄存器",        false, true, "" ),
                        //new DeviceAddressExample( "ZR0", "ZR文件寄存器",     false, true, "" ),
                        new DeviceAddressExample( "TN0", "定时器当前值",     false, true, "" ),
                        //new DeviceAddressExample( "SN0", "累计定时器当前值", false, true, "" ),
                        new DeviceAddressExample( "CN0", "计数器当前值",     false, true, "" )
                };
    }


    public static DeviceAddressExample[] GetFxLinksAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "M0",  "内部继电器",       true, true, "" ),
                        new DeviceAddressExample( "X0",  "输入继电器",       true, true, "8进制地址" ),
                        new DeviceAddressExample( "Y0",  "输出继电器",       true, true, "8进制地址" ),
                        new DeviceAddressExample( "S0", "步进继电器",        true, true, "" ),
                        new DeviceAddressExample( "TS0", "定时器触点",       true, false, "" ),
                        new DeviceAddressExample( "CS0", "计数器触点",       true, false, "" ),
                        new DeviceAddressExample( "D0", "数据寄存器",        false, true, "" ),
                        new DeviceAddressExample( "R0", "文件寄存器",        false, true, "" ),
                        new DeviceAddressExample( "TN0", "定时器当前值",     false, true, "" ),
                        new DeviceAddressExample( "CN0", "计数器当前值",     false, true, "" ),
                        new DeviceAddressExample( "s=2;D0", "数据寄存器",        false, true, "支持额外指定其他的站号信息" ),
                };
    }


    public static DeviceAddressExample[] GetFxSerialAddress( )
    {
        return new DeviceAddressExample[]
                {
                        new DeviceAddressExample( "M0",  "内部继电器",       true, true, "" ),
                        new DeviceAddressExample( "X0",  "输入继电器",       true, true, "8进制地址" ),
                        new DeviceAddressExample( "Y0",  "输出继电器",       true, true, "8进制地址" ),
                        new DeviceAddressExample( "S0", "步进继电器",        true, true, "" ),
                        new DeviceAddressExample( "TS0", "定时器触点",       true, false, "" ),
                        new DeviceAddressExample( "TC0", "定时器线圈",       true, false, "" ),
                        new DeviceAddressExample( "CS0", "计数器触点",       true, false, "" ),
                        new DeviceAddressExample( "CC0", "计数器线圈",       true, false, "" ),
                        new DeviceAddressExample( "D0", "数据寄存器",        false, true, "" ),
                        new DeviceAddressExample( "TN0", "定时器当前值",     false, true, "" ),
                        new DeviceAddressExample( "CN0", "计数器当前值",     false, true, "" )
                };
    }
}
