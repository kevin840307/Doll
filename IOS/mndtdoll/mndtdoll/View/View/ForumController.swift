//
//  ForumController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/25.
//  Copyright © 2019年 Ghost. All rights reserved.
//


import UIKit

class ForumController:  UIViewController{
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        //self.navigationItem.title = "1111"
    }
    
    override func viewDidAppear(_ animated: Bool) {
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInitView() {
        let dollColor = DollColor()
        let sizeFullScreen = UIScreen.main.bounds.size
        
        let newTheme = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.5 - 30, height: 50))
        newTheme.center = CGPoint(x: sizeFullScreen.width * 0.25, y: 100)
        newTheme.setTitleColor(UIColor.darkGray,for: .normal)
        newTheme.setTitle("新增主題", for: .normal)
        newTheme.layer.cornerRadius = 10
        newTheme.layer.borderWidth = 2
        newTheme.layer.borderColor = dollColor.RED.cgColor
        newTheme.addTarget(nil, action: #selector(self.fnOpenInsert), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(newTheme)
        
        let theme0 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.5 - 30, height: 50))
        theme0.center = CGPoint(x: sizeFullScreen.width * 0.75, y: 100)
        theme0.setTitleColor(UIColor.darkGray,for: .normal)
        theme0.setTitle("我的主題", for: .normal)
        theme0.layer.cornerRadius = 10
        theme0.layer.borderWidth = 2
        theme0.layer.borderColor = dollColor.BLUE.cgColor
        theme0.tag = 6

        theme0.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme0)
        

        let theme1 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        theme1.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 160)
        theme1.backgroundColor = dollColor.RED
        theme1.tintColor = UIColor.white
        theme1.setTitle("閒聊區", for: .normal)
        theme1.layer.cornerRadius = 10
        theme1.tag = 1
        theme1.addTarget(self, action: #selector(self.fnRedDown(sender:)), for: .touchDown)
        theme1.addTarget(self, action: #selector(self.fnRedUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme1.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme1)
        
        let theme2 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        theme2.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 220)
        theme2.backgroundColor = dollColor.BLUE
        theme2.tintColor = UIColor.white
        theme2.setTitle("娃娃出租區", for: .normal)
        theme2.layer.cornerRadius = 10
        theme2.tag = 2
        theme2.addTarget(self, action: #selector(self.fnBlueDown(sender:)), for: .touchDown)
        theme2.addTarget(self, action: #selector(self.fnBlueUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme2.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme2)
        
        let theme3 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        theme3.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 280)
        theme3.backgroundColor = dollColor.GREEN
        theme3.tintColor = UIColor.white
        theme3.setTitle("夾物交易區", for: .normal)
        theme3.layer.cornerRadius = 10
        theme3.tag = 3
        theme3.addTarget(self, action: #selector(self.fnGreenUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme3.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme3.addTarget(self, action: #selector(self.fnGreenDown(sender:)), for: .touchDown)
        //theme1.addTarget(nil, action: #selector(self.fnOpenVisitorsController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme3)
        
        let theme4 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        theme4.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 340)
        theme4.backgroundColor = dollColor.ORANGE
        theme4.tintColor = UIColor.white
        theme4.setTitle("問題回報區", for: .normal)
        theme4.layer.cornerRadius = 10
        theme4.tag = 4
        theme4.addTarget(self, action: #selector(self.fnOrangeUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme4.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme4.addTarget(self, action: #selector(self.fnOrangeDown(sender:)), for: .touchDown)
        //theme1.addTarget(nil, action: #selector(self.fnOpenVisitorsController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme4)
        
        let theme5 = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        theme5.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 400)
        theme5.backgroundColor = UIColor.darkGray
        theme5.tintColor = UIColor.white
        theme5.setTitle("夾點分享區", for: .normal)
        theme5.layer.cornerRadius = 10
        theme5.tag = 5
        theme5.addTarget(self, action: #selector(self.fnBlackUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme5.addTarget(self, action: #selector(self.fnAction(sender:)), for: [.touchUpInside, .touchUpOutside])
        theme5.addTarget(self, action: #selector(self.fnBlackDown(sender:)), for: .touchDown)
        //theme1.addTarget(nil, action: #selector(self.fnOpenVisitorsController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(theme5)
    }
    
    @objc func fnRedDown(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.RED_T
    }
    
    @objc func fnRedUp(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.RED
    }
    
    @objc func fnBlueDown(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.BLUE_T
    }
    
    @objc func fnBlueUp(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.BLUE
    }
    
    @objc func fnGreenDown(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.GREEN_T
    }
    
    @objc func fnGreenUp(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.GREEN
    }
    
    @objc func fnOrangeDown(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.ORANGE_T
    }
    
    @objc func fnOrangeUp(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.ORANGE
    }
    
    @objc func fnBlackDown(sender: UIButton) {
        sender.backgroundColor = UIColor.black
    }
    
    @objc func fnBlackUp(sender: UIButton) {
        sender.backgroundColor = UIColor.darkGray
    }
    
    @objc func fnAction(sender: UIButton) {
        let themeController = ThemeController()
        themeController.fnSetType(sender.tag)
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(themeController, animated: true)
    }
    
    @objc func fnOpenInsert() {
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(ThemeInsertController(), animated: true)
    }
}
