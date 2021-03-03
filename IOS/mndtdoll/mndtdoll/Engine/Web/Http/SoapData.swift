//
//  SoapData.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class SoapData {
    
    private var g_sAvgDatas: [String: String] = [:]
    
    func fnAdd(sKey: String, sValue: String) -> Void {
        g_sAvgDatas[sKey] = sValue
    }
    
    func fnGetDatas() -> [String: String] {
        return g_sAvgDatas
    }
}
