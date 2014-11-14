//
//  ChartViewController.h
//  HCMas
//
//  Created by zhang wei on 14-11-4.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KpiHistoryModel.h"
@interface ChartViewController : UIViewController{
    UIWebView* _webPieChartView;
    NSString* _kpiType;
}

@property(nonatomic,copy)NSString* kpiType;

@property (nonatomic, retain) UIWebView    *webPieChartView;
-(id) initWithKpiType:(NSString*)kpiType;
@end
