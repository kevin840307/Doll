//
//  LoginModel.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class LoginModel: ILoginModel {    
    private var g_sAccount: String!
    private var g_sPassword: String!
    
    func fnRunLogin(sAccount: String, sPassword: String, fnSuccess: @escaping ([[String]]) -> Void) -> Void {
        g_sAccount = sAccount
        g_sPassword = sPassword
        
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapData: SoapData = self.fnGetLoginArg()
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnLogin", soapData: soapData)
        
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                let sDatas = xmlEngine.fnGetSplitData(iSize: 2)
                fnSuccess(sDatas)
            }
        })
        taskThread.resume()
    }
    
    private func fnGetLoginArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sIMEI", sValue: "356939076001089")
        soapData.fnAdd(sKey: "sAccount", sValue: g_sAccount)
        soapData.fnAdd(sKey: "sPwd", sValue: g_sPassword)
        return soapData
    }
}
