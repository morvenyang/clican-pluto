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
    NSNumber* _docNumber;
    NSNumber* _avgDocCount;
    NSNumber* _avgPrice;
    NSNumber* _aps;
}
@property (nonatomic, copy) NSString* channel;
@property (nonatomic, retain) NSNumber* dayAmount;
@property (nonatomic, retain) NSNumber* docNumber;
@property (nonatomic, retain) NSNumber* avgDocCount;
@property (nonatomic, retain) NSNumber* avgPrice;
@property (nonatomic, retain) NSNumber* aps;
@end
