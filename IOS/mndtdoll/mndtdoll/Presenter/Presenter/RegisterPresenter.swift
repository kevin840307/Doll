//
//  RegisterPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class RegisterPresenter: IRegisterPresenter  {    
    private var g_registerController: IRegisterController!
    private var g_registerModel: IRegisterModel!
    
    var View: IRegisterController {
        get {
            return g_registerController
        }
    }
    
    init(_ registerController: IRegisterController, _ registerModel: IRegisterModel) {
        g_registerController = registerController
        g_registerModel = registerModel
    }
    
    init(_ registerController: IRegisterController) {
        g_registerController = registerController
        g_registerModel = RegisterModel()
    }
    
    func fnStart() -> Void {

    }
    
    func fnRegister(sName: String, sAccount: String, sPassword: String, sCheckPass: String, imgData: UIImage?) {
        if sPassword != sCheckPass {
            self.g_registerController.fnBottomMsg("密碼輸入錯誤")
        } else if sName.count == 0 || sName.count > 10 {
            self.g_registerController.fnBottomMsg("名稱長度錯誤")
        } else if sAccount.count < 6 || sAccount.count > 20 {
            self.g_registerController.fnBottomMsg("帳號長度錯誤")
        } else if sPassword.count < 6 || sPassword.count > 20 {
            self.g_registerController.fnBottomMsg("密碼長度錯誤")
        } else {
            let md5Str = MD5().MD5(sPassword)
            g_registerModel.fnRunRegister(sName: sName, sAccount: sAccount, sPassword: md5Str, imgData: imgData, fnSuccess: fnRegisterSuccess)
        }
    }
    
    func fnSetSex(sSex: String) {
        if g_registerModel.sex != sSex {
            if sSex == "男" {
                g_registerController.fnSetMan()
            } else {
                g_registerController.fnSetWoman()
            }
            g_registerModel.sex = sSex
        }
    }
    
    func fnRegisterSuccess(sCheck: String) -> Void {
        DispatchQueue.main.async {
            if sCheck == "true" {
                self.g_registerController.fnBottomMsg("註冊成功", fnSuccess: self.g_registerController.fnCloseController)
            } else {
                self.g_registerController.fnBottomMsg("註冊失敗")
            }
        }
    }
}
