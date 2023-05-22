package com.company;

import HslCommunication.Authorization;
import HslCommunication.Core.Types.ActionOperateExTwo;
import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Core.Types.OperateResultExOne;
import HslCommunication.Core.Types.OperateResultExTwo;
import HslCommunication.MQTT.MqttSyncClient;
import HslCommunication.ModBus.ModbusTcpNet;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import HslCommunication.Utilities;
import HslCommunicationDemo.FormLoad;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("    欢迎 使用 HslCommunication");
        System.out.println("	CopyRight by Richard.Hu 2017-2021");
        System.out.println("	 本程序的版权归Richard.Hu及杭州胡工物联科技有限公司所有，源代码仅限于学术研究使用，商用需要授权，在没有获得商用版权的情况下，应用于商用项目，将依法追究法律责任，感谢支持，详细说明请参照：");
        System.out.println("");
        System.out.println("	 企业商用说明：仅限于公司开发的软件，该软件的署名必须为授权公司，不得改成他人或是公司（除非他人或公司已经取得商用授权），该软件不能被转卖。授权不限制项目，一次授权，终生使用。");
        System.out.println("");
        System.out.println("	 关于授权的步骤：");
        System.out.println("	     1. 签合同，双方在合同上盖章签字");
        System.out.println("		 2. 付款，银行公对公打款");
        System.out.println("		 3. 开发票，增值税普票及专票");
        System.out.println("		 4. 加入 Hsl超级VIP群 即获得源代码和超级激活码，永久支持更新。");
        System.out.println("		 5. 专业培训额外付费，1000元人民币1小时，培训控件使用，控件开发。");
        System.out.println("		 6. 联系方式：Email:hsl200909@163.com   QQ:200962190   WeChat:13516702732");
        System.out.println("");
        System.out.println("	官网：http://www.hslcommunication.cn  如果不能访问，请访问：http://118.24.36.220");
        System.out.println("");
        System.out.println("");
        System.out.println("	The copyright of this program belongs to Richard.Hu and Hangzhou Hugong IoT Technology Co., Ltd.. The source code is limited to academic research. Commercial licenses are required. ");
        System.out.println("	If commercial copyrights are not obtained and applied to commercial projects, legal liability will be investigated. Thank you for your support. For details, please refer to:");
        System.out.println("");
        System.out.println("");
        System.out.println("	Personal commercial description: It is limited to software developed by individuals. The software must be signed by an authorized person. ");
        System.out.println("	It must not be changed to another person or company (unless another person or company has obtained a commercial license). ");
        System.out.println("	The software cannot be resold. Authorization does not limit the project, once authorized, lifetime use.");
        System.out.println("");
        System.out.println("	Enterprise commercial description: It is limited to the software developed by the company. The signature of the software must be an authorized company. ");
        System.out.println("	It must not be changed to another person or company (unless someone else or the company has obtained a commercial license). The software cannot be resold. ");
        System.out.println("	Authorization does not limit the project, once authorized, lifetime use.");
        System.out.println("");
        System.out.println("	Steps on authorization:");
        System.out.println("	1. Sign the contract, both parties sign the contract");
        System.out.println("	2. Payment, support Alipay, WeChat, bank card, paypal");
        System.out.println("	3. Invoice, general VAT ticket");
        System.out.println("	4. Join the Hsl Super VIP Group to get the source code and super activation code, and always support updates.");
        System.out.println("	5. Register git account on official website");
        System.out.println("	6. Professional training costs an additional fee of 1,000 yuan per hour, training controls use, control development.");
        System.out.println("	7. Contact: Email: hsl200909@163.com QQ: 200962190 Weichat: 13516702732");
        System.out.println("	");
        System.out.println("	Website：http://www.hslcommunication.cn  If you cannot access, please visit: http://118.24.36.220");

//        ModbusTcpNet modbus = new ModbusTcpNet("127.0.0.1", 502, (byte) 1);
//        modbus.SetPersistentConnection();
//        while (true){
//            try{
//                Thread.sleep(1000);
//            }
//            catch (Exception ex){
//
//            }
//
//            OperateResultExOne<Short> read = modbus.ReadInt16("100");
//            if (read.IsSuccess)
//            {
//                System.out.println(new Date().toString() + " Read success! " + read.Content.toString());
//            }
//            else {
//                System.out.println(new Date().toString() + " Read failed! " + read.Message);
//            }
//        }

        // 在使用HslCommunication之前，需要先激活jar包，激活码需要根据授权来获得，激活的代码如下
        // Before using HslCommunication, you need to activate the jar package first.
        // The activation code needs to be obtained according to authorization. The activation code is as follows
        if (Authorization.SetAuthorizationCode("8ba6cfd8-920f-41cf-a364-a8527914c2e6")){
            System.out.println("Active success! [激活成功]");
        }
        else {
            System.out.println("Active failed, it can be use only 24 hours! [激活失败，当前的jar包只能连续使用24小时]");
        }
        FormLoad formLoad = new FormLoad();
        formLoad.setVisible(true);
        formLoad.dispose();
    }
}
