//
//  SwitchViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SwipeScrollView.h"
#import "PMCalendar.h"
@interface SwitchViewController : UIViewController<PMCalendarControllerDelegate>{
    NSString* _brand;
    SwipeScrollView* _contentView;
    int _index;
    UIView* _shareView;
    UIView* _backgroundShareView;
    UIImage* _preScreenShot;
    BOOL _swiping;
    CGFloat _swipeStartPoint;
    CGFloat _swipeEndPoint;
    NSDate* _selectedDate;
    PMPeriod* _initPeriod;
}
@property (nonatomic, retain) SwipeScrollView *contentView;
@property (nonatomic, copy) NSString *brand;
@property (nonatomic, assign) int index;
@property (nonatomic, retain) UIView *shareView;
@property (nonatomic, retain) UIView *backgroundShareView;
@property (nonatomic, retain) UIImage *preScreenShot;
@property (nonatomic, retain) NSDate *selectedDate;
@property (nonatomic, retain) PMPeriod *initPeriod;
-(void) backAction;
-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame;
-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment;
-(UILabel*) createDecimalLabel:(NSNumber*) number frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment;
-(UILabel*) createDecimalLabel:(NSNumber*) number unit:(NSString*) unit frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(NSTextAlignment) textAlignment;
-(void)openCalendar:(id)sender;
-(void)changeDateAndReload;
@end
