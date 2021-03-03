//
//  ILoginController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/12.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol ILoginController {
    func fnInitView() -> Void
    func fnBottomMsg(_ sMsg: String, fnSuccess: @escaping () -> Void) -> Void
    func fnBottomMsg(_ sMsg: String) -> Void
    func fnCloseController() -> Void
    func fnOpenMainController() -> Void
}
