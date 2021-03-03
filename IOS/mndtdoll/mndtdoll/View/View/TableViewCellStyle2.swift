//
//  TableViewCellStyle2.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/27.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class TableViewCellStyle2: UITableViewCell {
    @IBOutlet weak var g_imgUser: UIImageView!
    @IBOutlet weak var g_imgIcon: UIImageView!
    @IBOutlet weak var g_labUserName: UILabel!
    @IBOutlet weak var g_labTitle: UILabel!
    @IBOutlet weak var g_labContent: UILabel!
    @IBOutlet weak var g_labDateTime: UILabel!
    @IBOutlet weak var g_labMsgCount: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        g_imgUser.layer.masksToBounds = true
        g_imgUser.layer.cornerRadius = self.g_imgUser.bounds.size.width / 2
    }
}


