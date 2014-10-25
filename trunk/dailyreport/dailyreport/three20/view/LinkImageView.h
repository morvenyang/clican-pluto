//
//  LinkImageView.h
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LinkImageView : TTImageView{
    NSString* _actionUrl;
}

@property (nonatomic, copy) NSString* actionUrl;


@end
