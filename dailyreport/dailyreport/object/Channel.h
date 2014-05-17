//
//  Channel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Channel : NSObject{
    NSString* _channel;
    NSNumber* _dayAmount;
}
@property (nonatomic, copy) NSString* channel;
@property (nonatomic, retain) NSNumber* dayAmount;

@end
