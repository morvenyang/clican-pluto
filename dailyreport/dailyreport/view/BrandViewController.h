//
//  BrandViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "BrandModel.h"
#import "SwitchViewController.h"
@interface BrandViewController : SwitchViewController<BrandDelegate,UIWebViewDelegate>{

    BrandModel* _brandModel;
    UIWebView* _webLineChartView;
    NSString* _dailyLineChart;
    NSString* _weeklyLineChart;
    NSString* _monthlyLineChart;
    NSString* _yearlyLineChart;
    UIButton* _dailyButton;
    UIButton* _weeklyButton;
    UIButton* _monthlyButton;
    UIButton* _yearlyButton;
    CGFloat _chartYOffset;
    UIView* _infoView;
    NSString* _lineChart;
    UIImageView* _periodImageView;
}

@property (nonatomic, retain) BrandModel *brandModel;
@property (nonatomic, retain) UIWebView *webLineChartView;
@property (nonatomic, copy) NSString *dailyLineChart;
@property (nonatomic, copy) NSString *weeklyLineChart;
@property (nonatomic, copy) NSString *monthlyLineChart;
@property (nonatomic, copy) NSString *yearlyLineChart;
@property (nonatomic, copy) NSString *lineChart;
@property (nonatomic, retain) UIButton *dailyButton;
@property (nonatomic, retain) UIButton *weeklyButton;
@property (nonatomic, retain) UIButton *monthlyButton;
@property (nonatomic, retain) UIButton *yearlyButton;
@property (nonatomic, retain) UIView *infoView;
@property (nonatomic, retain) UIImageView *periodImageView;

-(id) initWithBrand:(NSString*) brand;
@end
