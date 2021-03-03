//
//  SearchShopController.swift
//  mndtdoll
//
//  Created by Ghost on 2017/12/13.
//  Copyright © 2017年 Ghost. All rights reserved.
//

import UIKit

class SearchShopController:  UIViewController, ISearchShopController, UIPickerViewDelegate, UIPickerViewDataSource  {
    private var g_pickerView: UIPickerView!
    private var g_btnArea: UIButton!
    private var g_btnLocation: UIButton!
    private let WIDTH = UIScreen.main.bounds.size.width
    private let HEIGHT = UIScreen.main.bounds.size.height
    private var AREA: [String]!
    private var LOCATION: [String]!
    private var g_sArea = "全部"
    private var g_sLocation = "全部"
    private var g_dollShopSql: DollShopSql!
    
    
    private var g_fieldKeyWord: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    func fnInitView() -> Void {
        fnInitLabel()
        fnInitTextField()
        fnInitButton()
        fnInitPickerView()
    }
    
    func fnInit() {
        g_dollShopSql = DollShopSql()
        if !g_dollShopSql.fnConnection() {
            return
        }
        AREA = g_dollShopSql.fnGetAreaData()
        LOCATION = g_dollShopSql.fnGetLocationData(sArea: "%")
    }
    
    func fnInitLabel() {
        let fullScreenSize = UIScreen.main.bounds.size
        let labKeyWord = UILabel(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width - 30, height: 40))
        labKeyWord.text = "關鍵查詢"
        labKeyWord.center = CGPoint(x: fullScreenSize.width * 0.5, y: 80)
        self.view.addSubview(labKeyWord)
        
        let labArea = UILabel(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width - 30, height: 40))
        labArea.text = "地區夾點"
        labArea.center = CGPoint(x: fullScreenSize.width * 0.5, y: 180)
        self.view.addSubview(labArea)
        
        let labLocation = UILabel(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width - 30, height: 40))
        labLocation.text = "地區夾點"
        labLocation.center = CGPoint(x: fullScreenSize.width * 0.5, y: 280)
        self.view.addSubview(labLocation)
    }
    
    func fnInitTextField() {
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_fieldKeyWord = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        g_fieldKeyWord.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 130)
        g_fieldKeyWord.placeholder = "請輸入關鍵字"
        g_fieldKeyWord.clearButtonMode = .whileEditing
        g_fieldKeyWord.returnKeyType = .done
        g_fieldKeyWord.textColor = UIColor.lightGray
        g_fieldKeyWord.layer.borderColor = UIColor.lightGray.cgColor
        g_fieldKeyWord.layer.borderWidth = 1
        g_fieldKeyWord.layer.cornerRadius = 10
        g_fieldKeyWord.leftViewMode = .always
        g_fieldKeyWord.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 50))
        g_fieldKeyWord.backgroundColor = UIColor.white
        //g_fieldDelegateName = TextFieldDelegate(scrollView: g_scrollView, fMoveOffset: 405 - sizeFullScreen.height * 0.5)
        //g_fieldName.delegate = g_fieldDelegateName
        self.view.addSubview(g_fieldKeyWord)
    }
    
    func fnInitButton() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_btnArea = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        g_btnArea.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 230)
        g_btnArea.backgroundColor = UIColor.white
        g_btnArea.layer.borderColor = UIColor.lightGray.cgColor
        g_btnArea.layer.borderWidth = 1
        g_btnArea.setTitle("全部", for: .normal)
        g_btnArea.setTitleColor(UIColor.black,for: .normal)
        g_btnArea.layer.cornerRadius = 10
        g_btnArea.addTarget(self, action: #selector(self.buttonpressed(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnArea)
        
        g_btnLocation = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        g_btnLocation.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 330)
        g_btnLocation.backgroundColor = UIColor.white
        g_btnLocation.layer.borderColor = UIColor.lightGray.cgColor
        g_btnLocation.layer.borderWidth = 1
        g_btnLocation.setTitle("全部", for: .normal)
        g_btnLocation.setTitleColor(UIColor.black,for: .normal)
        g_btnLocation.layer.cornerRadius = 10
        g_btnLocation.addTarget(self, action: #selector(self.buttonpressed(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(g_btnLocation)
        
        
        let btnSearch = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 30, height: 50))
        btnSearch.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 410)
        btnSearch.backgroundColor = DollColor().GREEN
        btnSearch.layer.borderColor = UIColor.lightGray.cgColor
        btnSearch.layer.borderWidth = 1
        btnSearch.setTitle("搜尋", for: .normal)
        btnSearch.layer.cornerRadius = 10
        btnSearch.addTarget(self, action: #selector(self.fnSearchDown(sender:)), for: [.touchDown])
        btnSearch.addTarget(self, action: #selector(self.fnSearchClick(sender:)), for: [.touchUpInside, .touchUpOutside])
        self.view.addSubview(btnSearch)
    }
    
    func fnInitPickerView() {
        g_pickerView = UIPickerView(frame: CGRect(x: 0, y: HEIGHT, width: WIDTH, height: 300))
        
        //g_pickerView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 220)
        g_pickerView.delegate = self
        g_pickerView.dataSource = self
        //g_pickerView.backgroundColor = DollColor().BLUE
        self.view.addSubview(g_pickerView)
    }
    
    @objc func buttonpressed(sender: UIButton) {
        g_pickerView.frame.origin.y == self.HEIGHT ? fnViewslide(false) : fnViewslide(true)
    }
    
    @objc func fnSearchDown(sender: UIButton) {
        sender.backgroundColor = DollColor().GREEN_T
    }
    
    @objc func fnSearchClick(sender: UIButton) {
        sender.backgroundColor = DollColor().GREEN
        let searchController = SearchController()
        searchController.g_sArea = g_sArea
        searchController.g_sLocation = g_sLocation
        searchController.g_sKeyWord = g_fieldKeyWord.text
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(searchController, animated: true)
        self.hidesBottomBarWhenPushed = false
    }
    
    func fnViewslide(_ BOOL: Bool, completion: ((Bool) -> Void)! = nil) {
        UIView.animate(withDuration: 0.3,
                       delay: 0,
                       options: [.curveEaseInOut],
                       animations: { () -> Void in
                        self.g_pickerView.frame.origin.y = BOOL ? self.HEIGHT : self.HEIGHT - 300
                        
        },completion: nil)
    }
    
    func pickerView(_ pickerView: UIPickerView,
                    titleForRow row: Int,
                    forComponent component: Int) -> String? {
        return component == 0 ? AREA[row] : LOCATION[row]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return component == 0 ? AREA.count : LOCATION.count
    }
    
    func pickerView(_ pickerView: UIPickerView,
                    didSelectRow row: Int, inComponent component: Int) {
        if component == 0 {
            g_sArea = AREA[row]
            LOCATION = g_dollShopSql.fnGetLocationData(sArea: g_sArea == "全部" ? "%" : g_sArea)
            g_btnArea.setTitle(AREA[row], for: .normal)
            g_sLocation = "全部"
            g_btnLocation.setTitle("全部", for: .normal)
            g_pickerView.reloadAllComponents()
        } else if component == 1 {
            g_sLocation = LOCATION[row]
            g_btnLocation.setTitle(LOCATION[row], for: .normal)
        }
    }
}

