//
//  ChartViewController.h
//  HCMas
//
//  Created by zhang wei on 14-11-4.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KpiHistoryModel.h"
@interface ChartViewController : UIViewController<KpiHistoryDelegate>{
    NSString* _kpiType;
    NSString* _pointName;
    NSString* _startTime;
    NSString* _endTime;
    KpiHistoryModel* _kpiHistoryModel;
    UIWebView* _webPieChartView;
}
@property (nonatomic, retain) NSString* kpiType;
@property (nonatomic, retain) NSString* pointName;
@property (nonatomic, retain) NSString* startTime;
@property (nonatomic, retain) NSString* endTime;
@property (nonatomic, retain) UIWebView    *webPieChartView;
-(id) initWithKpiType:(NSString*)kpiType pointName:(NSString*)pointName startTime:(NSString*)startTime endTime:(NSString*)endTime;
@end
