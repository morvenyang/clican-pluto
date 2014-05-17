//
//  BrandModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Brand.h"
#import "Channel.h"

@protocol BrandDelegate;

@interface BrandModel : TTURLRequestModel{
    NSString* _brand;
    id<BrandDelegate> _delegate;
}

@property(nonatomic,assign) id<BrandDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
@end

@protocol BrandDelegate <NSObject>

- (void) brandDidFinishLoad:(Brand*) brand channels:(NSArray*) channels weeks:(NSArray*) weeks;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
