//
//  ShopDataContentPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/23.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class ShopDataContentPresenter: IShopDataContentPresenter {
    private var g_shopDataContentController: IShopDataContentController!
    var View: IShopDataContentController {
        get {
            return g_shopDataContentController
        }
    }
    
    init(_ shopDataContentController: IShopDataContentController) {
        g_shopDataContentController = shopDataContentController
    }
    
    func fnStart() {
        
    }
    
    func fnSetImage(imgView: UIImageView, url: String) -> Void {
        DispatchQueue.global().async {
            Image().fnUrlSetImage(sUrl: url, imgData: imgView)
        }
        
    }
    
}
