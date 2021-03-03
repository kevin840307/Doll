//
//  LoginWayController.swift
//  doll
//
//  Created by Ghost on 2017/12/5.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class LoginWayController: UIViewController {
    
    var g_btnVisitors: UIButton!
    var g_btnLogin: UIButton!
    var g_btnRegister: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
    }
    
    override func viewDidAppear(_ animated: Bool) {

    }
    
    override func viewWillDisappear(_ animated: Bool) {
        //super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
   
    func fnInitView() -> Void {
        self.view.backgroundColor = UIColor.white
        self.title = "抓寶龍"
        self.navigationController?.navigationBar.titleTextAttributes = [NSAttributedStringKey.foregroundColor: DollColor().BLUE]
        fnInitImage()
        fnInitAdmob()
        fnButton()
    }

    
    func fnInitImage() -> Void {
        let sizeFullScreen = UIScreen.main.bounds.size
        let imgLogoView: UIImageView = UIImageView()
        imgLogoView.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.5, height: sizeFullScreen.height * 0.3)
        imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.3)
        imgLogoView.image = UIImage(named: "logo.png")
        self.view.addSubview(imgLogoView)
    }
    
    func fnInitAdmob() -> Void {
        let admobView = AdmobView()
        admobView.fnSetBannerView(self)
    }
    
    func fnButton() -> Void {
        let dollColor = DollColor()
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_btnVisitors = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnVisitors.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 205)
        g_btnVisitors.backgroundColor = dollColor.BLUE
        g_btnVisitors.tintColor = UIColor.white
        g_btnVisitors.setTitle("訪客登入", for: .normal)
        g_btnVisitors.layer.cornerRadius = 10
        g_btnVisitors.addTarget(nil, action: #selector(self.fnVisitorsDown), for: .touchDown)
        g_btnVisitors.addTarget(nil, action: #selector(self.fnOpenVisitorsController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnVisitors)
        
        
        g_btnLogin = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnLogin.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 145)
        g_btnLogin.backgroundColor = dollColor.RED
        g_btnLogin.tintColor = UIColor.white
        g_btnLogin.setTitle("帳號登入", for: .normal)
        g_btnLogin.layer.cornerRadius = 10
        g_btnLogin.addTarget(nil, action: #selector(self.fnLoginDown), for: .touchDown)
        g_btnLogin.addTarget(nil, action: #selector(self.fnOpenLoginController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnLogin)
        
        g_btnRegister = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnRegister.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 85)
        g_btnRegister.backgroundColor = dollColor.ORANGE
        g_btnRegister.tintColor = UIColor.white
        g_btnRegister.setTitle("註冊", for: .normal)
        g_btnRegister.layer.cornerRadius = 10
        g_btnRegister.addTarget(nil, action: #selector(self.fnRegisterDown), for: .touchDown)
        g_btnRegister.addTarget(nil, action: #selector(self.fnOpenRegisterController), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnRegister)
    }
    
    @objc func fnOpenVisitorsController() {
        let dollColor = DollColor()
        g_btnVisitors.backgroundColor = dollColor.BLUE
        fnOpenMainController()
    }
    
    @objc func fnVisitorsDown() {
        let dollColor = DollColor()
        g_btnVisitors.backgroundColor = dollColor.BLUE_T
    }

    @objc func fnOpenLoginController() {
        let dollColor = DollColor()
        g_btnLogin.backgroundColor = dollColor.RED
        self.navigationController?.pushViewController(LoginController(), animated: true)

    }
    
    @objc func fnLoginDown() {
        let dollColor = DollColor()
        g_btnLogin.backgroundColor = dollColor.RED_T
    }
    
    @objc func fnOpenRegisterController() {
        let dollColor = DollColor()
        g_btnRegister.backgroundColor = dollColor.ORANGE
        self.navigationController?.pushViewController(RegisterController(), animated: true)
    }
    
    @objc func fnRegisterDown() {
        let dollColor = DollColor()
        g_btnRegister.backgroundColor = dollColor.ORANGE_T
    }
    
    func fnOpenMainController() -> Void {
        UserData.fnSetLoginType(sType: "1")
        self.dismiss(animated: false, completion:nil)
        ViewController.PAGE_TYPE = 2
    }
    
    
}
