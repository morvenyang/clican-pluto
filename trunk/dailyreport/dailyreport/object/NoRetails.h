//
//  NoRetails.h
//  dailyreport
//
//  Created by zhang wei on 14-12-11.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NoRetails : NSObject{
    NSString* _channel;
    NSArray* _stores;
}
@property (nonatomic, copy) NSString* channel;
@property (nonatomic, retain) NSArray* stores;
@end
