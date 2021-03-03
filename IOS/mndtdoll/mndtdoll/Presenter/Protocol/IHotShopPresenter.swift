//
//  IHotShopPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/16.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol IHotShopPresenter {
    var View: IHotShopController { get }
    var shopDatas: [ShopData] { get }
    func fnStart() -> Void
}
