//
//  XmlEngine.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class XmlEngine {
    
    private var g_data: Data
    
    init(_ data: Data) {
        g_data = data
    }
    
    func fnGetAllData() -> [String] {
        let xmlDelegate = XMLDelegate()
        let xmlParser = XMLParser(data: g_data)
        xmlParser.delegate = xmlDelegate
        xmlParser.parse()
        xmlParser.shouldResolveExternalEntities = true
        return xmlDelegate.fnGetDatas()
    }
    
    func fnGetSplitData(iSize: Int) -> [[String]] {
        let sAllDatas = fnGetAllData()
        var sDatas: [[String]] = [[String]]()
        var iIndex: Int = 0
        
        while iIndex < sAllDatas.count {
            var sValues: [String] = [String]()
            for iPos in stride(from: iIndex, to: iIndex + iSize, by: 1) {
                sValues.append(sAllDatas[iPos])
            }
            sDatas.append(sValues)
            iIndex += iSize
        }
        return sDatas
    }
}
