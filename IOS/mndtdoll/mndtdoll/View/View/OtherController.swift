//
//  OtherController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class OtherController:  UIViewController, IOtherController, UITableViewDelegate, UITableViewDataSource {
    var g_otherPresenter: OtherPresenter!
    var g_viewTableHeader: UIView!
    var g_imgLogoView: UIImageView!
    var g_labTitle: UILabel!
    var g_tableView: UITableView!
    var g_sDatas = [["個人資料", "我的附近", "聊天室", "討論區"]
        ,["粉絲專頁", "抓寶龍社團", "娃娃機夾娃高手交流區"]
        ,["登出"]
    ]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fnInit()
    }
    
    func fnInitView() -> Void {
        fnInitImageView()
        fnInitLabel()
        fnInitHeader()
        fnInitTableView()
    }
    
    func fnInit() -> Void {
        g_otherPresenter = OtherPresenter(self)
        g_otherPresenter.fnStart()
        g_otherPresenter.fnSetImage(g_imgLogoView)
    }
    
    func fnInitImageView() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_imgLogoView = UIImageView()
        g_imgLogoView.frame = CGRect(x: 0, y: 0, width: 100, height: 100)
        g_imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 60)
        g_imgLogoView.image = UIImage(named: "logo.png")
        g_imgLogoView.layer.masksToBounds = true
        g_imgLogoView.layer.cornerRadius = g_imgLogoView.frame.width / 2
    }
    
    func fnInitLabel() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_labTitle = UILabel(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.9, height: 20))
        g_labTitle.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 130)
        g_labTitle.textAlignment = NSTextAlignment.center
        if UserData.fnGetLoginType() == "2" {
            g_labTitle.text = UserData.fnGetName()
        } else {
            g_labTitle.text = "訪客"
        }
    }
    
    func fnInitHeader() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_viewTableHeader = UIView()
        g_viewTableHeader.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 140)
        g_viewTableHeader.addSubview(g_imgLogoView)
        g_viewTableHeader.addSubview(g_labTitle)
    }
    
    func fnInitTableView() {
        let fullScreenSize = UIScreen.main.bounds.size
        g_tableView = UITableView(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width,height: fullScreenSize.height - 49)
            ,style: .grouped)
        g_tableView.register(UITableViewCell.self, forCellReuseIdentifier: "Cell")
        g_tableView.delegate = self
        g_tableView.dataSource = self
        g_tableView.separatorStyle = .singleLine
//       g_tableView.separatorInset = UIEdgeInsetsMake(50, 20, 100, 20)
        g_tableView.allowsSelection = true
        g_tableView.allowsMultipleSelection = false
        g_tableView.tableHeaderView = g_viewTableHeader
        self.view.addSubview(g_tableView)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return g_sDatas[section].count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as UITableViewCell
        
        cell.accessoryType = .disclosureIndicator
        
        if let myLabel = cell.textLabel {
            myLabel.text = "\(g_sDatas[indexPath.section][indexPath.row])"
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let sCommand = g_sDatas[indexPath.section][indexPath.row]
        g_otherPresenter.fnAction(sCommamd: sCommand)
    }
    
//    func tableView(_ tableView: UITableView, accessoryButtonTappedForRowWith indexPath: IndexPath) {
//        let name = g_sDatas[indexPath.section][indexPath.row]
//        print("按下的是 \(name) 的 detail")
//    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return g_sDatas.count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        let title = section == 0 ? "中心" : section == 1 ? "連結" : "其他"
        return title
    }
    
    func fnCloseController() -> Void {
        ViewController.PAGE_TYPE = 1
        self.dismiss(animated: true, completion: nil)
    }
    
    func fnOpenThemeController() -> Void {
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(ForumController(), animated: true)
        self.hidesBottomBarWhenPushed = false
    }
}
