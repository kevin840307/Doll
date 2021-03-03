//
//  Test.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/6.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit
import GoogleMobileAds

class AdmobView {
    func fnSetBannerView(_ viewController: UIViewController) {
        let bannerView = GADBannerView(adSize: kGADAdSizeSmartBannerPortrait)
        bannerView.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.addSubview(bannerView)
        viewController.view.addConstraints(
            [NSLayoutConstraint(item: bannerView,
                                attribute: .bottom,
                                relatedBy: .equal,
                                toItem: viewController.bottomLayoutGuide,
                                attribute: .top,
                                multiplier: 1,
                                constant: 0),
             NSLayoutConstraint(item: bannerView,
                                attribute: .centerX,
                                relatedBy: .equal,
                                toItem: viewController.view,
                                attribute: .centerX,
                                multiplier: 1,
                                constant: 0)
            ])
        
        bannerView.adUnitID = DataInfo.ADMOB_ID
        bannerView.rootViewController = viewController
        bannerView.load(GADRequest())
    }
    
    func fnSetTopBannerView(_ viewController: UIViewController) {
        let bannerView = GADBannerView(adSize: kGADAdSizeSmartBannerPortrait)
        bannerView.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.addSubview(bannerView)
        viewController.view.addConstraints(
            [
             NSLayoutConstraint(item: bannerView,
                                attribute: .centerX,
                                relatedBy: .equal,
                                toItem: viewController.view,
                                attribute: .centerX,
                                multiplier: 1,
                                constant: 0)
            ])
        bannerView.topAnchor.constraint(equalTo: viewController.view.topAnchor, constant: 60).isActive = true
        bannerView.adUnitID = DataInfo.ADMOB_ID
        bannerView.rootViewController = viewController
        bannerView.load(GADRequest())
    }
}
