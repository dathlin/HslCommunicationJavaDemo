package HslCommunicationDemo.PLC.WeCon;

import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusMappingAddress;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.PLC.Modbus.FormModbusRtuOverTcp;

import javax.swing.*;

public class FormWeConModbusRtuOverTcp extends FormModbusRtuOverTcp {
    public FormWeConModbusRtuOverTcp(JTabbedPane tabbedPane) {
        super(tabbedPane);

        // 注册了维控PLC的地址映射关系
        addressMapping = new FunctionOperateExTwo<String, Byte, OperateResultExOne<String>>() {
            @Override
            public OperateResultExOne<String> Action(String content1, Byte content2) {
                return ModbusMappingAddress.WeCon_Lx5v(content1, content2);
            }
        };
    }

    @Override
    public String getWindowHead() {
        return "WeCon ModbusRtu";
    }


    @Override
    public String getDefaultAddress() {
        return "D100";
    }

    @Override
    public DeviceAddressExample[] getAddressExample() {
        return WeConHelper.GetWeConLx5vAddress();
    }
}
