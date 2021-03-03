//
//  ResponseController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/28.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class ResponseController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {
    private var g_view: UIView!
    private var g_isBottom = false
    private var g_isDown = true
    private var g_fieldInput: UITextField!
    private var panGestureRecongnizer: UIPanGestureRecognizer!
    var interactor: Interactor? = nil
    private var g_themeData: ThemeData!
    private var g_responseDatas = [ResponseData]()
    private var g_tableView: UITableView!
    private var g_iEnd = 0
    private var g_iStart = 0
    private var g_iNowSize = 0
    private var GET_DATA_SIZE = 10
    private var animatorType = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        if !g_isDown && NSStringFromClass((touch.view?.classForCoder)!) == "UITableViewCellContentView" {
            return false
        }
        return true
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
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
        fnInitContentView()
        fnInitTabelView()
        fnInitInput()
        self.transitioningDelegate = self
        panGestureRecongnizer = UIPanGestureRecognizer(target: self, action: #selector(ResponseController.handlePanGesture(sender:)))
        self.view.addGestureRecognizer(panGestureRecongnizer)
        g_tableView.addGestureRecognizer(panGestureRecongnizer)
        panGestureRecongnizer.delegate = self
        self.navigationController?.isToolbarHidden = true
        view.backgroundColor = UIColor(red: 0
            , green: 0
            , blue: 0
            , alpha: 0.5)
    }
    
    func fnInitContentView() {
        let fullScreenSize = UIScreen.main.bounds.size

        g_view = UIView(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width - 20, height: fullScreenSize.height - 40))
        g_view.center = CGPoint(x: fullScreenSize.width * 0.5, y: fullScreenSize.height * 0.5)
        g_view.layer.cornerRadius = 10
        g_view.backgroundColor = UIColor.white
        view.addSubview(g_view)
    }
    
    @objc func handlePanGesture(sender: UIPanGestureRecognizer) {
        //1
        let percentThreshold:CGFloat = 0.3
        //2
        let translation = sender.translation(in: view)
        let verticalMovement = (animatorType == 0 ? translation.y : -translation.y) / view.bounds.height
        let downwardMovement = fmaxf(Float(verticalMovement),0.0)
        let downwardMovementPercent = fminf(downwardMovement, 1.0)
        let progress = CGFloat(downwardMovementPercent)

        guard let interactor = interactor else { return }

        
        //3
        switch sender.state {
        case .began:
            interactor.hasStarted = true
            self.dismiss(animated: true, completion: nil)
        case .changed:
            interactor.shouldFinish = abs(progress) > percentThreshold
            interactor.update(progress)
        case .ended:
            interactor.hasStarted = false
            interactor.shouldFinish ? interactor.finish() : interactor.cancel()
        case .cancelled:
            interactor.hasStarted = false
            interactor.cancel()
        default:
            break
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {

        if(scrollView.contentOffset.y >= scrollView.contentSize.height - scrollView.bounds.height && g_isBottom) {
            g_isDown = true
            animatorType = 1
        } else if (scrollView.contentOffset.y <= 0) {
            g_isDown = true
            animatorType = 0
        } else {
            g_isDown = false
            //animatorType = 0
        }
    }

    
    func fnInit() {
        fnGetResponseData()
    }
    
    func fnSetThemeData(_ themeData: ThemeData) {
        g_themeData = themeData
        fnSetDataRange(iEnd: Int(g_themeData.ResponseCount)!)
        
    }
    
    private func fnSetDataRange(iEnd: Int) {
        self.g_iEnd = iEnd
        self.g_iStart = iEnd - GET_DATA_SIZE
        g_iNowSize += GET_DATA_SIZE
    }
    
    private func fnGetResponseData() {
        let urlSession = URLSession.shared
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        let soapData: SoapData = self.fnGetResponseDataArg()
        let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnGetResponseData", soapData: soapData)
        
        let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let xmlEngine: XmlEngine = XmlEngine(data!)
                let sDatas = xmlEngine.fnGetSplitData(iSize: 6)
                self.fnResponseDataResult(sDatas)
            }
        })
        taskThread.resume()
    }
    
    private func fnGetResponseDataArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sPlateId", sValue: g_themeData.PlateId)
        soapData.fnAdd(sKey: "sThemeId", sValue: g_themeData.ThemeId)
        soapData.fnAdd(sKey: "sStart", sValue: String(g_iStart))
        soapData.fnAdd(sKey: "sEnd", sValue: String(g_iEnd))
        return soapData
    }
    
    private func fnResponseDataResult(_ sAllDatas: [[String]]) {
        if sAllDatas.count == 0 {
            return
        }
        var indexPaths = [IndexPath]()
        for sDatas in sAllDatas {
            let indexPath = IndexPath(row: g_responseDatas.count, section: 0)
            g_responseDatas.append(ResponseData(sPlateId: g_themeData.PlateId, sThemeId: g_themeData.ThemeId, sDatas: sDatas))
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
        let fullScreenSize = g_view.bounds.size
        g_tableView = UITableView(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width, height: fullScreenSize.height - 70)
            ,style: .plain)
        g_tableView.center = CGPoint(x: fullScreenSize.width * 0.5, y: fullScreenSize.height * 0.5 - 30)
        let nib = UINib(nibName: "TableViewCellStyle3", bundle: nil)
        g_tableView.register(nib, forCellReuseIdentifier: "Cell")
        g_tableView.delegate = self
        g_tableView.dataSource = self
        g_tableView.separatorStyle = .singleLine
        g_tableView.allowsSelection = true
        g_tableView.allowsMultipleSelection = false
        
        g_view.addSubview(g_tableView)
    }
    
    func fnInitInput() {
        let fullScreenSize = g_view.bounds.size
        g_fieldInput = UITextField(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width - 50, height: 40))
        g_fieldInput.center = CGPoint(x: fullScreenSize.width * 0.5 - 20, y: fullScreenSize.height - 30)
        g_fieldInput.leftViewMode = .always
        g_fieldInput.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 40))
        g_fieldInput.layer.borderWidth = 1
        g_fieldInput.placeholder = "請輸入"
        g_fieldInput.layer.borderColor = UIColor.darkText.cgColor
        g_fieldInput.layer.cornerRadius = 10
        g_view.addSubview(g_fieldInput)
        
        let btnSend = UIButton(frame: CGRect(x: 0, y: 0, width: 38, height: 38))
        btnSend.center = CGPoint(x: fullScreenSize.width - 24, y: fullScreenSize.height - 30)
        btnSend.setBackgroundImage(UIImage(named: "send.png"), for: .normal)
        btnSend.addTarget(nil, action: #selector(self.fnSend), for: [.touchUpInside, .touchUpOutside])
        g_view.addSubview(btnSend)
        
    }
    
    @objc func fnSend() {
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return g_responseDatas.count
    }
    
    //    func numberOfSections(in tableView: UITableView) -> Int {
    //        return g_hotShopPresenter.shopDatas.count
    //    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! TableViewCellStyle3
        let responseData = g_responseDatas[indexPath.row]

        cell.g_imgUser.image  = UIImage(named: "logo.png")
        cell.g_labContent.text = responseData.Content
        cell.g_labUserName.text = responseData.Name
        cell.g_labDateTime.text = responseData.Date
        
        DispatchQueue.global().async {
            Image().fnUrlSetImage(sUrl: "\(DataInfo.WEB_SERVICE_USER_URL)\(responseData.Account).jpg", imgData: cell.g_imgUser)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if g_iStart < 0 {
            g_isBottom = true
            return
        }
        
        if indexPath.row == g_iNowSize - 1 {
            fnSetDataRange(iEnd: g_iStart)
            fnGetResponseData()
        }
    }
    
    func tableView(_ tableView: UITableView,heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 83
    }
    
}

extension ResponseController: UIViewControllerTransitioningDelegate {
    func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        switch animatorType {
        case 0:
            return DismissAnimator1()
        case 1:
            return DismissAnimator2()
        default:
            return DismissAnimator1()
        }
    }
    
    func interactionControllerForDismissal(using animator: UIViewControllerAnimatedTransitioning) -> UIViewControllerInteractiveTransitioning? {
        guard let interactor = self.interactor else {
            return nil
        }
        return interactor.hasStarted ? interactor : nil
    }
}
