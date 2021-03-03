//
//  SearchController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/25.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit

class SearchController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    private var g_tableView: UITableView!
    private var g_shopDatas: [ShopData]!
    var g_sKeyWord: String!
    var g_sArea: String!
    var g_sLocation: String!

    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    func fnInit() {
        let dollShopSql = DollShopSql()
        if !dollShopSql.fnConnection() {
            return
        }
        g_shopDatas = dollShopSql.fnGetSearchData(sKeyWord: g_sKeyWord,
                                    sArea: g_sArea == "全部" ? "%" : g_sArea,
                                    sLocation: g_sLocation == "全部" ? "%" : g_sLocation)
    }
    
    func fnInitView() -> Void {
        fnInitTableView()
    }
    
    func fnInitTableView() {
        let fullScreenSize = UIScreen.main.bounds.size
        g_tableView = UITableView(frame: CGRect(x: 0, y: 0, width: fullScreenSize.width,height: fullScreenSize.height)
            ,style: .plain)
        let nib = UINib(nibName: "TableViewCellStyle1", bundle: nil)
        g_tableView.register(nib, forCellReuseIdentifier: "Cell")
        g_tableView.delegate = self
        g_tableView.dataSource = self
        g_tableView.separatorStyle = .singleLine
        g_tableView.allowsSelection = true
        g_tableView.allowsMultipleSelection = false
        self.view.addSubview(g_tableView)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return g_shopDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! TableViewCellStyle1
        let shopData = g_shopDatas[indexPath.row]
        cell.g_labShopName.text = shopData.ShopName
        cell.g_labAddress.text = shopData.Address
        cell.g_btnGps.tag = indexPath.row
        cell.g_btnGps.addTarget(self, action: #selector(self.fnOpenGPSController(sender:)), for: [.touchUpInside, .touchUpOutside])
        cell.g_imgFirstImage.image = UIImage(named: "love.png")
        cell.g_imgFirstImage.isHidden = shopData.MyLove != "1"
        cell.g_imgShopImage.image  = UIImage(named: "logo.png")

        let urlSession = URLSession.shared
        let request = NSMutableURLRequest(url: NSURL(string: DataInfo.fnGetShopURLString(shopData.AddressNo))! as URL)
        let taskThread = urlSession.dataTask(with: request as URLRequest,completionHandler: {data, response, error -> Void in
            if error != nil {
                print("Error: " + error.debugDescription)
            } else {
                let image = UIImage.init(data :data!)
                if image != nil {
                    DispatchQueue.main.async {
                        cell.g_imgShopImage?.image = image
                    }
                }
            }
        })
        taskThread.resume()
        return cell
    }
    
    @objc func fnOpenGPSController(sender: UIButton) {
        let shopData = g_shopDatas[sender.tag]
        let mapController = MapController()
        mapController.g_shopData = shopData
        //self.present(navController, animated: true, completion: nil)
        self.navigationController?.pushViewController(mapController, animated: true)
    }
    
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let dollColor = DollColor()
        let top = UITableViewRowAction(style: .normal, title: "評價") {
            action, index in
            self.hidesBottomBarWhenPushed = true
            let evaluationController = EvaluationController()
            evaluationController.g_sAddressNo = self.g_shopDatas[index.row].AddressNo
            evaluationController.modalPresentationStyle = .overCurrentContext
            self.navigationController?.pushViewController(evaluationController, animated: true)
            self.hidesBottomBarWhenPushed = false
            //self.present(EvaluationController(), animated: true, completion: nil)
        }
        top.backgroundColor = dollColor.BLUE
        
        let readed = UITableViewRowAction(style: .normal, title: "內容") {
            action, index in
            let shopDataContentController = ShopDataContentController()
            shopDataContentController.g_shopData = self.g_shopDatas[index.row]
            self.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(shopDataContentController, animated: true)
            self.hidesBottomBarWhenPushed = false
            
        }
        readed.backgroundColor = dollColor.ORANGE
        
        var loveTitle = "加入最愛"
        if g_shopDatas[indexPath.row].MyLove == "1" {
            loveTitle = "取消最愛"
        }
        
        let love = UITableViewRowAction(style: .normal, title: loveTitle) {
            action, index in
            if self.fnUpdateLove(self.g_shopDatas[index.row]) {
                self.g_tableView.reloadRows(at: [indexPath], with: .fade)
            }
        }
        
        love.backgroundColor = dollColor.RED
        
        return [readed, top, love]
    }
    
    func fnUpdateLove(_ shopData: ShopData) -> Bool {
        let dollShop = DollShopSql()
        if !dollShop.fnConnection() {
            return false
        }
        
        if shopData.MyLove == "1" {
            shopData.MyLove = "0"
        } else {
            shopData.MyLove = "1"
        }
        return dollShop.fnUpdateMyLove(sMyLove: shopData.MyLove, sAddressNo: shopData.AddressNo)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let shopDataContentController = ShopDataContentController()
        shopDataContentController.g_shopData = g_shopDatas[indexPath.row]
        self.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(shopDataContentController, animated: true)
        self.hidesBottomBarWhenPushed = false
    }
    
    func tableView(_ tableView: UITableView,heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80
    }
    
}
