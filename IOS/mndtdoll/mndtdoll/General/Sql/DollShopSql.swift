//
//  DollShopSql.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation
class DollShopSql: SqlEngineProtocol {
    var g_pDBEngine: OpaquePointer?
    public let TABLE_NAME = "DOLLshop"
    public let ADDRESS_NO = "address_no"
    public let AREA = "area"
    public let LOCATION = "location"
    public let SHOP_NAME = "shop_name"
    public let ADDRESS = "address"
    public let POPULAR = "popular"
    public let MACHINE_AMOUNT = "machine_amount"
    public let MACHINE_TYPE = "machine_type"
    public let REMARKS = "remarks"
    public let LATITUDE = "latitude"
    public let LONGITUDE = "longitude"
    public let STAR = "star"
    public let CREATE_DATETIME = "create_datetime"
    public let MODIFY_DATETIME = "modify_datetime"
    public let MYLOVE = "mylove"
    
    func fnConnection() -> Bool {
        return fnConnection(&g_pDBEngine)
    }
    
    func fnCreate() -> Bool {
        let sSql = "CREATE TABLE IF NOT EXISTS \(TABLE_NAME)("
            + "  \(ADDRESS_NO) TEXT PRIMARY KEY "
            + " , \(AREA) TEXT "
            + " , \(LOCATION) TEXT "
            + " , \(SHOP_NAME) TEXT "
            + " , \(ADDRESS) TEXT "
            + " , \(POPULAR) TEXT "
            + " , \(MACHINE_AMOUNT) TEXT "
            + " , \(MACHINE_TYPE) TEXT "
            + " , \(REMARKS) TEXT "
            + " , \(LATITUDE) TEXT "
            + " , \(LONGITUDE) TEXT "
            + " , \(STAR) TEXT "
            + " , \(CREATE_DATETIME) TEXT "
            + " , \(MODIFY_DATETIME) TEXT "
            + " , \(MYLOVE) TEXT) "
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnInsert(sData: [String]) -> Bool {

        if fnGetID(sAddressNo: sData[0]) == "-1" {
        let sSql = " INSERT INTO \(TABLE_NAME)( "
            + "  \(ADDRESS_NO) "
            + " , \(AREA) "
            + " , \(LOCATION) "
            + " , \(SHOP_NAME) "
            + " , \(ADDRESS) "
            + " , \(POPULAR) "
            + " , \(MACHINE_AMOUNT) "
            + " , \(MACHINE_TYPE) "
            + " , \(REMARKS) "
            + " , \(LATITUDE) "
            + " , \(LONGITUDE) "
            + " , \(STAR) "
            + " , \(CREATE_DATETIME) "
            + " , \(MODIFY_DATETIME) "
            + " , \(MYLOVE)) "
            + " VALUES ( "
            + "  '\(sData[0])' "
            + " , '\(sData[1])' "
            + " , '\(sData[2])' "
            + " , '\(sData[3])' "
            + " , '\(sData[4])' "
            + " , '\(sData[5])' "
            + " , '\(sData[6])' "
            + " , '\(sData[7])' "
            + " , '\(sData[8])' "
            + " , '\(sData[9])' "
            + " , '\(sData[10])' "
            + " , '\(sData[11])' "
            + " , '\(sData[12])' "
            + " , '\(sData[13])' "
            + " , '0') "
        return fnExecute(&g_pDBEngine, sSql: sSql)
        } else {
            return fnUpdate(sValue: sData)
        }
    }
    
    func fnDelete(sKeyValue: [String]) -> Bool {
        let sSql = " DELETE FROM \(TABLE_NAME) "
            + " WHERE 1 = 1 "
            + "     AND \(ADDRESS_NO) = '\(sKeyValue[0])' "
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnUpdate(sValue: [String]) -> Bool {
        let sSql = " UPDATE \(TABLE_NAME) SET "
            + "  \(AREA) = '\(sValue[1])' "
            + " , \(LOCATION) = '\(sValue[2])' "
            + " , \(SHOP_NAME) = '\(sValue[3])' "
            + " , \(ADDRESS) = '\(sValue[4])' "
            + " , \(POPULAR) = '\(sValue[5])' "
            + " , \(MACHINE_AMOUNT) = '\(sValue[6])' "
            + " , \(MACHINE_TYPE) = '\(sValue[7])' "
            + " , \(REMARKS) = '\(sValue[8])' "
            + " , \(LATITUDE) = '\(sValue[9])' "
            + " , \(LONGITUDE) = '\(sValue[10])' "
            + " , \(STAR) = '\(sValue[11])' "
            + " , \(CREATE_DATETIME) = '\(sValue[12])' "
            + " , \(MODIFY_DATETIME) = '\(sValue[13])' "
            + " WHERE 1 = 1 "
            + "  AND \(ADDRESS_NO) = '\(sValue[0])' "
    
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnUpdateMyLove(sMyLove: String, sAddressNo: String) -> Bool {
        let sSql = " UPDATE \(TABLE_NAME) SET "
            + "  \(MYLOVE) = '\(sMyLove)' "
            + " WHERE 1 = 1 "
            + "  AND \(ADDRESS_NO) = '\(sAddressNo)' "
        
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnGetMyLoveData() -> [ShopData] {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT  \(ADDRESS_NO) " +
            " , \(AREA) " +
            " , \(LOCATION) " +
            " , \(SHOP_NAME) " +
            " , \(ADDRESS) " +
            " , \(POPULAR) " +
            " , \(LATITUDE) " +
            " , \(LONGITUDE) " +
            " , \(MACHINE_AMOUNT) " +
            " , \(MACHINE_TYPE) " +
            " , \(REMARKS) " +
            " , \(STAR) " +
            " , \(MYLOVE) " +
            " FROM \(TABLE_NAME) " +
            " WHERE \(MYLOVE) = '1' "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        var shopDatas: [ShopData] = [ShopData]()
        while sqlite3_step(pState) == SQLITE_ROW {
            let sAddressNo = String(cString: sqlite3_column_text(pState, 0))
            let sArea = String(cString: sqlite3_column_text(pState, 1))
            let sLocation = String(cString: sqlite3_column_text(pState, 2))
            let sShopName = String(cString: sqlite3_column_text(pState, 3))
            let sAddress = String(cString: sqlite3_column_text(pState, 4))
            let sPopular = String(cString: sqlite3_column_text(pState, 5))
            let sLatitude = String(cString: sqlite3_column_text(pState, 6))
            let sLongitude = String(cString: sqlite3_column_text(pState, 7))
            let sMachineAmount = String(cString: sqlite3_column_text(pState, 8))
            let sMachineType = String(cString: sqlite3_column_text(pState, 9))
            let sRemarks = String(cString: sqlite3_column_text(pState, 10))
            let sStar = String(cString: sqlite3_column_text(pState, 11))
            let sMyLove = String(cString: sqlite3_column_text(pState, 12))
            let shopData = ShopData(sAddressNo: sAddressNo, sArea: sArea
                , sLocation: sLocation, sShopName: sShopName
                , sAddress: sAddress, sPopular: sPopular
                , sLatitude: sLatitude, sLongitude: sLongitude
                , sMachineAmount: sMachineAmount, sMachineType: sMachineType
                , sRemarks: sRemarks, sStar: sStar
                , sMyLove: sMyLove)
            shopDatas.append(shopData)
        }
        return shopDatas
    }
    
    func fnGetSearchData(sKeyWord: String, sArea: String, sLocation: String) -> [ShopData] {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT  \(ADDRESS_NO) " +
            " , \(AREA) " +
            " , \(LOCATION) " +
            " , \(SHOP_NAME) " +
            " , \(ADDRESS) " +
            " , \(POPULAR) " +
            " , \(LATITUDE) " +
            " , \(LONGITUDE) " +
            " , \(MACHINE_AMOUNT) " +
            " , \(MACHINE_TYPE) " +
            " , \(REMARKS) " +
            " , \(STAR) " +
            " , \(MYLOVE) " +
            " FROM \(TABLE_NAME) " +
            " WHERE \(SHOP_NAME) LIKE '%\(sKeyWord)%' " +
            "   AND \(AREA) LIKE '\(sArea)' " +
            "   AND \(LOCATION) LIKE '\(sLocation)' "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        var shopDatas: [ShopData] = [ShopData]()
        while sqlite3_step(pState) == SQLITE_ROW {
            let sAddressNo = String(cString: sqlite3_column_text(pState, 0))
            let sArea = String(cString: sqlite3_column_text(pState, 1))
            let sLocation = String(cString: sqlite3_column_text(pState, 2))
            let sShopName = String(cString: sqlite3_column_text(pState, 3))
            let sAddress = String(cString: sqlite3_column_text(pState, 4))
            let sPopular = String(cString: sqlite3_column_text(pState, 5))
            let sLatitude = String(cString: sqlite3_column_text(pState, 6))
            let sLongitude = String(cString: sqlite3_column_text(pState, 7))
            let sMachineAmount = String(cString: sqlite3_column_text(pState, 8))
            let sMachineType = String(cString: sqlite3_column_text(pState, 9))
            let sRemarks = String(cString: sqlite3_column_text(pState, 10))
            let sStar = String(cString: sqlite3_column_text(pState, 11))
            let sMyLove = String(cString: sqlite3_column_text(pState, 12))
            let shopData = ShopData(sAddressNo: sAddressNo, sArea: sArea
                , sLocation: sLocation, sShopName: sShopName
                , sAddress: sAddress, sPopular: sPopular
                , sLatitude: sLatitude, sLongitude: sLongitude
                , sMachineAmount: sMachineAmount, sMachineType: sMachineType
                , sRemarks: sRemarks, sStar: sStar
                , sMyLove: sMyLove)
            shopDatas.append(shopData)
        }
        return shopDatas
    }
    
    func fnGetShopData() -> [ShopData] {
        return fnGetSearchData(sKeyWord: "", sArea: "%", sLocation: "%")
    }
    
    func fnGetLocationData(sArea: String) -> [String] {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT DISTINCT \(LOCATION) " +
                    " FROM \(TABLE_NAME) " +
                    " WHERE \(AREA) LIKE '\(sArea)' "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        var datas: [String] = ["全部"]
        while sqlite3_step(pState) == SQLITE_ROW {
            let sLocation = String(cString: sqlite3_column_text(pState, 0))
            datas.append(sLocation)
        }
        return datas
    }
    
    func fnGetAreaData() -> [String] {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT DISTINCT \(AREA) " +
                    " FROM \(TABLE_NAME) "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        var datas: [String] = ["全部"]
        while sqlite3_step(pState) == SQLITE_ROW {
            let sArea = String(cString: sqlite3_column_text(pState, 0))
            datas.append(sArea)
        }
        return datas
    }
    
    func fnGetID(sAddressNo: String) -> String {
        var pState :OpaquePointer? = nil
        var sId = "-1"
        let sSql = " SELECT  \(ADDRESS_NO) " +
            " FROM \(TABLE_NAME) " +
            " WHERE \(ADDRESS_NO) = '\(sAddressNo)' " +
            " LIMIT 1 "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        
        while sqlite3_step(pState) == SQLITE_ROW {
            sId = String(cString: sqlite3_column_text(pState, 0))
            //print("Address=\(sAddressNo)")
        }
        
        sqlite3_finalize(pState)
        return sId;
    }
}

