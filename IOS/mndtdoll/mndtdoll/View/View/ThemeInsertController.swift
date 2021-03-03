//
//  ThemeInsertController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/25.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit
import Photos

class ThemeInsertController:  UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout  {
    private var g_assets: [PHAsset]! = [PHAsset]()
    private var g_btnType: UIButton!
    private var g_btnArea: UIButton!
    private var g_btnFormat: UIButton!
    private var g_pickerView: UIPickerView!
    private var g_fieldTitle: UITextField!
    private var g_textContent: UITextView!
    private var g_collectionview: UICollectionView!
    private var g_nowPicker = -1
    private let THEME = ["閒聊區", "娃娃機台出租區", "夾物交易區", "問題回報區", "夾點分享區"]
    private let AREA = ["【南部】", "【中部】", "【北部】", "【東部】"]
    private let FORMAT = ["無", "格式1", "格式2"]
    private let FORMAT_CONTENT = [""
                                ,"【地點】：\n" +
                                "【機台數量】：\n" +
                                "【租金】：\n" +
                                "【壓金】：\n" +
                                "【場內特色,規定】：\n" +
                                "【預計開場營業時間】：\n" +
                                "【備註】：\n"
                                ,"【賣】：\n" +
                                "【價格】：\n" +
                                "【交易方式】：\n" +
                                "【備註】：\n"]
    private var g_iTheme = 1
    private var g_iArea = 0
    private var g_iFormat = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.didReceiveMemoryWarning()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func fnInitView() {
        fnInitRightButton()
        fnInitImageView()
        fnInitLabel()
        fnInitButton()
        fnInitTextField()
        fnInitTextView()
        fnInitCollectionview()
        fnInitPickerView()
    }
    
    func fnInitRightButton() {
        let sendButton = UIBarButtonItem(title: "發布", style: .done, target: self, action: #selector(self.fnSend))
        self.navigationItem.rightBarButtonItem = sendButton
    }
    
    func fnInitImageView() {
        let imgLogoView = UIImageView()
        imgLogoView.frame = CGRect(x: 0, y: 0, width: 80, height: 80)
        imgLogoView.center = CGPoint(x: 50, y: 100)
        imgLogoView.image = UIImage(named: "logo.png")
        imgLogoView.layer.masksToBounds = true
        imgLogoView.layer.cornerRadius = imgLogoView.frame.width / 2
        view.addSubview(imgLogoView)
        fnSetImage(imgLogoView)
    }
    
    func fnInitLabel() {
        let sizeFullScreen = view.bounds.size
        let labName = UILabel()
        labName.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width - 110, height: 40)
        labName.center = CGPoint(x: sizeFullScreen.width * 0.5 + 40, y: 80)
        labName.text = UserData.fnGetName()
        labName.font = UIFont(name: labName.font.familyName, size: 20)
        view.addSubview(labName)
    }
    
