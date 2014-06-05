//
//  Brand.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Brand : NSObject{
    NSString* _brand;
    NSDate* _date;
    NSNumber* _dayAmount;
    NSNumber* _weekAmount;
    NSNumber* _yearAmount;
    NSNumber* _weekLike;
    NSNumber* _yearLike;
}
@property (nonatomic, copy) NSString* brand;
@property (nonatomic, retain) NSDate* date;
@property (nonatomic, retain) NSNumber* dayAmount;
@property (nonatomic, retain) NSNumber* weekAmount;
@property (nonatomic, retain) NSNumber* yearAmount;
@property (nonatomic, retain) NSNumber* weekLike;
@property (nonatomic, retain) NSNumber* yearLike;


@end