//
//  ImagePickerDelegate.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/11.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class ImagePickerDelegate: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    private var g_imgView: UIImageView!
    private var g_viewController: UIViewController!
    
    init(_ viewController: UIViewController , _ imgView: UIImageView) {
        g_imgView = imgView
        g_viewController = viewController
    }
    
    func show() -> Void {
        let imagePickerVC = UIImagePickerController()
        imagePickerVC.sourceType = .photoLibrary
        imagePickerVC.delegate = self
        imagePickerVC.modalPresentationStyle = .popover
        let popover = imagePickerVC.popoverPresentationController
        popover?.sourceView = g_imgView!
        popover?.sourceRect = g_imgView!.bounds
        popover?.permittedArrowDirections = .any
        g_viewController.show(imagePickerVC, sender: g_viewController)
    }
    
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let image = info[UIImagePickerControllerOriginalImage] as! UIImage
        g_imgView.image =  image
        g_viewController.dismiss(animated: true, completion: nil)
    }
}
