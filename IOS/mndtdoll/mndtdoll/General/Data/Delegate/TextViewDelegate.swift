//
//  TextViewDelegate.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/24.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class TextViewDelegate: NSObject, UITextViewDelegate {
    
    private var g_viewController: UIViewController! = nil
    private var g_view: UIView!
    private var g_fMoveOffset: CGFloat!
    
    init(view: UIView, fMoveOffset: CGFloat) {
        g_view = view
        g_fMoveOffset = fMoveOffset
    }
    
    init(viewController: UIViewController, fMoveOffset: CGFloat) {
        g_viewController = viewController
        g_fMoveOffset = fMoveOffset
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        fnAnimateViewMoving(up: true, moveValue: g_fMoveOffset)
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        fnAnimateViewMoving(up: false, moveValue: g_fMoveOffset)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if g_viewController != nil {
            g_viewController.view.endEditing(true)
        } else {
            g_view.endEditing(true)
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
            //g_view.setContentOffset(CGPoint(x: 0, y: moveValue), animated: true)
        }
        UIView.commitAnimations()
    }
    
    
}
