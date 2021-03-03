//
//  IRegisterController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol IRegisterController {
    func fnInitView() -> Void
    func fnSetMan() -> Void
    func fnSetWoman() -> Void
    func fnBottomMsg(_ sMsg: String, fnSuccess: @escaping () -> Void) -> Void
    func fnBottomMsg(_ sMsg: String) -> Void
    func fnCloseController() -> Void
}
