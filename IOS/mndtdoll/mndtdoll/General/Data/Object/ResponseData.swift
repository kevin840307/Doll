//
//  ResponseData.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/28.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import Foundation

class ResponseData {
    private var g_sPlateId = ""
    private var g_sThemeId = ""
    private var g_sResponseId = ""
    private var g_sResponseType = ""
    private var g_sContent = ""
    private var g_sAccount = ""
    private var g_sName = ""
    private var g_sDate = ""
    
    init(sPlateId: String, sThemeId: String, sDatas: [String]) {
        g_sPlateId = sPlateId
        g_sThemeId = sThemeId
        g_sResponseId = sDatas[0]
        g_sResponseType = sDatas[1]
        g_sContent = sDatas[2]
        g_sAccount = sDatas[3]
        g_sName = sDatas[4]
        g_sDate = sDatas[5]
    }
    
    var PlateId: String {
        get {
            return g_sPlateId
        }
    }
    
    var ThemeId: String {
        get {
            return g_sThemeId
        }
    }
    
    
    var ResponseId: String {
        get {
            return g_sResponseId
        }
    }
    
    var ResponseType: String {
        get {
            return g_sResponseType
        }
    }
    
    var Content: String {
        get {
            return g_sContent
        }
    }
    
    var Date: String {
        get {
            return g_sDate
        }
    }
    
    var Name: String {
        get {
            return g_sName
        }
    }
    
    var Account: String {
        get {
            return g_sAccount
        }
    }
    
}
