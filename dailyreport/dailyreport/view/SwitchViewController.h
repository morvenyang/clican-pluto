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
#import "Constants.h"
#import <MessageUI/MessageUI.h>
@interface SwitchViewController : UIViewController<PMCalendarControllerDelegate,MFMailComposeViewControllerDelegate>{
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
    UILabel* _calendarLabel;
    CGFloat _yOffset;
    NSString* _direction;
 
}
@property (nonatomic, retain) SwipeScrollView *contentView;
@property (nonatomic, copy) NSString *brand;
@property (nonatomic, assign) int index;
@property (nonatomic, retain) UIView *shareView;
@property (nonatomic, retain) UIView *backgroundShareView;
@property (nonatomic, retain) UIImage *preScreenShot;
@property (nonatomic, retain) NSDate *selectedDate;
@property (nonatomic, retain) PMPeriod *initPeriod;
@property (nonatomic, retain) UILabel *calendarLabel;
@property (nonatomic, copy) NSString *direction;

-(void) backAction;
- (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize;
-(SwitchViewController*)openNextUrl:(NSString*)url;
-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame;
-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName point:(CGPoint) point;
-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame;
-(UIImageView*) createImageViewFromImage:(UIImage*) image frame:(CGRect) frame;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment;
-(UILabel*) createDecimalLabel:(NSNumber*) number frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment;
-(UILabel*) createDecimalLabel:(NSNumber*) number unit:(NSString*) unit frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor textAlignment:(int) textAlignment;
-(UIView*) createDailyView:(NSString*) iconName label:(NSString*)label;
#ifdef __IPHONE_6_0
-(NSTextAlignment) getAlignment:(int)textAlignment;
#else
-(UITextAlignment) getAlignment:(int)textAlignment;
#endif
-(CGFloat) getTabHeight;
-(void)openCalendar:(id)sender;
-(void)changeDateAndReload;
-(void)hideShareView;
-(int) getFont:(int) defaultFont ip6Offset:(int)ip6Offset ip6pOffset:(int)ip6pOffset;
@end
