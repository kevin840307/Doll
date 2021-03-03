//
//  IShopDataContentPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/23.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

protocol IShopDataContentPresenter {
    var View: IShopDataContentController { get }
    func fnStart() -> Void
    func fnSetImage(imgView: UIImageView, url: String) -> Void
}
