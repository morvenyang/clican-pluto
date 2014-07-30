//
//  GoodViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SwitchViewController.h"
#import "SwipeScrollView.h"
@interface GoodViewController : SwitchViewController<GoodSwitchDelegate>{
    NSMutableArray* _dyviews;
    NSMutableArray* _pointImageViews;
}
@property (nonatomic, retain) NSMutableArray* dyviews;
@property (nonatomic, retain) NSMutableArray* pointImageViews;
@end

