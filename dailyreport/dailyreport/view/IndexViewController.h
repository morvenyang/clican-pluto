//
//  IndexViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "PMCalendar.h"
@interface IndexViewController : TTTableViewController<UITableViewDelegate,PMCalendarControllerDelegate>{
    UIView* _configView;
    UIView* _backgroundView;
    UIButton* _configButton;
    CGFloat _rowHeight;
}
@property (nonatomic, retain) UIView *configView;
@property (nonatomic, retain) UIView *backgroundView;
@property (nonatomic, retain) UIButton *configButton;
@property (nonatomic, retain) NSDate *oldDate;
@property (nonatomic, assign) CGFloat rowHeight;
-(void)updateDate;
@end
