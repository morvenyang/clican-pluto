//
//  LinkImageView.m
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "LinkImageView.h"

@implementation LinkImageView

@synthesize actionUrl = _actionUrl;

-(void)dealloc{
    [_actionUrl release];
    [super dealloc];
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    NSLog(@"%@",_actionUrl);
    TTOpenURL(_actionUrl);
}


@end
