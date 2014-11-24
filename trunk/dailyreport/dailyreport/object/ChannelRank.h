//
//  ChannelRank.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ChannelRank : NSObject{
    NSString* _channel;
    NSArray* _ranks;
    NSArray* _reverseRanks;
}
@property (nonatomic, copy) NSString* channel;
@property (nonatomic, retain) NSArray* ranks;
@property (nonatomic, retain) NSArray* reverseRanks;
@end
