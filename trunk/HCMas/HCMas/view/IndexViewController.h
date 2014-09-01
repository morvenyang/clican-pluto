//
//  IndexViewController.h
//  HCMas
//
//  Created by zhang wei on 14-8-29.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface IndexViewController : UIViewController<UITableViewDelegate>{
    int _imageIndex;
    NSMutableArray* _pointImageViews;
    UIImageView* _topImageView;
    NSMutableArray* _menuBgImageViews;
    NSMutableArray* _menuButtonViews;
}
@property (nonatomic, assign) int imageIndex;
@property (nonatomic, retain) NSMutableArray* pointImageViews;
@property (nonatomic, retain) UIImageView* topImageView;
@property (nonatomic, retain) NSMutableArray* menuBgImageViews;
@property (nonatomic, retain) NSMutableArray* menuButtonViews;
@end
