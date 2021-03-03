//
//  TableViewCellStyle3.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/28.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class TableViewCellStyle3: UITableViewCell {
    @IBOutlet weak var g_imgUser: UIImageView!
    @IBOutlet weak var g_labUserName: UILabel!
    @IBOutlet weak var g_labContent: UILabel!
    @IBOutlet weak var g_labDateTime: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        g_imgUser.layer.masksToBounds = true
        g_imgUser.layer.cornerRadius = self.g_imgUser.bounds.size.width / 2
    }
}
