//
//  Rank.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Rank : NSObject{
    NSString *_name;
    NSNumber* _dayAmount;
    NSNumber* _rate;
}

@property (nonatomic, copy) NSString* name;
@property (nonatomic, retain) NSNumber* dayAmount;
@property (nonatomic, retain) NSNumber* rate;
@end
