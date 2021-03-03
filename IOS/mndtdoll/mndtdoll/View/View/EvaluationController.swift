//
//  EvaluationController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/21.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class EvaluationController:  UIViewController, IEvaluationController, YTTStarRatingDelegate {
    var g_sAddressNo: String!
    private var g_hasData = false
    private var g_labelStart: UILabel!
    private var g_viewContent: UIView!
    private var g_textContent: UITextView!
    private var g_star: Float!
    private var g_btnSend: UIButton!
    private var g_btnCancel: UIButton!
    private var g_starView: YTTStarRatingView!
    private var g_evalSql: DollShopEvaluation!
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

    }
    
    override func viewWillDisappear(_ animated: Bool) {
        // super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInit() {
        g_evalSql = DollShopEvaluation()
        
        if !g_evalSql.fnConnection() {
            return
        }
        
        let evalData = g_evalSql.fnGetEvalData(sAccount: UserData.fnGetAccount(), sAddressNo: g_sAddressNo)
        
        if evalData == nil {
            return
        }
        
        g_hasData = true
        g_textContent.text = evalData!.Message
        g_star = Float(evalData!.Star)
        g_starView.start = CGFloat(g_star)
    }
    
    
    func fnInitView() {
        view.backgroundColor = UIColor(red: 0
            , green: 0
            , blue: 0
            , alpha: 0.5)
        fnInitContentView()
        fnInitLabel()
        fnInitStartView()
        fnInitTextView()
        fnInitButton()
    }
    
    func fnInitContentView() {
        let sizeFullScreen = UIScreen.main.bounds.size
        g_viewContent = UIView(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width - 60, height: 450))
        g_viewContent.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.5)
        g_viewContent.backgroundColor = UIColor.white
        g_viewContent.layer.masksToBounds = true
        g_viewContent.layer.cornerRadius = 10
        view.addSubview(g_viewContent)
    }
    
    func fnInitLabel() {
        let sizeFullScreen = g_viewContent.bounds.size
        let labTitle = UILabel(frame: CGRect(x: 0, y: 0, width: sizeFullScreen.width, height: 30))
        labTitle.font = UIFont(name: labTitle.font.familyName, size: 20)
        labTitle.textAlignment = .center
        labTitle.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 30)
        labTitle.text = "評價"
        g_viewContent.addSubview(labTitle)
        
        let labStar = UILabel()
        labStar.font = UIFont(name: labTitle.font.familyName, size: 16)
        labStar.translatesAutoresizingMaskIntoConstraints = false
        labStar.text = "評價："
        g_viewContent.addSubview(labStar)
        labStar.leftAnchor.constraint(equalTo: g_viewContent.leftAnchor, constant: 15).isActive = true
        labStar.topAnchor.constraint(equalTo: g_viewContent.topAnchor, constant: 65).isActive = true
        
        let labEval = UILabel()
        labEval.font = UIFont(name: labTitle.font.familyName, size: 16)
        labEval.translatesAutoresizingMaskIntoConstraints = false
        labEval.text = "評語："
        g_viewContent.addSubview(labEval)
        labEval.leftAnchor.constraint(equalTo: g_viewContent.leftAnchor, constant: 15).isActive = true
        labEval.topAnchor.constraint(equalTo: g_viewContent.topAnchor, constant: 105).isActive = true
    }
    
    func fnInitStartView() {
        let sizeFullScreen = g_viewContent.bounds.size
        g_starView = YTTStarRatingView(frame: CGRect(x: 0, y: 70, width: sizeFullScreen.width - 80, height: 30), delegate: self)
        g_starView.center = CGPoint(x: sizeFullScreen.width * 0.5 + 10, y: 75)
        g_viewContent.addSubview(g_starView)
        g_starView.rate = 0
    }
    
    func fnInitTextView() {
        let sizeFullScreen = g_viewContent.bounds.size
        g_textContent = UITextView(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 150))
        g_textContent.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 210)
        g_textContent.returnKeyType = .done
        //g_textContent.textColor = UIColor.lightGray
        g_textContent.layer.borderColor = UIColor.lightGray.cgColor
        g_textContent.layer.borderWidth = 1
        g_textContent.layer.cornerRadius = 10
        g_textContent.backgroundColor = UIColor.white
        //let textViewDelegate = TextViewDelegate(view: g_textContent, fMoveOffset: 420)
        //g_textContent.delegate = textViewDelegate
        g_viewContent.addSubview(g_textContent)
    }
    
    func fnInitButton() {
        let dollColor = DollColor()
        let sizeFullScreen = g_viewContent.bounds.size
        g_btnSend = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnSend.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 330)
        g_btnSend.backgroundColor = dollColor.ORANGE
        g_btnSend.tintColor = UIColor.white
        g_btnSend.setTitle("送出", for: .normal)
        g_btnSend.layer.cornerRadius = 10
        g_btnSend.addTarget(nil, action: #selector(self.fnSendDown), for: .touchDown)
        g_btnSend.addTarget(nil, action: #selector(self.fnSend), for: [.touchUpInside, .touchUpOutside])
        g_viewContent.addSubview(g_btnSend)
        
        g_btnCancel = UIButton(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width * 0.9, height: 50))
        g_btnCancel.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 390)
        g_btnCancel.backgroundColor = dollColor.BLUE
        g_btnCancel.tintColor = UIColor.white
        g_btnCancel.setTitle("取消", for: .normal)
        g_btnCancel.layer.cornerRadius = 10
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancelDown), for: .touchDown)
        g_btnCancel.addTarget(nil, action: #selector(self.fnCancel), for: [.touchUpInside, .touchUpOutside])
        g_viewContent.addSubview(g_btnCancel)
    }
    
    func yttStarRatingView(_ starRatingView: YTTStarRatingView, rate: CGFloat, star: CGFloat) {
        print(rate)
        print(star)
        g_star = Float(star)
    }
    
    func fnCloseController() {
        
    }
    
    @objc func fnSendDown() {
        let dollColor = DollColor()
        g_btnSend.backgroundColor = dollColor.ORANGE_T
    }
    
    @objc func fnSend() {
        let dollColor = DollColor()
        g_btnSend.backgroundColor = dollColor.ORANGE
        
        let queueThread = OperationQueue()
        queueThread.addOperation {
            var sFunction = "fnInsertEvaluation"
            if self.g_hasData {
                sFunction = "fnUpdateEvaluation"
            }
            let urlSession = URLSession.shared
            let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
            let soapData: SoapData = self.fnGetArg()
            let soapRequest = soapEngine.fnGetRequest(sFunctions: sFunction, soapData: soapData)
            
            let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
                if error != nil {
                    print("Error: " + error.debugDescription)
                } else {
                    let xmlEngine: XmlEngine = XmlEngine(data!)
                    var sDatas = xmlEngine.fnGetAllData()
                    self.fnUpdate(result: sDatas[0])
                }
            })
            taskThread.resume()
        }
        
        
    }
    
    private func fnGetArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sIMEI", sValue: "356939076001089")
        soapData.fnAdd(sKey: "sAccount", sValue: UserData.fnGetAccount())
        soapData.fnAdd(sKey: "sPwd", sValue: UserData.fnGetMD5Password())
        soapData.fnAdd(sKey: "sAddressNo", sValue: g_sAddressNo)
        soapData.fnAdd(sKey: "star", sValue: String(g_star))
        soapData.fnAdd(sKey: "sMessage", sValue: String(g_textContent.text))
        return soapData
    }
    
    private func fnUpdate(result: String) {
        var sMsg = "評價失敗"
        if result == "true" {
            let now = Date()
            let dateFormat = DateFormatter()
            dateFormat.dateFormat = "yyyy/MM/dd"
            let sDatas = [UserData.fnGetAccount()
                        , g_sAddressNo
                        , String(g_star)
                        , String(g_textContent.text)
                        , dateFormat.string(from: now)
                        , dateFormat.string(from: now)]
            g_evalSql.fnInsert(sData: sDatas as! [String])
            sMsg = "評價成功"
        }
        
        let alertController = UIAlertController(title: "訊息", message: sMsg, preferredStyle: .actionSheet)
        let okAction = UIAlertAction(
            title: "確認",
            style: .default,
            handler: nil)
        alertController.addAction(okAction)
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @objc func fnCancelDown() {
        let dollColor = DollColor()
        g_btnCancel.backgroundColor = dollColor.BLUE_T
    }
    
    @objc func fnCancel() {
         self.dismiss(animated: false, completion:nil)
    }
}
