//
//  StarRatingView.swift
//  StarRating
//
//  Created by Andy on 2017/7/13.
//  Copyright © 2017年 AndyCuiYTT. All rights reserved.
//

/**
 *
 *  Author: AndyCui
 *
 *  Description: 防淘宝星级评分,通过图片实现评分视图的实现.添加背景视图和前景视图,通过控制前景视图实现评分的实现
 *
 */



import UIKit


protocol YTTStarRatingDelegate: class {
    func yttStarRatingView(_ starRatingView: YTTStarRatingView, rate: CGFloat, star: CGFloat);
}


class YTTStarRatingView: UIView {
    
    private var totalStarNumber: Int!
    private var foregroundView: UIView!
    weak var delegate: YTTStarRatingDelegate?
    
    var rate: CGFloat! = 0{
        didSet{
            var rect = foregroundView.frame
            rect.size.width = self.frame.width * rate
            foregroundView.frame = rect
        }
    }
    
    var start: CGFloat! = 0{
        didSet{
            var rect = foregroundView.frame
            rect.size.width = self.frame.width * start / 5.0
            foregroundView.frame = rect
        }
    }


    init(frame: CGRect, totalStarts: Int = 5, delegate: YTTStarRatingDelegate? = nil) {
        super.init(frame: frame)
        totalStarNumber = totalStarts
        self.delegate = delegate
        self.addSubview(self.yttCreatStartView("haoping_gray"))
        foregroundView = self.yttCreatStartView("haoping_orange")
        self.addSubview(foregroundView)
    }
    
   
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private func yttCreatStartView(_ imageName: String) -> UIView {
        let starView = UIView(frame: CGRect(x: 0, y: 0, width: self.frame.width, height: self.frame.height))
        starView.clipsToBounds = true
        starView.backgroundColor = UIColor.clear
        starView.isUserInteractionEnabled = false
        let imgViewWidth = (self.frame.width - CGFloat(totalStarNumber - 1) * 3) / CGFloat(totalStarNumber)
        
        for i in 0 ..< totalStarNumber {
            let imageView = UIImageView(image: UIImage(named: imageName))
            imageView.frame = CGRect(x: CGFloat(i) * (imgViewWidth + 3), y: 0, width: imgViewWidth, height: self.frame.height)
            imageView.contentMode = .scaleAspectFit
            starView.addSubview(imageView)
        }
        return starView
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        let point = touch?.location(in: self)
        if (point?.x)! >= CGFloat(0) && (point?.x)! <= self.frame.width {
            rate = (point?.x)! / self.frame.width
            delegate?.yttStarRatingView(self, rate: rate, star: rate * 5)
        }
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first
        let point = touch?.location(in: self)
        rate = (point?.x)! / self.frame.width
        delegate?.yttStarRatingView(self, rate: rate, star: rate * 5)
    }
    
}
