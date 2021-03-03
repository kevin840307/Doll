//
//  ThemeData.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/26.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import Foundation

class ThemeData {
    private var g_sPlateId = "";
    private var g_sThemeId = "";
    private var g_sArticleType = "";
    private var g_sTitle = "";
    private var g_sContent = "";
    private var g_sResponseCount = "";
    private var g_sAccount = "";
    private var g_sName = "";
    private var g_sPicAmount = "";
    private var g_sDate = "";
    
    init(sDatas: [String]) {
        g_sPlateId = sDatas[0]
        g_sThemeId = sDatas[1]
        g_sArticleType = sDatas[2]
        g_sTitle = sDatas[3]
        g_sContent = sDatas[4]
        g_sResponseCount = sDatas[5]
        g_sAccount = sDatas[6]
        g_sName = sDatas[7]
        g_sPicAmount = sDatas[8]
        g_sDate = sDatas[9]
    }
    
    init(sPlateId: String, sDatas: [String]) {
        g_sPlateId = sPlateId
        g_sThemeId = sDatas[0]
        g_sArticleType = sDatas[1]
        g_sTitle = sDatas[2]
        g_sContent = sDatas[3]
        g_sResponseCount = sDatas[4]
        g_sAccount = sDatas[5]
        g_sName = sDatas[6]
        g_sPicAmount = sDatas[7]
        g_sDate = sDatas[8]
    }

    init(sPlateId: String, sThemeId: String, sArticleType: String, sTitle: String
        , sContent: String, sResponseCount: String, sAccount: String, sName: String
        , sPicAmount: String, sDate: String) {
        g_sPlateId = sPlateId
        g_sThemeId = sThemeId
        g_sArticleType = sArticleType
        g_sTitle = sTitle
        g_sContent = sContent
        g_sResponseCount = sResponseCount
        g_sAccount = sAccount
        g_sName = sName
        g_sPicAmount = sPicAmount
        g_sDate = sDate
    }
    
    var Date: String {
        get {
            return g_sDate
        }
    }
    
    var PicAmount: String {
        get {
            return g_sPicAmount
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
    
    var ResponseCount: String {
        get {
            return g_sResponseCount
        }
    }
    
    var Content: String {
        get {
            return g_sContent
        }
    }
    
    var Title: String {
        get {
            return g_sTitle
        }
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
    
    var ArticleType: String {
        get {
            return g_sArticleType
        }
    }
    
}
