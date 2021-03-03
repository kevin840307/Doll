//
//  ViewController.swift
//  doll
//
//  Created by Ghost on 2017/12/4.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    public static var PAGE_TYPE = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fnInit()
    }
    
    func fnInit() {
        switch ViewController.PAGE_TYPE {
        case 0:
             self.present(LoginWaitController(), animated: false, completion: nil)
            break
        case 1:
            let navController = UINavigationController(rootViewController: LoginWayController())
            self.present(navController, animated: true, completion: nil)
            break
        case 2:
            self.present(MainController(), animated: true, completion: nil)
            break
        default:
            break
        }
    }
}

