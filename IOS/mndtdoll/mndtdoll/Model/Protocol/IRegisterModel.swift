//
//  IRegisterModel.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

protocol IRegisterModel {
    var sex: String { get set }
    func fnRunRegister(sName: String, sAccount: String, sPassword: String, imgData: UIImage?, fnSuccess: @escaping (String) -> Void) -> Void
}
