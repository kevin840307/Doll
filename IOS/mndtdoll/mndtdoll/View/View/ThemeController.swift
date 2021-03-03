//
//  ThemeController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/25.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit
class ThemeController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    private var g_tableView: UITableView!
    private var g_themeDatas = [ThemeData]()
    private var g_iEnd = 0
    private var g_iStart = 0
    private var g_iNowSize = 0
    private var GET_DATA_SIZE = 10
    private var g_iType = -1
    private let g_iTitles = ["置頂區", "閒聊區", "娃娃機台出租區", "夾物交易區", "問題回報區", "夾點分享區", "我的主題"]
    
    public func fnSetType(_ type: Int) {
        g_iType = type
        self.navigationItem.title = g_iTitles[type]
    }
    
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
    
    func fnInitView() {
        fnInitTabelView()
    }

    func fnInit() {
        fnInitTopData()
    }
    
    private func fnInitTopData() {
        let queueThread = OperationQueue()
        queueThread.addOperation {
            let urlSession = URLSession.shared
            let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
            let soapData: SoapData = self.fnGetGetFirstThemeArg()
            let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetFirstThemeData", soapData: soapData)
            
            let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
                if error != nil {
                    print("Error: " + error.debugDescription)
                } else {
                    let xmlEngine: XmlEngine = XmlEngine(data!)
                    var sDatas = xmlEngine.fnGetSplitData(iSize: 10)
                    self.fnTopResult(&sDatas)
                    self.fnInitMax()
                }
            })
            taskThread.resume()
        }
    }
    
    private func fnGetGetFirstThemeArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sPlateId", sValue: String(g_iType))
        return soapData
    }
    
    private func fnTopResult(_ sAllDatas: inout [[String]]) {
        if sAllDatas.count == 0 {
            return
        }
        
        var indexPaths = [IndexPath]()
        for sDatas in sAllDatas {
            let indexPath = IndexPath(row: g_themeDatas.count, section: 0)
            g_themeDatas.append(ThemeData(sDatas: sDatas))
            indexPaths.append(indexPath)
        }
        fnInsertTable(indexPaths)
    }
    
    private func fnInitMax() {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapData: SoapData = self.fnGetThemeCountArg()
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetCodeValue", soapData: soapData)
            
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                let sDatas = xmlEngine.fnGetAllData()
                self.fnSetDataRange(iEnd: Int(sDatas[0])!)
                self.fnGetThemeData()
            }
        })
        taskThread.resume()
    }
    
    private func fnSetDataRange(iEnd: Int) {
        self.g_iEnd = iEnd
        self.g_iStart = iEnd - GET_DATA_SIZE
        g_iNowSize += GET_DATA_SIZE
    }
    
    private func fnGetThemeCountArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sCodeKind", sValue: "1")
        soapData.fnAdd(sKey: "sCode", sValue: String(g_iType))
        return soapData
    }
    
    private func fnGetThemeData() {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapData: SoapData = self.fnGetThemeDataArg()
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetThemeData", soapData: soapData)
        
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                let sDatas = xmlEngine.fnGetSplitData(iSize: 9)
                self.fnThemeDataResult(sDatas)
            }
        })
        taskThread.resume()
    }
    
    private func fnGetThemeDataArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sPlateId", sValue: String(g_iType))
        soapData.fnAdd(sKey: "sStart", sValue: String(g_iStart))
        soapData.fnAdd(sKey: "sEnd", sValue: String(g_iEnd))
        return soapData
    }
    
    private func fnThemeDataResult(_ sAllDatas: [[String]]) {
        if sAllDatas.count == 0 {
            return
        }
        var indexPaths = [IndexPath]()

        for sDatas in sAllDatas {
            let indexPath = IndexPath(row: g_themeDatas.count, section: 0)
            self.g_themeDatas.append(ThemeData(sPlateId: String(g_iType), sDatas: sDatas))
            indexPaths.append(indexPath)
        }
        fnInsertTable(indexPaths)
    }
    
    private func fnInsertTable(_ indexPaths: [IndexPath]) {
        DispatchQueue.main.async {
            self.g_tableView.insertRows(at: indexPaths, with: .automatic)
        }
    }
    
    func fnInitTabelView() {
        let fullScreenSize = UIScreen.main.bounds.size
        g_tableView = UITableView(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width,height: fullScreenSize.height)
            ,style: .plain)
        let nib = UINib(nibName: "TableViewCellStyle2", bundle: nil)
        g_tableView.register(nib, forCellReuseIdentifier: "Cell")
        g_tableView.delegate = self
        g_tableView.dataSource = self
        g_tableView.separatorStyle = .singleLine
        g_tableView.allowsSelection = true
        g_tableView.allowsMultipleSelection = false
        self.view.addSubview(g_tableView)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let themeViewController = ThemeViewController()
        themeViewController.g_themeData = g_themeDatas[indexPath.row]
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(themeViewController, animated: true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return g_themeDatas.count
    }
    
    //    func numberOfSections(in tableView: UITableView) -> Int {
    //        return g_hotShopPresenter.shopDatas.count
    //    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! TableViewCellStyle2
        let themeData = g_themeDatas[indexPath.row]
        
        cell.g_imgIcon.isHidden = themeData.ArticleType == "1"
        cell.g_imgUser.image  = UIImage(named: "logo.png")
        cell.g_labTitle.text = themeData.Title
        cell.g_labContent.text = themeData.Content
        cell.g_labUserName.text = themeData.Name
        cell.g_labDateTime.text = themeData.Date
        cell.g_labMsgCount.text = themeData.ResponseCount

        DispatchQueue.global().async {
            Image().fnUrlSetImage(sUrl: "\(DataInfo.WEB_SERVICE_USER_URL)\(themeData.Account).jpg", imgData: cell.g_imgUser)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if g_iStart < 0 {
            return
        }
        
        if indexPath.row == g_iNowSize - 1 {
            fnSetDataRange(iEnd: g_iStart)
            fnGetThemeData()
        }
    }
    
    func tableView(_ tableView: UITableView,heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 85
    }
}
