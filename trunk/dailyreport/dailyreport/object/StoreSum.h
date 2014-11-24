//
//  StoreSum.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StoreSum : NSObject{
    NSString* _sumType;
    NSNumber* _total;
    NSNumber* _selfV;
    NSNumber* _join;
    NSNumber* _unionV;
}
@property (nonatomic, copy) NSString* sumType;
@property (nonatomic, retain) NSNumber* total;
@property (nonatomic, retain) NSNumber* selfV;
@property (nonatomic, retain) NSNumber* join;
@property (nonatomic, retain) NSNumber* unionV;
@end
