//
//  MapController.swift
//  mndtdoll
//
//  Created by Ghost on 2019/3/18.
//  Copyright © 2019年 Ghost. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class MapController:  UIViewController, IMapController, MKMapViewDelegate, CLLocationManagerDelegate {
    
    var g_shopData: ShopData? = nil
    var g_locationManager :CLLocationManager!
    var g_mapView: MKMapView!
    var g_latitude: Double = 22.996675
    var g_longitude: Double = 120.222993
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fnInitView()
        fnInit()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        fnCallPermission()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        // super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        g_locationManager.stopUpdatingLocation()
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
            if annotation is MKUserLocation {
                // 建立可重複使用的 MKAnnotationView
                let reuseId = "MyPin"
                var pinView =
                    mapView.dequeueReusableAnnotationView(
                        withIdentifier: reuseId)
                if pinView == nil {
                    // 建立一個地圖圖示視圖
                    pinView = MKAnnotationView(
                        annotation: annotation,
                        reuseIdentifier: reuseId)
                    // 設置點擊地圖圖示後額外的視圖
                    pinView?.canShowCallout = false
                    // 設置自訂圖示
                    pinView?.image = UIImage(named:"gps.png")
                } else {
                    pinView?.annotation = annotation
                }
                
                return pinView
            } else {
                
                // 建立可重複使用的 MKPinAnnotationView
                let reuseId = "Pin"
                var pinView =
                    mapView.dequeueReusableAnnotationView(
                        withIdentifier: reuseId) as? MKPinAnnotationView
                if pinView == nil {
                    pinView = MKPinAnnotationView(annotation: annotation, reuseIdentifier: reuseId)
                    // 設置點擊大頭針後額外的視圖
                    pinView?.canShowCallout = true
                    // 會以落下釘在地圖上的方式出現
                    pinView?.animatesDrop = true
                    pinView?.pinTintColor = UIColor.blue
                    let leftIconView = UIImageView(frame: CGRect.init(x: 0, y: 0, width: 50, height: 50))
                    leftIconView.image = UIImage(named: "logo.png")
                    pinView?.leftCalloutAccessoryView = leftIconView
                    let btnLocaton = UIButton(type: .detailDisclosure)
                    btnLocaton.addTarget(self, action: #selector(self.fnPinClickEvent(sender:)), for: [.touchUpInside, .touchUpOutside])
                    pinView?.rightCalloutAccessoryView = btnLocaton
                } else {
                    pinView?.annotation = annotation
                }
                
                return pinView
            }
    }
    
    @objc func fnPinClickEvent(sender: UIButton) {
        fnCalcDirection(latitude: Double(g_shopData!.Latitude)!,
                        longitude: Double(g_shopData!.Longitude)!)
    }
    
    func fnCallPermission() {
        // 首次使用 向使用者詢問定位自身位置權限
        if CLLocationManager.authorizationStatus()
            == .notDetermined {
            // 取得定位服務授權
            g_locationManager.requestWhenInUseAuthorization()
            
            // 開始定位自身位置
            g_locationManager.startUpdatingLocation()
        }
            // 使用者已經拒絕定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .denied {
            // 提示可至[設定]中開啟權限
            let alertController = UIAlertController(
                title: "定位權限已關閉",
                message:
                "如要變更權限，請至 設定 > 隱私權 > 定位服務 開啟",
                preferredStyle: .alert)
            let okAction = UIAlertAction(
                title: "確認", style: .default, handler:nil)
            alertController.addAction(okAction)
            self.present(
                alertController,
                animated: true, completion: nil)
        }
            // 使用者已經同意定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .authorizedWhenInUse {
            // 開始定位自身位置
            g_locationManager.startUpdatingLocation()
        }
    }
    
    func fnInit() {
        // 建立一個地點圖示 (圖示預設為紅色大頭針)
        let objectAnnotation = MKPointAnnotation()

        objectAnnotation.coordinate = CLLocation(
            latitude: Double(g_shopData!.Latitude)!,
            longitude: Double(g_shopData!.Longitude)!).coordinate
        objectAnnotation.title = g_shopData?.ShopName
        objectAnnotation.subtitle = g_shopData?.Address

        g_mapView.addAnnotation(objectAnnotation)
        
    }
    
    func fnInitView() {
        fnInitMap()
        fnInitManager()
    }
    
    
    func fnInitMap() {

        let sizeFullScreen = UIScreen.main.bounds.size
        
        g_mapView = MKMapView(frame: CGRect(
            x: 0, y: 20,
            width: sizeFullScreen.width,
            height: sizeFullScreen.height - 20))
        g_mapView.delegate = self
        g_mapView.mapType = .standard
        g_mapView.showsUserLocation = true
        
        // 允許縮放地圖
        g_mapView.isZoomEnabled = true
        
        fnSetZoom(zoom: 0.005, latitude: Double(g_shopData!.Latitude)!,
                  longitude: Double(g_shopData!.Longitude)!)
        
        self.view.addSubview(g_mapView)
    }
    
    
    func fnSetZoom(_ zoom: Double) {
        let currentLocationSpan:MKCoordinateSpan = MKCoordinateSpanMake(zoom, zoom)
        let center:CLLocation = CLLocation(latitude: g_latitude, longitude: g_longitude)
        let currentRegion:MKCoordinateRegion =
            MKCoordinateRegion(center: center.coordinate, span: currentLocationSpan)
        g_mapView.setRegion(currentRegion, animated: true)
    }
    
    func fnSetZoom(zoom: Double, latitude: Double, longitude: Double) {
        let currentLocationSpan:MKCoordinateSpan = MKCoordinateSpanMake(zoom, zoom)
        let center:CLLocation = CLLocation(latitude: latitude, longitude: longitude)
        let currentRegion:MKCoordinateRegion =
            MKCoordinateRegion(center: center.coordinate, span: currentLocationSpan)
        g_mapView.setRegion(currentRegion, animated: true)
    }
    
    func fnInitManager() {
        g_locationManager = CLLocationManager()
        g_locationManager.delegate = self
        
        // 距離篩選器 用來設置移動多遠距離才觸發委任方法更新位置
        g_locationManager.distanceFilter = kCLLocationAccuracyHundredMeters
        
        // 取得自身定位位置的精確度
        g_locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }
    
    func locationManager(_ manager: CLLocationManager,
                         didUpdateLocations locations: [CLLocation]) {
        
        let currentLocation :CLLocation = locations[0] as CLLocation
        
        g_latitude = currentLocation.coordinate.latitude
        g_longitude = currentLocation.coordinate.longitude

    }
    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        let renderer = MKPolylineRenderer(overlay: overlay)
        renderer.strokeColor = UIColor.blue
        renderer.lineWidth = 2.0
        return renderer
    }
    
    func fnCalcDirection(latitude: Double, longitude: Double) {
        let srcLocation = CLLocationCoordinate2D(latitude: g_latitude, longitude: g_longitude)
        let desLocation = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        
        let srcPlacemark = MKPlacemark(coordinate: srcLocation, addressDictionary: nil)
        let desPlacemark = MKPlacemark(coordinate: desLocation, addressDictionary: nil)
        
        let srcMapItem = MKMapItem(placemark: srcPlacemark)
        let desMapItem = MKMapItem(placemark: desPlacemark)
        
        let directionRequest = MKDirectionsRequest()
        directionRequest.source = srcMapItem
        directionRequest.destination = desMapItem
        directionRequest.transportType = .automobile
        //directionRequest.requestsAlternateRoutes = false
        
        let directions = MKDirections(request: directionRequest)
        directions.calculate(completionHandler:{
            (response, error) in
            if error != nil {
                print("Error getting directions")
            } else {
                if let response = response {
                    self.fnShowRoute(response)
                }
            }
        })
    }
    
    func fnShowRoute(_ response: MKDirectionsResponse) {
        for route in response.routes {
            self.g_mapView.add(route.polyline, level: MKOverlayLevel.aboveRoads)
        }
        fnSetZoom(0.005)
    }
    

}
