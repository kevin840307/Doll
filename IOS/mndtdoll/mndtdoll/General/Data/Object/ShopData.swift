//
//  ShopData.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/16.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class ShopData {
    private var g_sAddressNo = ""
    private var g_sArea = ""
    private var g_sLocation = ""
    private var g_sShopName = ""
    private var g_sAddress = ""
    private var g_sPopular = ""
    private var g_sLatitude = ""
    private var g_sLongitude = ""
    private var g_sMachineAmount = ""
    private var g_sMachineType = ""
    private var g_sRemarks = ""
    private var g_sStar = ""
    private var g_sMyLove = ""
    
    init(sAddressNo: String, sArea: String, sLocation: String, sShopName: String
        , sAddress: String, sPopular: String, sLatitude: String, sLongitude: String
        , sMachineAmount: String, sMachineType: String, sRemarks: String, sStar: String
        , sMyLove: String) {
        g_sAddressNo = sAddressNo
        g_sArea = sArea
        g_sLocation = sLocation
        g_sShopName = sShopName
        g_sAddress = sAddress
        g_sPopular = sPopular
        g_sLatitude = sLatitude
        g_sLongitude = sLongitude
        g_sMachineAmount = sMachineAmount
        g_sMachineType = sMachineType
        g_sRemarks = sRemarks
        g_sMyLove = sMyLove
        g_sStar = sStar
    }
    
    var AddressNo: String {
        get {
            return g_sAddressNo
        }
    }
    
    var Area: String {
        get {
            return g_sArea
        }
    }
    
    var Location: String {
        get {
            return g_sLocation
        }
    }
    
    var ShopName: String {
        get {
            return g_sShopName
        }
    }
    
    var Address: String {
        get {
            return g_sAddress
        }
    }
    
    var Popular: String {
        get {
            return g_sPopular
        }
    }

    var Latitude: String {
        get {
            return g_sLatitude
        }
    }
    
    var Longitude: String {
        get {
            return g_sLongitude
        }
    }
    
    var MachineAmount: String {
        get {
            return g_sMachineAmount
        }
    }
    
    var MachineType: String {
        get {
            return g_sMachineType
        }
    }

    
    var Remarks: String {
        get {
            return g_sRemarks
        }
    }
    
    var Star: String {
        get {
            return g_sStar
        }
    }
    
    var MyLove: String {
        get {
            return g_sMyLove
        }
        set {
            g_sMyLove = newValue
        }
    }
}
