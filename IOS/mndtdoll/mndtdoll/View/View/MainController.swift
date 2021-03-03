//
//  MainController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit
class MainController: UITabBarController {
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitPage()
        //fnInitAdmob()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    private func fnInitAdmob() -> Void {
        let admobView = AdmobView()
        admobView.fnSetTopBannerView(self)
    }
    
    func fnInitPage() -> Void {
        let reSize = CGSize(width: 38, height: 38)
        let imgHot  = Image().fnResize(imgData: UIImage(named: "hot.png")!, reSize: reSize)
        let imgSearch  = Image().fnResize(imgData: UIImage(named: "search.png")!, reSize: reSize)
        let imgLove  = Image().fnResize(imgData: UIImage(named: "love.png")!, reSize: reSize)
        let imgOther  = Image().fnResize(imgData: UIImage(named: "other.png")!, reSize: reSize)
        let hotShopController = HotShopController()
        hotShopController.tabBarItem = UITabBarItem(
            title: "熱門",
            image: imgHot,
            selectedImage: imgHot)
        hotShopController.title = "熱門"
        
        let searchShopController = SearchShopController()
        searchShopController.tabBarItem = UITabBarItem(
            title: "搜尋",
            image: imgSearch,
            selectedImage: imgSearch)
        searchShopController.title = "搜尋"
        
        let myLoveController = MyLoveController()
        myLoveController.tabBarItem = UITabBarItem(
            title: "我的最愛",
            image: imgLove,
            selectedImage: imgLove)
        myLoveController.title = "我的最愛"
        
        let otherController = OtherController()
        otherController.tabBarItem = UITabBarItem(
            title: "其他",
            image: imgOther,
            selectedImage: imgOther)
        otherController.title = "其他"
        
        let listControllers = [hotShopController, searchShopController, myLoveController, otherController]
        self.viewControllers = listControllers
        self.viewControllers = listControllers.map { UINavigationController(rootViewController: $0)}
        self.selectedIndex = 0
    }

}
