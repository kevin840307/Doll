//
//  OtherPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/14.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class OtherPresenter: IOtherPresenter {
    private var g_otherController: IOtherController!
    
    var View: IOtherController {
        get {
            return g_otherController
        }
    }
    
    init(_ otherController: IOtherController) {
        g_otherController = otherController
    }
    
    func fnStart() -> Void {
        
    }
    
    func fnAction(sCommamd: String) -> Void {
        switch sCommamd {
        case "登出":
            fnCloseController()
            break
        case "討論區":
            g_otherController.fnOpenThemeController()
            break
        default:
            break
        }
    }
    
    func fnSetImage(_ imgView: UIImageView) -> Void {
        if UserData.fnGetLoginType() == "2" {
            DispatchQueue.global().async {
                Image().fnUrlSetImage(sUrl: "\(DataInfo.WEB_SERVICE_USER_URL)\(UserData.fnGetAccount()!).jpg", imgData: imgView)
            }
        }
    }
    
    private func fnCloseController() -> Void {
        UserData.fnSetLoginType(sType: "0")
        g_otherController.fnCloseController()
    }
    
}
