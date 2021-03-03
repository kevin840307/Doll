//
//  ILoginPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol ILoginPresenter {
    var View: ILoginController { get }
    func fnStart() -> Void
    func fnLogin(sAccount: String, sPassword: String) -> Void
}
