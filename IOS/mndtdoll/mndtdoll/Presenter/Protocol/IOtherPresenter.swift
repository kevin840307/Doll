//
//  IOtherPresenter.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/14.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

protocol IOtherPresenter {
    var View: IOtherController { get }
    func fnStart() -> Void
    func fnAction(sCommamd: String) -> Void
    func fnSetImage(_ imgView: UIImageView) -> Void
}
