//
//  RegisterModel.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class RegisterModel: IRegisterModel {
    
    private var g_s64BaseImage: String = ""
    private var g_sSex: String = "男"
    private var g_sName: String = ""
    private var g_sAccount: String = ""
    private var g_sPassword: String = ""
    
    var sex: String {
        get {
            return g_sSex
        }
        
        set {
            g_sSex = newValue
        }
    }
    
    func fnRunRegister(sName: String, sAccount: String, sPassword: String, imgData: UIImage?, fnSuccess: @escaping (String) -> Void) {
        let queueThread = OperationQueue()
        queueThread.addOperation {
            self.g_sName = sName
            self.g_sAccount = sAccount
            self.g_sPassword = sPassword
            if imgData != nil && !((imgData?.isEqual(UIImage(named: "logo.png")))!) {
                let imgReData = Image().fnResize(imgData: imgData!, width: 100)
                self.g_s64BaseImage = Image().fnUIImageToBase64(imgData: imgReData)!
                //print("\(self.g_s64BaseImage )")
            }
            
            let urlSession = URLSession.shared
            let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
            let soapData: SoapData = self.fnGetRegisterArg()
            let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnRegister", soapData: soapData)
            
            let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
                if error != nil {
                    print("Error: " + error.debugDescription)
                } else {
                    let xmlEngine: XmlEngine = XmlEngine(data!)
                    var sDatas = xmlEngine.fnGetAllData()
                    fnSuccess(sDatas[0])
                }
            })
            taskThread.resume()
        }
        
    }
    
    private func fnGetRegisterArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sIMEI", sValue: "356939076001089")
        soapData.fnAdd(sKey: "sCheckIMEI", sValue: "43893")
        soapData.fnAdd(sKey: "sStrBase64", sValue: g_s64BaseImage)
        soapData.fnAdd(sKey: "sName", sValue: g_sName)
        soapData.fnAdd(sKey: "sAccount", sValue: g_sAccount)
        soapData.fnAdd(sKey: "sPwd", sValue: g_sPassword)
        soapData.fnAdd(sKey: "sSex", sValue: g_sSex)
        return soapData
    }
}
