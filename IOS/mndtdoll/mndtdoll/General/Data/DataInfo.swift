//
//  DataInfo.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/6.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation
class DataInfo {
    //public static let ADMOB_ID: String = "ca-app-pub-2519809314889727/4954612350"
    
    public static let ADMOB_ID: String = "ca-app-pub-3940256099942544/2934735716"

    public static let WEB_SERVICE_URL: String = "http://mndtghost.zapto.org/WebService.asmx"
    public static let WEB_SERVICE_USER_URL: String = "http://mndtghost.zapto.org/UserImage/"
    public static let WEB_SERVICE_SHOP_URL: String = "http://mndtghost.zapto.org/DollImage/shop_"
    
    public static func fnGetShopURLString(_ addressNo: String) -> String {
        return WEB_SERVICE_SHOP_URL + addressNo + ".jpg"
    }
    
    public static func fnGetThemeURLString(sPlateId: String, sThemeId: String, sIndex: String) -> String {
        return "http://mndtghost.zapto.org/Theme/\(sPlateId)\(sThemeId)/\(sIndex).jpg"
    }
}
