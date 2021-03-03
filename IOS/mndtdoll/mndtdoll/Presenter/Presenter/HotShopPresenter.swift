//
//  HotShopPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/16.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

class HotShopPresenter: IHotShopPresenter {
    private var g_hotShopController: IHotShopController!
    private var g_shopDatas: [ShopData]!
    var View: IHotShopController {
        get {
            return g_hotShopController
        }
    }
    
    var shopDatas: [ShopData] {
        get {
            return g_shopDatas
        }
    }
    
    init(_ hotShopController: IHotShopController) {
        g_hotShopController = hotShopController
        g_shopDatas = [ShopData]()
    }
    
    func fnStart() -> Void {
        fnSelectShopSql()
    }
    
    private func fnSelectShopSql() -> Void {
        let sqlShop: DollShopSql = DollShopSql()
        if sqlShop.fnConnection() {
            g_shopDatas = sqlShop.fnGetShopData()
            print("\(g_shopDatas.count)")
        }
    }
    

}
