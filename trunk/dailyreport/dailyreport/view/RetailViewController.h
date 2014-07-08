//
//  RetailViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "RetailModel.h"
@interface RetailViewController : SwitchViewController<RetailDelegate>{
    UIWebView* _webPieChartView;
    NSMutableArray* _tabLables;
    NSMutableArray* _tableViews;
    NSString* _type;
    UILabel* _calendarLabel;
}

@property (nonatomic, retain) RetailModel *retailModel;
@property (nonatomic, retain) UIWebView *webPieChartView;
@property (nonatomic, retain) NSMutableArray *tabLables;
@property (nonatomic, retain) NSMutableArray *tableViews;
@property (nonatomic, retain) UILabel *calendarLabel;
@property (nonatomic, retain) NSString *type;
-(id) initWithBrand:(NSString*) brand;
@end
