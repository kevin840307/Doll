//
//  RegisterController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class RegisterController: UIViewController, IRegisterController {
    
    var g_registerPresenter: IRegisterPresenter!
    var g_scrollView: UIScrollView!
    var g_imgLogoView: UIImageView!
    var g_btnPhoto: UIButton!
    var g_btnMan: UIButton!
    var g_btnWoman: UIButton!
    var g_btnRegister: UIButton!
    var g_btnCancel: UIButton!
    var g_fieldName: UITextField!
    var g_fieldAccount: UITextField!
    var g_fieldPassword: UITextField!
    var g_fieldCheckPass: UITextField!
    var g_fieldDelegateName: TextFieldDelegate!
    var g_fieldDelegateAccount: TextFieldDelegate!
    var g_fieldDelegatePassword: TextFieldDelegate!
    var g_fieldDelegateCheckPass: TextFieldDelegate!
    var g_imgPickerDelegate: ImagePickerDelegate!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fnInit()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        //super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() {
        g_registerPresenter = RegisterPresenter(self)
        g_registerPresenter.fnStart()
    }
    
    func fnInitView() -> Void {
        self.view.backgroundColor = UIColor.white
        fnInitAdmob()
        fnInitScrollView()
        fnInitImageView()
        fnInitButton()
        fnInitTextField()
    }
    
    func fnInitAdmob() -> Void {
        let admobView = AdmobView()
        admobView.fnSetBannerView(self)
    }
    
    func fnInitScrollView() {
        let fHeight = (self.navigationController?.navigationBar.bounds.size.height)!
        let sizeFullScreen = UIScreen.main.bounds.size
        g_scrollView = UIScrollView()
        g_scrollView.frame = CGRect(x: 0, y: fHeight, width: sizeFullScreen.width, height: sizeFullScreen.height - 50 - fHeight)
        g_scrollView.contentSize = CGSize(width: sizeFullScreen.width * 1, height: 605 + fHeight)
        //g_scrollView.showsVerticalScrollIndicator = true
        
        // 滑動條的樣式
        g_scrollView.indicatorStyle = .default
        
        // 是否可以滑動
        g_scrollView.isScrollEnabled = true
        
        // 是否可以按狀態列回到最上方
        g_scrollView.scrollsToTop = false
        
        // 是否限制滑動時只能單個方向 垂直或水平滑動
        g_scrollView.isDirectionalLockEnabled = true
        
        // 縮放元件的預設縮放大小
        g_scrollView.zoomScale = 1.0
        
        // 縮放元件可縮小到的最小倍數
        g_scrollView.minimumZoomScale = 1
        
        // 縮放元件可放大到的最大倍數
        g_scrollView.maximumZoomScale = 1

        g_scrollView.isPagingEnabled = false
        self.view.addSubview(g_scrollView)
    }
    
    func fnInitImageView() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_imgLogoView = UIImageView()
        g_imgLogoView.frame = CGRect(x: 0, y: 0, width: 100, height: 100)
        g_imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 100)
        g_imgLogoView.image = UIImage(named: "logo.png")
        g_imgLogoView.layer.masksToBounds = true
        g_imgLogoView.layer.cornerRadius = g_imgLogoView.frame.width / 2
        g_scrollView.addSubview(g_imgLogoView)
    }
    
    func fnInitButton() {
        let dollColor = DollColor()
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_btnPhoto = UIButton(frame: CGRect(x: 0, y: 0,  width: 100, height: 50))
        g_btnPhoto.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 185)
        g_btnPhoto.backgroundColor = dollColor.RED
        g_btnPhoto.tintColor = UIColor.white
        g_btnPhoto.setTitle("選擇圖片", for: .normal)
        g_btnPhoto.layer.cornerRadius = 10
        g_btnPhoto.addTarget(nil, action: #selector(self.fnPhotoDown), for: .touchDown)
        g_btnPhoto.addTarget(nil, action: #selector(self.fnOpenPhotoController), for: [.touchUpInside, .touchUpOutside])
        g_scrollView.addSubview(g_btnPhoto)
        
        g_btnMan = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.4, height: 50))
        g_btnMan.center = CGPoint(x: sizeFullScreen.width * 0.25, y: 485)
        g_btnMan.backgroundColor = UIColor.gray
        g_btnMan.layer.borderColor = UIColor.gray.cgColor
        g_btnMan.layer.borderWidth = 1
        g_btnMan.setTitle("男生", for: .normal)
        g_btnMan.layer.cornerRadius = 10
        g_btnMan.addTarget(nil, action: #selector(self.fnManTouch), for: .touchUpInside)
        g_scrollView.addSubview(g_btnMan)
        
        g_btnWoman = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.4, height: 50))
        g_btnWoman.center = CGPoint(x: sizeFullScreen.width * 0.75, y: 485)
        g_btnWoman.backgroundColor = UIColor.white
        g_btnWoman.layer.borderColor = UIColor.gray.cgColor
        g_btnWoman.layer.borderWidth = 1
        g_btnWoman.setTitleColor(UIColor.gray, for: .normal)
        g_btnWoman.setTitle("女生", for: .normal)
        g_btnWoman.layer.cornerRadius = 10
        g_btnWoman.addTarget(nil, action: #selector(self.fnWomanTouch), for: .touchUpInside)
        g_scrollView.addSubview(g_btnWoman)
        
        g_btnRegister = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnRegister.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 545)
        g_btnRegister.backgroundColor = dollColor.BLUE
        g_btnRegister.tintColor = UIColor.white
        g_btnRegister.setTitle("註冊", for: .normal)
        g_btnRegister.layer.cornerRadius = 10
        g_btnRegister.addTarget(nil, action: #selector(self.fnRegisterDown), for: .touchDown)
        g_btnRegister.addTarget(nil, action: #selector(self.fnRegister), for: [.touchUpInside, .touchUpOutside])
        g_scrollView.addSubview(g_btnRegister)
        
        g_btnCancel = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnCancel.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 605)
        g_btnCancel.backgroundColor = dollColor.RED
        g_btnCancel.tintColor = UIColor.white
        g_btnCancel.setTitle("取消", for: .normal)
        g_btnCancel.layer.cornerRadius = 10
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancelDown), for: .touchDown)
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancel), for: [.touchUpInside, .touchUpOutside])
        g_scrollView.addSubview(g_btnCancel)
    }
    
    func fnInitTextField() {
        let sizeFullScreen = UIScreen.main.bounds.size
    
        g_fieldName = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldName.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 245)
        g_fieldName.placeholder = "請輸入名稱(1 ~ 10)"
        g_fieldName.clearButtonMode = .whileEditing
        g_fieldName.returnKeyType = .done
        g_fieldName.textColor = UIColor.lightGray
        g_fieldName.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldName.layer.borderWidth = 1
        g_fieldName.layer.cornerRadius = 10
        g_fieldName.leftViewMode = .always
        g_fieldName.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldName.backgroundColor = UIColor.white
        g_fieldDelegateName = TextFieldDelegate(scrollView: g_scrollView, fMoveOffset: 405 - sizeFullScreen.height * 0.5)
        g_fieldName.delegate = g_fieldDelegateName
        g_scrollView.addSubview(g_fieldName)
        
        g_fieldAccount = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldAccount.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 305)
        g_fieldAccount.placeholder = "請輸入帳號(6 ~ 20)"
        g_fieldAccount.clearButtonMode = .whileEditing
        g_fieldAccount.returnKeyType = .done
        g_fieldAccount.textColor = UIColor.lightGray
        g_fieldAccount.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldAccount.layer.borderWidth = 1
        g_fieldAccount.layer.cornerRadius = 10
        g_fieldAccount.leftViewMode = .always
        g_fieldAccount.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldAccount.backgroundColor = UIColor.white
        // g_fieldAccount.becomeFirstResponder()
        g_fieldDelegateAccount = TextFieldDelegate(scrollView: g_scrollView, fMoveOffset: 465 - sizeFullScreen.height * 0.5)
        g_fieldAccount.delegate = g_fieldDelegateAccount
        g_scrollView.addSubview(g_fieldAccount)
        
        g_fieldPassword = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldPassword.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 365)
        g_fieldPassword.placeholder = "輸入密碼(6 ~ 20)"
        g_fieldPassword.clearButtonMode = .whileEditing
        g_fieldPassword.returnKeyType = .done
        g_fieldPassword.textColor = UIColor.lightGray
        g_fieldPassword.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldPassword.layer.borderWidth = 1
        g_fieldPassword.layer.cornerRadius = 10
        g_fieldPassword.leftViewMode = .always
        g_fieldPassword.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldPassword.backgroundColor = UIColor.white
        g_fieldDelegatePassword = TextFieldDelegate(scrollView: g_scrollView, fMoveOffset: 525 - sizeFullScreen.height * 0.5)
        g_fieldPassword.delegate = g_fieldDelegatePassword
        g_fieldPassword.isSecureTextEntry = true
        g_scrollView.addSubview(g_fieldPassword)
        
        g_fieldCheckPass = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_fieldCheckPass.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 425)
        g_fieldCheckPass.placeholder = "輸入二次密碼"
        g_fieldCheckPass.clearButtonMode = .whileEditing
        g_fieldCheckPass.returnKeyType = .done
        g_fieldCheckPass.textColor = UIColor.lightGray
        g_fieldCheckPass.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldCheckPass.layer.borderWidth = 1
        g_fieldCheckPass.layer.cornerRadius = 10
        g_fieldCheckPass.leftViewMode = .always
        g_fieldCheckPass.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 375))
        g_fieldCheckPass.backgroundColor = UIColor.white
        g_fieldDelegateCheckPass = TextFieldDelegate(scrollView: g_scrollView, fMoveOffset: 585 - sizeFullScreen.height * 0.5)
        g_fieldCheckPass.delegate = g_fieldDelegateCheckPass
        g_fieldCheckPass.isSecureTextEntry = true
        g_scrollView.addSubview(g_fieldCheckPass)
    }
    
    
    
    @objc func fnOpenPhotoController() {
        let dollColor = DollColor()
        g_btnPhoto.backgroundColor = dollColor.RED
        g_imgPickerDelegate = ImagePickerDelegate(self, g_imgLogoView!)
        g_imgPickerDelegate.show()
        
    }
    
    @objc func fnPhotoDown() {
        let dollColor = DollColor()
        g_btnPhoto.backgroundColor = dollColor.RED_T
    }
    
    @objc func fnRegister() {
        let dollColor = DollColor()
        g_btnRegister.backgroundColor = dollColor.BLUE
        g_registerPresenter.fnRegister(sName: g_fieldName.text!, sAccount: g_fieldAccount.text!
            , sPassword: g_fieldPassword.text!, sCheckPass: g_fieldCheckPass.text!, imgData: g_imgLogoView.image)
    }
    
    @objc func fnRegisterDown() {
        let dollColor = DollColor()
        g_btnRegister.backgroundColor = dollColor.BLUE_T
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
    
    @objc func fnManTouch() {
        g_registerPresenter.fnSetSex(sSex: "男")
    }
    
    @objc func fnWomanTouch() {
       g_registerPresenter.fnSetSex(sSex: "女")
    }
    
    func fnSetMan() {
        g_btnMan.backgroundColor = UIColor.gray
        g_btnMan.setTitleColor(UIColor.white, for: .normal)
        g_btnWoman.backgroundColor = UIColor.white
        g_btnWoman.setTitleColor(UIColor.gray, for: .normal)
    }
    
    func fnSetWoman() {
        g_btnMan.backgroundColor = UIColor.white
        g_btnMan.setTitleColor(UIColor.gray, for: .normal)
        g_btnWoman.backgroundColor = UIColor.gray
        g_btnWoman.setTitleColor(UIColor.white, for: .normal)

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
}
