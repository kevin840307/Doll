//
//  SoapEngine.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class SoapEngine {
    

    var URL = ""

    init(sUrl: String) {
        URL = sUrl
    }
    
    func fnGetRequest(sFunctions: String, soapData: SoapData) -> NSMutableURLRequest {
        var soapMsg = "<?xml version='1.0' encoding='utf-8'?>  "
            + "  <soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>  "
            + "    <soap:Body>  "
            + "      <\(sFunctions) xmlns='http://tempuri.org/' >  "
        
        for(sKey, sValue) in soapData.fnGetDatas() {
            soapMsg += " <\(sKey)>\(sValue)</\(sKey)> "
        }
        
        soapMsg += "    </\(sFunctions)>  "
            + "     </soap:Body>  "
            + " </soap:Envelope>"
        return fnRequst(soapMsg)
    }
    
    func fnGetRequest(sFunctions: String) -> NSMutableURLRequest {
        let soapMsg = "<?xml version='1.0' encoding='utf-8'?>  "
            + "  <soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>  "
            + "    <soap:Body>  "
            + "      <\(sFunctions) xmlns='http://tempuri.org/' >  "
            + "    </\(sFunctions)>  "
            + "     </soap:Body>  "
            + " </soap:Envelope>"
        return fnRequst(soapMsg)
    }
    
    private func fnRequst(_ soapMsg: String) -> NSMutableURLRequest {
        let iCount = soapMsg.count
        let is_URL: String = URL
        let lobjRequest = NSMutableURLRequest(url: NSURL(string: is_URL)! as URL)
    
        lobjRequest.httpMethod = "POST"
        lobjRequest.httpBody = soapMsg.data(using: String.Encoding.utf8)
        lobjRequest.addValue("text/xml; charset=utf-8", forHTTPHeaderField: "Content-Type")
        lobjRequest.addValue(String(iCount), forHTTPHeaderField: "Content-Length")
        return lobjRequest
    }
    
}
