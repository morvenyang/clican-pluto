//
//  SwitchViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SwitchViewController : UIViewController{
    UIScrollView* _contentView;
}
@property (nonatomic, retain) UIScrollView *contentView;
-(void)addCommonViewFromIndex:(int)index;
@end
