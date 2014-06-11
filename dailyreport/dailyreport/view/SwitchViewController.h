//
//  SwitchViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SwipeScrollView.h"
@interface SwitchViewController : UIViewController{
    NSString* _brand;
    SwipeScrollView* _contentView;
    int _index;
    UIView* _shareView;
    UIView* _backgroundShareView;
    UIImage* _preScreenShot;
    BOOL _swiping;
    CGFloat _swipeStartPoint;
    CGFloat _swipeEndPoint;
}
@property (nonatomic, retain) SwipeScrollView *contentView;
@property (nonatomic, copy) NSString *brand;
@property (nonatomic, assign) int index;
@property (nonatomic, retain) UIView *shareView;
@property (nonatomic, retain) UIView *backgroundShareView;
@property (nonatomic, retain) UIImage *preScreenShot;
-(void) backAction;
-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame;
-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment;
-(UILabel*) createDecimalLabel:(NSNumber*) number frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment;
@end
