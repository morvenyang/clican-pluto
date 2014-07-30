//
//  GoodViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SwitchViewController.h"

@interface GoodViewController : SwitchViewController{
    int _goodIndex;
    NSMutableArray* _dyviews;
}
@property (nonatomic, assign) int goodIndex;
@property (nonatomic, retain) NSMutableArray* dyviews;
@end
