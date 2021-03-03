//
//  LoginWaitView.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/7.
//  Copyright © 2017年 Ghost. All rights reserved.
//


import UIKit

class LoginWaitController: UIViewController, ILoginWaitController {

    var g_loginWaitPresenter: ILoginWaitPresenter!
    var g_progressView: UIProgressView!
    var g_labMsg: UILabel!
    var g_imgLogoView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
     }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        fnInit()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        // super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() -> Void {
        g_loginWaitPresenter = LoginWaitPresenter(self)
        g_loginWaitPresenter.fnStart()
        g_imgLogoView.layer.add(fnCreateAnimation(keyPath: "transform.rotation.z", toValue: CGFloat(Double.pi * 2)), forKey: nil)
    }
    
    func fnInitView() -> Void {
        fnInitImage()
        fnInitLable()
        fnInitProgress()
    }
    
    func fnUpdateProgress(_ fCount: Float) -> Void {
        self.g_progressView.progress = (fCount / 100)
        self.g_labMsg.text = "載入中 \(Int(fCount)) %"
    }
    
    func fnConfirm() {
        // 建立一個提示框
        let alertController = UIAlertController(title: "下載",
                                                message: "是否要下載最新資料？",
                                                preferredStyle: .alert)
        
        // 建立[取消]按鈕
        let cancelAction = UIAlertAction(title: "取消", style: .cancel) {
            (action: UIAlertAction!) -> Void in
            self.g_loginWaitPresenter.View.fnUpdateProgress(100)
            self.g_loginWaitPresenter.fnOpenController()
        }
        
        alertController.addAction(cancelAction)
        
        // 建立[下載]按鈕
        let okAction = UIAlertAction(title: "下載", style: .default) {
            (action: UIAlertAction!) -> Void in
            self.g_loginWaitPresenter.fnDowloadDatas()
        }
        alertController.addAction(okAction)
        
        // 顯示提示框
        self.present(alertController, animated: true, completion: nil)
    }
    
    private func fnInitImage() -> Void {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_imgLogoView = UIImageView()
        g_imgLogoView.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.5, height: sizeFullScreen.height * 0.3)
        g_imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.3)
        g_imgLogoView.image = UIImage(named: "logo.png")
        self.view.addSubview(g_imgLogoView)
    }
    
    private func fnInitLable() -> Void  {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_labMsg = UILabel(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.9, height: 50))
        g_labMsg.text = "載入中 0 %"
        g_labMsg.textColor = UIColor.black
        g_labMsg.font = UIFont.boldSystemFont(ofSize: UIFont.labelFontSize)
        g_labMsg.textAlignment = NSTextAlignment.center
        g_labMsg.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.8)
        self.view.addSubview(g_labMsg)
    }
    
    private func fnInitProgress() -> Void {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_progressView = UIProgressView(progressViewStyle : .default)
        g_progressView.progressTintColor = UIColor.blue
        g_progressView.trackTintColor = UIColor.gray
        
        g_progressView.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width * 0.9, height: 50)
        g_progressView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.7)
        self.view.addSubview(g_progressView)
    }
    
    
    private func fnCreateAnimation(keyPath: String, toValue: CGFloat) -> CABasicAnimation {
        let scaleAni = CABasicAnimation()
        scaleAni.keyPath = keyPath
        scaleAni.toValue = toValue
        scaleAni.duration = 1.5;
        scaleAni.repeatCount = Float(CGFloat.greatestFiniteMagnitude)
        return scaleAni;
    }
    
    func fnOpenLoginWayController() -> Void {
        self.dismiss(animated: false, completion:nil)
        ViewController.PAGE_TYPE = 1
    }
    
    func fnOpenMainController() -> Void {
        self.dismiss(animated: false, completion:nil)
        ViewController.PAGE_TYPE = 2
    }
}

