//
//  ShowImageController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/26.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit
import Photos

class ShowImageController: UIViewController {
    var g_imgView: UIImageView!
    var g_assets: [PHAsset]? = nil
    var g_urls: [String]? = nil
    var g_index = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnShowImage()
    }
    
    override func viewDidAppear(_ animated: Bool) {
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnShowImage() {
        if g_assets != nil {
            g_imgView.image = fnGetAssetUIImage(g_assets![g_index])
        } else if g_urls != nil {
            Image().fnUrlImage(sUrl: g_urls![g_index], imgData: g_imgView)
        }
    }
    
    func fnInitView() {
        fnInitImageView()
        fnInitButton()
    }
    
    func fnInitImageView() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_imgView = UIImageView()
        g_imgView.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width - 100, height: sizeFullScreen.height - 50)
        g_imgView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.5)
        g_imgView.contentMode = .scaleAspectFit
        g_imgView.image = UIImage(named: "logo.png")
        self.view.addSubview(g_imgView)
    }
    
    func fnInitButton() {
        let sizeFullScreen = UIScreen.main.bounds.size
        
        let btnLeft = UIButton()
        btnLeft.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        btnLeft.center = CGPoint(x: 20, y: sizeFullScreen.height * 0.5)
        btnLeft.setBackgroundImage(UIImage(named: "left.png"), for: .normal)
        btnLeft.addTarget(nil, action: #selector(self.fnLeft), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(btnLeft)
        
        let btnRight = UIButton()
        btnRight.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        btnRight.center = CGPoint(x: sizeFullScreen.width - 20, y: sizeFullScreen.height * 0.5)
        btnRight.setBackgroundImage(UIImage(named: "right.png"), for: .normal)
        btnRight.addTarget(nil, action: #selector(self.fnRight), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(btnRight)
    }
    
    @objc func fnLeft() {
        if g_assets != nil && g_index == 0 {
            return
        }
        
        if g_urls != nil && g_index == 0 {
            return
        }
        g_index -= 1
        fnShowImage()
    }
    
    @objc func fnRight() {
        if g_assets != nil && g_index == (g_assets?.count)! - 1 {
            return
        }
        
        if g_urls != nil && g_index == (g_urls?.count)! - 1 {
            return
        }
        
        g_index += 1
        fnShowImage()
    }
    
    func fnGetAssetUIImage(_ asset: PHAsset) -> UIImage {
        let sizeFullScreen = UIScreen.main.bounds.size
        let manager = PHImageManager.default()
        let option = PHImageRequestOptions()
        var image = UIImage()
        option.isSynchronous = true
        manager.requestImage(for: asset, targetSize: CGSize(width: sizeFullScreen.width - 100, height: sizeFullScreen.height - 50), contentMode: .aspectFit, options: option, resultHandler: {(result, info)->Void in
            image = result!
        })
        return image
    }
}
