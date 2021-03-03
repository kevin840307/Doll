//
//  ThemeViewController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/27.
//  Copyright © 2019年 Ghost. All rights reserved.
//


import UIKit
import Photos

class ThemeViewController:  UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout  {
    var g_themeData: ThemeData!
    private var g_sUrls: [String]! = [String]()
    private var g_collectionview: UICollectionView!

    private let THEME = ["全版", "閒聊區", "娃娃機台出租區", "夾物交易區", "問題回報區", "夾點分享區"]
    private let AREA = ["【南部】", "【中部】", "【北部】", "【東部】"]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() {
        if g_themeData.PicAmount == "0" {
            return
        }
        
        for index in 0...Int(g_themeData.PicAmount)! - 1 {
            g_sUrls.append(DataInfo.fnGetThemeURLString(sPlateId: g_themeData.PlateId, sThemeId: g_themeData.ThemeId, sIndex: String(index)))
        }
        
        self.navigationItem.title = THEME[Int(g_themeData.PlateId)!]
        //print(self.navigationController?.navigationBar.bounds.height)
    }
    
    func fnInitView() {
        fnInitRightButton()
        fnInitImageView()
        fnInitLabel()
        fnInitTextView()
        fnInitCollectionview()
    }
    
    func fnInitRightButton() {
        let sendButton = UIBarButtonItem(title: "留言", style: .done, target: self, action: #selector(self.fnOpenResponseController))
        self.navigationItem.rightBarButtonItem = sendButton
    }
    
    @objc func fnOpenResponseController() {
        let responseController = ResponseController()
        responseController.fnSetThemeData(g_themeData)
        //responseController.modalPresentationStyle = .overCurrentContext
        responseController.interactor = Interactor()
        self.present(responseController, animated: true, completion: nil)
        //self.navigationController?.pushViewController(responseController, animated: true)
    }
    
    func fnInitImageView() {
        let imgLogoView = UIImageView()
        imgLogoView.frame = CGRect(x: 0, y: 0, width: 80, height: 80)
        imgLogoView.center = CGPoint(x: 50, y: 150)
        imgLogoView.image = UIImage(named: "logo.png")
        imgLogoView.layer.masksToBounds = true
        imgLogoView.layer.cornerRadius = imgLogoView.frame.width / 2
        view.addSubview(imgLogoView)
        fnSetImage(imgLogoView)
    }
    
    func fnInitLabel() {
        let sizeFullScreen = view.bounds.size
        let dollColor = DollColor()
        
        let labTitle = UILabel(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 50, height: 40))
        labTitle.font = UIFont(name: labTitle.font.familyName, size: 22)
        labTitle.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 80)
        labTitle.text = g_themeData.Title
        labTitle.layer.borderColor = UIColor.black.cgColor
        view.addSubview(labTitle)
        
