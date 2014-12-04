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
@synthesize brand = _brand;
@synthesize index = _index;
@synthesize goodSwitchDelegate = _goodSwitchDelegate;
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
        CATransition *animation = [CATransition animation];
        animation.duration = 3;
        animation.timingFunction = UIViewAnimationCurveEaseInOut;
        animation.type = kCATransitionReveal;
        _swipeEndPoint=[[touches anyObject] locationInView:self].x;
        CGFloat swipeDistance = _swipeEndPoint - _swipeStartPoint;
        NSLog(@"swipeDistance=%f",swipeDistance);
        if(swipeDistance==320){
            return;
        }
        // Swipe to left
        if (swipeDistance < -30.f) {
            animation.subtype = kCATransitionFromLeft;
            if(self.index==1){
                NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==2){
                NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==3){
                NSString* url = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==4){
                NSString* url = [NSString stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==5){
                NSString* url = [NSString stringWithFormat:@"peacebird://b2cKpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==6){
                NSString* url = [NSString stringWithFormat:@"peacebird://storeSum/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==7){
                NSString* url = [NSString stringWithFormat:@"peacebird://noRetails/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==-1){
                
                int goodIndex = DrAppDelegate.user.goodIndex;
                if(goodIndex<DrAppDelegate.user.goods.count-1){
                    goodIndex++;
                    DrAppDelegate.user.goodIndex=goodIndex;
                    [self.goodSwitchDelegate switchGood];
                }
            }
            return;
        }
        // Swipe to right
        else if (swipeDistance > 30.f) {
            animation.subtype = kCATransitionFromRight;
            if(self.index==2){
                NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==3){
                NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==4){
                NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==5){
                NSString* url = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==6){
                NSString* url = [NSString stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==7){
                NSString* url = [NSString stringWithFormat:@"peacebird://b2cKpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==8){
                NSString* url = [NSString stringWithFormat:@"peacebird://storeSum/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
                TTOpenURL(url);
            }else if(self.index==-1){
                int goodIndex = DrAppDelegate.user.goodIndex;
                
                if(goodIndex>0){
                    goodIndex--;
                    DrAppDelegate.user.goodIndex=goodIndex;
                    [self.goodSwitchDelegate switchGood];
                }
                
            }
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
    TT_RELEASE_SAFELY(_brand);
    [super dealloc];
}
@end
