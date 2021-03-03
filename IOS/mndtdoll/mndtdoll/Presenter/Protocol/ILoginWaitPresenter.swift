//
//  ILoginWaitPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol ILoginWaitPresenter {
    var View: ILoginWaitController { get }
    func fnStart() -> Void
    func fnDowloadDatas() -> Void
    func fnDowloadSuccess(_ sDatas: inout [[String]]) -> Void
    func fnCheckUpdate() -> Void
    func fnCheckSuccess(sCheck: String) -> Void
    func fnOpenController() -> Void
}
