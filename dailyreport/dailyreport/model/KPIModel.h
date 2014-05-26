//
//  KPIModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Brand.h"
#import "Channel.h"
#import "BaseModel.h"
@protocol KPIDelegate;

@interface KPIModel : BaseModel{
    NSString* _brand;
    id<KPIDelegate> _delegate;
}

@property(nonatomic,assign) id<KPIDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;

@end

@protocol KPIDelegate <NSObject>

- (void) brandDidFinishLoad:(NSArray*) channels date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
