//
//  GuideViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-12-1.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GuideViewController : UIViewController{
    int _index;
    UIImageView* _imageView;
    UIButton* _start;
}
@property (nonatomic, assign) int index;
@property (nonatomic, retain) UIImageView *imageView;
@property (nonatomic, retain) UIButton *start;
@end
