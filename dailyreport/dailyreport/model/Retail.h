//
//  Retail.h
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Retail : NSObject{
    NSString *_name;
    NSNumber* _dayAmount;
}

@property (nonatomic, copy) NSString* name;
@property (nonatomic, retain) NSNumber* dayAmount;

@end
