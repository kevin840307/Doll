//
//  UICollectionViewCell1.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/26.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit
class UICollectionViewCell1: UICollectionViewCell {
    var colorview = UIView()
    var imageview = UIImageView()
    let WIDTH = UIScreen.main.bounds.size.width
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        imageview = UIImageView(frame: CGRect(x: 5,
                                              y: 5,
                                              width: 50,
                                              height: 50))
        imageview.contentMode = .scaleToFill
        contentView.addSubview(imageview)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
