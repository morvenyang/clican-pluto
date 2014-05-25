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
    NSMutableArray* _tabLables;
    NSArray* _channels;
    NSArray* _sorts;
    NSArray* _regions;
    NSArray* _selectedData;
    NSMutableArray* _tableViews;
}

@property (nonatomic, retain) RetailModel *retailModel;
@property (nonatomic, retain) PieChartView *pieChartView;
@property (nonatomic, retain) NSMutableArray *tabLables;
@property (nonatomic, retain) NSArray *channels;
@property (nonatomic, retain) NSArray *sorts;
@property (nonatomic, retain) NSArray *regions;
@property (nonatomic, retain) NSArray *selectedData;
@property (nonatomic, retain) NSMutableArray *tableViews;
-(id) initWithBrand:(NSString*) brand;
@end
