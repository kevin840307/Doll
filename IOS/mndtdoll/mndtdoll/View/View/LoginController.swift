//
//  LoginController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/12.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class LoginController: UIViewController, ILoginController {
    
    var g_loginPresenter: ILoginPresenter!
    private var g_btnLogin: UIButton!
    private var g_btnCancel: UIButton!
    private var g_fieldAccount: UITextField!
    private var g_fieldPassword: UITextField!
    private var g_fieldDelegateAccount: TextFieldDelegate!
    private var g_fieldDelegatePassword: TextFieldDelegate!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fnInit()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() {
        g_loginPresenter = LoginPresenter(self)
        g_loginPresenter.fnStart()
    }
    
    func fnInitView() -> Void {
        self.view.backgroundColor = UIColor.white
        fnInitAdmob()
        fnInitImage()
        fnInitTextField()
        fnInitButton()
    }
    
    private func fnInitAdmob() -> Void {
        let admobView = AdmobView()
        admobView.fnSetBannerView(self)
    }
    
    private func fnInitImage() -> Void {
        let sizeFullScreen = UIScreen.main.bounds.size
        let imgLogoView: UIImageView = UIImageView()
        imgLogoView.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.5, height: sizeFullScreen.height * 0.3)
        imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.3)
        imgLogoView.image = UIImage(named: "logo.png")
        self.view.addSubview(imgLogoView)
    }
    
    private func fnInitTextField() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_fieldPassword = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldPassword.center = CGPoint(x: sizeFullScreen.width * 0.5, y:  sizeFullScreen.height - 205)
        g_fieldPassword.placeholder = "輸入密碼"
        g_fieldPassword.clearButtonMode = .whileEditing
        g_fieldPassword.returnKeyType = .done
        g_fieldPassword.textColor = UIColor.lightGray
        g_fieldPassword.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldPassword.layer.borderWidth = 1
        g_fieldPassword.layer.cornerRadius = 10
        g_fieldPassword.leftViewMode = .always
        g_fieldPassword.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldPassword.backgroundColor = UIColor.white
        g_fieldDelegatePassword = TextFieldDelegate(viewController: self, fMoveOffset: sizeFullScreen.height * 0.5 - 155)
        g_fieldPassword.delegate = g_fieldDelegatePassword
        g_fieldPassword.isSecureTextEntry = true
        self.view.addSubview(g_fieldPassword)
        
        g_fieldAccount = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldAccount.center = CGPoint(x: sizeFullScreen.width * 0.5, y:  sizeFullScreen.height - 265)
        g_fieldAccount.placeholder = "請輸入帳號"
        g_fieldAccount.clearButtonMode = .whileEditing
        g_fieldAccount.returnKeyType = .done
        g_fieldAccount.textColor = UIColor.lightGray
        g_fieldAccount.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldAccount.layer.borderWidth = 1
        g_fieldAccount.layer.cornerRadius = 10
        g_fieldAccount.leftViewMode = .always
        g_fieldAccount.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldAccount.backgroundColor = UIColor.white
        g_fieldDelegateAccount = TextFieldDelegate(viewController: self, fMoveOffset: sizeFullScreen.height * 0.5 - 215)
        g_fieldAccount.delegate = g_fieldDelegateAccount
        self.view.addSubview(g_fieldAccount)
    }
    
    private func fnInitButton() -> Void {
        let dollColor = DollColor()
        let sizeFullScreen = UIScreen.main.bounds.size

        g_btnCancel = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnCancel.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 85)
        g_btnCancel.backgroundColor = dollColor.RED
        g_btnCancel.tintColor = UIColor.white
        g_btnCancel.setTitle("取消", for: .normal)
        g_btnCancel.layer.cornerRadius = 10
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancelDown), for: .touchDown)
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancel), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnCancel)
        
        g_btnLogin = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnLogin.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 145)
        g_btnLogin.backgroundColor = dollColor.BLUE
        g_btnLogin.tintColor = UIColor.white
        g_btnLogin.setTitle("登入", for: .normal)
        g_btnLogin.layer.cornerRadius = 10
        g_btnLogin.addTarget(nil, action: #selector(self.fnLoginDown), for: .touchDown)
        g_btnLogin.addTarget(nil, action: #selector(self.fnLogin), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnLogin)
    }
    
    @objc func fnLogin() {
        let dollColor = DollColor()
        g_btnLogin.backgroundColor = dollColor.BLUE
        self.g_loginPresenter.fnLogin(sAccount: g_fieldAccount.text!, sPassword: g_fieldPassword.text!)
    }
    
    @objc func fnLoginDown() {
        let dollColor = DollColor()
        g_btnLogin.backgroundColor = dollColor.BLUE_T
    }
    
    @objc func fnCancel() {
        let dollColor = DollColor()
        g_btnCancel.backgroundColor = dollColor.RED
        fnCloseController()
    }
    
    @objc func fnCancelDown() {
        let dollColor = DollColor()
        g_btnCancel.backgroundColor = dollColor.RED_T
    }
    
    func fnBottomMsg(_ sMsg: String, fnSuccess: @escaping () -> Void) {
        let alertController = UIAlertController(title: "訊息", message: sMsg, preferredStyle: .actionSheet)
        let okAction = UIAlertAction(
            title: "確認",
            style: .default,
            handler: {
                (action: UIAlertAction!) -> Void in
                fnSuccess()
        })
        alertController.addAction(okAction)
        self.present(alertController, animated: true, completion: nil)
    }
    
    func fnBottomMsg(_ sMsg: String) {
        let alertController = UIAlertController(title: "訊息", message: sMsg, preferredStyle: .actionSheet)
        let okAction = UIAlertAction(
            title: "確認",
            style: .default,
            handler: nil)
        alertController.addAction(okAction)
        self.present(alertController, animated: true, completion: nil)
    }
    
    func fnCloseController() -> Void {
        self.navigationController?.popViewController(animated: true)
    }
    
    func fnOpenMainController() -> Void {
        self.dismiss(animated: false, completion:nil)
        ViewController.PAGE_TYPE = 2
    }
}
