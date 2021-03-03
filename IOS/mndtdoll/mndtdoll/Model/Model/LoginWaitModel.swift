//
//  LoginWaitModel.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class LoginWaitModel: ILoginWaitModel {
    
    func fnRunUpdate(fnSuccess: @escaping (String) -> Void) -> Void {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapData: SoapData = self.fnGetUpdateArg(sDate: UserData.fnGetUpdate()!)
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetVersionDate", soapData: soapData)
        
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
    
    private func fnGetUpdateArg(sDate: String) -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sDate", sValue: sDate)
        return soapData
    }
    
    func fnRunDowloadDatas(fnSuccess: @escaping (inout [[String]]) -> Void) -> Void {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetShopData")
        
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil{
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                var sDatas = xmlEngine.fnGetSplitData(iSize: 14)
                fnSuccess(&sDatas)
            }
        })
        taskThread.resume()
    }
}
