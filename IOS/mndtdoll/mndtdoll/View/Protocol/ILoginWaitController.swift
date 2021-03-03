//
//  LoginWaitProtocol.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol ILoginWaitController {
    func fnInitView() -> Void
    func fnUpdateProgress(_ fCount: Float) -> Void
    func fnConfirm() -> Void
    func fnOpenLoginWayController() -> Void
    func fnOpenMainController() -> Void
}
