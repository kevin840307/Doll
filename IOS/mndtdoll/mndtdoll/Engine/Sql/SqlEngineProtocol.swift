//
//  SqlEngineProtocol.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation


protocol SqlEngineProtocol {
    var g_pDBEngine :OpaquePointer? { set get }
    func fnConnection() -> Bool
    func fnCreate() -> Bool
    func fnInsert(sData: [String]) -> Bool
    func fnDelete(sKeyValue: [String]) -> Bool
    func fnUpdate(sValue: [String]) -> Bool
}

extension SqlEngineProtocol {
    func fnConnection(_ pDBEngine: inout OpaquePointer?) -> Bool {
        // Document的所有路徑
        let urlAllPaths = FileManager.default.urls(for: .documentDirectory,
                                                   in: .userDomainMask)
        let sSqlPath = urlAllPaths[urlAllPaths.count - 1].absoluteString
            + "sqlite3.db"
        
        if sqlite3_open(sSqlPath, &pDBEngine) == SQLITE_OK {
            print("資料庫連線成功")
            return true
        } else {
            print("資料庫連線失敗")
            return false
        }
    }

   
    func fnExecute(_ pDBEngine: inout OpaquePointer?, sSql: String) -> Bool {
        var pState :OpaquePointer? = nil
        if sqlite3_prepare_v2(
            pDBEngine, sSql, -1, &pState, nil) == SQLITE_OK{
            if sqlite3_step(pState) == SQLITE_DONE {
                sqlite3_finalize(pState)
                return true
            }
            sqlite3_finalize(pState)
        }
        return false
    }
}
