//
//  LoginWaitPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class LoginWaitPresenter: ILoginWaitPresenter {

    private var g_loginWaitController: ILoginWaitController!
    private var g_loginWaitModel: ILoginWaitModel!
    private var g_fProgress: Float = 0
    
    var View: ILoginWaitController {
        get {
            return g_loginWaitController
        }
    }
    
    init(_ loginWaitController: ILoginWaitController, _ loginWaitModel: ILoginWaitModel) {
        g_loginWaitController = loginWaitController
        g_loginWaitModel = loginWaitModel
    }
    
    init(_ loginWaitController: ILoginWaitController) {
        g_loginWaitController = loginWaitController
        g_loginWaitModel = LoginWaitModel()
    }
    
    func fnStart() -> Void {
        fnCheckUpdate()
    }
    
    func fnCheckUpdate() -> Void {
        //g_loginWaitController.fnConfirm()
        //return
        if UserData.fnGetUpdate() != nil {
            g_loginWaitModel.fnRunUpdate(fnSuccess: fnCheckSuccess)
        } else {
            g_loginWaitController.fnConfirm()
        }
    }
    
    func fnCheckSuccess(sCheck: String) -> Void {
        DispatchQueue.main.async {
            if sCheck == "true" {
                self.fnUpdateProgress(10)
                self.g_loginWaitController.fnConfirm()
            } else {
                self.fnOpenController()
            }
        }
    }
    
    func fnDowloadDatas() -> Void {
        g_loginWaitModel.fnRunDowloadDatas(fnSuccess: fnDowloadSuccess)
    }
    
    func fnDowloadSuccess(_ sDatas: inout [[String]]) -> Void {
        self.fnUpdateProgress(10)
        fnInsertShopSql(&sDatas)
        fnOpenController()
    }
    
    func fnOpenController() -> Void {
        if UserData.fnGetLoginType() != nil {
            switch UserData.fnGetLoginType()! {
            case "0":
                self.g_loginWaitController.fnOpenLoginWayController()
                break
            case "1", "2":
                self.g_loginWaitController.fnOpenMainController()
                break
            default:
                break
            }
        } else {
            self.g_loginWaitController.fnOpenLoginWayController()
        }
    }
    
    private func fnInsertShopSql(_ sDatas: inout [[String]]) -> Void {
        let sqlShop: DollShopSql = DollShopSql()
        if sqlShop.fnConnection() {
            sqlShop.fnCreate()
            let fProgressAdd: Float = Float(80) / Float(sDatas.count)
            for sValues in sDatas {
                sqlShop.fnInsert(sData: sValues)
                self.fnUpdateProgress(fProgressAdd)
            }
        }
        UserData.fnSetUserUpdate()
    }
    
    private func fnUpdateProgress(_ fProgress: Float) -> Void {
        g_fProgress += fProgress
        DispatchQueue.main.async {
            self.g_loginWaitController.fnUpdateProgress(self.g_fProgress)
        }
        
    }
}
