//
//  XMLDelegate.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class XMLDelegate: NSObject, XMLParserDelegate {
    private var g_sElementName: String = "string"
    private var g_sData: [String] = [String]()
    
    func parser(_ parser: XMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?) {
        g_sElementName = elementName
    }
    
    func parser(_ parser: XMLParser, foundCharacters string: String) {

        if g_sElementName == "" {
            g_sData[g_sData.count - 1].append(string)
        } else {
            g_sData.append(string)
        }
        g_sElementName = ""
    }
    
    func fnGetDatas() -> [String] {
        return g_sData
    }
}
