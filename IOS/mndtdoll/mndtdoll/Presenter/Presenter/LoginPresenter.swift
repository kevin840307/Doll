//
//  LoginPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class LoginPresenter: ILoginPresenter {
    private var g_loginController: ILoginController!
    private var g_loginModel: ILoginModel!
    
    var View: ILoginController {
        get {
            return g_loginController
        }
    }
    
    init(_ loginController: ILoginController, _ loginModel: ILoginModel) {
        g_loginController = loginController
        g_loginModel = loginModel
    }
    
    init(_ loginController: ILoginController) {
        g_loginController = loginController
        g_loginModel = LoginModel()
    }

    func fnStart() {
        
    }
    
    func fnLogin(sAccount: String, sPassword: String) {
        let md5Str = MD5().MD5(sPassword)
        UserData.fnSetUserAccount(sAccount: sAccount, sPassword: sPassword)
        g_loginModel.fnRunLogin(sAccount: sAccount, sPassword: md5Str, fnSuccess: fnLoginSuccess)
    }
    
    func fnLoginSuccess(sData: [[String]]) -> Void {
        DispatchQueue.main.async {
            if sData.count == 1 {
                UserData.fnSetLoginType(sType: "2")
                UserData.fnSetUserData(sName: sData[0][0], sSex: sData[0][1])
                self.fnRunDowloadEvaluation()
            } else {
                self.g_loginController.fnBottomMsg("登入失敗")
            }
        }
    }
    
    func fnRunDowloadEvaluation() -> Void {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetMyEvaluation", soapData: fnGetArg())
        
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil{
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                var sDatas = xmlEngine.fnGetSplitData(iSize: 6)
                self.fnInsertSql(&sDatas)
            }
        })
        taskThread.resume()
    }
    
    private func fnGetArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sIMEI", sValue: "356939076001089")
        soapData.fnAdd(sKey: "sAccount", sValue: UserData.fnGetAccount())
        soapData.fnAdd(sKey: "sPwd", sValue: UserData.fnGetMD5Password())
        return soapData
    }
    
    private func fnInsertSql(_ sDatas: inout [[String]]) -> Void {
        let sqlEvaluation: DollShopEvaluation = DollShopEvaluation()
        if sqlEvaluation.fnConnection() {
            sqlEvaluation.fnCreate()
            for sValues in sDatas {
                sqlEvaluation.fnInsert(sData: sValues)
                print(sValues[0])
            }
        }
        fnAllSuccess()
    }
    
    func fnAllSuccess() -> Void {
        DispatchQueue.main.async {
            self.g_loginController.fnBottomMsg("登入成功", fnSuccess: self.g_loginController.fnOpenMainController)
        }
    }
    
    
    
}
