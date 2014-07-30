//
//  GoodRank.h
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GoodRank : NSObject{
    NSString *_name;
    NSNumber* _amount;
    NSNumber* _count;
    NSString* _imageLink;
    NSString* _imageLinkMin;
    NSString* _season;
    NSString* _line;
    NSString* _wave;
    NSString* _year;
}
@property (nonatomic, copy) NSString* name;
@property (nonatomic, retain) NSNumber* amount;
@property (nonatomic, retain) NSNumber* count;
@property (nonatomic, copy) NSString* imageLink;
@property (nonatomic, copy) NSString* imageLinkMin;
@property (nonatomic, copy) NSString* season;
@property (nonatomic, copy) NSString* line;
@property (nonatomic, copy) NSString* wave;
@property (nonatomic, copy) NSString* year;
@end
