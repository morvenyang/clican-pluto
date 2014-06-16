//
//  CannotCancelUISwipeGestureRecognizer.m
//  dailyreport
//
//  Created by zhang wei on 14-6-16.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "CannotCancelUISwipeGestureRecognizer.h"

@implementation CannotCancelUISwipeGestureRecognizer
-(BOOL)canBePreventedByGestureRecognizer:(UIGestureRecognizer*)gestureRecognizer{
    return NO;
}
@end
