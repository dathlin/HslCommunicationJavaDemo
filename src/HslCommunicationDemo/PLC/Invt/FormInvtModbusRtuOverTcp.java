package HslCommunicationDemo.PLC.Invt;

import HslCommunication.Core.Types.FunctionOperateExTwo;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.ModBus.ModbusMappingAddress;
import HslCommunicationDemo.Demo.DeviceAddressExample;
import HslCommunicationDemo.PLC.Modbus.FormModbusRtuOverTcp;

import javax.swing.*;

public class FormInvtModbusRtuOverTcp  extends FormModbusRtuOverTcp {
    public FormInvtModbusRtuOverTcp(JTabbedPane tabbedPane) {
        super(tabbedPane);

        // 注册了英威腾PLC的地址映射关系
        addressMapping = new FunctionOperateExTwo<String, Byte, OperateResultExOne<String>>() {
            @Override
            public OperateResultExOne<String> Action(String content1, Byte content2) {
                return ModbusMappingAddress.Invt_Ts(content1, content2);
            }
        };
    }

    @Override
    public String getWindowHead() {
        return "Invt ModbusRtu";
    }


    @Override
    public String getDefaultAddress() {
        return "D100";
    }

    @Override
    public DeviceAddressExample[] getAddressExample() {
        return InvtHelper.GetInvtAddress();
    }
}
