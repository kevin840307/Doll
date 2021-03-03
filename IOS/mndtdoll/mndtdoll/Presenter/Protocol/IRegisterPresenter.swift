//
//  IRegisterPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

protocol IRegisterPresenter {
    var View: IRegisterController { get }
    func fnStart() -> Void
    func fnRegister(sName: String, sAccount: String, sPassword: String, sCheckPass: String, imgData: UIImage?) -> Void
    func fnSetSex(sSex: String) -> Void
}