        let lineView1 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 10, height: 8.0))
        lineView1.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 100)
        lineView1.layer.borderWidth = 8.0
        lineView1.layer.borderColor = dollColor.RED.cgColor
        lineView1.backgroundColor = dollColor.RED
        view.addSubview(lineView1)
        
        
        let labName = UILabel()
        labName.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width - 110, height: 40)
        labName.center = CGPoint(x: sizeFullScreen.width * 0.5 + 45, y: 130)
        labName.text = g_themeData.Name
        labName.font = UIFont(name: labName.font.familyName, size: 22)
        view.addSubview(labName)
        
        let labDateTime = UILabel(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 110, height: 40))
        labDateTime.textColor = UIColor.darkGray
        labDateTime.center = CGPoint(x: sizeFullScreen.width * 0.5 + 45, y: 170)
        labDateTime.font = UIFont(name: labDateTime.font.familyName, size: 18)
        labDateTime.text = g_themeData.Date
        view.addSubview(labDateTime)
        
        let lineView2 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 10, height: 8.0))
        lineView2.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 200)
        lineView2.layer.borderWidth = 8.0
        lineView2.layer.borderColor = dollColor.BLUE.cgColor
        lineView2.backgroundColor = dollColor.BLUE
        view.addSubview(lineView2)
        
        let lineView3 = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 10, height: 8.0))
        lineView3.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height - 100)
        lineView3.layer.borderWidth = 8.0
        lineView3.layer.borderColor = dollColor.ORANGE.cgColor
        lineView3.backgroundColor = dollColor.ORANGE
        view.addSubview(lineView3)

    }
    
    func fnInitTextView() {
        let sizeFullScreen = view.bounds.size
        let textContent = UITextView(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 20, height: sizeFullScreen.height - 330))
        textContent.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.5 + 50)
        textContent.returnKeyType = .done
        textContent.font = UIFont(name: "Courier", size: 18)
        //g_textContent.textColor = UIColor.lightGray
        textContent.layer.borderColor = UIColor.darkGray.cgColor
        textContent.layer.borderWidth = 1
        textContent.layer.cornerRadius = 10
        textContent.text = g_themeData.Content
        textContent.backgroundColor = UIColor.white
        textContent.isEditable = false
        view.addSubview(textContent)
    }
   
    func fnSetImage(_ imgView: UIImageView) -> Void {
        if UserData.fnGetLoginType() == "2" {
            DispatchQueue.global().async {
                Image().fnUrlSetImage(sUrl: "\(DataInfo.WEB_SERVICE_USER_URL)\(self.g_themeData.Account).jpg", imgData: imgView)
            }
        }
    }
    
    func fnInitCollectionview() {
        let sizeFullScreen = view.bounds.size
        
        let collectionViewFlowLayout = UICollectionViewFlowLayout()
        collectionViewFlowLayout.scrollDirection = .horizontal
        // frame
        g_collectionview = UICollectionView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 20, height: 70), collectionViewLayout: collectionViewFlowLayout)
        g_collectionview.center = CGPoint(x: sizeFullScreen.width * 0.5 , y: sizeFullScreen.height - 50)
        
        // 委任
        g_collectionview.delegate = self
        g_collectionview.dataSource = self
        
        // 註冊cell, header, footer
        g_collectionview.register(UICollectionViewCell1.self,
                                  forCellWithReuseIdentifier: "cell")
        g_collectionview.register(UICollectionReusableView.self,
                                  forSupplementaryViewOfKind: UICollectionElementKindSectionHeader,
                                  withReuseIdentifier: "header")
        g_collectionview.register(UICollectionReusableView.self,
                                  forSupplementaryViewOfKind: UICollectionElementKindSectionFooter,
                                  withReuseIdentifier: "footer")
        g_collectionview.backgroundColor = UIColor.white
        // 允許點擊
        g_collectionview.allowsSelection = true
        // 允許雙點擊
        g_collectionview.allowsMultipleSelection = false
        // 垂直指示器
        g_collectionview.showsVerticalScrollIndicator = false
        // 水平指示器
        g_collectionview.showsHorizontalScrollIndicator = true
        // 指示器樣式
        g_collectionview.indicatorStyle = .default
        // 可否滑動
        g_collectionview.isScrollEnabled = true
        
        // 預加載
        //g_collectionview.isPrefetchingEnabled = false
        g_collectionview.layer.borderWidth = 1
        g_collectionview.layer.cornerRadius = 10
        g_collectionview.layer.borderColor = UIColor.darkGray.cgColor

        view.addSubview(g_collectionview)
    }
    
    
    // 位置(top, left, bottom, right)
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsetsMake(5, 5, 5, 5)
    }
    // header frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return CGSize(width: 0, height: 40)
    }
    // footer frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForFooterInSection section: Int) -> CGSize {
        return CGSize(width: 0, height: 0)
    }
    // cell frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 60, height: 60)
    }
    // 垂直間距
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat{
        return 10
    }
    // 水平間距
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat{
        return 2.5
    }
    // header, footer 內容
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        return UICollectionReusableView()
    }
    // 組數
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    // 內容數
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return g_sUrls.count
    }
    // 內容
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell",
                                                      for: indexPath) as! UICollectionViewCell1
        cell.imageview.image = UIImage(named: "logo.png")
        Image().fnUrlImage(sUrl: g_sUrls[indexPath.row], imgData: cell.imageview)
        return cell
    }
    
    // 點選觸發
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let showImageController = ShowImageController()
        showImageController.g_index = indexPath.row
        showImageController.g_urls = g_sUrls
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(showImageController, animated: true)
    }

}

