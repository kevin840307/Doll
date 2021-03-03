//
//  DollShopEvaluation.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/24.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import Foundation
class DollShopEvaluation: SqlEngineProtocol {
    var g_pDBEngine: OpaquePointer?
    public let TABLE_NAME = "DOLLEvaluation";
    public let ACCOUNT = "account";
    public let ADDRESS_NO = "address_no";
    public let STAR = "star";
    public let MESSAGE = "message";
    public let CREATE_DATETIME = "create_datetime";
    public let MODIFY_DATETIME = "modify_datetime";

    func fnConnection() -> Bool {
        return fnConnection(&g_pDBEngine)
    }
    
    func fnCreate() -> Bool {
        let sSql = "CREATE TABLE IF NOT EXISTS \(TABLE_NAME)("
            + " ID INTEGER PRIMARY KEY AUTOINCREMENT "
            + " , \(ACCOUNT) TEXT "
            + " , \(ADDRESS_NO) TEXT "
            + " , \(STAR) TEXT "
            + " , \(MESSAGE) TEXT "
            + " , \(CREATE_DATETIME) TEXT "
            + " , \(MODIFY_DATETIME) TEXT) "
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnInsert(sData: [String]) -> Bool {
        if fnGetID(sAccount: sData[0], sAddressNo: sData[1]) == "-1" {
            let sSql = " INSERT INTO \(TABLE_NAME)( "
                + "  \(ACCOUNT) "
                + " , \(ADDRESS_NO) "
                + " , \(STAR) "
                + " , \(MESSAGE) "
                + " , \(CREATE_DATETIME) "
                + " , \(MODIFY_DATETIME)) "
                + " VALUES ( "
                + "  '\(sData[0])' "
                + " , '\(sData[1])' "
                + " , '\(sData[2])' "
                + " , '\(sData[3])' "
                + " , '\(sData[4])' "
                + " , '\(sData[5])') "
            return fnExecute(&g_pDBEngine, sSql: sSql)
        } else {
            return fnUpdate(sValue: sData)
        }
    }
    
    func fnDelete(sKeyValue: [String]) -> Bool {
        let sSql = " DELETE FROM \(TABLE_NAME) "
            + " WHERE 1 = 1 "
            + "     AND \(ACCOUNT) = '\(sKeyValue[0])' "
            + "     AND \(ADDRESS_NO) = '\(sKeyValue[1])' "
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnUpdate(sValue: [String]) -> Bool {
        let sSql = " UPDATE \(TABLE_NAME) SET "
            + "  \(STAR) = '\(sValue[2])' "
            + " , \(MESSAGE) = '\(sValue[3])' "
            + " , \(MODIFY_DATETIME) = '\(sValue[4])' "
            + " WHERE 1 = 1 "
            + "  AND \(ACCOUNT) = '\(sValue[0])' "
            + "  AND \(ADDRESS_NO) = '\(sValue[1])' "
        
        return fnExecute(&g_pDBEngine, sSql: sSql)
    }
    
    func fnGetEvalData(sAccount: String, sAddressNo: String) -> EvaluationData? {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT \(ACCOUNT)" +
            " , \(STAR) " +
            " , \(MESSAGE) " +
            " , \(MODIFY_DATETIME) " +
            " FROM \(TABLE_NAME) " +
            " WHERE \(ACCOUNT) LIKE '\(sAccount)' " +
            "   AND \(ADDRESS_NO) = '\(sAddressNo)' "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        while sqlite3_step(pState) == SQLITE_ROW {
            let sAcount = String(cString: sqlite3_column_text(pState, 0))
            let sStar = String(cString: sqlite3_column_text(pState, 1))
            let sMessage = String(cString: sqlite3_column_text(pState, 2))
            let sModifyDateTime = String(cString: sqlite3_column_text(pState, 3))
            
            let evalData = EvaluationData(sAcount: sAcount, sStar: sStar
                , sMessage: sMessage, sModifyDateTime: sModifyDateTime)
            return evalData
        }
        
        return nil
    }
    
    func fnGetAllEvalData(sAccount: String, sAddressNo: String) -> [EvaluationData] {
        var pState :OpaquePointer? = nil
        let sSql = " SELECT \(ACCOUNT)" +
            " , \(STAR) " +
            " , \(MESSAGE) " +
            " , \(MODIFY_DATETIME) " +
            " FROM \(TABLE_NAME) " +
        " WHERE \(ACCOUNT) LIKE '\(sAccount)' " +
        "   AND \(ADDRESS_NO) = '\(sAddressNo)' "
        
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        var evalDatas: [EvaluationData] = [EvaluationData]()
        while sqlite3_step(pState) == SQLITE_ROW {
            let sAcount = String(cString: sqlite3_column_text(pState, 0))
            let sStar = String(cString: sqlite3_column_text(pState, 1))
            let sMessage = String(cString: sqlite3_column_text(pState, 2))
            let sModifyDateTime = String(cString: sqlite3_column_text(pState, 3))
            
            let evalData = EvaluationData(sAcount: sAcount, sStar: sStar
                , sMessage: sMessage, sModifyDateTime: sModifyDateTime)
            evalDatas.append(evalData)
        }
        
        
        return evalDatas
    }
    
    func fnGetID(sAccount: String, sAddressNo: String) -> String {
        var pState :OpaquePointer? = nil
        var sId = "-1"
        let sSql = " SELECT ID " +
            " FROM \(TABLE_NAME) " +
            " WHERE \(ACCOUNT) = '\(sAccount)' " +
            "   AND \(ADDRESS_NO) = '\(sAddressNo)' " +
            " LIMIT 1 "
        sqlite3_prepare_v2(g_pDBEngine, sSql, -1, &pState, nil)
        
        while sqlite3_step(pState) == SQLITE_ROW {
            sId = String(cString: sqlite3_column_text(pState, 0))
        }
        print("sId=\(sId)")
        sqlite3_finalize(pState)
        return sId;
    }
    
}
