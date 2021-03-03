//
//  Image.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/10.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class Image {
    
    func fnUIImageToBase64(imgData: UIImage, fCompress: CGFloat = 0.9) -> String? {
        let sBase64String = UIImageJPEGRepresentation(imgData, fCompress)?.base64EncodedString()
        return sBase64String
    }
    
    func fnResize(imgData: UIImage, width: CGFloat) -> UIImage {
        var fOffset: CGFloat = 1
        var fWidth = imgData.size.width
        while fWidth > width {
            fWidth /= 2
            fOffset /= 2
        }
        let reSize = CGSize(width: imgData.size.width * fOffset, height: imgData.size.height * fOffset)
        return fnResize(imgData: imgData, reSize: reSize)
    }
    
    func fnResize(imgData: UIImage, reSize: CGSize) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(reSize,false,UIScreen.main.scale)
        imgData.draw(in: CGRect(x: 0, y: 0, width: reSize.width, height: reSize.height))
        let reSizeImage: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return reSizeImage
    }
    
    func fnUrlImage(sUrl: String, imgData: UIImageView) {
        let urlSession = URLSession.shared
        let request = NSMutableURLRequest(url: NSURL(string: sUrl)! as URL)
        let taskThread = urlSession.dataTask(with: request as URLRequest,completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let image = UIImage.init(data :data!)
                if image != nil {
                    DispatchQueue.main.async {
                        imgData.image = image
                    }
                }
            }
        })
        taskThread.resume()
    }
    
    func fnUrlSetImage(sUrl: String, imgData: UIImageView) -> Void {
        if let url = URL.init(string: sUrl) {
            do {
                let imageData = try Data(contentsOf: url)
                var imgView = UIImage(data: imageData)
                let reSize = CGSize(width: imgData.bounds.width, height: imgData.bounds.height)
                imgView = fnResize(imgData: imgView!, reSize: reSize)
                DispatchQueue.main.async {
                    imgData.image = imgView
                    imgData.sizeToFit()
                }
            } catch {
                print(error)
            }
        }
    }

}
