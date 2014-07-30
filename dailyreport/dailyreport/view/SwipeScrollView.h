//
//  SwipeScrollView.h
//  dailyreport
//
//  Created by zhang wei on 14-5-28.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol GoodSwitchDelegate;
@interface SwipeScrollView : UIScrollView{
    BOOL _swiping;
    CGFloat _swipeStartPoint;
    CGFloat _swipeEndPoint;
    NSString* _brand;
    int _index;
    id<GoodSwitchDelegate> _goodSwitchDelegate;
}

@property (nonatomic, copy) NSString *brand;
@property (nonatomic, assign) int index;
@property (nonatomic, assign) id<GoodSwitchDelegate> goodSwitchDelegate;
@end

@protocol GoodSwitchDelegate <NSObject>
- (void) switchGood;
@end