//
//  StoreRankModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Rank.h"
#import "BaseModel.h"
@protocol StoreRankDelegate;

@interface StoreRankModel : BaseModel{
    NSString* _brand;
    id<StoreRankDelegate> _delegate;
}

@property(nonatomic,assign) id<StoreRankDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
@end

@protocol StoreRankDelegate <NSObject>

- (void) brandDidFinishLoad:(NSMutableArray*) channels date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
