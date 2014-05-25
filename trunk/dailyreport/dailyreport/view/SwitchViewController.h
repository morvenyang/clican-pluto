//
//  SwitchViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SwitchViewController : UIViewController{
    NSString* _brand;
    UIScrollView* _contentView;
    int _index;
}
@property (nonatomic, retain) UIScrollView *contentView;
@property (nonatomic, copy) NSString *brand;
@property (nonatomic, assign) int index;

-(void) backAction;
-(UIImageView*) createImageViewFromNamedImage:(NSString*) imageName frame:(CGRect) frame;
-(UIImageView*) createImageViewFromColor:(UIColor*) color frame:(CGRect) frame;
-(UILabel*) createLabel:(NSString*) text frame:(CGRect) frame textColor:(NSString*) textColor font:(int) font backgroundColor:(NSString*) backgroundColor;
@end
