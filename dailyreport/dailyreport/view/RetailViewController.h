//
//  RetailViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "RetailModel.h"
#import "PieChartView.h"

@interface RetailViewController : SwitchViewController<RetailDelegate,PieChartViewDelegate,
PieChartViewDataSource>{
    RetailModel* _retailModel;
    PieChartView* _pieChartView;
}

@property (nonatomic, retain) RetailModel *retailModel;
@property (nonatomic, retain) PieChartView *pieChartView;
-(id) initWithBrand:(NSString*) brand;
@end
