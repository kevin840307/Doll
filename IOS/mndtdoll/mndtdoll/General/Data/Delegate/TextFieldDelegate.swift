//
//  TextFieldDelegate.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/8.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class TextFieldDelegate: NSObject, UITextFieldDelegate {
    
    private var g_viewController: UIViewController! = nil
    private var g_scrollView: UIScrollView!
    private var g_fMoveOffset: CGFloat!
    
    init(scrollView: UIScrollView, fMoveOffset: CGFloat) {
        g_scrollView = scrollView
        g_fMoveOffset = fMoveOffset
    }
    
    init(viewController: UIViewController, fMoveOffset: CGFloat) {
        g_viewController = viewController
        g_fMoveOffset = fMoveOffset
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        fnAnimateViewMoving(up: true, moveValue: g_fMoveOffset)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        fnAnimateViewMoving(up: false, moveValue: g_fMoveOffset)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if g_viewController != nil {
            g_viewController.view.endEditing(true)
        } else {
            g_scrollView.endEditing(true)
        }
        return true
    }
    
    func fnAnimateViewMoving (up:Bool, moveValue :CGFloat){
        let movementDuration:TimeInterval = 0.3
        let movement:CGFloat = ( up ? -moveValue : moveValue)
        UIView.beginAnimations( "animateView", context: nil)
        UIView.setAnimationBeginsFromCurrentState(true)
        UIView.setAnimationDuration(movementDuration )
        if g_viewController != nil {
            g_viewController.view.frame = g_viewController.view.frame.offsetBy(dx: 0, dy: movement)
        } else {
            //g_scrollView.frame = g_scrollView.frame.offsetBy(dx: 0, dy: movement)
            
            //g_scrollView.contentOffset = CGPoint(x: 1000, y: 450)
            g_scrollView.setContentOffset(CGPoint(x: 0, y: moveValue), animated: true)
        }
        UIView.commitAnimations()
    }
    
    
}