    func fnInitPickerView() {
        let sizeFullScreen = view.bounds.size
        g_pickerView = UIPickerView(frame: CGRect(x: 0, y: sizeFullScreen.height, width: sizeFullScreen.width, height: 300))
        
        //g_pickerView.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 220)
        g_pickerView.delegate = self
        g_pickerView.dataSource = self
        g_pickerView.backgroundColor = UIColor.white
        //g_pickerView.backgroundColor = DollColor().BLUE
        self.view.addSubview(g_pickerView)
    }
    
    
    func fnInitButton() {
        let sizeFullScreen = view.bounds.size
        let dollColor = DollColor()
        g_btnType = UIButton()
        g_btnType.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width - 110, height: 40)
        g_btnType.center = CGPoint(x: sizeFullScreen.width * 0.5 + 45, y: 120)
        g_btnType.titleEdgeInsets.left = 10
        g_btnType.setTitle("閒聊區", for: .normal)
        g_btnType.contentHorizontalAlignment = .left
        g_btnType.setTitleColor(UIColor.darkGray,for: .normal)
        g_btnType.layer.cornerRadius = 10
        g_btnType.layer.borderWidth = 1
        g_btnType.layer.borderColor = UIColor.darkGray.cgColor
        g_btnType.tag = 0
        g_btnType.addTarget(self, action: #selector(self.buttonpressed(sender:)), for: [.touchUpInside, .touchUpOutside])
        view.addSubview(g_btnType)
        
        
        g_btnArea = UIButton()
        g_btnArea.frame = CGRect(x: 0, y: 0, width: 80, height: 40)
        g_btnArea.center = CGPoint(x: 50, y: 170)
        g_btnArea.setTitle("【南部】", for: .normal)
        g_btnArea.setTitleColor(UIColor.darkGray,for: .normal)
        g_btnArea.layer.cornerRadius = 10
        g_btnArea.layer.borderWidth = 1
        g_btnArea.layer.borderColor = UIColor.darkGray.cgColor
        g_btnArea.tag = 1
        g_btnArea.addTarget(self, action: #selector(self.buttonpressed(sender:)), for: [.touchUpInside, .touchUpOutside])
        view.addSubview(g_btnArea)
        
        
        g_btnFormat = UIButton()
        g_btnFormat.frame = CGRect(x: 0, y: 0, width: sizeFullScreen.width - 20, height: 40)
        g_btnFormat.center = CGPoint(x: sizeFullScreen.width * 0.5, y: 220)
        g_btnFormat.setTitle("無", for: .normal)
        g_btnFormat.setTitleColor(UIColor.darkGray,for: .normal)
        g_btnFormat.layer.cornerRadius = 10
        g_btnFormat.layer.borderWidth = 1
        g_btnFormat.layer.borderColor = UIColor.darkGray.cgColor
        g_btnFormat.tag = 2
        g_btnFormat.addTarget(self, action: #selector(self.buttonpressed(sender:)), for: [.touchUpInside, .touchUpOutside])
        view.addSubview(g_btnFormat)
        
        
        let btnSelectImage = UIButton()
        btnSelectImage.frame = CGRect(x: 0, y: 0, width: 100, height: 70)
        btnSelectImage.center = CGPoint(x: 60, y: sizeFullScreen.height - 50)
        btnSelectImage.setTitle("選擇圖片", for: .normal)
        btnSelectImage.backgroundColor = dollColor.BLUE
        btnSelectImage.setTitleColor(UIColor.white,for: .normal)
        btnSelectImage.layer.cornerRadius = 10
        btnSelectImage.addTarget(self, action: #selector(self.fnBlueDown(sender:)), for: [.touchDown])
        btnSelectImage.addTarget(self, action: #selector(self.fnBlueUp(sender:)), for: [.touchUpInside, .touchUpOutside])
        btnSelectImage.addTarget(nil, action: #selector(self.fnSelectImage), for: [.touchUpInside, .touchUpOutside])
        view.addSubview(btnSelectImage)
    }
    
    @objc func fnBlueDown(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.BLUE_T
    }
    
    @objc func fnBlueUp(sender: UIButton) {
        let dollColor = DollColor()
        sender.backgroundColor = dollColor.BLUE
    }
    
    func fnInitTextField() {
        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_fieldTitle = UITextField(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 110, height: 40))
        g_fieldTitle.center = CGPoint(x: sizeFullScreen.width * 0.5 + 45, y: 170)
        g_fieldTitle.placeholder = "請輸入標題"
        g_fieldTitle.clearButtonMode = .whileEditing
        g_fieldTitle.returnKeyType = .done
        //g_fieldTitle.textColor = UIColor.lightGray
        g_fieldTitle.layer.borderColor = UIColor.darkGray.cgColor
        g_fieldTitle.layer.borderWidth = 1
        g_fieldTitle.layer.cornerRadius = 10
        g_fieldTitle.leftViewMode = .always
        g_fieldTitle.leftView = UIView(frame: CGRect(x: 0, y: 0,  width: 10, height: 40))
        g_fieldTitle.backgroundColor = UIColor.white
        view.addSubview(g_fieldTitle)
    }
    
    func fnInitTextView() {
        let sizeFullScreen = view.bounds.size
        g_textContent = UITextView(frame: CGRect(x: 0, y: 0,  width: sizeFullScreen.width - 20, height: sizeFullScreen.height - 350))
        g_textContent.center = CGPoint(x: sizeFullScreen.width * 0.5, y: sizeFullScreen.height * 0.5 + 80)
        g_textContent.returnKeyType = .done
        //g_textContent.textColor = UIColor.lightGray
        g_textContent.layer.borderColor = UIColor.black.cgColor
        g_textContent.layer.borderWidth = 1
        g_textContent.layer.cornerRadius = 10
        g_textContent.backgroundColor = UIColor.white
        //let textViewDelegate = TextViewDelegate(view: g_textContent, fMoveOffset: 420)
        //g_textContent.delegate = textViewDelegate
        view.addSubview(g_textContent)
    }
    
    @objc func fnSend() {
        let queueThread = OperationQueue()
        queueThread.addOperation {
            let urlSession = URLSession.shared
            let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
            let soapData: SoapData = self.fnGetInsertArg()
            let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnInsertThemeData", soapData: soapData)
            
            let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
                if error != nil {
                    print("Error: " + error.debugDescription)
                } else {
                    let xmlEngine: XmlEngine = XmlEngine(data!)
                    var sDatas = xmlEngine.fnGetAllData()
                    self.fnInsertResult(result: sDatas[0])
                }
            })
            taskThread.resume()
        }
    }
    
    private func fnGetInsertArg() -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sIMEI", sValue: "356939076001089")
        soapData.fnAdd(sKey: "sAccount", sValue: UserData.fnGetAccount())
        soapData.fnAdd(sKey: "sPwd", sValue: UserData.fnGetMD5Password())
        soapData.fnAdd(sKey: "sPlateId", sValue: String(g_iTheme))
        soapData.fnAdd(sKey: "sTitle", sValue: "\(AREA[g_iArea])\(g_fieldTitle.text!)")
        soapData.fnAdd(sKey: "sContent", sValue: String(g_textContent.text!))
        soapData.fnAdd(sKey: "sPicAmount", sValue: String(g_assets.count))
        return soapData
    }
    
    func fnInsertResult(result: String) {

        if result != "N" {
            fnSendImage(result)
            return
        }
        
        let alertController = UIAlertController(title: "訊息", message: "新增失敗", preferredStyle: .actionSheet)
        let okAction = UIAlertAction(
            title: "確認",
            style: .default) {
                (action: UIAlertAction!) -> Void in
                self.navigationController?.popViewController(animated: true)
        }
        
        alertController.addAction(okAction)
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
     func fnSendImage(_ sThemeId: String) {
        if g_assets.count == 0 {
            Sussace()
            return
        }
        
        let imageEngine = Image()
        let soapEngine: SoapEngine = SoapEngine(sUrl: DataInfo.WEB_SERVICE_URL)
        var count = 0
        for index in 0...g_assets.count - 1 {
            objc_sync_enter(self)
            let urlSession = URLSession.shared
            let image = fnGetOriginalAssetUIImage(g_assets[index])
            let soapData: SoapData = self.fnGetImageArg(sThemeId: sThemeId, index: String(index), sStrBase64: imageEngine.fnUIImageToBase64(imgData: image)!)
            let soapRequest = soapEngine.fnGetRequest(sFunctions: "fnUpdateThemeImage", soapData: soapData)
            let taskThread = urlSession.dataTask(with: soapRequest as URLRequest, completionHandler: {data, response, error -> Void in
                if error != nil {
                    print("Error: " + error.debugDescription)
                } else {
                    //let xmlEngine: XmlEngine = XmlEngine(data!)
                    //var sDatas = xmlEngine.fnGetAllData()
                    //self.fnInsertResult(result: sDatas[0])
                    count += 1
                }
                objc_sync_exit(self)
            })
            taskThread.resume()
        }
        Sussace()
    }
    
    func Sussace() {
        let alertController = UIAlertController(title: "訊息", message: "新增成功", preferredStyle: .actionSheet)
        let okAction = UIAlertAction(
            title: "確認",
            style: .default) {
                (action: UIAlertAction!) -> Void in
                self.navigationController?.popViewController(animated: true)
        }
        
        alertController.addAction(okAction)
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    private func fnGetImageArg(sThemeId: String, index: String, sStrBase64: String) -> SoapData {
        let soapData: SoapData = SoapData()
        soapData.fnAdd(sKey: "sPlateId", sValue: String(g_iTheme))
        soapData.fnAdd(sKey: "sThemeId", sValue: sThemeId)
        soapData.fnAdd(sKey: "sStrBase64", sValue: sStrBase64)
        soapData.fnAdd(sKey: "sIndex", sValue: index)
        return soapData
    }
    
    func fnSetImage(_ imgView: UIImageView) -> Void {
        if UserData.fnGetLoginType() == "2" {
            DispatchQueue.global().async {
                Image().fnUrlSetImage(sUrl: "\(DataInfo.WEB_SERVICE_USER_URL)\(UserData.fnGetAccount()!).jpg", imgData: imgView)
            }
        }
    }
    
    func fnInitCollectionview() {
        let sizeFullScreen = view.bounds.size
        
        let collectionViewFlowLayout = UICollectionViewFlowLayout()
        collectionViewFlowLayout.scrollDirection = .horizontal
        // frame
        g_collectionview = UICollectionView(frame: CGRect(x: 0,
                                                        y: 0,
                                                        width: sizeFullScreen.width - 130,
                                                        height: 70),
                                          collectionViewLayout: collectionViewFlowLayout)
        g_collectionview.center = CGPoint(x: sizeFullScreen.width * 0.5 + 55, y: sizeFullScreen.height - 50)
        
        // 委任
        g_collectionview.delegate = self
        g_collectionview.dataSource = self
        
        // 註冊cell, header, footer
        g_collectionview.register(UICollectionViewCell1.self,
                                forCellWithReuseIdentifier: "cell")
        g_collectionview.register(UICollectionReusableView.self,
                                forSupplementaryViewOfKind: UICollectionElementKindSectionHeader,
                                withReuseIdentifier: "header")
        g_collectionview.register(UICollectionReusableView.self,
                                forSupplementaryViewOfKind: UICollectionElementKindSectionFooter,
                                withReuseIdentifier: "footer")
        g_collectionview.backgroundColor = UIColor.white
        // 允許點擊
        g_collectionview.allowsSelection = true
        // 允許雙點擊
        g_collectionview.allowsMultipleSelection = false
        // 垂直指示器
        g_collectionview.showsVerticalScrollIndicator = false
        // 水平指示器
        g_collectionview.showsHorizontalScrollIndicator = true
        // 指示器樣式
        g_collectionview.indicatorStyle = .default
        // 可否滑動
        g_collectionview.isScrollEnabled = true
        
        // 預加載
        //g_collectionview.isPrefetchingEnabled = false
        g_collectionview.layer.borderWidth = 1
        g_collectionview.layer.cornerRadius = 10
        g_collectionview.layer.borderColor = UIColor.darkGray.cgColor
        let longPress = UILongPressGestureRecognizer(target: self, action: #selector(longPressGesture(_:)))
        g_collectionview.addGestureRecognizer(longPress)
        
        view.addSubview(g_collectionview)
    }
    
    @objc func longPressGesture(_ gesture :UILongPressGestureRecognizer) {
        let point = gesture.location(in: self.g_collectionview)
        let indexPath = self.g_collectionview.indexPathForItem(at: point)
        if indexPath == nil {
            return
        }
        let alertController = UIAlertController(
            title: "刪除",
            message: "確認要刪除嗎？",
            preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(
            title: "取消",
            style: .cancel,
            handler: nil)
        alertController.addAction(cancelAction)
        
        let okAction = UIAlertAction(
            title: "刪除",
            style: .destructive) {
                (action: UIAlertAction!) -> Void in
                self.g_assets.remove(at: indexPath!.row)
                self.g_collectionview.deleteItems(at: [indexPath!])
            }
        alertController.addAction(okAction)
        

        self.present(alertController, animated: true, completion: nil)
        


    }
    
    // 位置(top, left, bottom, right)
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsetsMake(5, 5, 5, 5)
    }
    // header frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return CGSize(width: 0, height: 40)
    }
    // footer frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForFooterInSection section: Int) -> CGSize {
            return CGSize(width: 0, height: 0)
    }
    // cell frame
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 60, height: 60)
    }
    // 垂直間距
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat{
        return 10
    }
    // 水平間距
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat{
        return 2.5
    }
    // header, footer 內容
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        return UICollectionReusableView()
    }
    // 組數
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    // 內容數
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return g_assets.count
    }
    // 內容
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell",
                                                      for: indexPath) as! UICollectionViewCell1
        cell.imageview.image = fnGetAssetUIImage(g_assets[indexPath.row])

        return cell
    }
    
    // 點選觸發
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let showImageController = ShowImageController()
        showImageController.g_index = indexPath.row
        showImageController.g_assets = g_assets
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(showImageController, animated: true)
    }
    
    func fnGetAssetUIImage(_ asset: PHAsset) -> UIImage {
        let manager = PHImageManager.default()
        let option = PHImageRequestOptions()
        var image = UIImage()
        option.isSynchronous = true
        manager.requestImage(for: asset, targetSize: CGSize(width: 50, height: 50), contentMode: .aspectFit, options: option, resultHandler: {(result, info)->Void in
            image = result!
        })
        return image
    }
    
    func fnGetOriginalAssetUIImage(_ asset: PHAsset) -> UIImage {
        let manager = PHImageManager.default()
        let option = PHImageRequestOptions()
        var image = UIImage()
        option.isSynchronous = true
        manager.requestImage(for: asset, targetSize: CGSize(width: asset.pixelWidth, height: asset.pixelHeight), contentMode: .aspectFit, options: option, resultHandler: {(result, info)->Void in
            image = result!
        })
        return image
    }
    
    @objc func fnSelectImage() {
        self.hidesBottomBarWhenPushed = true
        _ = self.presentHGImagePicker(maxSelected:10) { (assets) in
            if assets.count == 0 {
                return
            }
            self.g_assets = assets
            self.g_collectionview.reloadData()
        }
    }
    
    @objc func buttonpressed(sender: UIButton) {
        let sizeFullScreen = view.bounds.size
        if ((sender.tag == g_nowPicker) &&
            (g_pickerView.frame.origin.y != sizeFullScreen.height)) {
            fnViewslide(true)
        } else {
            g_nowPicker = sender.tag
            g_pickerView.reloadAllComponents()
            fnViewslide(false)
        }
    }
    
    func fnViewslide(_ BOOL: Bool, completion: ((Bool) -> Void)! = nil) {
        let sizeFullScreen = view.bounds.size
        UIView.animate(withDuration: 0.3,
                       delay: 0,
                       options: [.curveEaseInOut],
                       animations: { () -> Void in
                        self.g_pickerView.frame.origin.y = BOOL ? sizeFullScreen.height : sizeFullScreen.height - 300
                        
        },completion: nil)
    }
    
    func pickerView(_ pickerView: UIPickerView,
                    titleForRow row: Int,
                    forComponent component: Int) -> String? {
        return g_nowPicker == 0 ? THEME[row] : g_nowPicker == 1 ? AREA[row] : FORMAT[row]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return g_nowPicker == 0 ? THEME.count : g_nowPicker == 1 ? AREA.count : FORMAT.count
    }
    
    func pickerView(_ pickerView: UIPickerView,
                    didSelectRow row: Int, inComponent component: Int) {
        switch g_nowPicker {
        case 0:
            self.g_iTheme = row + 1
            g_btnType.setTitle(THEME[row], for: .normal)
            break
        case 1:
            self.g_iArea = row
            g_btnArea.setTitle(AREA[row], for: .normal)
            break
        case 2:
            self.g_iFormat = row
            g_btnFormat.setTitle(FORMAT[row], for: .normal)
            g_textContent.text = FORMAT_CONTENT[row]
            break
        default:
            break
        }
    }
}
