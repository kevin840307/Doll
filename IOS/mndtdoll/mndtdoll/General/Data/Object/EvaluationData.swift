//
//  EvaluationData.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/24.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import Foundation

class EvaluationData {
    
    private var g_sAcount: String!
    private var g_sStar: String!
    private var g_sMessage: String!
    private var g_sModifyDateTime: String!
    
    init(sAcount: String, sStar: String, sMessage: String, sModifyDateTime: String) {
        g_sAcount = sAcount
        g_sStar = sStar
        g_sMessage = sMessage
        g_sModifyDateTime = sModifyDateTime
    }
    
    var Acount: String {
        get {
            return g_sAcount
        }
    }
    
    var Star: String {
        get {
            return g_sStar
        }
    }
    
    var Message: String {
        get {
            return g_sMessage
        }
    }
    
    var ModifyDateTime: String {
        get {
            return g_sModifyDateTime
        }
    }
}
