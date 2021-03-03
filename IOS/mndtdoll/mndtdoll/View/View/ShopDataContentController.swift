//
//  ShopDataContent.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/23.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class ShopDataContentController:  UIViewController, IShopDataContentController {
    var g_shopData: ShopData!
    private var g_shopDataContentPresenter: IShopDataContentPresenter!
    private var g_scrollView: UIScrollView!
    private var g_imgLogoView: UIImageView!
    private var g_btnEvaluation: UIButton!
    private var g_btnMyLove: UIButton!
    private var g_labStar: UILabel!
    private var g_labShopName: UILabel!
    private var g_labAdress: UILabel!
    private var g_labMachineAmount: UILabel!
    private var g_labMachineType: UILabel!
    private var g_labReMarks: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
    }

    override func viewWillDisappear(_ animated: Bool) {
        // super.viewWillDisappear(animated)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() -> Void {
        g_shopDataContentPresenter = ShopDataContentPresenter(self)
        g_shopDataContentPresenter.fnStart()
        g_shopDataContentPresenter.fnSetImage(imgView: g_imgLogoView, url: DataInfo.fnGetShopURLString(g_shopData.AddressNo))
    }
    
    func fnInitView() {
        fnInitScrollView()
        fnInitImageView()
        fnInitButton()
        fnInitContent()
    }
    
    func fnInitScrollView() {
        let fHeight = (self.navigationController?.navigationBar.bounds.size.height)!
        let sizeFullScreen = UIScreen.main.bounds.size
        g_scrollView = UIScrollView()
        g_scrollView.frame = CGRect(x: 0, y: fHeight, width: sizeFullScreen.width, height: sizeFullScreen.height - fHeight)
        g_scrollView.contentSize = CGSize(width: sizeFullScreen.width, height: 720 + fHeight)
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
        g_imgLogoView.frame = CGRect(x: 0, y: 0, width: 300, height: 300)
        g_imgLogoView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 175)
        g_imgLogoView.image = UIImage(named: "logo.png")
        g_imgLogoView.layer.masksToBounds = true
        //g_imgLogoView.layer.cornerRadius = g_imgLogoView.frame.width / 2
        g_scrollView.addSubview(g_imgLogoView)
    }
    
    func fnInitButton() {
        let dollColor = DollColor()
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_btnEvaluation = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.5 - 38, height: 50))
        g_btnEvaluation.center = CGPoint(x: sizeFullScreen.width * 0.25, y: 370)
        g_btnEvaluation.backgroundColor = dollColor.BLUE
        g_btnEvaluation.tintColor = UIColor.white
        g_btnEvaluation.setTitle("評價", for: .normal)
        g_btnEvaluation.layer.cornerRadius = 10
        g_btnEvaluation.addTarget(nil, action: #selector(self.fnEvaluationDown), for: .touchDown)
        g_btnEvaluation.addTarget(nil, action: #selector(self.fnOpenEvaluationController), for: [.touchUpInside, .touchUpOutside])
        g_scrollView.addSubview(g_btnEvaluation)
        
        var myLoveTitle = "加入最愛"
        if g_shopData.MyLove == "1" {
            myLoveTitle = "取消最愛"
        }
        g_btnMyLove = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.5 - 38, height: 50))
        g_btnMyLove.center = CGPoint(x: sizeFullScreen.width * 0.75, y: 370)
        g_btnMyLove.backgroundColor = dollColor.RED
        g_btnMyLove.tintColor = UIColor.white
        g_btnMyLove.setTitle(myLoveTitle, for: .normal)
        g_btnMyLove.layer.cornerRadius = 10
        g_btnMyLove.addTarget(nil, action: #selector(self.fnMyLoveDown), for: .touchDown)
        g_btnMyLove.addTarget(nil, action: #selector(self.fnUpdateMyLove), for: [.touchUpInside, .touchUpOutside])
        g_scrollView.addSubview(g_btnMyLove)
    }
    
    func fnInitContent() {
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_labShopName = UILabel()
        g_labShopName.textColor = UIColor.darkGray
        g_labShopName.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labShopName.translatesAutoresizingMaskIntoConstraints = false
        g_labShopName.text = "店家：" + g_shopData.ShopName
        g_scrollView.addSubview(g_labShopName)
        g_labShopName.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labShopName.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 410).isActive = true
        
        let lineView1 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView1.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 440)
        lineView1.layer.borderWidth = 1.0
        lineView1.backgroundColor = UIColor.darkGray
        lineView1.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView1)
        
        
        g_labStar = UILabel()
        g_labStar.textColor = UIColor.darkGray
        g_labStar.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labStar.translatesAutoresizingMaskIntoConstraints = false
        g_labStar.text = "評價："
        g_scrollView.addSubview(g_labStar)
        g_labStar.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labStar.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 470).isActive = true
        
        let starView = YTTStarRatingView(frame: CGRect(x: 0, y: 70, width: sizeFullScreen.width - 80, height: 20))
        starView.translatesAutoresizingMaskIntoConstraints = false
        g_scrollView.addSubview(starView)
        starView.start = NumberFormatter().number(from: g_shopData.Star) as! CGFloat
        starView.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 50).isActive = true
        starView.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 470).isActive = true
        
        let lineView2 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView2.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 500)
        lineView2.layer.borderWidth = 1.0
        lineView2.backgroundColor = UIColor.darkGray
        lineView2.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView2)
        
        
        
        g_labAdress = UILabel()
        g_labAdress.textColor = UIColor.darkGray
        g_labAdress.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labAdress.translatesAutoresizingMaskIntoConstraints = false
        g_labAdress.text = "地址：" + g_shopData.Address
        g_scrollView.addSubview(g_labAdress)
        g_labAdress.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labAdress.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 520).isActive = true
        
        let lineView3 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView3.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 560)
        lineView3.layer.borderWidth = 1.0
        lineView3.backgroundColor = UIColor.darkGray
        lineView3.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView3)
        
        
        
        g_labMachineAmount = UILabel()
        g_labMachineAmount.textColor = UIColor.darkGray
        g_labMachineAmount.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labMachineAmount.translatesAutoresizingMaskIntoConstraints = false
        g_labMachineAmount.text = "機台：" + g_shopData.MachineAmount
        g_scrollView.addSubview(g_labMachineAmount)
        g_labMachineAmount.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labMachineAmount.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 580).isActive = true
        
        let lineView4 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView4.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 620)
        lineView4.layer.borderWidth = 1.0
        lineView4.backgroundColor = UIColor.darkGray
        lineView4.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView4)
        
        
        
        g_labMachineType = UILabel()
        g_labMachineType.textColor = UIColor.darkGray
        g_labMachineType.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labMachineType.translatesAutoresizingMaskIntoConstraints = false
        g_labMachineType.text = "機型：" + g_shopData.MachineType
        g_scrollView.addSubview(g_labMachineType)
        g_labMachineType.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labMachineType.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 640).isActive = true
        
        let lineView5 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView5.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 680)
        lineView5.layer.borderWidth = 1.0
        lineView5.backgroundColor = UIColor.darkGray
        lineView5.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView5)
        
        
        g_labMachineType = UILabel()
        g_labMachineType.textColor = UIColor.darkGray
        g_labMachineType.font = UIFont(name: g_labShopName.font.familyName, size: 16)
        g_labMachineType.translatesAutoresizingMaskIntoConstraints = false
        g_labMachineType.text = "備註：" + g_shopData.Remarks
        g_scrollView.addSubview(g_labMachineType)
        g_labMachineType.leftAnchor.constraint(equalTo: g_scrollView.leftAnchor, constant: 15).isActive = true
        g_labMachineType.topAnchor.constraint(equalTo: g_scrollView.topAnchor, constant: 710).isActive = true
        
        let lineView6 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 1.0))
        lineView6.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 770)
        lineView6.layer.borderWidth = 1.0
        lineView6.backgroundColor = UIColor.darkGray
        lineView6.layer.borderColor = UIColor.black.cgColor
        g_scrollView.addSubview(lineView6)
    }
    
    @objc func fnEvaluationDown() {
        let dollColor = DollColor()
        g_btnEvaluation.backgroundColor = dollColor.BLUE_T
    }
    
    @objc func fnOpenEvaluationController() {
        let dollColor = DollColor()
        g_btnEvaluation.backgroundColor = dollColor.BLUE_T
        let evaluationController = EvaluationController()
        evaluationController.g_sAddressNo = g_shopData.AddressNo
        evaluationController.modalPresentationStyle = .overCurrentContext
        self.present(evaluationController, animated: true, completion: nil)

        //self.navigationController?.pushViewController(EvaluationController(), animated: true)
        
    }
    
    @objc func fnMyLoveDown() {
        let dollColor = DollColor()
        g_btnMyLove.backgroundColor = dollColor.RED_T
    }
    
    @objc func fnUpdateMyLove() {
        let dollColor = DollColor()
        g_btnMyLove.backgroundColor = dollColor.RED
        
        let dollShop = DollShopSql()
        if !dollShop.fnConnection() {
            return
        }
        
        if g_shopData.MyLove == "1" {
            g_btnMyLove.setTitle("加入最愛", for: .normal)
            g_shopData.MyLove = "0"
        } else {
            g_btnMyLove.setTitle("取消最愛", for: .normal)
            g_shopData.MyLove = "1"
        }
        dollShop.fnUpdateMyLove(sMyLove: g_shopData.MyLove, sAddressNo: g_shopData.AddressNo)
    }

}
