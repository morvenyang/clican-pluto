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
@interface BrandViewController : SwitchViewController<BrandDelegate>{

    BrandModel* _brandModel;
    UIWebView* _webLineChartView;
}

@property (nonatomic, retain) BrandModel *brandModel;
@property (nonatomic, retain) UIWebView *webLineChartView;
-(id) initWithBrand:(NSString*) brand;
@end
