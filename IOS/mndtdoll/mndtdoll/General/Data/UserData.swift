//
//  UserData.swift
//  doll
//
//  Created by Ghost on 2017/12/5.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class UserData {
    
    static let UPDATE_DATE = "UPDATE_DATE"
    static let LOGIN_TYPE = "LOGIN_TYPE"
    static let ACCOUNT = "ACCOUNT"
    static let PASSWORD = "PASSWORD"
    static let NAME = "NAME"
    static let SEX = "SEX"
    static let g_userData = UserDefaults()
    
    static func fnSetUserUpdate() {
        let currentDate = NSDate()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd"
        let convertedDate = dateFormatter.string(from: currentDate as Date)
        print("\(convertedDate)")
        g_userData.set(convertedDate, forKey: UPDATE_DATE)
    }
    
    static func fnGetUpdate() -> String? {
        let sData = g_userData.object(forKey: UPDATE_DATE)
        if sData == nil {
            return nil
        }
        return sData as! String?
    }
    
    static func fnSetLoginType(sType: String) {
        g_userData.set(sType, forKey: LOGIN_TYPE)
    }
    
    static func fnGetLoginType() -> String? {
        let sData = g_userData.object(forKey: LOGIN_TYPE)
        if sData == nil {
            return nil
        }
        return sData as! String?
    }
    
    static func fnSetUserAccount(sAccount: String, sPassword: String) {
        g_userData.set(sAccount, forKey: ACCOUNT)
        g_userData.set(sPassword, forKey: PASSWORD)
    }
    
    static func fnGetAccount() -> String! {
        let sData = g_userData.object(forKey: ACCOUNT)
        if sData == nil {
            return nil
        }
        return sData as! String
    }
    
    static func fnGetPassword() -> String! {
        let sData = g_userData.object(forKey: PASSWORD)
        if sData == nil {
            return nil
        }
        return sData as! String
    }
    
    static func fnGetMD5Password() -> String! {
        let md5 = MD5()
        let sData = g_userData.object(forKey: PASSWORD)
        if sData == nil {
            return nil
        }
        return md5.MD5(sData as! String)
    }
    
    static func fnSetUserData(sName: String, sSex: String) {
        g_userData.set(sName, forKey: NAME)
        g_userData.set(sSex, forKey: SEX)
    }
    
    static func fnGetName() -> String! {
        let sData = g_userData.object(forKey: NAME)
        if sData == nil {
            return nil
        }
        return sData as! String
    }
}
