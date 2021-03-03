//
//  ILoginWaitModel.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import Foundation

protocol ILoginWaitModel {
    func fnRunUpdate(fnSuccess: @escaping (String) -> Void) -> Void
    func fnRunDowloadDatas(fnSuccess: @escaping (inout [[String]]) -> Void) -> Void
}
