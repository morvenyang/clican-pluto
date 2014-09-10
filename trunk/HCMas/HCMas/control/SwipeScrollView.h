//
//  SwipeScrollView.h
//  dailyreport
//
//  Created by zhang wei on 14-5-28.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SwipeScrollView : UIScrollView{
    BOOL _swiping;
    CGFloat _swipeStartPoint;
    CGFloat _swipeEndPoint;
}

@end