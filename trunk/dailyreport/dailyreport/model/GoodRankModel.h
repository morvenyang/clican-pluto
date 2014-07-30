//
//  GoodRankModel.h
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BaseModel.h"
#import "GoodRank.h"
@protocol GoodRankDelegate;
@interface GoodRankModel : BaseModel{
    NSString* _brand;
    id<GoodRankDelegate> _delegate;
}

@property(nonatomic,assign) id<GoodRankDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
@end

@protocol GoodRankDelegate <NSObject>

- (void) brandDidFinishLoad:(NSMutableArray*) goods date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
