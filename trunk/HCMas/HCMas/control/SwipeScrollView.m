//
//  SwipeScrollView.m
//  dailyreport
//
//  Created by zhang wei on 14-5-28.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwipeScrollView.h"
#import "AppDelegate.h"
@implementation SwipeScrollView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.delaysContentTouches =NO;
    }
    return self;
}

#pragma mark - Touch Actions

// Tells the receiver when one or more fingers touch down in a view or window.
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    if ([touches count] != 1) return;
    _swipeStartPoint = [[touches anyObject] locationInView:self].x;
    _swiping = YES;
    [super touchesBegan:touches withEvent:event];
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    NSLog(@"touch cancelled");
    [self doSwipe:touches];
}
// Tells the receiver when one or more fingers associated
//   with an event move within a view or window.
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesMoved:touches withEvent:event];
}
-(void)doSwipe:(NSSet*)touches{
    NSLog(@"swipe=%hhd",_swiping);
    if (_swiping){
        _swipeEndPoint=[[touches anyObject] locationInView:self].x;
        CGFloat swipeDistance = _swipeEndPoint - _swipeStartPoint;
        NSLog(@"swipeDistance=%f",swipeDistance);
        if(swipeDistance==320){
            return;
        }
        // Swipe to left
        NSLog(@"width=%f,right=%f",self.contentSize.width,self.contentOffset.x);
        if (swipeDistance <= -10.f) {
            if(self.contentOffset.x>=50||self.contentOffset.x==0){
                NSLog(@"swipe left");
            }else{
                 NSLog(@"not swipe left");
            }
            return;
        }
        // Swipe to right
        else if (swipeDistance >= 10.f) {
            return;
        }
        _swiping = NO;
    }
}
// Tells the receiver when one or more fingers are raised from a view or window.
-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    [self doSwipe:touches];
    [super touchesEnded:touches withEvent:event];
}

- (BOOL)touchesShouldBegin:(NSSet *)touches withEvent:(UIEvent *)event inContentView:(UIView *)view{
    return YES;
}
-(BOOL) canCancelContentTouches{
    return NO;
}
- (void)dealloc
{
    [super dealloc];
}
@end
