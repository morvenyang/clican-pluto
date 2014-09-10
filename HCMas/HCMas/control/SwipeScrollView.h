//
//  SwipeScrollView.h
//  dailyreport
//
//  Created by zhang wei on 14-5-28.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol SwitchDataDelegate;
@interface SwipeScrollView : UIScrollView{
    BOOL _swiping;
    CGFloat _swipeStartPoint;
    CGFloat _swipeEndPoint;
    id<SwitchDataDelegate> _switchDataDelegate;
}
@property (nonatomic, assign) id<SwitchDataDelegate> switchDataDelegate;
@end

@protocol SwitchDataDelegate <NSObject>
- (void) displayCurrent;
- (void) displayHistory;
@end