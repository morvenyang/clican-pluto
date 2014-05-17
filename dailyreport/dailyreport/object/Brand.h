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
    NSNumber* _dayAmount;
}
@property (nonatomic, retain) NSNumber* dayAmount;
@property (nonatomic, copy) NSString* brand;

@end
